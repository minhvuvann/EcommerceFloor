import React, {useContext} from 'react';

import classNames from "classnames/bind";

import styles from "./ItemOrder.module.scss";
import {Avatar, Box, Button, Grid} from "@mui/material";
import ColorConvertor from "~/utils/ColorConvertor";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function ItemOrder(props) {
    const {orderItem} = props;
    const {localeList} = useContext(LanguageContext);
    return (

        <div className={cx('itemOrder')}>
            <Grid container spacing={10}>
                <Grid item xs={1.5}>
                    <div className={cx('itemOrder_avatar')}>
                        <div className={cx('img-product-order')}>
                            <Avatar className={cx('avatar-product')}
                                    src={orderItem?.variant?.imageUrl}
                                    variant="square"/>

                        </div>
                    </div>
                </Grid>
                <Grid item xs={10}>
                    <h1>{orderItem?.variant?.productName}</h1>
                    <div className={cx('order-purchase-label-vp')}>
                        <p className={cx('order_purchase_label_variation')}>{localeList['info_product_classify']}: {ColorConvertor.convert(orderItem?.variant?.color)}</p>
                        <p className={cx('order_purchase_label_price')}>
                           </p>
                    </div>
                    <p className={cx('order_purchase_label_quality')}>x{orderItem?.quantity}</p>
                </Grid>
            </Grid>
        </div>

    );
}

export default ItemOrder;
