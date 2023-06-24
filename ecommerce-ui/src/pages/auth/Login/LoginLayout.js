import React, {useContext} from 'react';

import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import Login from '~/pages/auth/Login/Login';
import {LanguageContext} from "~/config/provider/LanguageProvider";

function LoginLayout(props) {
    const {localeList} = useContext(LanguageContext);

    return (

        <LoginRegisterLayout titleHelp={localeList['button_help']} title={localeList['header_login']} children={<Login/>}/>
    );
}

export default LoginLayout;