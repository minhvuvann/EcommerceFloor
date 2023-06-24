import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";

import styles from "./CartDetail.module.scss";

import config from "~/config";
import {
    addCart,
    getCartDetail,
    getFeeShipment,
    getShopAll,
    removeCart
} from "~/services/workspaces.sevices";
import MoneyUtils from "~/utils/MoneyUtils";
import {Container} from "@mui/material";
import ProductTable from "~/pages/payment/cart/ProductTable";
import {CartContext} from "~/config/provider/CartProvider";
import {Link} from "react-router-dom";
import {LogoReverse} from "~/components/Icon";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function CartDetail(props) {
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {cartItems, setShouldUpdate} = useContext(CartContext);
    const [badge, setBadge] = useState();
    const [totalPrice, setTotalPrice] = useState(0);
    const [market, setMarket] = useState(new Map());
    const [cartItemIds, setCartItemIds] = useState();
    const{localeList}= useContext(LanguageContext);


    useEffect(() => {

        fetchData();
    }, [market]);

    const fetchData = async () => {
        const shopInfos = await getShopAll();
        const cartDetail = await getCartDetail(rsUser?.id);
        setBadge(cartItems.length);
        let map = new Map();
        for (const {id, shopId, productVariant} of cartDetail.data.cartItems) {
            const shopByGet = getShopDistrict(shopId, shopInfos?.data);
            const FeeShippingGHNInput = {
                from_district_id: shopByGet,
                service_type_id: 2,
                to_district_id: rsUser?.address?.districtCode,
                to_ward_code: rsUser?.address?.wardCode,
                height: 4,
                width: 10,
                length: 14,
                weight: productVariant?.weight,
                insurance_value: productVariant?.price?.amount,
            }
            let dataFee = await getFeeShipment('3' + shopId, FeeShippingGHNInput);
            map.set(id, dataFee?.data?.amount);
        }
        setMarket(map);
        console.log(map);
    }

    function getShopDistrict(id, shops) {
        for (const shop of shops) {
            if (shop.shopId === id)
                return shop.address.districtCode;
        }
    }

    const handleDelete = async (cartItemId, quantity) => {
        await removeCart(cartItemId, quantity);
        setShouldUpdate(prev => !prev);

    };
    const handleUpQuantity = async (cartItemId, quantity) => {
        if (quantity > 1) {
            await
                removeCart(cartItemId, 1);
            setShouldUpdate(prev => !prev);


        }
    }
    const handleDownQuantity = async (id, cartId, shopId, productVariant, totalPrice) => {
        const CartItem = {
            id: id,
            cartId: cartId,
            shopId: shopId,
            productVariant: productVariant,
            quantity: 1,
            totalPrice: totalPrice
        }
        await addCart(CartItem);
        setShouldUpdate(prev => !prev);

    }

    const operatorTotal = async (checkboxs) => {

        let total = 0;
        const arred = [];
        for (let i = 0; i < cartItems.length; i++) {
            const item = cartItems[i];
            if (checkboxs.get(item.id)) {
                if (!arred.includes(item.id)) {
                    total += market.get(item.id);
                    arred.push(item.id);
                }
                total += item.totalPrice;
            }

        }
        setCartItemIds(arred);
        setTotalPrice(total);
    }
    const purchaseAction = () => {
        console.log(cartItemIds);
        if (cartItemIds && cartItemIds.length > 0) {
            let id = '';
            cartItemIds.forEach((cartItemId) => {
                id += cartItemId + ".";
            })
            id = id.substring(0, id.length - 1);
            window.location.href = `order-detail/${id}/sample`;

        }

    }

    return (
        <div className={cx('wrapper')}>
            <div className={cx("banner-container")}>
                <Link to={config.routes.home}>
                    <LogoReverse className={cx("lg-reverse")}/>
                </Link>

                <div className={cx('title')}>{localeList['cart_title']}</div>
            </div>
            <Container>
                <ProductTable
                    market={market}
                    cartItems={cartItems}
                    onDelete={handleDelete}
                    checkedPrice={operatorTotal}
                    upQuantity={handleUpQuantity}
                    downQuantity={handleDownQuantity}
                />

                <div className={cx('checkout')}>
                    <span className={cx('label-price')}>{localeList['total_payment']}</span>
                    <span className={cx('total-price')}>{MoneyUtils.getMoney(totalPrice)}</span>
                    <button onClick={purchaseAction} className={cx('btn-purchase')}>{localeList['button_purchase']}</button>
                </div>
            </Container>
        </div>
    );
}

export default CartDetail;
