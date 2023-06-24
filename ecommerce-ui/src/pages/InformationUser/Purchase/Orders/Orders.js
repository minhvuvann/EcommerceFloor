import React, {useContext} from 'react';

import classNames from "classnames/bind";

import styles from "./Orders.module.scss";
import {Button, createTheme, Grid, ThemeProvider} from "@mui/material";
import {ChatBubbleOutline, LocalShippingOutlined, PriceCheckOutlined, StorefrontOutlined} from "@mui/icons-material";
import ItemOrder from "~/pages/InformationUser/Purchase/Orders/ItemOrder/ItemOrder";
import ConvertorStatus from "~/utils/order/ConvertorStatus";
import MoneyUtils from "~/utils/MoneyUtils";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function Orders(props) {
    const {orderDetail} = props;
    const {localeList} = useContext(LanguageContext);

    const theme = createTheme({
        palette: {
            primary: {
                // Purple and green play nicely together.
                main: '#ff5588 ',
            },
            secondary: {
                // This is green.A700 as hex.
                main: '#e3f2fd',
            },
        },
    });
    return (
        <div className={cx('orders')}>
            <div className={cx('header')}>

                <Grid container rowSpacing={1} columnSpacing={{xs: 1, sm: 2, md: 3}}>
                    <Grid item xs={7.5}>
                        <div className={cx('header-left')}>
                            <Grid container spacing={2}>
                                <Grid item xs={1.5}>
                                    <p className={cx('like')}>{localeList['info_like']}</p>
                                </Grid>
                                <Grid item xs={4}>
                                    <p>{orderDetail?.order?.shopName}</p>
                                </Grid>
                                <Grid item xs={6}>
                                    <Grid container rowSpacing={1} columnSpacing={{xs: 1, sm: 2, md: 3}}>
                                        <Grid item xs={3}>
                                            <ThemeProvider theme={theme}>
                                                <Button variant="contained" color='primary'><ChatBubbleOutline
                                                    sx={{fontSize: 16}}/> Chat</Button>
                                            </ThemeProvider>
                                        </Grid>
                                        <Grid item xs={3}>
                                            <ThemeProvider theme={theme}>
                                                <Button className={cx('view-shop')} variant="outlined" color='primary'
                                                        sx={{width: 95}}><StorefrontOutlined
                                                    sx={{fontSize: 16}}/> {localeList['info_see_shop']}</Button>
                                            </ThemeProvider>
                                        </Grid>

                                    </Grid>
                                </Grid>
                            </Grid>
                        </div>
                    </Grid>
                    <Grid item xs={4.5}>
                        <div className={cx('header-right')}>
                            <Grid container spacing={0} columnSpacing={{xs: 1, sm: 2, md: 3}}>
                                <Grid item xs={6}>
                                </Grid>
                                <Grid justifyContent={"center"} item xs={6}>
                                    <p className={cx("status-order")}>{
                                        ConvertorStatus.convert(localeList,orderDetail?.order?.status)}</p>
                                </Grid>

                            </Grid>
                        </div>
                    </Grid>

                </Grid>
            </div>
            <div className={cx('order-item')}>
                {orderDetail?.orderItems.map((orderItem) => (
                    <ItemOrder orderItem={orderItem}/>
                ))}

            </div>

            <div className={cx('label-order-bottom')}>
                <div className={cx('label-order-bottom-price')}>
                    <Grid container spacing={0} columnSpacing={{xs: 1, sm: 2, md: 3}}>
                        <Grid item xs={1}>

                        </Grid>
                        <Grid item xs={11}>
                            <p>{localeList['label_money']}:<span style={{color: '#f75583', marginLeft: '3px', fontSize: 18}}>
                                {MoneyUtils.getMoney(orderDetail?.order?.totalPrice?.amount)}
                            </span>
                            </p>

                        </Grid>
                    </Grid>

                </div>

            </div>
        </div>
    );
}

export default Orders;
