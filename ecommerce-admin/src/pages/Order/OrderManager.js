import React, {useEffect} from 'react'
import classNames from 'classnames/bind'

import styles from './OrderManager.module.scss'
import {  Button, Grid, } from '@mui/material';

import AddIcon from '@mui/icons-material/Add';
import { Link } from 'react-router-dom';
import config from '../../config';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Home } from '@mui/icons-material';
import OrderList from "../../components/Order/list";

function handleClick(event) {
    event.preventDefault();
    console.info('You clicked a breadcrumb.');
}

const cx = classNames.bind(styles);


export default function OrderManager() {
    const breadcrumbs = [
        <Link underline="hover" key="1" color="primary"  to={config.routes.dashboard} onClick={handleClick}>
            <Home color='disabled'/>
        </Link>,

        <Typography key="3" color="text.primary" sx={{fontSize: '1.1rem'}}>
            Quản lý đơn hàng
        </Typography>,
    ];


    return (
        <div className={cx('users')}>
            <Stack spacing={2} sx={{marginBottom: 3}}>
                <Breadcrumbs
                    separator={<NavigateNextIcon fontSize="small" />}
                    aria-label="breadcrumb"
                >
                    {breadcrumbs}
                </Breadcrumbs>
            </Stack>
            <div className={cx('content')}>
                <div className={cx('user-header')}>
                    <Grid container spacing={2}>
                        <Grid item xs={8} >
                            <h1>Danh sách đơn hàng</h1>
                        </Grid>
                        <Grid item xs={4} sx={{ textAlign: "right"}}>

                            <div className={cx('box-btn-add')}>
                                <Link to ={config.routes.addOrder}>
                                    <Button  variant="contained" className={cx('btn-add')} sx={{backgroundColor: '#222222'}}>
                                        <AddIcon sx={{fontSize: '1.5rem'}}/> Thêm mới</Button></Link>
                            </div>
                        </Grid>
                    </Grid>
                </div>
                <OrderList />
            </div>

        </div>
    )
}
