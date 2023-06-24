import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {Avatar, Grid, NativeSelect} from "@mui/material";

import styles from "./RegisterSeller.module.scss";
import {Link} from "react-router-dom";
import config from "~/config";
import {LogoReverse} from "~/components/Icon";
import Tippy from "@tippyjs/react";
import {IoIosLogOut} from "react-icons/io";
import {UserContext} from "~/config/provider/UserProvider";
import UpLoadFileImage from "~/components/UploadFileImage";
import {Dialog, DialogContent, DialogContentText, FormControl, TextField} from "@material-ui/core";
import {ShopContext} from "~/config/provider/ShopProvider";
import {createShop, getDistricts, getProvinces, getWards} from "~/services/workspaces.sevices";
import {getDownloadURL, ref, uploadBytes} from "firebase/storage";
import {storage} from "~/firebase";
import {nanoid} from "nanoid";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function RegisterSeller(props) {
    const {customer} = useContext(UserContext);
    const [showButton, setShowButton] = useState(true);
    const [image, setImage] = useState([]);
    const [open, setOpen] = useState(true);
    const [provinceList, setProvinceList] = useState();
    const [province, setProvince] = useState();
    const [districtList, setDistrictList] = useState();
    const [district, setDistrict] = useState();
    const [wardList, setWardList] = useState();
    const [ward, setWard] = useState();
    const [shopName, setShopName] = useState();
    const [description, setDescription] = useState();
    const [shopAddress, setShopAddress] = useState();
    const [telephone, setTelephone] = useState();
    const [errorP, setErrorP] = useState(false);
    const [errorN, setErrorN] = useState(false);
    const [errorD, setErrorD] = useState(false);
    const [contact, setContact] = useState(false);
    const {shop, setShouldUpdate} = useContext(ShopContext);
    const {localeList} = useContext(LanguageContext);


    const handleProvince = async (event) => {
        setProvince(event.target.value);
        const state = await getDistricts(event.target.value);
        const stateD = await getWards(state?.data[0].ghn_id);
        setDistrict(state?.data[0].ghn_id);
        setDistrictList(state?.data);
        setWardList(stateD?.data);

    };
    const handleDistrict = async (event) => {
        const state = await getWards(event.target.value);
        setDistrict(event.target.value);
        setWard(state?.data[0].ghn_id);
        setWardList(state?.data);
    }
    const handleWard = (event) => {
        setWard(event.target.value);
    }


    const maxNumber = 1;
    const onChange = (img, addUpdateIndex) => {
        setImage(img);
        setShowButton(false)
        setOpen(false);
    };
    useEffect(() => {
        async function fetchData() {
            const provinceData = await getProvinces();
            const stateD = await getDistricts(provinceData?.data[0]?.ghn_id);
            const stateW = await getWards(stateD?.data[0].ghn_id);
            setProvinceList(provinceData?.data);
            setDistrictList(stateD?.data);
            setWardList(stateW?.data);
            setProvince(provinceData?.data[0].ghn_id);
            setDistrict(stateD?.data[0].ghn_id);
            setWard(stateW?.data[0].ghn_id);
            console.log(image);
        }

        fetchData();

    }, []);


    const logOut = () => {
        localStorage.clear();
        window.location.replace(config.routes.home);
    }

    const saveSeller = async () => {
        if (image && image.length !== 0
            && !errorN && !errorD && !errorP) {
            const imgRef = ref(storage, `images/${customer?.id + '_' + nanoid()}`);

            const resFire = await uploadBytes(imgRef, image[0].file);
            let imgUrl = await getDownloadURL(resFire.ref);
            const body = {
                name: shopName,
                imageUrl: imgUrl,
                description: description,
                wardCode: ward,
                district_id: district,
                phone: telephone,
                address: shopAddress,
                addressShop: {
                    address1: shopAddress,
                    provinceCode: province,
                    districtCode: district,
                    wardCode: ward
                },
            }
            setContact(true);
            const response = await createShop(customer?.id, body);
            setShouldUpdate(prev => !prev);
            localStorage.setItem("cs-obj", JSON.stringify(response?.data));
            console.log(response);
            setTimeout(() => {
                setContact(false);
                window.location.href = config.routes.sellerManager;
            }, 3500);
        }

    }
    return (
        <div className={cx('wrapper')}>
            <Dialog open={contact}>
                <DialogContent style={{paddingTop: "8px"}}>
                    <DialogContentText style={{marginBottom: 0, fontFamily: "inherit"}}>
                        <div style={{
                            display: 'block'
                        }} className={cx("err-lg-not-found")}>
                            <div className={cx("ic-err-lg")}>
                                <i style={{
                                    fill: "#5c7",
                                    width: "16px"
                                }}>
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16">
                                        <path fillRule="evenodd"
                                              d="M8,1 C11.8659932,1 15,4.13400675 15,8 C15,11.8659932 11.8659932,15 8,15 C4.13400675,15 1,11.8659932 1,8 C1,4.13400675 4.13400675,1 8,1 Z M11.1464466,5.92870864 L7.097234,9.97792125 L4.85355339,7.73424065 C4.65829124,7.5389785 4.34170876,7.5389785 4.14644661,7.73424065 C3.95118446,7.92950279 3.95118446,8.24608528 4.14644661,8.44134743 L6.7436806,11.0385814 C6.93894275,11.2338436 7.25552524,11.2338436 7.45078739,11.0385814 L11.8535534,6.63581542 C12.0488155,6.44055327 12.0488155,6.12397078 11.8535534,5.92870864 C11.6582912,5.73344649 11.3417088,5.73344649 11.1464466,5.92870864 Z"/>
                                    </svg>
                                </i>
                            </div>

                            <div style={{fontWeight: 0}} className={cx("tx-err-lg")}>Lưu thành công
                            </div>
                        </div>
                    </DialogContentText>
                </DialogContent>
            </Dialog>
            <div className={cx('header-wrapper')}>
                <div className={cx('logo')}>
                    <div>
                        <Link to={config.routes.home}>
                            <LogoReverse className={cx('logo-image')}/>
                        </Link>
                    </div>
                    <h4 className={cx('logo-text')}>{localeList['register_seller_title']}</h4>
                </div>
                <Tippy
                    offset={[12, 10]}
                    interactive
                    render={attrs => (
                        <div className={cx('profile')} tabIndex="-1" {...attrs}>
                            <Link className={cx('profile-item')} onClick={logOut}>
                                <IoIosLogOut/>
                                Đăng xuất
                            </Link>
                        </div>
                    )}
                >
                    <div className={cx('info-user')}>
                        <Avatar
                            alt={customer?.username}
                            src={customer?.imageUrl}
                            sx={{width: 30, height: 30}}
                        />
                        <span className={cx('name')}>{customer?.username}</span>
                    </div>
                </Tippy>

            </div>

            <div className={cx("wrapper-content")}>
                <h3 className={cx("header-content")}>{localeList['seller_shop_info_basic']}</h3>
                <div style={{marginTop: "20px"}} className={cx("form-function")}>
                    <Grid container>
                        <Grid container item xs={12} md={12}>
                            <div className={cx("form-item")}>
                                <div className={cx("label")}>
                                    <span className={cx("requirement")}>* </span>{localeList['seller_shop_name']}
                                </div>
                                <div className={cx('border-input-item')}>
                                    <input style={{
                                        background: errorN ? "#fff6f7" : "",
                                        borderColor: errorN ? "#ff424f" : ""
                                    }} onChange={(event => {
                                        setErrorN(event.target.value.length === 0);
                                        setShopName(event.target.value);
                                    })} placeholder={localeList['register_seller_shop_enter_name']}
                                           className={cx("input-item")} type="text"/>
                                    <div style={{
                                        display: !errorN ? 'none' : 'flex'
                                    }} className={cx("err-lg")}>
                                        {localeList['register_seller_shop_valid_name']}
                                    </div>
                                </div>
                            </div>
                        </Grid>
                        <Grid container item xs={12} md={12}>
                            <div className={cx("form-item")}>
                                <div className={cx("label")}>
                                    <span className={cx("requirement")}>* </span>{localeList['info-telephone']}
                                </div>
                                <div className={cx('border-input-item')}>
                                    <input style={{
                                        background: errorP ? "#fff6f7" : "",
                                        borderColor: errorP ? "#ff424f" : ""
                                    }} onChange={(event => {
                                        setErrorP(event.target.value.length === 0);
                                        setTelephone(event.target.value);
                                    })} placeholder={localeList['register_seller_enter_telephone']}
                                           className={cx("input-item")} type="text"/>
                                    <div style={{
                                        display: !errorP ? 'none' : 'flex'
                                    }} className={cx("err-lg")}>
                                        {localeList['register_seller_valid_telephone']}
                                    </div>
                                </div>
                            </div>
                        </Grid>
                        <Grid container item xs={12} md={12}>
                            <div style={{marginBottom: "10px"}} className={cx("box-image")}>
                                <h3 className={cx("label")}>
                                    <span className={cx("requirement")}>*</span>Logo shop
                                </h3>
                                <UpLoadFileImage imageProduct={shop?.imageUrl}
                                                 showButton={showButton}
                                                 open={open}
                                                 buttonChose={localeList['info_chose_image']}
                                                 buttonUpdate={localeList['info_update_image']}
                                                 images={image}
                                                 name={shopName}
                                                 maxNumber={maxNumber}
                                                 onChange={onChange}/>
                            </div>
                        </Grid>
                        <Grid container item xs={12} md={12}>
                            <div className={cx("form-item")}>
                                <div className={cx("label")}>
                                    <span className={cx("requirement")}>* </span>{localeList['info_address']}
                                </div>
                                <div style={{flex: 1}}>
                                    <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                                          justifyContent={"flex-start"}>
                                        <Grid container item xs={12} md={1.5}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={province}
                                                    onChange={handleProvince}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{provinceList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                        <Grid item xs={12} md={1.5}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={district}
                                                    onChange={handleDistrict}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{districtList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                        <Grid item xs={12} md={1.5}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={ward}
                                                    onChange={handleWard}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{wardList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                    </Grid>
                                    <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                                          justifyContent={"flex-start"}>

                                        <Grid container item xs={12} md={5}>
                                            <TextField
                                                style={{
                                                    width: "100%",
                                                    marginRight: "10%"
                                                }}
                                                id="filled-multiline-static"
                                                label={localeList['info_address_detail']}
                                                multiline
                                                rows={2}
                                                value={shopAddress}
                                                onChange={(event) =>
                                                    setShopAddress(event.target.value)}
                                                variant="filled"
                                            />
                                        </Grid>
                                    </Grid>
                                </div>
                            </div>

                        </Grid>
                        <Grid container item xs={12} md={12}>
                            <div className={cx("form-item")}>
                                <div className={cx("label")}>
                                    <span className={cx("requirement")}>* </span>{localeList['seller_shop_description']}
                                </div>
                                <div className={cx('border-input-item')}>

                                    <textarea style={{
                                        background: errorD ? "#fff6f7" : "",
                                        borderColor: errorD ? "#ff424f" : ""
                                    }} onChange={(e) => {
                                        setErrorD(e.target.value.length === 0);
                                        setDescription(e.target.value);
                                    }} className={cx("input-area")} rows="4" cols="50"/>

                                    <div style={{
                                        display: !errorD ? 'none' : 'flex'
                                    }} className={cx("err-lg")}>
                                        {localeList['register_seller_valid_description']}
                                    </div>
                                </div>
                            </div>
                        </Grid>
                    </Grid>
                    <button onClick={saveSeller} style={{
                        marginTop: "20px",
                        marginBottom: "20px",
                        marginLeft: "99.5rem"
                    }} className={cx("btn-add")}>
                        {localeList['button_save_view']}
                    </button>
                </div>
            </div>

        </div>
    );
}


export default RegisterSeller;
