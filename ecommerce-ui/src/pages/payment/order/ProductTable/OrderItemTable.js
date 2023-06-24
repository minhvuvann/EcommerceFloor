import React, {useContext, useEffect, useRef, useState} from 'react';

import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
} from "@mui/material";
import {makeStyles} from '@material-ui/core/styles';
import {Avatar} from "@material-ui/core";


import MoneyUtils from "~/utils/MoneyUtils";
import classNames from "classnames/bind";
import styles from "~/pages/payment/cart/ProductTable/ProductTable.module.scss";
import {getShopAll} from "~/services/workspaces.sevices";
import ColorConvertor from "~/utils/ColorConvertor";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function OrderItemTable(props) {
    const {market, cartItems, checkedPrice} = props;
    const [shops, setShops] = useState([]);
    const {localeList}= useContext(LanguageContext);

    useEffect(() => {
        fetchShop();
    }, []);
    const fetchShop =
        async () => {
            const shopInfos = await getShopAll();
            setShops(shopInfos?.data);
        }

    function getShopName(id) {
        for (const shop of shops) {
            if (shop.shopId === id)
                return shop.name;
        }

    }

    const useStyles = makeStyles({
        root: {
            width: '100%',
            overflowX: 'auto',
        },
        table: {
            minWidth: 768,
        },
        tableCell: {
            whiteSpace: 'nowrap',
        },
    });
    const classes = useStyles();


    return (
        <TableContainer sx={{borderRadius: "0"}} component={Paper}>
            <Table className={classes.table}>
                <TableHead>
                    <TableRow>
                        <TableCell>{localeList['cart_product_table_title']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_price']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_quantity']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_amount']}</TableCell>
                    </TableRow>

                </TableHead>
                <TableBody>
                    {cartItems.map(({id, cartId, shopId, productVariant, quantity, totalPrice}) => (

                            <TableRow key={productVariant.id}>
                                <TableCell>
                                    <Avatar sx={{width: 56, height: 56}} src={productVariant.imageUrl}
                                            alt={productVariant.productName}/>
                                    {productVariant.productName + " "}
                                    <br/>
                                    <small className={cx("sm-pl")}>{localeList['cart_product_table_classify']}:
                                        {" " + ColorConvertor.convert(productVariant.color)}</small>
                                    <br/>
                                    <small className={cx("sm-pl")}>Shop :
                                        {getShopName(shopId)}
                                    </small>
                                    <br/>
                                    <small className={cx("sm-pl")}>{localeList['cart_product_table_shipping']} :
                                        {MoneyUtils.getMoney(market.get(id) ? market.get(id) : 0)}
                                    </small>


                                </TableCell>
                                <TableCell align={"center"}> <span
                                    className={cx("old-price")}>
                                        {MoneyUtils.getMoney(productVariant.price.amount)}</span>
                                    {MoneyUtils.getMoney(productVariant.salePrice.amount)}</TableCell>
                                <TableCell align={"center"}>
                                    <span>{quantity}</span>
                                </TableCell>
                                <TableCell align={"center"}>{MoneyUtils.getMoney(totalPrice)}</TableCell>

                            </TableRow>


                        )
                    )

                    }

                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default OrderItemTable;
