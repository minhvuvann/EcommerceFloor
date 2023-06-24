import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import OrderReady from "~/pages/SellerManager/order/orderReady/OrderReady";


function OrderReadyManager(props) {
    return (
        <SellerManagerWrapper child={<OrderReady/>}/>
    );
}

export default OrderReadyManager;
