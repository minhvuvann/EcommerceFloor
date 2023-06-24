import React, {useEffect, useState} from 'react';
import classNames from "classnames/bind";
import styles from "./SellerManager.module.scss";
import Header from "~/pages/SellerManager/Header";
import {Grid} from "@mui/material";
import ContentSellerManager from "~/pages/SellerManager/ContentSellerManager";


const cx = classNames.bind(styles);

function SellerManagerWrapper({child}) {
    const [shopId, setShopId] = useState(null);
    useEffect(
        () => {
            setShopId(localStorage.getItem("seller-id"));
        }, shopId
    )
    return (
        <div className={cx('wrapper')}>
            <Header/>
            <Grid container>
                <Grid item md={12} sm={12}>
                    <ContentSellerManager shopId={shopId} child={child}/>
                </Grid>
            </Grid>

        </div>
    );
}

export default SellerManagerWrapper;
