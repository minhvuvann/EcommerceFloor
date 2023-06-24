import React from 'react';

import SellerManagerWrapper from "~/layout/SellerManager";
import AddProduct from "~/pages/SellerManager/AddProduct/AddProduct";



function AddProductManager(props) {
    return (
        <SellerManagerWrapper child={<AddProduct/>}/>
    );
}

export default AddProductManager;
