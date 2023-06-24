import React from 'react'
import classNames from 'classnames/bind'
import styles from './Shops.module.scss'
import User from '../../components/Shop/list/User';
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
import InputBase from '@mui/material/InputBase';
import SearchIcon from '@mui/icons-material/Search';
import { useState } from 'react';
function handleClick(event) {
  event.preventDefault();
  console.info('You clicked a breadcrumb.');
}

const cx = classNames.bind(styles);
const Search = styled('div')(({ theme }) => ({
  borderBottom: '1px solid black',
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(1),
    width: 'auto',
  },
}));

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'inherit',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      width: '12ch',
      '&:focus': {
        width: '20ch',
      },
    },
  },
}));

export default function Shops() {


  const breadcrumbs = [
    <Link underline="hover" key="1" color="primary"  to={config.routes.dashboard} onClick={handleClick}>
      <Home color='disabled'/>
    </Link>,
   
    <Typography key="3" color="text.primary" sx={{fontSize: '1.1rem'}}>
          Người bán 
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
                <h1>Danh sách người bán </h1>
                </Grid>
                <Grid item xs={4} sx={{ textAlign: "right"}}>
              <div className={cx('user-search')}>
              <Search style={{
                // width: '70%',
              }}>
                <SearchIconWrapper>
                  <SearchIcon  />
                </SearchIconWrapper>
                <StyledInputBase
                  placeholder="Search…"
                  inputProps={{ 'aria-label': 'search' }}
                />
              </Search>
              </div>
              <div className={cx('btn-add')}>
                 <Link to ={config.routes.addShop}> 
                 <Button  variant="contained" sx={{}}> 
                 <AddIcon sx={{fontSize: '1.5rem'}}/> Thêm</Button></Link>
              </div>
  
                </Grid>
              </Grid>
          </div>
          <User/>
        </div>
      
    </div>
  )
}
