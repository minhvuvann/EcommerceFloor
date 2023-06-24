import React from 'react';
import classNames from "classnames/bind";
import Carousel from 'react-material-ui-carousel';
import {Paper, Button} from '@mui/material';
import styles from "./BannerSliderRight.module.scss";
import BannerItem from "~/components/main-home/banner-slider/banner-item";

const cx = classNames.bind(styles);

function BannerSliderRight(props) {
    var items = [
        {
            id: "banner-right-001",
            title: "spaylater",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-5209530fe212df8e91abfd9f73d7136b_xhdpi"
        },
        {
            id: "banner-right-002",
            title: "welcome",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-18d00caa9a252004c48dfb13e7f09ade_xhdpi"
        },
    ]
    return (
        <div className={cx('wrapper-banner-right')}>
            <div
                className={cx("b-img-top")}><img src={items[0].imgUrl}/></div>
            <div
                className={cx("b-img-bottom")}><img src={items[1].imgUrl}/></div>

        </div>
    );
}

export default BannerSliderRight;
