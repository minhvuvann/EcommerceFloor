import React, {createContext, useEffect, useState} from 'react';

import {getCartDetail} from "~/services/workspaces.sevices";


export const CartContext = createContext([]);

function CartProvider(props) {
    const [cart, setCart] = useState();
    const [cartItems, setCartItems] = useState([]);
    const [shouldUpdate, setShouldUpdate] = useState(false);
    const [totalPrice, setTotalPrice] = useState();

    async function pullCart() {
        const user = JSON.parse(localStorage.getItem("cs-obj"));
        if (user) {
            const cartData = await getCartDetail(user.id);
            if (cartData.data !== null) {
                setCart(cartData.data?.cart);
                setTotalPrice(cartData?.data?.cart?.totalPrice);
                setCartItems(cartData?.data?.cartItems)
            } else {
                setCart({});
                setTotalPrice(0);
                setCartItems([]);
            }
        }

    }

    useEffect(() => {
        pullCart()
    }, [shouldUpdate]);


    return (
        <CartContext.Provider value={{cartItems, setCartItems, totalPrice, setShouldUpdate}}>
            {props.children}
        </CartContext.Provider>
    );
}

export default CartProvider;