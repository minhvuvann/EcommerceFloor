import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {Grid, NativeSelect} from "@mui/material";

import styles from "./AddEditProduct.module.scss";
import UpLoadFileImage from "~/components/UploadFileImage";
import {FormControl} from "@material-ui/core";
import CurrencyTextField from '@unicef/material-ui-currency-textfield'
import {getIndustrials, getTrackMarks} from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function AddProduct(props) {
    const [showButton, setShowButton] = useState(true);
    const [images, setImages] = useState([]);
    const [industrialId, setIndustrialId] = useState();
    const [open, setOpen] = useState(true);
    const [industrials, setIndustrials] = useState();
    const [trackMarkId, setTrackMarkId] = useState();
    const [trackMarks, setTrackMarks] = useState();
    const {localeList} = useContext(LanguageContext);
    const [productName, setProductName] = useState();

    const maxNumber = 1;
    useEffect(() => {

        async function load() {
            const industry = await getIndustrials();
            const trackmarkD = await getTrackMarks(industry?.data[0]?.id);
            setIndustrials(industry?.data);
            setIndustrialId(industry?.data[0]?.id);
            setTrackMarkId(trackmarkD?.data[0]?.id);
            setTrackMarks(trackmarkD?.data);


        }

        load();
    }, []);
    const onChange = (img, addUpdateIndex) => {
        setImages(img);
        setShowButton(false)
        setOpen(false);
    };
    const handleIndustrials = async (event) => {
        setIndustrialId(event.target.value);
        const trackmarkD = await getTrackMarks(event.target.value);
        setTrackMarkId(trackmarkD?.data[0]?.id);
        setTrackMarks(trackmarkD?.data);

    }
    return (
        <div className={cx("wrapper")}>
            <h3 className={cx("header")}>{localeList['seller_shop_info_basic']}</h3>
            <div className={cx("form-function")}>
                <Grid container>
                    <Grid style={{marginBottom:"10px"}} container item xs={12} md={12}>
                        <Grid container item xs={12} md={2} justifyContent={'flex-start'}>
                            <h3 className={cx("label")}>
                                <span className={cx("requirement")}>*</span> {localeList['add_product_image']}
                            </h3>
                        </Grid>
                        <Grid item xs={12} md={10}>
                            <UpLoadFileImage
                                showButton={showButton}
                                open={open}
                                buttonChose={localeList['info_chose_image']}
                                buttonUpdate={localeList['info_update_image']}
                                images={images}
                                name={productName}
                                maxNumber={maxNumber}
                                onChange={onChange}/>
                        </Grid>
                    </Grid>
                    <Grid container item xs={12} md={12}>
                        <div className={cx("form-item")}>
                            <div className={cx("label")}>
                                <span className={cx("requirement")}>* </span>{localeList['add_product_name']}
                            </div>
                            <input placeholder={localeList['add_product_enter_name']} className={cx("input-item")} type="text"/>
                        </div>
                    </Grid>
                    <Grid container item xs={12} md={12}>
                        <div className={cx("form-item")}>
                            <div className={cx("label")}>
                                <span className={cx("requirement")}>* </span> {localeList['add_product_industry']}
                            </div>
                            <FormControl style={{width: "15%"}}>
                                <NativeSelect
                                    labelId="demo-multiple-name-label"
                                    id="demo-multiple-name"
                                    value={industrialId}
                                    onChange={handleIndustrials}
                                    inputProps={{
                                        name: 'values',
                                        id: 'values',
                                    }}
                                >
                                    {industrials?.map(({id, name}) => (
                                        <option value={id}>{name}</option>
                                    ))}

                                </NativeSelect>
                            </FormControl>
                            <div className={cx("label")}>
                                <span className={cx("requirement")}>* </span>{localeList['add_product_trackmaker']}
                            </div>
                            <FormControl style={{width: "15%"}}>
                                <NativeSelect
                                    labelId="demo-multiple-name-label"
                                    id="demo-multiple-name"
                                    value={trackMarkId}
                                    onChange={(event) => setTrackMarkId(
                                        event.target.value
                                    )}
                                    inputProps={{
                                        name: 'values',
                                        id: 'values',
                                    }}
                                >
                                    {trackMarks?.map(({id, name}) => (
                                        <option value={id}>{name}</option>
                                    ))}
                                </NativeSelect>
                            </FormControl>
                        </div>

                    </Grid>
                    <Grid container item xs={12} md={12}>
                        <div className={cx("form-item")}>
                            <div className={cx("label")}>
                                <span className={cx("requirement")}>* </span>{localeList['add_product_description']}
                            </div>
                            <textarea className={cx("input-area")} rows="4" cols="50"/>
                        </div>
                    </Grid>
                    <Grid container item xs={12} md={12}>
                        <div className={cx("form-item")}>
                            <div className={cx("label")}>
                                <span className={cx("requirement")}>* </span> {localeList['add_product_price']}
                            </div>
                            <CurrencyTextField
                                label={localeList['add_product_enter_price']}
                                variant="standard"
                                currencySymbol="₫"
                                outputFormat="number"
                                style={{
                                    focused: {
                                        color: "white"
                                    }
                                }}
                                className={cx("currency-text")}
                            />
                        </div>
                    </Grid>
                </Grid>
                <button style={{
                    marginTop: "20px",
                    marginLeft: "100rem"
                }} className={cx("btn-add")}>
                    {localeList['button_save_view']}
                </button>
            </div>
        </div>
    );
}


export default AddProduct;
