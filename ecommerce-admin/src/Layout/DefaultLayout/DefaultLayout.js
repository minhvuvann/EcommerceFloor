import React, {useContext} from 'react'
import classNames from 'classnames/bind'
import styles from './DefaultLayout.module.scss'
import Navbar from '../Navbar/Navbar'
import {UserContext} from "../../config/provider/UserProvider";
import NotFound_404 from "../../pages/404/404";

const cx = classNames.bind(styles);

export default function DefaultLayout({children}) {
    const {admin} = useContext(UserContext);
    const isLogin = admin !== undefined && Object.keys(admin).length > 0;
    return (isLogin ?
            <div className={cx('default')}>
                <Navbar children={children}/>
                {/* eslint-disable-next-line react/jsx-pascal-case */}
            </div> : <NotFound_404/>
    )
}
