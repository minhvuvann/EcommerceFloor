import React from 'react';

import classNames from 'classnames/bind';
import styles from './DefaultLayout.module.scss';
import Header from "~/pages/Header";
import Footer from "~/pages/Footer";

const cx = classNames.bind(styles);

function DefaultLayout({children}) {

    return (
        <div className={cx("wrapper")}>
            <div className={cx("sticky-thc")}>
                <Header/>
            </div>
            <div className={cx("container-content")}>{children}</div>
            <Footer/>

        </div>

    );
}

export default DefaultLayout;
