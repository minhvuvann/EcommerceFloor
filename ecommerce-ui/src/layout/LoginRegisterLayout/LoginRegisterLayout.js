import React, {useContext} from 'react';
import classNames from "classnames/bind";


import styles from "./LoginRegisterLayout.module.scss";
import Header from "~/layout/LoginRegisterLayout/Header";
import Content from "~/layout/LoginRegisterLayout/Content";
import {UserContext} from "~/config/provider/UserProvider";
import NotFound_404 from "~/pages/404/404";


const cx = classNames.bind(styles);

function LoginRegisterLayout({title, titleHelp, children}) {
    const {customer} = useContext(UserContext);
    const isLogin = customer !== undefined && Object.keys(customer).length > 0;
    return (!isLogin ?
            <div className={cx('wrapper')}>
                <Header titleHelp={titleHelp} title={title}/>
                <Content children={children}/>
            </div> : <NotFound_404/>
    );
}

export default LoginRegisterLayout;