import React from 'react';


import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import PasswordForm from "~/pages/InformationUser/account/PasswordChange/PasswordForm/PasswordForm";

function PasswordFormManager(props) {
    return (
        <div style={{
            width: "100%",
            paddingTop: "var(--padding-top)",
            backgroundColor: "#f5f5f5",
            paddingBottom: "20px  "
        }}>
            <InformationUserWrapper child={<PasswordForm/>}/>
        </div>
    );
}

export default PasswordFormManager;
