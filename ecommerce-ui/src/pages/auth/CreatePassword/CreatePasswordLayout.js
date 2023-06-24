import React from 'react';
import LoginRegisterLayout from '~/layout/LoginRegisterLayout';
import CreatePassword from '~/pages/auth/CreatePassword/CreatePassword';

function CreatePasswordLayout(props) {
    return (
        <LoginRegisterLayout children={<CreatePassword/>}/>
    );
}

export default CreatePasswordLayout;