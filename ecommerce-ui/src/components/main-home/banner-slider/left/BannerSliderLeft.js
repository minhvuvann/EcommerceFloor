import React from 'react';
import classNames from "classnames/bind";
import Carousel from 'react-material-ui-carousel';
import {Paper, Button} from '@mui/material';
import styles from "./BannerSliderLeft.module.scss";
import BannerItem from "~/components/main-home/banner-slider/banner-item";

const cx = classNames.bind(styles);

function BannerSliderLeft(props) {
    var items = [
        {
            id: "banner-left-001",
            title: "deal-hot-trend",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-d5805972982d5c9ca7b371a91aaeb235_xxhdpi"
        },
        {
            id: "banner-left-002",
            title: "Sự kiện 2",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-a9ac47e74b0924157ed7fa8e5c967f4a_xxhdpi"
        },
        {
            id: "banner-left-003",
            title: "Sự kiện 3",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-753bd24ff694a1657dd4717f799aaa64_xxhdpi"
        },
        {
            id: "banner-left-004",
            title: "Sự kiện 4",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-1f6badc0070798ef613c97f88de92977_xxhdpi"
        },
        {
            id: "banner-left-005",
            title: "Sự kiện 5",
            imgUrl: " https://cf.shopee.vn/file/vn-50009109-42b4a26e6fc5c19c98e0f4863d60616b_xxhdpi"
        },
        {
            id: "banner-left-006",
            title: "Sự kiện 6",
            imgUrl: "https://cf.shopee.vn/file/vn-50009109-1ebe2fd8519daa0177f33ef4250a7bb8_xxhdpi"
        },

    ]
    return (
        <div className={cx('wrapper')}>
            <Carousel className={cx('carousel')}
                      autoPlay
                      infiniteLoop
                      showStatus={false}
                      showIndicators={false}
                      showThumbs={false}
                      interval={2000}>{
                items.map((item, i) =>
                    <BannerItem key={i} item={item}/>)
            }
            </Carousel>
        </div>
    );
}

export default BannerSliderLeft;
