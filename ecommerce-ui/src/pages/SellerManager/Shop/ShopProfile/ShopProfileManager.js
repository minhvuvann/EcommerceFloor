import React from 'react';


import SellerManagerWrapper from "~/layout/SellerManager";
import ShopProfile from "~/pages/SellerManager/Shop/ShopProfile/ShopProfile";

function ShopProfileManager(props) {
    return (
        <SellerManagerWrapper child={<ShopProfile/>}/>
    );
}
export default ShopProfileManager;
