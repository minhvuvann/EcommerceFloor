import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabPanel from '@mui/lab/TabPanel'
import {
    Button,
    createTheme,
    IconButton, InputBase,
    Paper,
    Table,
    TableBody,
    TableCell,
    tableCellClasses,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow,
    Tabs,
    ThemeProvider,
    Tooltip,
    tooltipClasses
} from "@mui/material";
import {Link} from "react-router-dom";

import styles from "./AllProduct.module.scss";
import {themeCustomer} from "~/components/CustomerMaterial";
import styled from "@emotion/styled";
import {AiFillDelete, AiTwotoneEdit} from 'react-icons/ai';
import MoneyUtils from "~/utils/MoneyUtils";
import {getProductFilter} from "~/services/workspaces.sevices";
import {BiSearch} from "react-icons/bi";
import DateUtils from "~/utils/DateUtils";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);


function ContentAllProduct(props) {
    const shopId = localStorage.getItem("seller-id");
    const theme = createTheme(themeCustomer)
    const [productShopAlls, setProductShopAlls] = useState([]);
    const [productEnds, setProductEnds] = useState([]);
    const [value, setValue] = useState('1');
    const [textSearch, setTextSearch] = useState("");
    const {localeList} = useContext(LanguageContext);
    useEffect(() => {
        const body = {
            maxResult: 100,
            shopId: shopId
        };
        getProductFilter(body).then((res) => setProductShopAlls(res.data.resultList));
    }, [shopId]);
    const enterSearch = (event) => {
        if (event.key === "Enter") {
            const body = {
                maxResult: 100,
                search: textSearch,
                shopId: shopId
            };
            getProductFilter(body).then((res) => setProductShopAlls(res.data.resultList));
            setTextSearch('');
        }

    }
    const clickSearch = () => {
        const body = {
            maxResult: 100,
            search: textSearch,
            shopId: shopId
        };
        getProductFilter(body).then((res) => setProductShopAlls(res.data.resultList));
        setTextSearch('');

    }
    const categoriesHeader = [
        {id: 1, label: localeList['tab_product_all']},
        {id: 2, label: localeList['tab_product_active']},
        {id: 3, label: localeList['tab_product_over']},

    ]
    const StyledTableCell = styled(TableCell)(({theme}) => ({
        [`&.${tableCellClasses.head}`]: {
            // backgroundColor: theme.palette.common.black,
            // color: theme.palette.common.white,
        },
        [`&.${tableCellClasses.body}`]: {
            fontWeight: 'normal',
            fontFamily: "Helvetica Neue,Helvetica,Arial,文泉驛正黑,WenQuanYi Zen Hei,Hiragino Sans GB,儷黑 Pro,LiHei Pro,Heiti TC,微軟正黑體,Microsoft JhengHei UI,Microsoft JhengHei,sans-serif"

        },

    }));
    const convertDate = (date) => {
        const newDate = new Date(date);
        return newDate.getDate() + '/' + newDate.getMonth() + '/' + newDate.getFullYear();

    }

    const StyledTableRow = styled(TableRow)(({theme}) => ({
        '&:nth-of-type(odd)': {
            backgroundColor: '#F5F5F5',
        },
        // hide last border
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));
    const LightTooltip = styled(({className, ...props}) => (
        <Tooltip {...props} classes={{popper: className}}/>
    ))(({theme}) => ({
        [`& .${tooltipClasses.tooltip}`]: {
            fontSize: 16,
        },
    }));

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };
    const getPrice = (newPrice) => {
        return MoneyUtils.getMoney(newPrice);
    }

    return (
        <div className={cx('content-wrapper')}>
            <div className={cx('search')}>
                <InputBase
                    fullWidth
                    style={{fontSize: "1.6rem"}}
                    spellCheck={false}
                    value={textSearch}
                    onChange={(event) => setTextSearch(event.target.value)}
                    // onFocus={()=> setShowResult(true)}
                    onKeyPress={enterSearch}
                    placeholder={
                        localeList['search_product']
                    }
                />
                <Button
                    onClick={clickSearch}
                    className={cx("btn-search")}
                    size={"medium"}
                    variant="contained"
                >
                    <BiSearch className={cx("search-icon")}/>
                </Button>
            </div>
            <div className={cx('content-wrapper')}>
                <div className={cx('tab-header')}>
                    <Box sx={{width: '100%', typography: 'body1'}}>
                        <TabContext value={value}>
                            <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                                <ThemeProvider theme={theme}>
                                    <Tabs onChange={handleChange}
                                          value={value}
                                          textColor="primary"
                                          indicatorColor="primary"
                                    >
                                        {
                                            categoriesHeader.map((item, index) => {
                                                return (
                                                    <Tab key={item.id}
                                                         label={item.label}
                                                         value={`${item.id}`}
                                                         sx={{fontSize: "1.6rem", textTransform: 'none'}}
                                                    />
                                                )
                                            })
                                        }

                                    </Tabs>
                                </ThemeProvider>
                            </Box>
                            <TabPanel value="1">
                                <Paper sx={{width: '100%', overflow: 'hidden'}}>
                                    <TableContainer>
                                        <Table sx={{minWidth: 768}} aria-label="customized table">
                                            <TableHead>
                                                <TableRow>
                                                    <StyledTableCell>{localeList['td_product_id']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_name']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_image']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_price']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_quantity']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_created']}</StyledTableCell>
                                                    <StyledTableCell align="left">{localeList['td_product_operation']}</StyledTableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {productShopAlls?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)?.map((row) => (
                                                    <StyledTableRow key={row.id}>
                                                        <StyledTableCell align="left">{row.id}</StyledTableCell>
                                                        <StyledTableCell align="left">{row.name}</StyledTableCell>
                                                        <StyledTableCell align="left">
                                                            <img style={{width: "100px", height: "100px"}}
                                                                 src={row.featuredImageUrl} alt={row.name}/>
                                                        </StyledTableCell>
                                                        <StyledTableCell
                                                            align="left">{getPrice(row.mediumPrice.amount)}</StyledTableCell>
                                                        <StyledTableCell
                                                            align="left">{row.quantityAvailable}</StyledTableCell>
                                                        <StyledTableCell
                                                            align="left">{DateUtils.convert(row.createdAt)}</StyledTableCell>
                                                        <StyledTableCell align="left">
                                                            <div className={cx('function')}>
                                                                <ThemeProvider theme={theme}>
                                                                    <LightTooltip title="Delete">
                                                                        <IconButton color='danger' size="large"
                                                                                    aria-label="delete">
                                                                            <AiFillDelete/>
                                                                        </IconButton>
                                                                    </LightTooltip>

                                                                    <Link to={`/product/edit/${row.id}/product`}>
                                                                        <LightTooltip title="EditAccount">
                                                                            <IconButton color='edit'
                                                                                        size="large"
                                                                                        aria-label="edit"
                                                                            >
                                                                                <AiTwotoneEdit/>
                                                                            </IconButton>

                                                                        </LightTooltip>
                                                                    </Link>
                                                                </ThemeProvider>

                                                            </div>
                                                        </StyledTableCell>

                                                    </StyledTableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </TableContainer>
                                    <TablePagination
                                        sx={{
                                            fontWeight: 'bold',
                                            mx: 0.5,
                                            fontSize: 16,
                                        }}
                                        rowsPerPageOptions={[5, 10, 25, 50, 100]}

                                        component="div"
                                        count={productShopAlls?.length || 0}
                                        rowsPerPage={rowsPerPage}
                                        labelRowsPerPage={localeList['td_product_chose_paging']}
                                        page={page}
                                        onPageChange={handleChangePage}
                                        onRowsPerPageChange={handleChangeRowsPerPage}
                                    />
                                </Paper>
                            </TabPanel>
                            <TabPanel value="2"> <Paper sx={{width: '100%', overflow: 'hidden'}}>
                                <TableContainer>
                                    <Table sx={{minWidth: 768}} aria-label="customized table">
                                        <TableHead>
                                            <TableRow>
                                                <StyledTableCell>{localeList['td_product_id']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_name']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_image']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_price']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_quantity']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_created']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_operation']}</StyledTableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {productShopAlls?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)?.map((row) => (
                                                <StyledTableRow key={row.id}>
                                                    <StyledTableCell align="left">{row.id}</StyledTableCell>
                                                    <StyledTableCell align="left">{row.name}</StyledTableCell>
                                                    <StyledTableCell align="left">
                                                        <img style={{width: "100px", height: "100px"}}
                                                             src={row.featuredImageUrl} alt={row.name}/>
                                                    </StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{getPrice(row.mediumPrice.amount)}</StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{row.quantityAvailable}</StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{DateUtils.convert(row.createdAt)}</StyledTableCell>
                                                    <StyledTableCell align="left">
                                                        <div className={cx('function')}>
                                                            <ThemeProvider theme={theme}>
                                                                <LightTooltip title="Delete">
                                                                    <IconButton color='danger' size="large"
                                                                                aria-label="delete">
                                                                        <AiFillDelete/>
                                                                    </IconButton>
                                                                </LightTooltip>

                                                                <Link to={`/product/edit/${row.id}/product`}>
                                                                    <LightTooltip title="EditAccount">
                                                                        <IconButton color='edit'
                                                                                    size="large"
                                                                                    aria-label="edit"
                                                                        >
                                                                            <AiTwotoneEdit/>
                                                                        </IconButton>

                                                                    </LightTooltip>
                                                                </Link>
                                                            </ThemeProvider>

                                                        </div>
                                                    </StyledTableCell>

                                                </StyledTableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                                <TablePagination
                                    sx={{
                                        fontWeight: 'bold',
                                        mx: 0.5,
                                        fontSize: 16,
                                    }}
                                    rowsPerPageOptions={[5, 10, 25, 50, 100]}

                                    component="div"
                                    count={productShopAlls?.length || 0}
                                    rowsPerPage={rowsPerPage}
                                    labelRowsPerPage={localeList['td_product_chose_paging']}

                                    page={page}
                                    onPageChange={handleChangePage}
                                    onRowsPerPageChange={handleChangeRowsPerPage}
                                />
                            </Paper></TabPanel>
                            <TabPanel value="3"> <Paper sx={{width: '100%', overflow: 'hidden'}}>
                                <TableContainer>
                                    <Table sx={{minWidth: 768}} aria-label="customized table">
                                        <TableHead>
                                            <TableRow>
                                                <StyledTableCell>{localeList['td_product_id']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_name']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_image']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_price']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_quantity']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_created']}</StyledTableCell>
                                                <StyledTableCell align="left">{localeList['td_product_operation']}</StyledTableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {productEnds?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)?.map((row) => (
                                                <StyledTableRow key={row.id}>
                                                    <StyledTableCell align="left">{row.id}</StyledTableCell>
                                                    <StyledTableCell align="left">{row.name}</StyledTableCell>
                                                    <StyledTableCell align="left">
                                                        <img style={{width: "100px", height: "100px"}}
                                                             src={row.featuredImageUrl} alt={row.name}/>
                                                    </StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{getPrice(row.mediumPrice.amount)}</StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{row.quantityAvailable}</StyledTableCell>
                                                    <StyledTableCell
                                                        align="left">{DateUtils.convert(row.createdAt)}</StyledTableCell>
                                                    <StyledTableCell align="left">
                                                        <div className={cx('function')}>
                                                            <ThemeProvider theme={theme}>
                                                                <LightTooltip title="Delete">
                                                                    <IconButton color='danger' size="large"
                                                                                aria-label="delete">
                                                                        <AiFillDelete/>
                                                                    </IconButton>
                                                                </LightTooltip>

                                                                <Link to={`/product/edit/${row.id}/product`}>
                                                                    <LightTooltip title="EditAccount">
                                                                        <IconButton color='edit'
                                                                                    size="large"
                                                                                    aria-label="edit"
                                                                        >
                                                                            <AiTwotoneEdit/>
                                                                        </IconButton>

                                                                    </LightTooltip>
                                                                </Link>
                                                            </ThemeProvider>

                                                        </div>
                                                    </StyledTableCell>

                                                </StyledTableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                                <TablePagination
                                    sx={{
                                        fontWeight: 'bold',
                                        mx: 0.5,
                                        fontSize: 16,
                                    }}
                                    rowsPerPageOptions={[5, 10, 25, 50, 100]}
                                    component="div"
                                    count={productShopAlls?.length || 0}
                                    rowsPerPage={rowsPerPage}
                                    labelRowsPerPage={localeList['td_product_chose_paging']}
                                    page={page}
                                    onPageChange={handleChangePage}
                                    onRowsPerPageChange={handleChangeRowsPerPage}
                                />
                            </Paper></TabPanel>

                        </TabContext>
                    </Box>

                </div>
            </div>
        </div>
    );
}

export default ContentAllProduct;
