import React, { useState } from 'react'

import classNames from 'classnames/bind';
import styles from './Add.module.scss';
import { Box, Grid, MenuItem, TextField } from '@mui/material';
import UpLoadFileImage from '../../UploadFileImage/UpLoadFileImage';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import { Home } from '@mui/icons-material';


function handleClick(event) {
   
    console.info('You clicked a breadcrumb.');
  }
const cx= classNames.bind(styles);
const status = [
  {
    value: 'new',
    label: 'Mới',
  },
  {
    value: 'waitPay',
    label: 'Chờ thanh toán',
  },
  {
    value: 'Shipping',
    label: 'Vận chuyển',
  },
  {
    value: 'delivery',
    label: 'Đang giao',
  },
  {
    value: 'completed',
    label: 'Hoàn thành',
  },
  {
    value: 'cancel',
    label: 'Hủy',
  },
  {
    value: 'refund',
    label: 'Trả hàng/Hoàn tiền',
  },
];
const payment = [
  {
    value: 'unpaid',
    label: 'Chưa thanh toán',
  },
  {
    value: 'paid',
    label: 'Đã thanh toán',
  },


];
function Add() {
    const breadcrumbs = [
        <a underline="hover" href="/dashboard" onClick={handleClick}>
          <Home/>
        </a>,
       <a underline="hover" href="/users" key="1"   onClick={handleClick}>
       Đơn Hàng
     </a>,
        <Typography key="3" color="text.primary" >
              Thêm đơn hàng mới
        </Typography>,
      ];
      
  return (
    <div className={cx('add')}>
        <Grid container spacing={2} rowSpacing={0}>
  
        <Grid item xs={1.7}>
        <h4>Đơn hàng</h4>
            
        </Grid>
        <Grid item xs={8}>
        <Stack spacing={2}>
     
     <Breadcrumbs
       separator={<NavigateNextIcon fontSize="medium" />}
       aria-label="breadcrumb"
     >
       {breadcrumbs}
     </Breadcrumbs>
        </Stack>
        </Grid>
</Grid>
       
       <div className={cx('form-add')}>
        <h1>Thêm mới</h1>
     
           <form >
           <Box
              component="form"
              sx={{
                '& .MuiTextField-root': { m: 1, width: '25ch' },
              }}
              noValidate
              autoComplete="off"
            >
              <div>
                <TextField
                  required
                  id="outlined-required"
                  label="Mã đơn hàng"
                />
                
                
                <TextField
                  id="outlined-read-only-input"
                  label="Khách hàng"
                 
                />
                <TextField
                  id="outlined-number"
                  label="Kênh bán hàng"
                 
                />
               <TextField
                  id="outlined-select-currency"
                  select
                  label="Trạng thái"
                  defaultValue="new"
                  helperText=""
                >
                  {status.map((option) => (
                    <MenuItem key={option.value} value={option.value}>
                      {option.label}
                    </MenuItem>
                  ))}
                </TextField>
                <TextField
                  id="outlined-select-currency"
                  select
                  label="Thanh toán"
                  defaultValue="unpaid"
                  helperText=""
                >
                  {payment.map((option) => (
                    <MenuItem key={option.value} value={option.value}>
                      {option.label}
                    </MenuItem>
                  ))}
                </TextField>
                <TextField
                    id="date"
                    label="Ngày tạo"
                    
                  />
              </div>
           
            </Box>
           
            <div className={cx("submit")}>
                <button type="button" className={cx("btn",'btn-submit')}>Xác nhận</button>
                <button type="button" className={cx("btn",'btn-cancel')}>Hủy</button>
    
            </div>
           </form>
       </div>
    </div>
  )
}

export default Add