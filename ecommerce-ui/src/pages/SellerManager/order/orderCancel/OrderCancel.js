import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import Box from '@mui/material/Box';
import {
    Button,
    createTheme,
    InputBase,
    Paper,
    Table,
    TableBody,
    TableCell,
    tableCellClasses,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow,

} from "@mui/material";

import styles from "./OrderCancel.module.scss";
import {themeCustomer} from "~/components/CustomerMaterial";
import styled from "@emotion/styled";
import MoneyUtils from "~/utils/MoneyUtils";
import {confirmStatusSequence, filterOrder, getShopById} from "~/services/workspaces.sevices";
import {BiSearch} from "react-icons/bi";
import ConvertorStatus from "~/utils/order/ConvertorStatus";
import {Dialog, DialogContent, DialogContentText} from "@material-ui/core";
import DateUtils from "~/utils/DateUtils";
import StatusColor from "~/utils/order/StatusColor";
import {LanguageContext} from "~/config/provider/LanguageProvider";


const cx = classNames.bind(styles);


function OrderCancel(props) {
    const shopId = localStorage.getItem("seller-id");
    const theme = createTheme(themeCustomer)
    const [orderCancells, setOrderCancells] = useState([]);
    const [textSearch, setTextSearch] = useState("");
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [showDialog, setShowDialog] = useState(false);
    const [message, setMessage] = useState("");
    const [shop, setShop] = useState();
    const {localeList} = useContext(LanguageContext);

    const StyledTableCell = styled(TableCell)(({theme}) => ({
        [`&.${tableCellClasses.head}`]: {
            // backgroundColor: theme.palette.common.black,
            // color: theme.palette.common.white,
        },
        [`&.${tableCellClasses.body}`]: {
            fontSize: 14,
            fontWeight: 'normal',
            fontFamily: "Helvetica Neue,Helvetica,Arial,文泉驛正黑,WenQuanYi Zen Hei,Hiragino Sans GB,儷黑 Pro,LiHei Pro,Heiti TC,微軟正黑體,Microsoft JhengHei UI,Microsoft JhengHei,sans-serif"

        },

    }));

    const StyledTableRow = styled(TableRow)(({theme}) => ({
        '&:nth-of-type(odd)': {
            backgroundColor: '#F5F5F5',
        },
        // hide last border
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));

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
    useEffect(() => {
        const body = {
            maxResult: 100,
            shopId: shopId,
            type: "SELL",
            status: "CANCELLED",

        };
        filterOrder(body).then((res) => setOrderCancells(res.data.resultList));
        getShopById(shopId).then((res) => setShop(res?.data));
    }, [shopId]);
    const enterSearch = (event) => {
        if (event.key === "Enter") {
            const body = {
                maxResult: 100,
                type: "SELL",
                search: textSearch,
                shopId: shopId,
                status: "CANCELLED",

            };
            filterOrder(body).then((res) => setOrderCancells(res.data.resultList));
            setTextSearch('');
        }

    }
    const clickSearch = () => {
        const body = {
            maxResult: 100,
            type: "SELL",
            search: textSearch,
            shopId: shopId,
            status: "CANCELLED",

        };
        filterOrder(body).then((res) => setOrderCancells(res.data.resultList));
        setTextSearch('');

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
                        localeList['search_order']
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

                        <Paper sx={{width: '100%', overflow: 'hidden'}}>
                            <TableContainer>
                                <Table sx={{minWidth: 768}} stickyHeader aria-label="sticky table">
                                    <TableHead>
                                        <TableRow>
                                            <StyledTableCell>{localeList['td_order_id']}</StyledTableCell>
                                            <StyledTableCell
                                                align="left">{localeList['td_order_info_customer']}</StyledTableCell>
                                            <StyledTableCell align="left">{localeList['td_order_pay']}</StyledTableCell>
                                            <StyledTableCell
                                                align="left">{localeList['td_order_total_price']}</StyledTableCell>
                                            <StyledTableCell
                                                align="center">{localeList['td_order_status']}</StyledTableCell>
                                            <StyledTableCell
                                                align="center">{localeList['td_order_created']}</StyledTableCell>

                                        </TableRow>
                                    </TableHead>
                                    <Dialog open={showDialog}>
                                        <DialogContent>
                                            <DialogContentText>
                                                {message}
                                            </DialogContentText>
                                        </DialogContent>
                                    </Dialog>
                                    <TableBody>
                                        {orderCancells?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)?.map((row) => (
                                            <StyledTableRow key={row.id}>
                                                <StyledTableCell align="left"> <span
                                                    style={{color: "#ff5588"}}>{row.id}</span></StyledTableCell>
                                                <StyledTableCell align="left">
                                                    <b>{localeList['info_name']}: </b> {row.nameCustomer}
                                                    <br/>
                                                    <b>Email: </b> {row.emailCustomer}
                                                    <br/>
                                                    <b>{localeList['info_telephone']}: </b> {row.phoneCustomer}
                                                </StyledTableCell>
                                                <StyledTableCell align="left">
                                                    {row.payment ? localeList['method_pay_qr'] : localeList['method_pay_receive']}
                                                </StyledTableCell>
                                                <StyledTableCell
                                                    align="left">{getPrice(row.totalPrice.amount)}</StyledTableCell>
                                                <StyledTableCell
                                                    align="center">
                                                 <span className={cx("status-sp",
                                                     StatusColor.convert(row.status))}>
                                                    {ConvertorStatus.convert(localeList,row.status)}
                                                </span>
                                                </StyledTableCell>
                                                <StyledTableCell
                                                    align="left">{DateUtils.convert(row.createdAt)}</StyledTableCell>
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
                                count={orderCancells?.length || 0}
                                rowsPerPage={rowsPerPage}
                                labelRowsPerPage={localeList['td_order_chose_paging']}

                                page={page}
                                onPageChange={handleChangePage}
                                onRowsPerPageChange={handleChangeRowsPerPage}
                            />
                        </Paper>
                    </Box>

                </div>
            </div>
        </div>
    );
}

export default OrderCancel;
