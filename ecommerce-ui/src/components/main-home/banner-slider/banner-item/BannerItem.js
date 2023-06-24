import React from 'react';
import classNames from "classnames/bind";
import styles from "./BannerItem.module.scss";
import Carousel from 'react-material-ui-carousel'
import {Paper, Button} from '@mui/material'
import {Image} from "@mui/icons-material";

const cx = classNames.bind(styles);

function BannerItem(props) {

    return (
        <Paper className={cx('paper')}>
            <div className={cx("wrapper")}>
                <img className={cx("image-background")}
                     src={props.item.imgUrl}
                     alt={props.title}
                     loading="lazy">

                </img></div>

        </Paper>

    );
}

export default BannerItem;
