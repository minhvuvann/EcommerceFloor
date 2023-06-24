import React, {useContext, useEffect, useState} from 'react';

import classNames from "classnames/bind";
import styles from "./Purchase.module.scss";


import {Box, Container, createTheme, Tab, Tabs, ThemeProvider} from "@mui/material";
import {Link} from "react-router-dom";
import {Menu, MenuItem, Sidebar,} from "react-pro-sidebar";
import Orders from "~/pages/InformationUser/Purchase/Orders/Orders";
import TabPanel from "@mui/lab/TabPanel";
import Grid from "@mui/material/Grid";
import UpLoadFileImage from "~/components/UploadFileImage";
import {TabContext} from "@mui/lab";
import {themeCustomer} from "~/components/CustomerMaterial";
import {filterOrder, getOrderDetails} from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function Purchase(props) {
    const theme = createTheme(themeCustomer);
    const [valueTab, setValueTab] = useState('1');
    const [orderAll, setOrderAll] = useState([]);
    const [orderReady, setOrderReady] = useState([]);
    const [orderDelivery, setOrderDelivery] = useState([]);
    const [orderCompleted, setOrderCompleted] = useState([]);
    const [orderCancelled, setOrderCancelled] = useState([]);
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {localeList} = useContext(LanguageContext);


    useEffect(() => {
        let OrderInput = {
            userId: rsUser.id,
            type: "PURCHASE"
        }

        async function loadData() {
            //tất cả đơn mua
            const allData = await filterOrder(OrderInput);
            const listOrderALl = [];
            const listOrderProcess = [];
            const listOrderDelivery = [];
            const listOrderCompleted = [];
            const listOrderCancelled = [];
            for (const item of allData?.data?.resultList) {
                const orderDetail = await getOrderDetails(item.id);
                listOrderALl.push(orderDetail?.data);
                if (item.status === "READY") {
                    listOrderProcess.push(orderDetail?.data);
                }
                if (item.status === "DELIVERY_ONLY") {
                    listOrderDelivery.push(orderDetail?.data);
                }
                if (item.status === "DELIVERED") {
                    listOrderCompleted.push(orderDetail?.data);
                }
                if (item.status === "CANCELLED") {
                    listOrderCancelled.push(orderDetail?.data);
                }


            }
            setOrderAll(listOrderALl);
            setOrderReady(listOrderProcess);
            setOrderDelivery(listOrderDelivery);
            setOrderCompleted(listOrderCompleted);
            setOrderCancelled(listOrderCancelled);


        }

        loadData();

    }, []);

    const handleChange = (event, newValue) => {
        setValueTab(newValue);
    };

    return (
        <div className={cx('wrapper')}>
            <Container>
                <div className={cx('purchase')}>
                    <TabContext value={valueTab}>
                        <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                            <ThemeProvider theme={theme}>
                                <Tabs onChange={handleChange}
                                      value={valueTab}
                                      textColor="primary"
                                      indicatorColor="primary"
                                >
                                    <Tab
                                        label={localeList['status_order_all']}
                                        value='1'
                                        sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                    />
                                    <Tab
                                        label={localeList['status_order_wating_process']}
                                        value='2'
                                        sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                    />
                                    <Tab
                                        label={localeList['status_order_delivering']}
                                        value='3'
                                        sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                    />

                                    <Tab
                                        label={localeList['status_order_delivered']}
                                        value='4'
                                        sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                    />

                                    <Tab
                                        label={localeList['status_order_cancelled']}
                                        value='5'
                                        sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                    />


                                </Tabs>
                            </ThemeProvider>
                        </Box>
                        <TabPanel value="1">
                            <div className={cx('items')}>
                                {orderAll.map((orderDetail) => (
                                    <Orders orderDetail={orderDetail}/>
                                ))
                                }
                            </div>
                        </TabPanel>
                        <TabPanel value="2">
                            <div className={cx('items')}>
                                {orderReady.map((orderDetail) => (
                                    <Orders orderDetail={orderDetail}/>
                                ))
                                }
                            </div>
                        </TabPanel>
                        <TabPanel value="3">
                            <div className={cx('items')}>
                                {orderDelivery.map((orderDetail) => (
                                    <Orders orderDetail={orderDetail}/>
                                ))
                                }
                            </div>
                        </TabPanel>
                        <TabPanel value="4">
                            <div className={cx('items')}>
                                {orderCompleted.map((orderDetail) => (
                                    <Orders orderDetail={orderDetail}/>
                                ))
                                }
                            </div>
                        </TabPanel>
                        <TabPanel value="5">
                            <div className={cx('items')}>
                                {orderCancelled.map((orderDetail) => (
                                    <Orders orderDetail={orderDetail}/>
                                ))
                                }
                            </div>
                        </TabPanel>
                    </TabContext>

                </div>
            </Container>
        </div>
    );
}

export default Purchase;
