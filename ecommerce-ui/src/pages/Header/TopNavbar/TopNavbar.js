import React, {useContext, useEffect, useState} from "react";
import {Container, Grid, Popover, Typography, Box} from "@mui/material";
import classNames from "classnames/bind";
import styles from "./TopNavbar.module.scss";
import {useMediaQuery} from "react-responsive";
import {Link} from "react-router-dom";

import {FaFacebook} from "react-icons/fa";
import {RiInstagramFill} from "react-icons/ri";
import Notify from "~/assets/notify/notify-empty.png";
import {
    MdContactSupport,
    MdNotifications,
    MdOutlineLanguage,
} from "react-icons/md";
import UserDropdown from "./UserDropdown";
import config from "~/config";
import {UserContext} from "~/config/provider/UserProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function TopNavbar(props) {
    const maxMd = useMediaQuery({maxWidth: 900});
    const minMd = useMediaQuery({minWidth: 900});
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [languageFunction, setLanguageFunction] = useState(null);
    const [login, setLogin] = useState(false);
    const [isShop, setIsShop] = useState(false);
    const {customer} = useContext(UserContext);
    const {localeList, setSyncUpdate, properties, setProperties} = useContext(LanguageContext);

    useEffect(() => {
        setLogin(customer !== undefined && Object.keys(customer).length > 0);
        setIsShop(customer !== undefined &&
            customer?.shop != null);
    }, [customer !== undefined, customer]);

    const handLeLanguageFunctionClick = (event) => {
        setLanguageFunction(event.currentTarget);
    };
    const handleLanguageFunctionClose = () => {
        setLanguageFunction(null);
    };
    const handleNotifyClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const changeVN = () => {
        setProperties('vi');
        setSyncUpdate(prev => !prev);
        handleLanguageFunctionClose();

    }
    const changeEN = () => {
        setProperties('en');
        setSyncUpdate(prev => !prev);
        handleLanguageFunctionClose();
    }

    const handleNotifyClose = () => {
        setAnchorEl(null);
    };


    const open = Boolean(anchorEl);
    const show = Boolean(languageFunction);
    const id = open ? "simple-popover" : undefined;
    return (
        <div className={cx("navbar-wrapper")}>
            <Container>
                <Grid className={cx("wrapper")} container={false}>
                    {minMd && (
                        <Grid
                            item
                            md={6}
                            lg={6}
                            container
                            direction={"row"}
                            justifyContent="flex-start"
                            alignItems="center"
                        >
                            <div className={cx("navbar-left")}>
                                {login ? (isShop ? (<Link
                                    to={config.routes.sellerManager}
                                    className={cx("navbar-item", "navbar-separate")}
                                >
                                    {localeList['header_seller']}
                                </Link>) : (
                                    <Link className={cx("navbar-item", "navbar-separate")}
                                          to={config.routes.registerShop}>
                                        {localeList['header_seller_start']}
                                    </Link>)) : ""}
                                <Link className={cx("navbar-item", "navbar-separate")} href="">
                                    {localeList['header_download']}
                                </Link>
                                <Link className={cx("navbar-item")}>
                                    {localeList['header_follow']}
                                </Link>
                                <a className={cx("icon")} href={"https://www.facebook.com/Mellow191/"}>
                                    <FaFacebook/>
                                </a>
                            </div>
                        </Grid>
                    )}
                    <Grid
                        item
                        container
                        md={6}
                        lg={6}
                        direction="row"
                        justifyContent={maxMd ? `flex-start` : "flex-end"}
                        alignItems="center"
                    >
                        <div className={cx("navbar-right")}>
                            <Popover
                                id={id}
                                open={open}
                                anchorEl={anchorEl}
                                onClose={handleNotifyClose}
                                anchorOrigin={{
                                    vertical: "bottom",
                                    horizontal: "right",
                                }}
                                transformOrigin={{
                                    vertical: "top",
                                    horizontal: "right",
                                }}
                                PaperProps={{
                                    style: {
                                        backgroundColor: "transparent",
                                        boxShadow: "none",
                                        borderRadius: 0,
                                    },
                                }}
                            >
                                <Box
                                    sx={
                                        maxMd
                                            ? {
                                                position: "relative",
                                                mt: "10px",
                                                "&::before": {
                                                    backgroundColor: "white",
                                                    content: '""',
                                                    display: "block",
                                                    position: "absolute",
                                                    width: 12,
                                                    height: 12,
                                                    top: -6,
                                                    transform: "rotate(45deg)",
                                                    left: `calc(10% - 6px)`,
                                                },
                                            }
                                            : {
                                                position: "relative",
                                                mt: "10px",
                                                "&::before": {
                                                    backgroundColor: "white",
                                                    content: '""',
                                                    display: "block",
                                                    position: "absolute",
                                                    width: 12,
                                                    height: 12,
                                                    top: -6,
                                                    transform: "rotate(45deg)",
                                                    left: `calc(90% - 6px)`,
                                                },
                                            }
                                    }
                                />
                                <Typography
                                    sx={{
                                        p: 2,
                                        backgroundColor: "white",
                                        borderRadius: "2px",
                                        padding: 0,
                                    }}
                                >
                                    <div className={cx("box-notify")}>
                                        <div className={cx("notify")}>
                                            <img
                                                className={cx("image-notify")}
                                                src={Notify}
                                                alt="notify image"
                                            />
                                            <h4 className={cx("notify-text")}>
                                                {!login ? localeList['header_login_notify'] : localeList['header_login_notify_not']}
                                            </h4>
                                        </div>
                                        {!login && (<div className={cx("function")}>
                                            <a href={""} className={cx("btn-function")}>
                                                {localeList['header_signup']}
                                            </a>
                                            <a href={""} className={cx("btn-function")}>
                                                {localeList['header_login']}
                                            </a>
                                        </div>)}
                                    </div>
                                </Typography>
                            </Popover>
                            <span
                                className={cx("navbar-right-item")}
                                onClick={handleNotifyClick}
                                aria-describedby={id}
                                variant="contained"
                            >
                <MdNotifications className={cx("icon")}/>
                                {localeList['header_notification']}
              </span>

                            <a className={cx("navbar-right-item")} href={config.routes.chatService}>
                                <MdContactSupport className={cx("icon")}/>
                                {localeList['header_help']}
                            </a>
                            <Popover
                                id={id}
                                open={show}
                                anchorEl={languageFunction}
                                onClose={handleLanguageFunctionClose}
                                anchorOrigin={{
                                    vertical: "bottom",
                                    horizontal: "right",
                                }}
                                transformOrigin={{
                                    vertical: "top",
                                    horizontal: "right",
                                }}
                                PaperProps={{
                                    style: {
                                        backgroundColor: "transparent",
                                        boxShadow: "none",
                                        borderRadius: 0,
                                    },
                                }}
                            >
                                <Box
                                    sx={{
                                        position: "relative",
                                        mt: "10px",
                                        "&::before": {
                                            backgroundColor: "white",
                                            content: '""',
                                            display: "block",
                                            position: "absolute",
                                            width: 12,
                                            height: 12,
                                            top: -6,
                                            transform: "rotate(45deg)",
                                            left: "calc(90% - 6px)",
                                        },
                                    }}
                                />
                                <Typography sx={{p: 2, backgroundColor: "white"}}>
                                    <div className={cx("box-language")}>
                                        <span onClick={changeVN} className={cx("language-text")}>Tiếng Việt</span>
                                        <span onClick={changeEN} className={cx("language-text")}>English</span>
                                    </div>
                                </Typography>
                            </Popover>
                            <span
                                className={cx("navbar-right-item")}
                                onClick={handLeLanguageFunctionClick}
                                aria-describedby={id}
                                variant="contained"
                            >
                <MdOutlineLanguage className={cx("icon")}/>
                                {!properties ? 'Tiếng việt' : (properties === 'vi' ? 'Tiếng việt' : 'English')}
              </span>

                            {!login ? (
                                <div className={cx("login-register")}>
                                    <Link
                                        to={config.routes.register}
                                        className={cx("navbar-item", "navbar-separate")}
                                    >
                                        {localeList['header_signup']}
                                    </Link>
                                    <Link
                                        to={config.routes.login}
                                        className={cx("navbar-right-item")}
                                    >
                                        {localeList['header_login']}
                                    </Link>
                                </div>
                            ) : (
                                <UserDropdown
                                    locale={localeList}
                                    imgUrl={customer?.imageUrl}
                                    id={customer?.id}
                                    userName={customer?.username}/>
                            )}
                        </div>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default TopNavbar;
