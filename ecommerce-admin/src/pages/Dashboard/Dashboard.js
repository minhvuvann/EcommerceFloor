import React from 'react'
import classNames from 'classnames/bind'
import styles from './Dashboard.module.scss'
import MainDash from '../../components/Dashboard/MainDash/MainDash'
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import Link from '@mui/material/Link';
import Stack from '@mui/material/Stack';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import {Home} from '@mui/icons-material';
import {Grid} from "@mui/material";

function handleClick(event) {
    event.preventDefault();
    console.info('You clicked a breadcrumb.');
}

const cx = classNames.bind(styles)

function Dashboard() {
    const breadcrumbs = [
        <Grid container style={{width:"10rem", display:"flex",justifyContent:"center"}}>
        </Grid>
    ];
    return (
        <div className={cx('dashboards')}>
            <Stack spacing={2}>
                <Breadcrumbs
                    separator={true}
                    aria-label="breadcrumb">
                    {breadcrumbs}
                </Breadcrumbs>
            </Stack>
            <div className={cx('dashboard')}>
                <h1>Trang chủ</h1>
                <MainDash/>
            </div>
        </div>
    )
}

export default Dashboard