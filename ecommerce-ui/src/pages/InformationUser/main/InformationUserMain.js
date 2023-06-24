import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import {Link} from "react-router-dom";
import {Sidebar, Menu, MenuItem, SubMenu} from 'react-pro-sidebar';

import styles from "./InformationUserMain.module.scss";

import {useMediaQuery} from "react-responsive";

import {AssignmentOutlined, CreateOutlined, PersonOutline} from "@mui/icons-material";
import {Avatar} from "@mui/material";
import config from "~/config";
import {UserContext} from "~/config/provider/UserProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);

function InformationUserMain(props) {
    const {child} = props;
    const {localeList} = useContext(LanguageContext);

    const handleRenderIcon = ({open}) => {

    }
    const {customer} = useContext(UserContext);

    const categories = [
        {
            id: 2,
            label: localeList['info_my_account'],
            icon: <PersonOutline/>,
            to: "",

            items: [
                {
                    name: localeList['info_profile'],
                    to: "/info-user"
                }, {
                    name: localeList['info_address'],
                    to: "/info-address"
                }, {
                    name: localeList['info_change_password'],
                    to: "/password-change"
                }
            ]
        },
        {
            id: 3,
            label: localeList['info_order_purchase'],
            icon: <AssignmentOutlined/>,
            to: "/purchase",

            items: [],

        },
    ]

    return (
        <div className={cx('wrapper')}>
            <Sidebar>
                <div className={cx('avatar')}>
                    <Avatar sizes={'large'}
                            alt={customer?.fullName}
                            src={customer?.imageUrl}
                    />
                    <div className={cx('name')}>
                        <h4>{customer?.username}</h4>
                        <Link to={config.routes.informationUser}>
                            <span><CreateOutlined sx={{fontSize: 20,}}/>{localeList['info_edit_profile']}</span>
                        </Link>
                    </div>

                </div>
                <Menu
                    renderExpandIcon={handleRenderIcon}
                    // menuItemStyles={{
                    //     button: ({level, active, disabled}) => {
                    //         if (level === 0)
                    //             return {
                    //                 color: disabled ? '#f5d9ff' : '#ff5588',
                    //                 backgroundColor: active ? '#eecef9' : undefined,
                    //             };
                    //     },
                    // }}
                >
                    {
                        categories.map((category, index) => {
                            return (
                                <SubMenu key={category.id}
                                         label={category.label}
                                         component={<Link to={category.to}/>}
                                         active={window.location.pathname === category.to}

                                         icon={category.icon}>
                                    {category.items.map((item, index) => {
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

export default InformationUserMain;
