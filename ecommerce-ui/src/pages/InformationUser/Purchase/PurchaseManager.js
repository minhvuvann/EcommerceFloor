import React from 'react';


import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import Purchase from "~/pages/InformationUser/Purchase/Purchase";

function PurchaseManager(props) {
    return (
        <div style={{
            width: "100%",
            paddingTop: "var(--padding-top)",
            backgroundColor: "#f5f5f5",
            paddingBottom: "20px  "
        }}>
            <InformationUserWrapper child={<Purchase/>}/>
        </div>
    );
}

export default PurchaseManager;
