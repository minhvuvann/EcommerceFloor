import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import OrderCancel from "~/pages/SellerManager/order/orderCancel/OrderCancel";


function OrderCancelManager(props) {
    return (
        <SellerManagerWrapper child={<OrderCancel/>}/>
    );
}

export default OrderCancelManager;
