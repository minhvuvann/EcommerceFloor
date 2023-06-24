import React, { useState } from 'react'

import classNames from 'classnames/bind';
import styles from './Add.module.scss';
import { Grid } from '@mui/material';
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

function Add() {
    const [value, setValue] = useState("");
    const [open, setOpen] = useState(true);
    const [images, setImages] = useState([]);
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const [showButton, setShowButton] = useState(true)
    const maxNumber = 1;
    const [submitted, setSubmitted] = useState(false);

    const handleSubmit = (values, { setSubmitting }) => {
      setSubmitting(false);
      setSubmitted(true);
    };
    const handleChange = (event) => {
        setValue(event.target.value);
    };
    const breadcrumbs = [
        <a underline="hover" href="/dashboard" onClick={handleClick}>
          <Home/>
        </a>,
       <a underline="hover" href="/users" key="1"   onClick={handleClick}>
       Người bán
     </a>,
        <Typography key="3" color="text.primary" >
              Thêm người bán mới
        </Typography>,
      ];
      const onChange = (imageList, addUpdateIndex) => {
        // data for submit
        // console.log(imageList, addUpdateIndex);
        setImages(imageList);
        setShowButton(false)
        setOpen(false);
    };
  return (
    <div className={cx('add')}>
        <Grid container spacing={2} rowSpacing={0}>
  
        <Grid item xs={1.7}>
        <h4>Người bán</h4>
            
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
  
           <form>
           <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="id" type="text" className={cx("validate","form-control")} placeholder="Mã người bán"/>
                </div>
                </Grid>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="name" type="text" className={cx("validate","form-control")} placeholder="Tên người bán"/>
                </div>
                </Grid>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="nameProduct" type="text" className={cx("validate","form-control")} placeholder="Tên ngành hàng"/>
                </div>
                </Grid>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="follower" type='number' min='0'  className={cx("validate","form-control")} placeholder="Số người theo dõi"/>
                </div>
                </Grid>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="evaluation" type="" className={cx("validate","form-control")} placeholder="Đánh giá"/>
                </div>
                </Grid>
                <Grid item xs={6}>
                <div className={cx("form-line")}>
                    <input id="dateCreate"  className={cx("validate","form-control")} placeholder="Ngày tạo (Giờ dd/mm/yyyy)"/>
                </div>
                </Grid>
               
            </Grid>
            <div className={cx("form-line")}>
                <input id="email" type="text" className={cx("validate","form-control")} placeholder="Địa chỉ"/>
            </div>
            <div className={cx("upload")}>
               <p>Chọn ảnh mới</p>
                <UpLoadFileImage  
                    imageProduct={rsUser.imageUrl}
                    showButton={showButton}
                    open={open}
                    images={images}
                    name={"product"}
                    maxNumber={maxNumber}
                    onChange={onChange}/>
            </div>
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