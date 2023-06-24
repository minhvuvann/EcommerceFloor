import React from 'react';


import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import InformationAddress from "~/pages/InformationUser/account/address/InformationAddress";

function OrdersManager(props) {
    return (
        <div style={{
            width: "100%",
            paddingTop: "var(--padding-top)",
            backgroundColor: "#f5f5f5",
            paddingBottom: "20px  "
        }}>
            <InformationUserWrapper child={<InformationAddress/>}/>
        </div>
    );
}

export default OrdersManager;
