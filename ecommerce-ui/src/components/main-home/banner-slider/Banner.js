import React from 'react';
import classNames from "classnames/bind";
import styles from "./Banner.module.scss";
import BannerSliderLeft from "~/components/main-home/banner-slider/left";
import {Grid} from "@mui/material";
import BannerSliderRight from "~/components/main-home/banner-slider/right";

const cx = classNames.bind(styles);

function Banner(props) {
    return (
        <div className={cx('wrapper')}>
            <Grid container spacing={2}>
                <Grid item xs={8}>
                    <BannerSliderLeft/>
                </Grid>
                <Grid item xs={4}>
                    <BannerSliderRight/>
                </Grid>
            </Grid>

        </div>
    );
}

export default Banner;
