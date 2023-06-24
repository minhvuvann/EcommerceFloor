import React, {useEffect, useState} from 'react'

import classNames from 'classnames/bind';
import styles from './EditAccount.module.scss';
import {FormControlLabel, Grid, RadioGroup, Radio, FormControl, NativeSelect} from '@mui/material';
import UpLoadFileImage from '../../UploadFileImage/UpLoadFileImage';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import {Home} from '@mui/icons-material';
import config from "../../../config";
import {getDistricts, getProvinces, getUser, getWards} from "../../../services/workspaces.sevices";
import {useParams} from "react-router-dom";
import {format} from "date-fns";

function handleClick(event) {

    console.info('You clicked a breadcrumb.');
}

const cx = classNames.bind(styles);

function EditAccount() {
    const {id} = useParams();
    const breadcrumbs = [
        <a underline="hover" href={config.routes.dashboard} onClick={handleClick}>
            <Home/>
        </a>,
        <a underline="hover" href={config.routes.accountManager} key="1" onClick={handleClick}>
            Quản lý tài khoản
        </a>,
        <Typography key="3" color="text.primary">
           Cập nhật thông tin tài khoản
        </Typography>,
    ];

    const [password, setPassword] = useState();
    const [open, setOpen] = useState(true);
    const [images, setImages] = useState([]);
    const [showButton, setShowButton] = useState(true);
    const maxNumber = 1;
    const [submitted, setSubmitted] = useState(false);
    const [gender, setGender] = useState();
    const [role, setRole] = useState();
    const [fullName, setFullName] = useState();
    const [email, setEmail] = useState();
    const [telephone, setTelephone] = useState();
    const [birthday, setBirthday] = useState();
    const [provinceList, setProvinceList] = useState();
    const [province, setProvince] = useState();
    const [districtList, setDistrictList] = useState();
    const [district, setDistrict] = useState();
    const [wardList, setWardList] = useState();
    const[imgProperties, setImageProperties] = useState();
    const [ward, setWard] = useState();
    useEffect(() => {

        async function loadData() {
            const rsUser = (await getUser(id))?.data;
            setImageProperties(rsUser?.imageUrl);
            const provinceData = await getProvinces();
            const state = await getDistricts(rsUser?.address?.provinceCode ?
                rsUser?.address?.provinceCode : provinceData?.data[0]?.ghn_id);
            const stateD = await getWards(rsUser?.address?.districtCode ?
                rsUser?.address?.districtCode : state?.data[0]?.ghn_id);
            setProvinceList(provinceData?.data);
            setDistrictList(state?.data);
            setWardList(stateD?.data);
            setProvince(rsUser?.address?.provinceCode?
                rsUser?.address?.provinceCode : provinceData?.data[0]?.ghn_id);
            setDistrict(rsUser?.address?.districtCode? rsUser?.address?.districtCode : state?.data[0]?.ghn_id);
            setWard(rsUser?.address?.wardCode?rsUser?.address?.wardCode:stateD?.data[0]?.ghn_id);
            setEmail(rsUser.email);
            setTelephone(rsUser.telephone);
            setFullName(rsUser.fullName);
            setGender(rsUser.gender);
            setBirthday(format(rsUser.birthday ? rsUser.birthday : new Date(), 'yyyy-MM-dd'));
            setRole(rsUser.roleType);
        }

        loadData();
    }, []);
    const handleBirthDay = (event) => {
        setBirthday(event.target.value);
    }
    const handleProvince = async (event) => {
        setProvince(event.target.value);
        const state = await getDistricts(event.target.value);
        const stateD = await getWards(state?.data[0].ghn_id);
        setDistrict(state?.data[0].ghn_id);
        setDistrictList(state?.data);
        setWardList(stateD?.data);

    };
    const handleDistrict = async (event) => {
        const state = await getWards(event.target.value);
        setDistrict(event.target.value);
        setWard(state?.data[0].ghn_id);
        setWardList(state?.data);
    }
    const handleWard = (event) => {
        setWard(event.target.value);
    }
    const onChange = (imageList, addUpdateIndex) => {
        setImages(imageList);
        setShowButton(false)
        setOpen(false);
    };

    return (
        <div className={cx('add')}>
            <Grid container spacing={2} rowSpacing={0}>
                <Grid item xs={8}>
                    <Stack spacing={2}>

                        <Breadcrumbs
                            separator={<NavigateNextIcon fontSize="medium"/>}
                            aria-label="breadcrumb"
                        >
                            {breadcrumbs}
                        </Breadcrumbs>
                    </Stack>
                </Grid>
            </Grid>


            <div className={cx("modal")}>
                <form className={cx("form")}>
                    <div className={cx("separator")}>
                        <hr className={cx("line")}/>
                        <p>CẬP NHẬT THÔNG TIN TÀI KHOẢN</p>
                        <hr className={cx("line")}/>
                    </div>
                    <div className={cx("credit-card-info--form")}>
                        <Grid container style={{padding: "4rem 0"}}>
                            <Grid item xs={1.5}>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Họ và tên</label>
                                    <input id="password_field" className={cx("input_field")} type="text"
                                           value={fullName}
                                           onChange={(event) => setFullName(event.target.value)}
                                           name="input-name"
                                           title="Input title" placeholder="Vui lòng nhập họ và tên"/>
                                </div>

                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Email</label>
                                    <input id="password_field" className={cx("input_field")} type="text"
                                           name="input-name"
                                           value={email}
                                           onChange={(event) => setEmail(event.target.value)}
                                           title="Input title" placeholder="Vui lòng nhập email"/>
                                </div>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span>Số điện thoại</label>
                                    <input id="password_field" className={cx("input_field")} type="text"
                                           name="input-name"
                                           value={telephone}
                                           onChange={(event) => setTelephone(event.target.value)}
                                           title="Input title" placeholder="Vui lòng nhập số điện thoại"/>
                                </div>
                            </Grid>
                        </Grid>
                        <Grid container style={{padding: "3rem 0"}}>
                            <Grid item xs={1.5}>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Ngày sinh</label>
                                    <input
                                        type="date"
                                        onChange={handleBirthDay}
                                        value={birthday}
                                        className={cx("input_field")}/>
                                </div>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Giới tính</label>
                                    <RadioGroup
                                        style={{padding: "0 1rem"}}
                                        value={gender}
                                        onChange={(event) =>
                                            setGender(event.target.value)}
                                        sx={{display: "flex", flexDirection: "row"}}
                                    >
                                        <FormControlLabel
                                            value="MAN"
                                            control={<Radio/>}
                                            label={"Nam"}
                                        />
                                        <FormControlLabel
                                            value="WOMEN"
                                            control={<Radio/>}
                                            label={'Nữ'}
                                        />
                                        <FormControlLabel
                                            value="OTHER"
                                            control={<Radio/>}
                                            label={'Khác'}
                                        />
                                    </RadioGroup>
                                </div>

                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Phân quyền</label>
                                    <RadioGroup
                                        style={{padding: "0 1rem"}}
                                        value={role}
                                        onChange={(event) =>
                                            setRole(event.target.value)}
                                        sx={{display: "flex", flexDirection: "row"}}
                                    >
                                        <FormControlLabel
                                            value="ADMIN"
                                            control={<Radio/>}
                                            label={"Quản trị viên"}
                                        />
                                        <FormControlLabel
                                            value="PERSONAL"
                                            control={<Radio/>}
                                            label={'Khách hàng'}
                                        />
                                    </RadioGroup>
                                </div>

                            </Grid>

                        </Grid>
                        <Grid container style={{padding: "3rem 0"}}>
                            <Grid item xs={1.5}>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span> Mật khẩu</label>
                                    <input
                                        type="password"
                                        onChange={(event) => setPassword(event.target.value)}
                                        value={password}
                                        placeholder={"Vui lòng nhập mật khẩu"}
                                        className={cx("input_field")}/>
                                </div>
                            </Grid>
                            <Grid item xs={4}>
                                <div className={cx("input_container")}>
                                    <label htmlFor="password_field" className={cx("input_label")}><span
                                        className={cx("requirement")}>*</span>Địa chỉ</label>
                                    <Grid container sx={{marginBottom: '12px', marginLeft: "1px"}} spacing={2}
                                          direction={"row"}
                                          justifyContent={"flex-start"}>
                                        <Grid container item xs={4}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={province}
                                                    onChange={handleProvince}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{provinceList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                        <Grid item xs={4}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={district}
                                                    onChange={handleDistrict}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{districtList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                        <Grid item xs={4}>
                                            <FormControl>
                                                <NativeSelect
                                                    labelId="multiple-select-label"
                                                    id="multiple-select"
                                                    value={ward}
                                                    onChange={handleWard}
                                                    inputProps={{
                                                        name: 'values',
                                                        id: 'values',
                                                    }}
                                                >{wardList?.map(({ghn_id, name}) => (
                                                    <option value={ghn_id}>{name}</option>
                                                ))}

                                                </NativeSelect>
                                            </FormControl>
                                        </Grid>
                                    </Grid>
                                </div>
                            </Grid>
                            <Grid item xs={0.5}>
                            </Grid>
                            <Grid item xs={3}>
                                <div className={cx("input_container")}>
                                    <UpLoadFileImage
                                        imageProduct={imgProperties}
                                        showButton={showButton}
                                        open={open}
                                        buttonChose={"Chọn hình ảnh"}
                                        buttonUpdate={"Cập nhật"}
                                        images={images}
                                        name={fullName}
                                        maxNumber={maxNumber}
                                        onChange={onChange}
                                    />
                                </div>
                            </Grid>

                        </Grid>
                    </div>
                    <div style={{width: "100%", display: "flex", justifyContent: "center"}}>
                        <button className={cx("purchase--btn")}>LƯU VÀ HIỂN THỊ</button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default EditAccount;