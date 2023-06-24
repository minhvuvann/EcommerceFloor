import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import Tippy from '@tippyjs/react/headless';

import styles from "./Header.module.scss";
import {Logo, LogoReverse} from "~/components/Icon";
import {Avatar} from "@mui/material";
import avatar from '~/assets/avatar/avatar.jpg'
import {AiOutlineFileDone} from 'react-icons/ai'
import {IoIosLogOut} from 'react-icons/io'
import {Link} from "react-router-dom";
import config from "~/config";
import {ShopContext} from "~/config/provider/ShopProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function Header(props) {
    const [isShop, setIsShop] = useState(false);
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {shop} = useContext(ShopContext);
    const {localeList} = useContext(LanguageContext);
    const logOut = () => {
        localStorage.clear();
        window.location.replace(config.routes.home);
    }

    useEffect(() => {
        localStorage.setItem("seller-id",rsUser?.shop?.shopId);
        setIsShop(rsUser !== null & rsUser?.shop!=null);

    }, [rsUser !== null & rsUser?.id !== ""]);
    return (
        <div className={cx('wrapper')}>
           <div className={cx('header-wrapper')}>
               <div className={cx('logo')}>
                  <div>
                     <Link to={config.routes.home}>
                         <Logo className={cx('logo-image')}/>
                     </Link>
                  </div>
                   <h4 className={cx('logo-text')}>{localeList['header_seller']}</h4>
               </div>
               <Tippy
                   offset={[12,10]}
                   interactive
                   render={attrs => (
                       <div className={cx('profile')} tabIndex="-1" {...attrs}>
                           <Link className={cx('profile-item')}
                              to={`/shop/profile/${shop?.shopId}`}>
                               <AiOutlineFileDone/>
                               {localeList['seller_shop_profile']}
                           </Link>
                           <Link className={cx('profile-item')} onClick={logOut}>
                               <IoIosLogOut/>
                               {localeList['header_user_down_logout']}
                           </Link>
                       </div>
                   )}
               >
                   <div className={cx('info-user')}>
                       <Avatar
                           alt={shop?.name}
                           src={shop?.imageUrl}
                           sx={{ width: 30, height: 30 }}
                       />
                       <span className={cx('name')}>{shop?.name}</span>
                   </div>
               </Tippy>

           </div>
        </div>
    );
}

export default Header;
