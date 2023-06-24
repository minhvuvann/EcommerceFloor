import React from 'react';
import SellerManagerWrapper from "~/layout/SellerManager";
import EditProduct from "~/pages/SellerManager/EditProduct/EditProduct";

function EditProductManager(props) {
    return (
        <SellerManagerWrapper child={<EditProduct/>}/>
    );
}

export default EditProductManager;
