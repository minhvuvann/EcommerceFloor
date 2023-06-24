import React from 'react';

import ContentAllProduct from "~/pages/SellerManager/AllProduct/ContentAllProduct";
import SellerManagerWrapper from "~/layout/SellerManager";


function AllProduct(props) {
    return (
        <SellerManagerWrapper child={<ContentAllProduct/>}/>
    );
}

export default AllProduct;
