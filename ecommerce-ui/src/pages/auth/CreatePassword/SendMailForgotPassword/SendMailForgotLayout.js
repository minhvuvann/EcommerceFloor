import React from 'react';
import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import SendMailForgot from "~/pages/auth/CreatePassword/SendMailForgotPassword/SendMailForgot";

function SendMailForgotLayout(props) {
    return (
        <LoginRegisterLayout children={<SendMailForgot/>}/>
    );
}

export default SendMailForgotLayout;