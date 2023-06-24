import React, {useContext, useEffect, useState} from 'react';

import classNames from "classnames/bind";

import styles from "./InformationAddress.module.scss";
import {
    Box,
    Button,
    Container,
    createTheme,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle, FormControl, InputLabel, MenuItem, NativeSelect, OutlinedInput, Select, TextField,
    ThemeProvider, useTheme
} from "@mui/material";
import ItemAddress from "~/pages/InformationUser/account/address/itemAddress/ItemAddress";
import {AddOutlined} from "@mui/icons-material";
import Grid from "@mui/material/Grid";
import {
    getDistricts,
    getProvinces, getUser,
    getWards,
    updateAddressUser,
    updateShopAddress
} from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);
const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

const citys = [
    'Oliver Hansen',
    'Van Henry',
    'April Tucker',
    'Ralph Hubbard',
    'Omar Alexander',
    'Carlos Abbott',
    'Miriam Wagner',
    'Bradley Wilkerson',
    'Virginia Andrews',
    'Kelly Snyder',
];

function getStyles(name, personName, theme) {
    return {
        fontWeight:
            personName.indexOf(name) === -1
                ? theme.typography.fontWeightRegular
                : theme.typography.fontWeightMedium,
    };
}

function InformationAddress(props) {
    const [open, setOpen] = React.useState(false);
    const themes = useTheme();
    const [personName, setPersonName] = React.useState([]);
    const [provinceList, setProvinceList] = useState();
    const [province, setProvince] = useState();
    const [districtList, setDistrictList] = useState();
    const [district, setDistrict] = useState();
    const [wardList, setWardList] = useState();
    const [ward, setWard] = useState();
    const [userAddress, setUserAddress] = useState();
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const {localeList} = useContext(LanguageContext);


    useEffect(() => {

        async function loadData() {
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
            setUserAddress(rsUser?.address?.address1);
            console.log(rsUser);
        }

        loadData();
    }, []);
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
    const saveAddress = async () => {
        const address = {
            address1: userAddress,
            provinceCode: province,
            districtCode: district,
            wardCode: ward
        }
        const shopSave = await updateAddressUser(rsUser?.id, address);
        localStorage.setItem("cs-obj", JSON.stringify(shopSave?.data,));
        setUserAddress(shopSave?.data?.address?.address1);
        setWard(shopSave?.data.address?.wardCode);
        setDistrict(shopSave?.data?.address?.districtCode);
        setProvince(shopSave?.data?.address?.provinceCode);
    }
    const handleChange = (event) => {
        const {
            target: {value},
        } = event;
        setPersonName(
            typeof value === 'string' ? value.split(',') : value,
        );
    };
    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const theme = createTheme({
        palette: {
            primary: {
                // Purple and green play nicely together.
                main: '#ff5588 ',
            },
            secondary: {
                // This is green.A700 as hex.
                main: '#e3f2fd',
            },
        },
    });
    return (
        <div className={cx('wrapper')}>
            <Container>
                <div className={cx('address')}>
                    <div className={cx('myAddress')}>
                        <div>{localeList['info_my_address']}</div>
                        <div className={cx('btn')}>
                            {!rsUser?.address?.address1 && <ThemeProvider theme={theme}>
                                <Button className={cx('btnAdd')} sx={{fontSize: 13}} onClick={handleClickOpen}
                                        color='primary' variant="contained">
                                    <AddOutlined/> {localeList['button_add_address']}
                                </Button>
                            </ThemeProvider>}

                            <Dialog open={open} onClose={handleClose}>
                                <DialogTitle sx={{fontSize: 20, fontWeight: 200}}>Địa chỉ mới</DialogTitle>
                                <DialogContent>
                                    <Box
                                        component="form"
                                        sx={{
                                            '& > :not(style)': {m: 1, width: '25ch'},
                                        }}
                                        noValidate
                                        autoComplete="off"
                                    >
                                        <TextField id="name" label="Họ và tên" variant="outlined"/>
                                        <TextField id="phone" label="Số điện thoại" variant="outlined"/>
                                        <FormControl sx={{m: 1, width: 100}}>
                                            <InputLabel id="demo-multiple-name-label">Tỉnh/Thành phố</InputLabel>
                                            <Select
                                                labelId="demo-multiple-name-label"
                                                id="demo-multiple-name"
                                                multiple
                                                value={personName}
                                                onChange={handleChange}
                                                input={<OutlinedInput label="Tỉnh/Thành phố"/>}
                                                MenuProps={MenuProps}
                                            >
                                                {citys.map((city) => (
                                                    <MenuItem
                                                        key={city}
                                                        value={city}
                                                        style={getStyles(city, personName, themes)}
                                                    >
                                                        {city}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>
                                        <FormControl sx={{m: 1, width: 100}}>
                                            <InputLabel id="demo-multiple-name-label">Tỉnh/Thành phố</InputLabel>
                                            <Select
                                                labelId="demo-multiple-name-label"
                                                id="demo-multiple-name"
                                                multiple
                                                value={personName}
                                                onChange={handleChange}
                                                input={<OutlinedInput label="Tỉnh/Thành phố"/>}
                                                MenuProps={MenuProps}
                                            >
                                                {citys.map((city) => (
                                                    <MenuItem
                                                        key={city}
                                                        value={city}
                                                        style={getStyles(city, personName, themes)}
                                                    >
                                                        {city}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>
                                        <FormControl sx={{m: 1, width: 100}}>
                                            <InputLabel id="demo-multiple-name-label">Tỉnh/Thành phố</InputLabel>
                                            <Select
                                                labelId="demo-multiple-name-label"
                                                id="demo-multiple-name"
                                                multiple
                                                value={personName}
                                                onChange={handleChange}
                                                input={<OutlinedInput label="Tỉnh/Thành phố"/>}
                                                MenuProps={MenuProps}
                                            >
                                                {citys.map((city) => (
                                                    <MenuItem
                                                        key={city}
                                                        value={city}
                                                        style={getStyles(city, personName, themes)}
                                                    >
                                                        {city}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>

                                    </Box>

                                </DialogContent>
                                <DialogActions>
                                    <Button onClick={handleClose} sx={{fontSize: 13, color: '#f75583'}}>Trở về</Button>
                                    <Button onClick={handleClose} variant="contained"
                                            sx={{fontSize: 13, background: '#f75583'}}>Hoàn Thành</Button>
                                </DialogActions>
                            </Dialog>
                        </div>

                    </div>

                    <div className={cx('itemAddress')}>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={1.5}>
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
                            <Grid item xs={12} md={1.5}>
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
                            <Grid item xs={12} md={1.5}>
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
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>

                            <Grid container item xs={12} md={4}>
                                <TextField
                                    style={{
                                        width: "100%",
                                        marginLeft: "10%"
                                    }}
                                    id="filled-multiline-static"
                                    label="Địa chỉ chi tiết"
                                    multiline
                                    rows={2}
                                    value={userAddress}
                                    onChange={(event) =>
                                        setUserAddress(event.target.value)}
                                    variant="filled"
                                />
                            </Grid>
                        </Grid>
                        <Grid container sx={{marginBottom: '12px'}} spacing={2} direction={"row"}
                              justifyContent={"flex-start"}>
                            <Grid container item xs={12} md={1.5}>

                            </Grid>
                            <Grid item xs={12} md={6}>
                                <button onClick={saveAddress} style={{
                                    marginTop: "20px",
                                    width: '120px'
                                }}
                                        className='btn-add'>{localeList['button_save']}
                                </button>
                            </Grid>
                        </Grid>


                    </div>
                </div>
            </Container>
        </div>
    );
}

export default InformationAddress;
