import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import {Tabs, Tab, Box, createTheme, ThemeProvider, useTheme, NativeSelect} from "@mui/material";
import TabPanel from '@mui/lab/TabPanel'
import TabContext from '@mui/lab/TabContext';
import Grid from '@mui/material/Grid';
import {useMediaQuery} from "react-responsive";
import {storage} from "~/firebase";
import {
    ref,
    uploadBytes,
    getDownloadURL,
    listAll,
    list,
} from "firebase/storage";

import styles from "./ShopProfile.module.scss";
import {themeCustomer} from "~/components/CustomerMaterial";
import UpLoadFileImage from "~/components/UploadFileImage";
import {
    getDistricts,
    getProvinces,
    getWards,
    updateShop,
    updateShopAddress
} from "~/services/workspaces.sevices";
import {useParams} from "react-router-dom";
import {FormControl, TextField} from "@material-ui/core";
import {nanoid} from "nanoid";
import {ShopContext} from "~/config/provider/ShopProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function ShopProfile(props) {
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {shopId} = useParams();
    const theme = createTheme(themeCustomer);
    const [valueTab, setValueTab] = useState('1');
    const [image, setImage] = useState([]);
    const [open, setOpen] = useState(true);
    const [showButton, setShowButton] = useState(true);
    const [provinceList, setProvinceList] = useState();
    const [province, setProvince] = useState();
    const [districtList, setDistrictList] = useState();
    const [district, setDistrict] = useState();
    const [wardList, setWardList] = useState();
    const [ward, setWard] = useState();
    const [shopName, setShopName] = useState();
    const [description, setDescription] = useState();
    const [shopAddress, setShopAddress] = useState();
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
        console.log(img);
        setShowButton(false)
        setOpen(false);
    };
    useEffect(() => {
        async function fetchData() {
            setShopName(shop?.name);
            setDescription(shop?.description);
            const provinceData = await getProvinces();
            const state = await getDistricts(shop?.address?.provinceCode);
            const stateD = await getWards(shop?.address?.districtCode);
            setProvinceList(provinceData?.data);
            setDistrictList(state?.data);
            setWardList(stateD?.data);
            setProvince(shop?.address?.provinceCode);
            setDistrict(shop?.address?.districtCode);
            setWard(shop?.address?.wardCode);
            setShopAddress(shop?.address?.address1);
        }

        fetchData();

    }, []);

    const handleChange = (event, newValue) => {
        setValueTab(newValue);
    };
    const uploadImgFirebase = async () => {
        const imgRef = ref(storage, `images/${shopId + '_' + nanoid()}`);
        if (image && image.length !== 0) {
            uploadBytes(imgRef, image[0].file).then(response => {
                getDownloadURL(response.ref).then(async (url) => {
                    const body = {
                        description: description,
                        imageUrl: url,
                        name: shopName,
                    }
                    const shopUpdate = await updateShop(rsUser.id, shopId, body);
                    setShouldUpdate(prev => !prev);

                })
            });
        }
        const body = {
            description: description,
            name: shopName,
        }
        const shopUpdate = await updateShop(rsUser.id, shopId, body);
        setShouldUpdate(prev => !prev);

    }
    const saveAddress = async () => {
        const shopSave = await updateShopAddress(province, district, ward, shopAddress, shopId);
        setShouldUpdate(prev => !prev);
        setShopAddress(shopSave?.data?.address?.address1);
        setWard(shopSave?.data.address?.wardCode);
        setDistrict(shopSave?.data?.address?.districtCode);
        setProvince(shopSave?.data?.address?.provinceCode);
    }


    const isTablet = useMediaQuery({maxWidth: 900})
    return (
        <div className={cx('wrapper')}>
            <h3 className={cx('title')}>{localeList['seller_shop_profile']}</h3>
            <span className={cx('notify')}>{localeList['seller_shop_label']}</span>
            <TabContext value={valueTab}>
                <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                    <ThemeProvider theme={theme}>
                        <Tabs onChange={handleChange}
                              value={valueTab}
                              textColor="primary"
                              indicatorColor="primary"
                        >
                            <Tab
                                label={localeList['seller_shop_info_basic']}
                                value='1'
                                sx={{fontSize: "1.6rem", textTransform: 'none'}}
                            />

                            <Tab
                                label={localeList['seller_shop_address']}
                                value='2'
                                sx={{fontSize: "1.6rem", textTransform: 'none'}}
                            />

                        </Tabs>
                    </ThemeProvider>
                </Box>
                <TabPanel value="1">
                    <div className={cx('shop-profile')}>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={3} justifyContent={isTablet ? 'flex-start' : "flex-end"}>
                                <h3 className={cx('title')}>{localeList['seller_shop_name']}</h3>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <input className={cx('input')} value={shopName}
                                       onChange={(event) => setShopName(
                                           event.target.value
                                       )}
                                       type="text"/>
                            </Grid>
                        </Grid>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={3} justifyContent={isTablet ? 'flex-start' : "flex-end"}>
                                <h3 className={cx('title')}>Logo shop</h3>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <UpLoadFileImage imageProduct={shop?.imageUrl}
                                                 showButton={showButton}
                                                 open={open}
                                                 buttonChose={localeList['info_chose_image']}
                                                 buttonUpdate={localeList['info_update_image']}
                                                 images={image}
                                                 name={shopName}
                                                 maxNumber={maxNumber}
                                                 onChange={onChange}/>
                            </Grid>
                        </Grid>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={3} justifyContent={isTablet ? 'flex-start' : "flex-end"}>
                                <h3 className={cx('title')}>{localeList['seller_shop_description']}</h3>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <textarea rows="4"
                                          value={description}
                                          onChange={(event) => setDescription(
                                              event.target.value
                                          )}
                                          cols="50" style={{height: '100px'}}
                                          className={cx('input')}/>
                            </Grid>
                        </Grid>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={3} justifyContent={isTablet ? 'flex-start' : "flex-end"}>

                            </Grid>
                            <Grid item xs={12} md={6}>
                                <button onClick={uploadImgFirebase} style={{marginTop: "20px", width: '19%'}}
                                        className='btn-add'>{localeList['button_save']}
                                </button>
                            </Grid>
                        </Grid>

                    </div>
                </TabPanel>
                <TabPanel value="2">
                    <div className={cx('shop-profile')}>
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

                            <Grid container item xs={12} md={4}>
                                <TextField
                                    style={{
                                        width: "100%",
                                        marginLeft: "10%"
                                    }}
                                    id="filled-multiline-static"
                                    label="Địa chỉ chi tiết"
                                    multiline
                                    rows={2}
                                    value={shopAddress}
                                    onChange={(event) =>
                                        setShopAddress(event.target.value)}
                                    variant="filled"
                                />
                            </Grid>
                        </Grid>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={1.5} justifyContent={isTablet ? 'flex-start' : "flex-end"}>

                            </Grid>
                            <Grid item xs={12} md={6}>
                                <button onClick={saveAddress} style={{marginTop: "20px", width: '120px'}}
                                        className='btn-add'>{localeList['button_save']}
                                </button>
                            </Grid>
                        </Grid>

                    </div>
                </TabPanel>

            </TabContext>

        </div>
    );
}

export default ShopProfile;
