import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import styles from "./MainContent.module.scss";
import {Grid} from "@mui/material";
import {useMediaQuery} from "react-responsive";
import {filterOrder, getProductFilter, getShopById} from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function MainContent(props) {
    const shopId = localStorage.getItem("seller-id");
    const [orderReady, setOrderReady] = useState();
    const [orderDelivery, setOrderDelivery] = useState();
    const [orderCompleted, setOrderCompleted] = useState();
    const [orderCancel, setOrderCancel] = useState();
    const [productAll, setProductAll] = useState();
    const {localeList} = useContext(LanguageContext);

    const listWorks = [
        {id: 1, name: localeList['status_order_wating_process'], amount: orderReady?.length},
        {id: 2, name: localeList['status_order_delivering'], amount: orderDelivery?.length},
        {id: 3, name: localeList['status_order_delivered'], amount: orderCompleted?.length},
        {id: 4, name: localeList['status_order_cancelled'], amount: orderCancel?.length},
        {id: 5, name: localeList['tab_product'], amount: productAll?.length},
        {id: 6, name: localeList['status_product_active'], amount: productAll?.length},
        {id: 7, name: localeList['status_product_over'], amount: 0},
    ]
    useEffect(() => {
        const body_ready = {
            shopId: shopId,
            type: "SELL",
            status: "READY"

        };
        filterOrder(body_ready).then((res) => setOrderReady(res.data.resultList));
        const body_delivery = {
            shopId: shopId,
            type: "SELL",
            status: "DELIVERY_ONLY"

        };
        filterOrder(body_delivery).then((res) => setOrderDelivery(res.data.resultList));
        const body_completed = {
            maxResult: 100,
            shopId: shopId,
            type: "SELL",
            status: "DELIVERED"

        };
        filterOrder(body_completed).then((res) => setOrderCompleted(res.data.resultList));
        const body_cancel = {
            maxResult: 100,
            shopId: shopId,
            type: "SELL",
            status: "CANCELLED",

        };
        filterOrder(body_cancel).then((res) => setOrderCancel(res.data.resultList));
        const body_product_all = {
            shopId: shopId
        };
        getProductFilter(body_product_all).then((res) => setProductAll(res.data.resultList));
    }, [shopId]);
    const isMinMobile = useMediaQuery({maxWidth: 600})
    return (
        <div className={cx('wrapper')}>
            <div className={cx('list-work')}>
                <h2 className={cx('title')}>{localeList['seller_statistic']}</h2>
                <p className={cx('text')}>{localeList['seller_order_product']}</p>
                <div className={cx('box-function')}>
                    <Grid container spacing={2} flexDirection={isMinMobile ? "column" : 'row'} alignItems='center'
                          justifyContent='center'>
                        {
                            listWorks.map((works, index) => {
                                return (
                                    <Grid style={{width: '100%'}} key={works.id} item md={3} sm={isMinMobile ? 12 : 6}>
                                        <div className={cx('work-item')}>
                                            <span className={cx('amount')}>{works.amount}</span>
                                            <span className={cx('status')}>{works.name}</span>
                                        </div>
                                    </Grid>
                                )
                            })
                        }


                    </Grid>


                </div>
            </div>
        </div>
    );
}

export default MainContent;
