import React, {useEffect} from 'react'
import classNames from 'classnames/bind'

import styles from './AccountManager.module.scss'
import AccountList from '../../components/Account/list/AccountList';
import {  Button, Grid, } from '@mui/material';
import { styled, alpha } from '@mui/material/styles';

import AddIcon from '@mui/icons-material/Add';
import { Link } from 'react-router-dom';
import config from '../../config';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Home } from '@mui/icons-material';

function handleClick(event) {
  event.preventDefault();
  console.info('You clicked a breadcrumb.');
}

const cx = classNames.bind(styles);


export default function AccountManager() {
  const breadcrumbs = [
    <Link underline="hover" key="1" color="primary"  to={config.routes.dashboard} onClick={handleClick}>
      <Home color='disabled'/>
    </Link>,
   
    <Typography key="3" color="text.primary" sx={{fontSize: '1.1rem'}}>
           Quản lý tài khoản
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
                <h1>Danh sách tài khoản</h1>
                </Grid>
                <Grid item xs={4} sx={{ textAlign: "right"}}>

              <div className={cx('box-btn-add')}>
                 <Link to ={config.routes.addAccount}>
                 <Button  variant="contained" className={cx('btn-add')} sx={{backgroundColor: '#222222'}}>
                 <AddIcon sx={{fontSize: '1.5rem'}}/> Thêm mới</Button></Link>
              </div>
                </Grid>
              </Grid>
          </div>
          <AccountList />
        </div>
      
    </div>
  )
}
