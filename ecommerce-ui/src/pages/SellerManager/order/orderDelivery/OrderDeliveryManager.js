import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import OrderDelivery from "~/pages/SellerManager/order/orderDelivery/OrderDelivery";


function OrderDeliveryManager(props) {
    return (
        <SellerManagerWrapper child={<OrderDelivery/>}/>
    );
}

export default OrderDeliveryManager;
