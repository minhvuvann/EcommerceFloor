import React from 'react';


import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import ItemAddress from "~/pages/InformationUser/account/address/itemAddress/ItemAddress";

function ItemOrderManager(props) {
    return (
        <div style={{
            width: "100%",
            paddingTop: "var(--padding-top)",
            backgroundColor: "#f5f5f5",
            paddingBottom: "20px  "
        }}>
            <InformationUserWrapper child={<ItemAddress/>}/>
        </div>
    );
}

export default ItemOrderManager;
