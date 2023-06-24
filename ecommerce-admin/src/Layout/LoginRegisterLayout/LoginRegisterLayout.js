import React from 'react';
import classNames from "classnames/bind";

import styles from "./LoginRegisterLayout.module.scss";
import Header from "./Header/Header";
import Content from "./Content/Content";

const cx = classNames.bind(styles);

function LoginRegisterLayout({children}) {
    return (
        <div className={cx('wrapper')}>
            <Header/>
            <Content children={children}/>
        </div>
    );
}

export default LoginRegisterLayout;