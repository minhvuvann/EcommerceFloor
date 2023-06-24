import React, {createContext, useEffect, useState} from 'react';
import {getShopById} from "~/services/workspaces.sevices";


export const ShopContext = createContext([]);

function ShopProvider(props) {
    const [shop, setShop] = useState({});
    const [shouldUpdate, setShouldUpdate] = useState(false);

    async function pullShop() {
        const user = JSON.parse(localStorage.getItem("cs-obj"));
        if (user !== null && user.shop) {
            const shop = await getShopById(user.shop.shopId);
            localStorage.setItem("seller-id", shop?.data?.shopId)
            setShop(shop?.data);
        }

    }

    useEffect(() => {
        pullShop()
    }, [shouldUpdate]);


    return (
        <ShopContext.Provider value={{shop, setShouldUpdate}}>
            {props.children}
        </ShopContext.Provider>
    );
}

export default ShopProvider;