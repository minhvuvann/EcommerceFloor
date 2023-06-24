import React from 'react';

import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import VertifyCodeRegister from './VertifyCodeRegister';

function VertifyCodeRegisterLayout(props) {
    return (
        <LoginRegisterLayout children={<VertifyCodeRegister/>}/>
    );
}

export default VertifyCodeRegisterLayout;