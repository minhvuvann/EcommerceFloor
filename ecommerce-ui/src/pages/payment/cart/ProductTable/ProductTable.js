import React, {useContext, useEffect, useRef, useState} from 'react';

import {
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, useTheme
} from "@mui/material";
import {makeStyles} from '@material-ui/core/styles';
import {Avatar, Checkbox, useMediaQuery} from "@material-ui/core";


import {AiFillDelete} from 'react-icons/ai';
import MoneyUtils from "~/utils/MoneyUtils";
import classNames from "classnames/bind";
import styles from "~/pages/payment/cart/ProductTable/ProductTable.module.scss";
import {getShopAll} from "~/services/workspaces.sevices";
import ColorConvertor from "~/utils/ColorConvertor";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function ProductTable(props) {
    const {market, cartItems, checkedPrice, onDelete, upQuantity, downQuantity} = props;
    let [checkboxs, setCheckBoxs] = useState(new Map());
    const [shops, setShops] = useState([]);
    const {localeList}= useContext(LanguageContext);
    useEffect(() => {
        console.log(market)
        fetchShop();
    }, []);
    const fetchShop =
        async () => {
            const shopInfos = await getShopAll();
            setShops(shopInfos?.data);
        }


    const upHandleQuantity = async (cartItemId, quantity) => {
        upQuantity(cartItemId, quantity);
    }
    const downHandleQuantity = async (id, cartId, shopId, productVariant, totalPrice) => {
        downQuantity(id, cartId, shopId, productVariant, totalPrice);
    }
    const handleDeleteClick = (cartItemId, quantity) => {
        onDelete(cartItemId, quantity);
    };
    const checkedTicker = (id) => {
        if (checkboxs.get(id)) {
            checkboxs.delete(id);
        } else {
            checkboxs.set(id, id);
        }
        setCheckBoxs(checkboxs);
        checkedPrice(checkboxs);
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
        <TableContainer component={Paper}>
            <Table className={classes.table}>
                <TableHead>
                    <TableRow>
                        <TableCell/>
                        <TableCell>{localeList['cart_product_table_title']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_price']}</TableCell>
                        <TableCell>{localeList['cart_product_table_quantity']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_amount']}</TableCell>
                        <TableCell align={"center"}>{localeList['cart_product_table_operation']}</TableCell>
                    </TableRow>

                </TableHead>
                <TableBody>
                    {cartItems.map(({id, cartId, shopId, productVariant, quantity, totalPrice}) => (

                            <TableRow key={productVariant.id}>
                                <TableCell>
                                    <Checkbox
                                        onClick={() => checkedTicker(id)}
                                        checked={checkboxs.get(id)}
                                        color="secondary"/></TableCell>
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
                                    <div className={cx("quantity")}>
                                    <span onClick={() => upHandleQuantity(id, quantity)}
                                          className={cx("btn-function")}>-</span>
                                        <span className={cx("amount")}>{quantity}</span>
                                        <span
                                            onClick={() => downHandleQuantity
                                            (id, cartId, shopId, productVariant, totalPrice)}
                                            className={cx("btn-function")}>+</span>


                                    </div>
                                </TableCell>
                                <TableCell align={"center"}>{MoneyUtils.getMoney(totalPrice)}</TableCell>
                                <TableCell align={"center"}>
                                    <IconButton
                                        onClick={() => handleDeleteClick(id, 0)}><AiFillDelete/></IconButton>
                                </TableCell>
                            </TableRow>


                        )
                    )

                    }

                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default ProductTable;
