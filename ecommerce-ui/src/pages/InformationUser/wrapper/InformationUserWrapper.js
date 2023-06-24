import React, {useEffect, useState} from 'react';
import classNames from "classnames/bind";
import styles from "./InformationUserWrapper.module.scss";
import Header from "~/pages/Header/Header";
import {Grid} from "@mui/material";
import InformationUserMain from "~/pages/InformationUser/main";


const cx = classNames.bind(styles);

function InformationUserWrapper({child}) {
    const [userId, setUserId] = useState();

    useEffect(() => {
        setUserId(JSON.parse(localStorage.getItem("cs-obj")).id);
    }, userId);
    return (
        <div className={cx('wrapper')}>
            <Header/>
            <Grid container>
                <Grid item md={12} sm={12}>
                    <InformationUserMain userId={userId} child={child}/>
                </Grid>
            </Grid>

        </div>
    );
}

export default InformationUserWrapper;
