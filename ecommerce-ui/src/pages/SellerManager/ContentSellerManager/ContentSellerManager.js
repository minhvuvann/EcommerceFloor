import React, {useContext, useEffect, useRef, useState} from 'react';
import classNames from "classnames/bind";
import {Link} from "react-router-dom";
import {Sidebar, Menu, MenuItem, SubMenu} from 'react-pro-sidebar';

import styles from "./ContentSellerManager.module.scss";
import {RiShoppingBag2Line} from 'react-icons/ri'

import {useMediaQuery} from "react-responsive";
import {AiOutlineShop} from 'react-icons/ai';
import config from "~/config";
import {ImFileText2} from "react-icons/im";
import {BiLineChartDown} from "react-icons/bi";
import {LanguageContext} from "~/config/provider/LanguageProvider";
import {BsChatDots} from "react-icons/bs";


const cx = classNames.bind(styles);

function ContentSellerManager(props) {
    const {shopId, child} = props;
    const isTablet = useMediaQuery({maxWidth: 768});
    const {localeList} = useContext(LanguageContext);

    const handleRenderIcon = ({open}) => {
        // if (isTablet) {
        //     return <span>{open ? <MdOutlineKeyboardArrowUp/> : <BsDot/>}</span>
        // } else {
        //     return <span>{open ? <MdOutlineKeyboardArrowUp/> : <RiArrowDownSLine/>}</span>
        // }
    }
    const categories = [
        {
            id: 1,
            label: localeList['seller_statistic'],
            icon: <BiLineChartDown/>,
            to: '/seller',
            items: []
        },
        {
            id: 2,
            label: localeList['seller_order_manager'],
            icon: <ImFileText2/>,
            items: [
                {
                    name: localeList['status_order_all'],
                    to: config.routes.shopOrderAll
                },
                {
                    name: localeList['status_order_wating_process'],
                    to: config.routes.shopOrderReady
                },
                {
                    name: localeList['status_order_delivering'],
                    to: config.routes.shopOrderDelivering
                },
                {
                    name: localeList['status_order_delivered'],
                    to: config.routes.shopOrderDelivered
                },
                {
                    name:localeList['status_order_cancelled'],
                    to: config.routes.shopOrderCancel
                },
            ]
        },
        {
            id: 3,
            label: localeList['seller_product_manager'],
            icon: <RiShoppingBag2Line/>,
            items: [
                {
                    name: localeList['tab_product'],
                    to: `/product/list/all-seller/${shopId}/shop`
                },
                {
                    name: localeList['tab_add_product'],
                    to: config.routes.addProduct
                },

            ]
        },
        {
            id: 4,
            label: localeList['seller_shop_profile'],
            icon: <AiOutlineShop/>,
            to: `/shop/profile/${shopId}`,
            items: []
        },
        {
            id: 5,
            label: localeList['support_title'],
            icon: <BsChatDots/>,
            to: `/shop/chat/${shopId}`,
            items: []
        },

    ]

    return (
        <div className={cx('wrapper')}>
            <Sidebar defaultCollapsed={isTablet} width='250px'>
                <Menu
                    renderExpandIcon={handleRenderIcon}
                    menuItemStyles={{
                        button: ({level, active, disabled}) => {
                            if (level === 0)
                                return {
                                    color: disabled ? '#f5d9ff' : '#ff5588',
                                };
                        },
                    }}
                >
                    {
                        categories.map((category, index) => {
                            return (
                                <SubMenu key={category.id}
                                         label={category.label}
                                         icon={category.icon}
                                         component={<Link to={category.to}/>}
                                         active={window.location.pathname === category.to}
                                >

                                    {
                                        category.items.map((item, index) => {
                                            return (
                                                <MenuItem key={index}
                                                          component={<Link to={item.to}/>}
                                                          active={window.location.pathname === item.to}
                                                >
                                                    {item.name}
                                                </MenuItem>
                                            )
                                        })
                                    }
                                </SubMenu>
                            )
                        })
                    }

                </Menu>
            </Sidebar>
            <main style={{width: '100%'}}>
                {child}
            </main>

        </div>
    );
}

export default ContentSellerManager;
