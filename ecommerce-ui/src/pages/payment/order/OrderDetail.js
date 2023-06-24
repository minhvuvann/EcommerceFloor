import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {useMediaQuery} from "react-responsive";
import styles from "./OrderDetail.module.scss";
import {
    createOrder,
    getCartDetail,
    getFeeShipment, getQRImage,
    getShopAll,
} from "~/services/workspaces.sevices";
import MoneyUtils from "~/utils/MoneyUtils";
import {Container, Grid} from "@mui/material";
import {Link, useParams} from "react-router-dom";
import OrderItemTable from "~/pages/payment/order/ProductTable";
import NumberUtils from "~/utils/NumberUtils";
import LocationOnIcon from '@mui/icons-material/LocationOn';
import config from "~/config";
import {Logo, LogoReverse, LogoVietQR} from "~/components/Icon";

import {
    Button,
    Dialog, DialogActions,
    DialogContent,
    DialogContentText,
    FormControl,
    FormControlLabel,
    FormLabel,
    Radio,
    RadioGroup
} from "@material-ui/core";
import {CartContext} from "~/config/provider/CartProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function OrderDetail(props) {
    const {id} = useParams();
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const maxWidth600 = useMediaQuery({maxWidth: 600});
    const [cartItems, setCartItems] = useState([]);
    const [cart, setCart] = useState();
    const [totalPayment, setTotalPayment] = useState(0);
    const [totalCommodity, setTotalCommodity] = useState(0);
    const [feeShipping, setFeeShipping] = useState();
    const [market, setMarket] = useState(new Map());
    const [methodPayment, setMethodPayment] = useState("qr");
    const [showQR, setShowQR] = useState(false);
    const [showSucceed, setShowSucceed] = useState(false);
    const [imgQR, setImgQR] = useState();
    const {setShouldUpdate} = useContext(CartContext);
    const {localeList} = useContext(LanguageContext);
    useEffect(() => {
        fetchData();
    }, [market]);

    const fetchData = async () => {
        const shopInfos = await getShopAll();
        const cartDetail = await getCartDetail(rsUser?.id);
        setCart(cartDetail?.data?.cart);
        const citem = [];
        let map = new Map();
        let idToken = NumberUtils.cutToken(id, '.');
        let totalPayment = 0;
        let totalShip = 0;
        let totalCommodity = 0;
        for (const c of cartDetail.data.cartItems) {
            if (idToken.get(c.id)) {
                citem.push(c);

                const shopByGet = getShopDistrict(c.shopId, shopInfos?.data);
                const FeeShippingGHNInput = {
                    from_district_id: shopByGet,
                    service_type_id: 2,
                    to_district_id: rsUser?.address?.districtCode,
                    to_ward_code: rsUser?.address?.wardCode,
                    height: 4,
                    width: 10,
                    length: 14,
                    weight: c.productVariant?.weight,
                    insurance_value: c.productVariant?.price?.amount,
                }
                let dataFee = await getFeeShipment('3' + c.shopId, FeeShippingGHNInput);
                map.set(c.id, dataFee?.data?.amount);
                totalCommodity += c.productVariant?.salePrice?.amount * c.quantity;
                totalPayment += c.productVariant?.salePrice?.amount * c.quantity + dataFee?.data?.amount;
                totalShip += dataFee?.data?.amount;

            }
        }
        setFeeShipping(totalShip);
        setTotalPayment(totalPayment);
        setTotalCommodity(totalCommodity);
        setCartItems(citem);
        setMarket(map);
    }

    function getShopDistrict(id, shops) {
        for (const shop of shops) {
            if (shop.shopId === id)
                return shop.address.districtCode;
        }
    }

    const handleMethodPayment = (event) => {
        setMethodPayment(event.target.value);

    }


    const placeOrder = async () => {
        const CreateOrderInput = {
            userId: rsUser.id,
            payment: methodPayment === "qr",
            cartItemInputs: cartItems,
            shipping: feeShipping


        }
        if (methodPayment === "qr") {
            const qr = await getQRImage(totalPayment);
            if (qr) {
                setImgQR(qr.data);
                setShowQR(true);
                setTimeout(() => {
                    setShowQR(false);
                    setShowSucceed(true);
                    createOrder(CreateOrderInput)
                        .then(() => {
                            setShouldUpdate(prev => !prev);
                            window.location.href = config.routes.home;
                        })
                        .catch(error => {
                            console.log(error);
                        });
                    setTimeout(() => {
                        setShowSucceed(false);
                    }, 3000);
                }, 5000);
            }
        } else {
            setShowSucceed(true);
            createOrder(CreateOrderInput)
                .then(() => {
                    setShouldUpdate(prev => !prev);
                    window.location.href = config.routes.home;
                })
                .catch(error => {
                    console.log(error);
                });
            setTimeout(() => {
                setShowSucceed(false);
            }, 3000);
        }


    }
    const backHome = () => {
        window.location.href = config.routes.home;
    }

    return (
        <div className={cx('wrapper')}>
            <div className={cx("banner-container")}>
                <Link to={config.routes.home}>
                    <LogoReverse className={cx("lg-reverse")}/>
                </Link>

                <div className={cx('title')}>{localeList['order_title']}</div>
            </div>
            <Container>


                <Grid container style={{
                    width: "100%",
                    height: "80%",
                    backgroundColor: "#FFFFFF"
                    , borderBottom: "1.5rem solid #F5F5F5"
                }}>
                    <div className={cx("line-add")}></div>

                    <Grid xs={2.1} className={cx("add-info-ship")} item>
                        <div className={cx("ic-container")}>
                            <LocationOnIcon fontSize={"medium"} className={cx("gr-location-ic")}
                                            style={{color: '#ff5588'}}/>

                            <div className={cx("title-add")}>
                                {localeList['order_address']}
                            </div>
                        </div>
                        <div className={cx("info-phone")}>
                            <div className={cx("info-ship")}>
                                {rsUser?.fullName}
                            </div>
                            <div className={cx("info-ship")}>
                                (+84){rsUser?.telephone}
                            </div>
                        </div>

                    </Grid>
                    <Grid style={{
                        paddingTop: "2rem"
                    }} item xs={7.9}>
                        <div className={cx("address-container")}>{rsUser?.address?.address1}</div>
                    </Grid>
                    <Grid item xs={2}>
                        <div style={{paddingTop: "5.5rem"}}>
                            <div className={cx("lable-address")}>{localeList['order_main_address']}</div>
                        </div>
                    </Grid>
                </Grid>


                <OrderItemTable
                    market={market}
                    cartItems={cartItems}
                />
                <Dialog open={showSucceed}>
                    <DialogContent>
                        <DialogContentText>
                            {localeList['order_dialog_success']}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button style={{
                            color: "#FFFFFF",
                            marginRight: "1.8rem",
                            width: "12.5rem",
                        }} className={cx('back-home')} onClick={backHome}>
                            {localeList['order_dialog_button']}
                        </Button>
                    </DialogActions>
                </Dialog>
                <Dialog open={showQR}>
                    <DialogContent>
                        <DialogContentText>
                            <img src={imgQR} alt={"Viet QR"}/>
                        </DialogContentText>
                    </DialogContent>
                </Dialog>

                <Grid container style={{
                    width: "100%",
                    height: "80%",
                    backgroundColor: "#FFFFFF"
                    , borderTop: "1.5rem solid #F5F5F5",
                    borderBottom: "1px dashed rgba(0,0,0,.09)"
                }}>

                    <Grid xs={2.5} item>
                        <div className={cx("method-payment-title")}>
                            {localeList['order_method_payment']}
                        </div>

                    </Grid>
                    <Grid style={{
                        paddingTop: "2rem"
                    }} item xs={7.9}>
                    </Grid>

                </Grid>
                <div className={cx('mt-wrapper')}>
                    <div className={cx("mt-pm-container")}>
                        <FormControl>
                            <RadioGroup
                                aria-labelledby="demo-controlled-radio-buttons-group"
                                name="controlled-radio-buttons-group"
                                value={methodPayment}
                            >
                                <Grid container>
                                    <Grid item>
                                        <FormControlLabel onClick={handleMethodPayment} value="qr"
                                                          control={<Radio/>}/></Grid>
                                    <Grid item>
                                        <div style={{height: "2rem"}}>
                                            <LogoVietQR width={80} height={35} className={cx("viet-qr")}/>
                                        </div>
                                    </Grid>

                                    <Grid item>
                                        <div className={cx("text-mt")}>{localeList['method_pay_qr']}</div>
                                    </Grid>
                                </Grid>

                                <Grid container>
                                    <Grid item>
                                        <FormControlLabel onClick={handleMethodPayment} value="reciving"
                                                          control={<Radio/>}/></Grid>
                                    <Grid item>

                                    </Grid>

                                    <Grid item>
                                        <div className={cx("text-mt")}>{localeList['method_pay_receive']}</div>
                                    </Grid>
                                </Grid>
                            </RadioGroup>
                        </FormControl>
                    </div>

                </div>
                <div style={{borderTop: "1px solid #F5F5F5"}} className={cx('checkout')}>
                    <span className={cx('label-price')}>{localeList['total_price']}</span>
                    <span className={cx('total-price')}>{MoneyUtils.getMoney(totalCommodity)}</span>
                </div>
                <div className={cx('checkout')}>
                    <span className={cx('label-price')}>{localeList['fee_shipping']}</span>
                    <span className={cx('total-price')}>{MoneyUtils.getMoney(feeShipping ? feeShipping : 0)}</span>
                </div>

                <div className={cx('checkout')}>
                    <span className={cx('label-price')}>{localeList['total_payment']}</span>
                    <span className={cx('total-price')}>{MoneyUtils.getMoney(totalPayment)}</span>
                </div>

                <div style={{borderTop: "1px dashed rgba(0,0,0,.09)"}} className={cx('checkout')}>

                    <button onClick={placeOrder}
                            className={cx('btn-purchase')}>{localeList['button_put_order']}</button>
                </div>
            </Container>
        </div>
    );
}

export default OrderDetail;
