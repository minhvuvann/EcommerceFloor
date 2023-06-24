import React from 'react';


import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import PasswordChange from "~/pages/InformationUser/account/PasswordChange/PasswordChange";

function PasswordChangeManager(props) {
    return (
        <div style={{
            width: "100%",
            paddingTop: "var(--padding-top)",
            backgroundColor: "#f5f5f5",
            paddingBottom: "20px  "
        }}>
            <InformationUserWrapper child={<PasswordChange/>}/>
        </div>
    );
}

export default PasswordChangeManager;
