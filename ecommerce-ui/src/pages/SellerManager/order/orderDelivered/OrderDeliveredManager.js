import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import OrderDelivered from "~/pages/SellerManager/order/orderDelivered/OrderDelivered";


function OrderDeliveredManager(props) {
    return (
        <SellerManagerWrapper child={<OrderDelivered/>}/>
    );
}

export default OrderDeliveredManager;
