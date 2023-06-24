import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import OrderAll from "~/pages/SellerManager/order/orderAll/OrderAll";


function OrderAllManager(props) {
    return (
        <SellerManagerWrapper child={<OrderAll/>}/>
    );
}

export default OrderAllManager;
