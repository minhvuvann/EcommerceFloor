import React, { useRef} from 'react';

import classNames from 'classnames/bind';
import styles from './Header.module.scss';

import TopNavbar from "~/pages/Header/TopNavbar";
import { Container, Grid} from "@mui/material";
import {Logo} from "~/components/Icon";
import {useMediaQuery} from "react-responsive";
import Search from "~/pages/Header/Search";
import Cart from "~/pages/Header/Cart";
import {MdMenu} from 'react-icons/md'
import {AiOutlineClose} from 'react-icons/ai'
import {Link} from "react-router-dom";
import config from "~/config";


const cx = classNames.bind(styles);
function Header(props) {

    const minTablet = useMediaQuery({minWidth: 900})
    const isTablet = useMediaQuery({maxWidth: 768})
    const maxWidth600 = useMediaQuery({maxWidth: 600})
    const navRef = useRef();
    const showNavbar = () => {
        navRef.current.classList.toggle(cx('responsive_nav'));
    };

    return (
        <div className={cx('wrapper')}>
            {!maxWidth600 && (<TopNavbar/>)}
            <div className={cx('header')}>
                <Container>
                    <Grid container direction={'row'} justifyContent={isTablet ? 'space-around' : 'space-around'}
                          alignItems="center">
                        {minTablet && (
                            <Grid item md={2} sm={2}>
                                <Link to={config.routes.home}>
                                    <div className={cx('logo')}>
                                        <Logo className={cx('logo-image')}/>
                                    </div>
                                </Link>
                            </Grid>
                        )}
                        <Grid item md={8} sm={8} justifyContent="center" xs={8}>
                            <Search/>
                        </Grid>

                        <Grid container item md={2} sm={1} xs={2}
                        >
                            <div className={cx('cart-menu')}>
                                <Cart/>
                            </div>
                        </Grid>
                        {
                            isTablet && (
                                <Grid item md={2} sm={1} xs={2}>
                                    <div className={cx('menu-mobile')}>
                                        <MdMenu onClick={showNavbar} className={cx('menu-icon','nav-btn')}/>
                                    </div>

                                </Grid>
                            )
                        }

                    </Grid>
                    <header>
                        <nav ref={navRef}>
                            <a href="/#">Đăng nhập</a>
                            <a href="/#">Đăng ký</a>
                            <a href="/#">Thông tin cá nhân</a>
                            <button
                                className={cx('nav-btn','nav-close-btn')}
                                onClick={showNavbar}>
                                <AiOutlineClose />
                            </button>
                        </nav>
                    </header>
                </Container>

            </div>


        </div>

    );
}

export default Header;
