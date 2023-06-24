import React from 'react';
import classNames from "classnames/bind";
import {Container, Grid} from "@mui/material";

import styles from "./Header.module.scss";
import Logo from "../../../assets/logo/Mellow.png";
import { Link } from 'react-router-dom';
import config from '../../../config';

const cx = classNames.bind(styles);

function Header(props) {
    const {title} = props;
    return (
        <div className={cx('wrapper')}>
            <div className={cx('header-wrapper')}>
                <Container>
                    <Grid container>
                        <Grid item xs={9} md={6}>
                            <div className={cx('header')}>
                                <Link to={config.routes.home}>
                                    <img src={Logo} className={cx('logo-header')}/>
                                </Link>
                                <h4 className={cx('header-text')}>{title}</h4>
                            </div>
                        </Grid>
                        <Grid item xs={3} md={6}>
                            <div className={cx('support')}>
                                <h4>Bạn Cần giúp đỡ?</h4>
                            </div>
                        </Grid>

                    </Grid>
                </Container>
            </div>
        </div>
    );
}

export default Header;
