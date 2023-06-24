import React from 'react';

import classNames from "classnames/bind";

import styles from "./PasswordForm.module.scss";
import Grid from "@mui/material/Grid";


const cx = classNames.bind(styles);

function PasswordForm(props) {
    return (
        <div className={cx('input')}>

            <Grid container columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                <Grid item xs={3}>
                    <label htmlFor={props.password} >{props.text}</label>
                </Grid>
                <Grid item xs={4}>
                    <input type='password'  id={props.id}
                           name={props.name}/>
                </Grid>

            </Grid>

        </div>
    );
}

export default PasswordForm;
