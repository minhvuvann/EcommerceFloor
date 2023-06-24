import React from 'react';

import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import VertifyForgotPassword from "~/pages/auth/CreatePassword/VeritifyForgotPassword/VertifyForgotPassword";

function VertifyForgotPasswordLayout(props) {
    return (
        <LoginRegisterLayout children={<VertifyForgotPassword/>}/>
    );
}

export default VertifyForgotPasswordLayout;