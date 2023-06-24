import React from 'react';
import classNames from "classnames/bind";


import styles from "./SellerManager.module.scss";
import SellerManagerWrapper from "~/layout/SellerManager";
import MainContent from "~/pages/SellerManager/MainContent";


const cx = classNames.bind(styles);
function SellerManager(props) {
    return (
        <div className={cx('wrapper')}>
            <SellerManagerWrapper child={<MainContent/>}/>
        </div>
    );
}

export default SellerManager;
