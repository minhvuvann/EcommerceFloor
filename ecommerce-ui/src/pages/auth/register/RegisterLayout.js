import React, {useContext} from 'react';

import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import Register from '~/pages/auth/register/Register';
import {LanguageContext} from "~/config/provider/LanguageProvider";

function RegisterLayout(props) {
    const {localeList} = useContext(LanguageContext);

    return (
        <LoginRegisterLayout titleHelp={localeList['button_help']} title={localeList['header_signup']} children={<Register/>}/>
    );
}

export default RegisterLayout;