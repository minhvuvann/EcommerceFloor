import React from 'react';

import classNames from "classnames/bind";

import styles from "./ItemAddress.module.scss";
import {Box, Button} from "@mui/material";


const cx = classNames.bind(styles);

function ItemAddress(props) {
    return (

            <div className={cx('items')}>
                <div className={cx('item')}>
                    <p><strong>{props.text}</strong> | (+84) {props.phone}</p>
                    <p>{props.address}</p>
                </div>
                <div className={cx('update')}>
                    <Box sx={{ '& button': { m: 1 } }}>
                        <div className={cx('btn-update')}>
                            <Button  sx={{fontSize:15, color: "#ff5583"}}>Cập nhật </Button>
                            <Button sx={{fontSize:15, color: "#ff5583"}}>Xóa</Button>

                        </div>
                    </Box>
                </div>
                </div>

    );
}

export default ItemAddress;
