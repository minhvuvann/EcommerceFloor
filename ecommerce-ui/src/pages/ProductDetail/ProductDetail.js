import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {Container, Grid, Paper} from "@mui/material";
import {Link, useParams} from "react-router-dom";

import styles from "./ProductDetail.module.scss";
import {AiFillStar} from "react-icons/ai";
import {BsCartPlus} from "react-icons/bs";
import {
    addCart,
    getAllCarrier,
    getCartDetail,
    getFeeShipment,
    getProductDetailById
} from "~/services/workspaces.sevices";
import config from "~/config";
import LocalShippingOutlinedIcon from '@mui/icons-material/LocalShippingOutlined';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import MoneyUtils from "~/utils/MoneyUtils";
import NumberUtils from "~/utils/NumberUtils";
import ColorConvertor from "~/utils/ColorConvertor";
import {Dialog, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";
import {CartContext} from "~/config/provider/CartProvider";
import ChatButton from "~/components/chatButton";
import ChatService from "~/pages/chat";
import Carousel from "react-material-ui-carousel";
import BannerItem from "~/components/main-home/banner-slider/banner-item";

const cx = classNames.bind(styles);


function ProductDetail(props) {
    let {id} = useParams();
    const {setShouldUpdate} = useContext(CartContext);
    const [chatOpen, setChatOpen] = useState(false);
    const [product, setProduct] = useState();
    const [colorTicker, setColorTicker] = useState("");
    const [weight, setWeight] = useState("");
    const [size, setSize] = useState("");
    const [count, setCount] = useState(1);
    const [variants, setVariants] = useState([]);
    const [shop, setShop] = useState([]);
    const [carriers, setCarriers] = useState([]);
    const [feeShipping, setFeeShipping] = useState();
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const [cart, setCart] = useState();
    const [errTicker, setErrorTicker] = useState(false);
    const [showCart, setShowCart] = useState(false);
    const imageUrls = product?.imageUrls || []; // Lấy danh sách hình ảnh từ product hoặc tạo một mảng rỗng nếu không có

    imageUrls.unshift(product?.featuredImageUrl);
    useEffect(() => {
        async function fetchData() {
            const response = await getProductDetailById(id);
            const carrierRs = await getAllCarrier();
            const data = response?.data;
            if (data) {
                if (rsUser) {
                    const cartDetail = await getCartDetail(rsUser?.id);
                    setCart(cartDetail?.data?.cart);
                    console.log(cartDetail?.data?.cart);

                }
                setProduct(data?.product);
                setVariants(data?.variants);
                setShop(data.shop);
                setCarriers(carrierRs?.data);
                const FeeShippingGHNInput = {

                    from_district_id: data?.shop?.address.districtCode,
                    service_type_id: 2,
                    to_district_id: rsUser ? (rsUser?.address?.districtCode) : 3695,
                    to_ward_code: rsUser ? (rsUser?.address?.wardCode) : 90735,
                    height: 4,
                    width: 10,
                    length: 14,
                    weight: data?.variants[0]?.weight,
                    insurance_value: data?.product?.mediumPrice?.amount,

                }
                const shippingGHN = await getFeeShipment(parseInt('3' + data.shop.shopId), FeeShippingGHNInput);
                setFeeShipping(shippingGHN?.data);

            }

        }

        fetchData();
    }, [id]);

    const shopMembers = [
        {id: 1, name: "Đánh Giá"},
        {
            id: 2, name: (Math.floor(Math.random() * 100 + 1)) + "," +
                Math.floor(Math.random(4) + 1) + "k"
        },
        {id: 3, name: "Tỉ Lệ phản hồi"},
        {id: 4, name: (Math.floor(Math.random() * 100 + 1)) + "%"},
        {id: 5, name: "Tham Gia"},
        {id: 6, name: (Math.floor(Math.random() * 100 + 1)) + " tháng trước"},
        {id: 7, name: "Sản phẩm"},
        {id: 8, name: "55"},
        {id: 9, name: "Thời gian phản hồi"},
        {id: 10, name: "trong vài giờ"},
        {id: 11, name: "Người theo dõi"},
        {id: 12, name: (Math.floor(Math.random() * 100 + 1)) + "," + Math.floor(Math.random(4) + 1) + "k"},
    ]

    const incrementCount = () => {
        setCount(count + 1);
    };
    const decrementCount = () => {
        if (count > 1) {
            setCount(count - 1);
        }
    };
    const isSize = (variants) => {
        let isSize = false;
        variants.map(({size}) => {
            if (size !== null) {
                isSize = true;
            }
        });
        return isSize;
    }
    const isWeight = (variants) => {
        let isWeight = false;
        variants.map(({weight}) => {
            if (weight !== null) {
                isWeight = true;
            }
        });
        return isWeight;
    }
    const isColor = (variants) => {
        let isColor = false;
        // eslint-disable-next-line array-callback-return
        variants.map(({color}) => {
            if (color.toString() !== null) {
                isColor = true;
            }
        });
        return isColor;
    }
    const handColorTicker = (id) => {
        setColorTicker(id);
        console.log(colorTicker)
    }
    const getVariant = (id) => {
        let i;
        for (i = 0; i < variants.length; i++) {
            if (variants[i].id === id) {
                return variants[i];
            }

        }
    }
    const validator = () => {
        if (colorTicker) {
            setErrorTicker(false);
        } else {
            setErrorTicker(true);
        }
    }


    const handCart = async () => {
        validator();
        if (!rsUser) {
            window.location.href = config.routes.login;
        }
        if (colorTicker) {
            setErrorTicker(false);
            const CartItem = {
                cartId: cart?.id,
                shopId: shop?.shopId,
                productVariant: getVariant(colorTicker),
                quantity: count,
                totalPrice: count * product?.salePrice?.amount
            }
            const addC = await addCart(CartItem);
            if (addC) {
                setShouldUpdate(prev => !prev);
                setShowCart(true);
                //kiểm tra trong 5s thêm giỏ hàng tắt arlet
                setTimeout(() => {
                    setShowCart(false);
                }, 1500);
            }
        }
    }

    return (
        <div className={cx("wrapper")}>
            <div className={cx("pt-20")}/>
            <Container>
                <div className={cx("product-wrapper")}>
                    <Grid container direction="row" spacing={2}>
                        <Grid container item md={5} sm={12}>
                            <div className={cx("box-image")}>
                                <Carousel className={cx('carousel')}
                                          autoPlay
                                          infiniteLoop
                                          showStatus={false}
                                          showIndicators={false}
                                          showThumbs={false}
                                          interval={2000}>
                                    { imageUrls?.map((item, i) =>
                                        <Paper className={cx('paper')}>
                                            <div className={cx("paper-wrap")}>
                                                <img className={cx("image-background")}
                                                     src={item}
                                                     alt={product?.name}
                                                     loading="lazy">

                                                </img></div>

                                        </Paper>)
                                }
                                </Carousel>

                            </div>
                        </Grid>

                        <Grid container item md={7} sm={12}>
                            <div className={cx("info-product")}>
                                <h3 className={cx("description")}>{product?.name}</h3>
                                <div className={cx("box-feedback")}>
                                    <div className={cx("star", "border-right")}>
                                        <span className={cx("amount-star")}>4</span>
                                        <AiFillStar/>
                                        <AiFillStar/>
                                        <AiFillStar/>
                                        <AiFillStar/>
                                        <AiFillStar/>
                                    </div>
                                    <div className={cx("evaluate", "border-right")}>
                                        <span className={cx("amount-evaluate")}>1,5k</span>
                                        <span>Đánh giá</span>
                                    </div>
                                    <div className={cx("evaluate")}>
                                        <span className={cx("amount-evaluate")}>1,5k</span>
                                        <span>Đã bán</span>
                                    </div>
                                </div>
                                <div className={cx("price")}>
                                    <span
                                        className={cx("old-price")}>
                                        {MoneyUtils.getMoney(product?.mediumPrice?.amount ? product?.mediumPrice?.amount : 0)}</span>
                                    <span className={cx("new-price")}>
                                        {MoneyUtils.getMoney(product?.salePrice?.amount ? product?.salePrice?.amount : 0)}
                                     </span>
                                    <span className={cx("sale")}>{product?.discount}% GIẢM</span>
                                </div>
                                {/*{isSize(variants) ?*/}
                                {/*    <h3 className={cx("title")}>*/}
                                {/*        Kích cỡ:{variants?.map(({id, size}) => (size !== null ?*/}
                                {/*            <button className={cx("product-variation")} key={id}>*/}
                                {/*                {size.toString()}*/}
                                {/*                <div style={{display: id === size ? "block" : "none"}}*/}
                                {/*                     className={cx("product-variation__tick")}>*/}
                                {/*                    <svg enable-background="new 0 0 12 12" viewBox="0 0 12 12" x="0"*/}
                                {/*                         y="0"*/}
                                {/*                         class={cx("shopee-svg-icon", "icon-tick-bold")}>*/}
                                {/*                        <g>*/}
                                {/*                            <path*/}
                                {/*                                d="m5.2 10.9c-.2 0-.5-.1-.7-.2l-4.2-3.7c-.4-.4-.5-1-.1-1.4s1-.5 1.4-.1l3.4 3 5.1-7c .3-.4 1-.5 1.4-.2s.5 1 .2 1.4l-5.7 7.9c-.2.2-.4.4-.7.4 0-.1 0-.1-.1-.1z"/>*/}
                                {/*                        </g>*/}
                                {/*                    </svg>*/}
                                {/*                </div>*/}
                                {/*            </button> : ""*/}
                                {/*    ))}*/}
                                {/*    </h3> : ""*/}
                                {/*}*/}
                                {/*{isWeight(variants) ?*/}
                                {/*    <h3 className={cx("title")}>*/}
                                {/*        Trọng lượng:{variants?.map(({id, weight, weightUnit}) => (*/}
                                {/*        <button className={cx("product-variation")}*/}
                                {/*                key={id}>{weight.toString() + " " + weightUnit.toString()}*/}
                                {/*            <div style={{display: id === weight ? "block" : "none"}}>*/}
                                {/*                <svg enable-background="new 0 0 12 12" viewBox="0 0 12 12" x="0" y="0"*/}
                                {/*                     class={cx("shopee-svg-icon", "icon-tick-bold")}>*/}
                                {/*                    <g>*/}
                                {/*                        <path*/}
                                {/*                            d="m5.2 10.9c-.2 0-.5-.1-.7-.2l-4.2-3.7c-.4-.4-.5-1-.1-1.4s1-.5 1.4-.1l3.4 3 5.1-7c .3-.4 1-.5 1.4-.2s.5 1 .2 1.4l-5.7 7.9c-.2.2-.4.4-.7.4 0-.1 0-.1-.1-.1z"/>*/}
                                {/*                    </g>*/}
                                {/*                </svg>*/}
                                {/*            </div>*/}
                                {/*        </button>*/}
                                {/*    ))}*/}
                                {/*    </h3> : ""*/}
                                {/*}*/}
                                {isColor(variants) ?
                                    <h3 className={cx("title")}>
                                        Màu:{variants?.map(({id, color}) => (
                                        <button onClick={() => handColorTicker(id)}
                                                className={cx("product-variation" + (id === colorTicker ? "-ticker" : "")
                                                )}
                                                key={id}>{ColorConvertor.convert(color)}
                                            <div style={{display: id === color ? "block" : "none"}}>
                                                <svg enable-background="new 0 0 12 12" viewBox="0 0 12 12" x="0" y="0"
                                                     class={cx("shopee-svg-icon", "icon-tick-bold")}>
                                                    <g>
                                                        <path
                                                            d="m5.2 10.9c-.2 0-.5-.1-.7-.2l-4.2-3.7c-.4-.4-.5-1-.1-1.4s1-.5 1.4-.1l3.4 3 5.1-7c .3-.4 1-.5 1.4-.2s.5 1 .2 1.4l-5.7 7.9c-.2.2-.4.4-.7.4 0-.1 0-.1-.1-.1z"/>
                                                    </g>
                                                </svg>
                                            </div>
                                        </button>
                                    ))}
                                        <span
                                            className={cx("error-ticker", errTicker
                                                ? "err-display" : "")}>Vui lòng chọn phân loại hàng</span>
                                    </h3> : ""
                                }
                                <Dialog open={showCart}>
                                    <DialogContent>
                                        <DialogContentText>
                                            Thêm vào giỏ hàng thành công
                                        </DialogContentText>
                                    </DialogContent>
                                </Dialog>
                                <div className={cx("quantity")}>
                                    <div className={cx("title")}>Vận chuyển</div>
                                    <LocalShippingOutlinedIcon/>
                                    <br/>
                                    <div className={cx("title")}>Vận chuyển tới</div>
                                    <br/>
                                    <div className={cx("title-address")}>{
                                        rsUser ? (rsUser?.address?.address1) :
                                            "Phường Linh Xuân, Thành Phố Thủ Đức, Hồ Chí Minh"}</div>
                                </div>
                                <div className={cx("quantity")}>
                                    <div className={cx("title")}>Phí Vận chuyển</div>
                                    <br/>
                                    <div className={cx("title")}>{carriers[0]?.name}</div>
                                    <div
                                        className={cx("title")}>{MoneyUtils.getMoney((feeShipping?.amount) ? feeShipping?.amount : 0)}</div>


                                </div>
                                <div className={cx("quantity")}>
                                    <div className={cx("title")}>Số lượng</div>
                                    <span
                                        onClick={decrementCount}
                                        className={cx("btn-function", "pl-20")}>-</span>
                                    <span className={cx("amount")}>{count}</span>
                                    <span onClick={incrementCount} className={cx("btn-function")}>+</span>
                                    <div className={cx("title")}> {product?.quantityAvailable} sản phẩm có sẵn
                                    </div>

                                </div>

                                <div className={cx("payment")}>
                  <span onClick={handCart} className={cx("btn-add__cart")}>
                    <BsCartPlus/>
                    Thêm vào giỏ hàng
                  </span>
                                    <button onClick={handCart} className={cx("btn-buy")}>
                                        Mua ngay
                                    </button>
                                </div>
                            </div>
                        </Grid>
                    </Grid>

                </div>
            </Container>
            <Container>
                <Grid container>
                    <div className={cx("product-wrapper")}>
                        <div className={cx("b-info-sh")}>
                            <Grid container>
                                <Grid style={{borderRight: "2px solid rgba(0,0,0,.09)"}} item container xs={6} md={4}>
                                    <Grid>
                                        <div className={cx("avatar", "border-radius")}>
                                            <div className={cx("grad", "border-radius", "spin")}/>
                                            <a href="#" className={cx("link")} id="profile">
                                                <img
                                                    src={shop?.imageUrl}
                                                    className={cx("avatar-img", "border-radius")}
                                                    alt={shop?.name}/>
                                                <div className={cx("live-marker")}>Yêu thích</div>
                                            </a>
                                        </div>
                                    </Grid>
                                    <Grid>
                                        <div>
                                            <div className={cx("n-sh-c")}>
                                                {shop?.name}
                                            </div>
                                            <Grid container>
                                                <Grid item> <span className={cx("a-sh-cr")}/></Grid>
                                                <Grid item>
                                                    <div className={cx("a-sh-c")}>
                                                        online
                                                    </div>
                                                </Grid>
                                            </Grid>
                                            <div>
                                                <button onClick={() => setChatOpen(true)} className={cx("btn-mes-now")}>
                                                    <svg viewBox="0 0 16 16"
                                                         className={cx("mellow-svg-icon", "JWAQyX")}>
                                                        <g fill-rule="evenodd">
                                                            <path
                                                                d="M15 4a1 1 0 01.993.883L16 5v9.932a.5.5 0 01-.82.385l-2.061-1.718-8.199.001a1 1 0 01-.98-.8l-.016-.117-.108-1.284 8.058.001a2 2 0 001.976-1.692l.018-.155L14.293 4H15zm-2.48-4a1 1 0 011 1l-.003.077-.646 8.4a1 1 0 01-.997.923l-8.994-.001-2.06 1.718a.5.5 0 01-.233.108l-.087.007a.5.5 0 01-.492-.41L0 11.732V1a1 1 0 011-1h11.52zM3.646 4.246a.5.5 0 000 .708c.305.304.694.526 1.146.682A4.936 4.936 0 006.4 5.9c.464 0 1.02-.062 1.608-.264.452-.156.841-.378 1.146-.682a.5.5 0 10-.708-.708c-.185.186-.445.335-.764.444a4.004 4.004 0 01-2.564 0c-.319-.11-.579-.258-.764-.444a.5.5 0 00-.708 0z"/>
                                                        </g>
                                                    </svg>
                                                    <span>Chat ngay</span>
                                                </button>
                                                <button className={cx("btn-eye-now")}>
                                                    <svg enable-background="new 0 0 15 15" viewBox="0 0 15 15" x="0"
                                                         y="0" stroke-width="0"
                                                         className={cx("mellow-svg-icon", "_9Sz-n3")}>
                                                        <path
                                                            d="m13 1.9c-.2-.5-.8-1-1.4-1h-8.4c-.6.1-1.2.5-1.4 1l-1.4 4.3c0 .8.3 1.6.9 2.1v4.8c0 .6.5 1 1.1 1h10.2c.6 0 1.1-.5 1.1-1v-4.6c.6-.4.9-1.2.9-2.3zm-11.4 3.4 1-3c .1-.2.4-.4.6-.4h8.3c.3 0 .5.2.6.4l1 3zm .6 3.5h.4c.7 0 1.4-.3 1.8-.8.4.5.9.8 1.5.8.7 0 1.3-.5 1.5-.8.2.3.8.8 1.5.8.6 0 1.1-.3 1.5-.8.4.5 1.1.8 1.7.8h.4v3.9c0 .1 0 .2-.1.3s-.2.1-.3.1h-9.5c-.1 0-.2 0-.3-.1s-.1-.2-.1-.3zm8.8-1.7h-1v .1s0 .3-.2.6c-.2.1-.5.2-.9.2-.3 0-.6-.1-.8-.3-.2-.3-.2-.6-.2-.6v-.1h-1v .1s0 .3-.2.5c-.2.3-.5.4-.8.4-1 0-1-.8-1-.8h-1c0 .8-.7.8-1.3.8s-1.1-1-1.2-1.7h12.1c0 .2-.1.9-.5 1.4-.2.2-.5.3-.8.3-1.2 0-1.2-.8-1.2-.9z"/>
                                                    </svg>
                                                    <span>Xem shop</span>
                                                </button>
                                            </div>
                                        </div>
                                    </Grid>
                                </Grid>
                                <Grid item xs={6} md={8}>
                                    <div style={{marginLeft: "0.3rem"}}>
                                        <Grid container spacing={{xs: 1, md: 2}} columns={{xs: 2, sm: 4, md: 15}}>
                                            {shopMembers.map(({id, name}) => (
                                                <Grid item xs={4} sm={8} md={2.5} key={id}>
                                                <span
                                                    style={{
                                                        fontStyle: "10px",
                                                        color:
                                                            !NumberUtils.isOdd(id) ? "#ff5588" : "#A1A1A1"
                                                    }}>{name}</span>
                                                </Grid>
                                            ))}
                                        </Grid>
                                    </div>

                                </Grid>
                            </Grid>
                        </div>
                    </div>
                    <div className={cx("product-wrapper")}>

                        <Grid item md={12}>
                            <div className={cx("description-product")}>
                                <div className={cx("header")}>
                                    <h3 className={cx("header-description")}>Mô tả sản phẩm</h3>
                                    <div className={cx("description-text")}>
                                        <p className={cx("text")}>
                                            {product?.description}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </Grid>
                    </div>
                </Grid></Container>

            <div className={cx("pb-20")}/>
            <Dialog open={chatOpen}
                    PaperProps={{
                        style: {
                            position: 'absolute',
                            bottom: '0',
                            right: '0',

                        },
                    }}>
                <DialogContent style={{padding: 0}}>
                    <DialogContentText style={{marginBottom: "7px", fontFamily: "inherit"}}>
                        <ChatService setChatOpen={setChatOpen}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>
            <ChatButton setChatOpen={setChatOpen}/>
        </div>
    );
}

export default ProductDetail;
