import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import classNames from "classnames/bind";
import styles from "./Table.module.scss";
import {useEffect, useState} from "react";
import {filterOrder} from "../../../services/workspaces.sevices";
import {Grid, Pagination, PaginationItem, useMediaQuery} from "@mui/material";
import {ArrowLeft, ArrowRight} from "../../Icon";
import ConvertorStatus from "../../../utils/order/ConvertorStatus";
import StatusColor from "../../../utils/order/StatusColor";
import DateUtils from "../../../utils/DateUtils";
import MoneyUtils from "../../../utils/MoneyUtils";

const cx = classNames.bind(styles);


const makeStyle = (status) => {
    if (status === 'Approved') {
        return {
            background: 'rgb(145 254 159 / 47%)',
            color: 'green',
        }
    } else if (status === 'Pending') {
        return {
            background: '#ffadad8f',
            color: 'red',
        }
    } else {
        return {
            background: '#59bfff',
            color: 'white',
        }
    }
}

export default function BasicTable() {
    const [orderAll, setOrderAll] = useState([]);
    const [page, setPage] = useState(1);
    const [rowsPerPage] = useState(4);
    const startIndex = (page - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;
    const currentOrders = orderAll.slice(startIndex, endIndex);
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };
    useEffect(() => {
        const body = {
            maxResult: 1000,
            type: 'SELL'
        };
        filterOrder(body).then((res) => setOrderAll(res.data.resultList));
    }, []);

    return (
        <div className={cx("Table")}>
            <h2>Danh sách đơn hàng</h2>
            <TableContainer
                component={Paper}
                style={{boxShadow: "0px 13px 20px 0px #80808029"}}
            >
                <Table sx={{minWidth: 650}} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Mã đơn hàng</TableCell>
                            <TableCell align="left">Người bán</TableCell>
                            <TableCell align="left">Người mua</TableCell>
                            <TableCell align="center">Tổng tiền</TableCell>
                            <TableCell align="center">Trạng thái</TableCell>
                            <TableCell align="center">Ngày tạo</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody style={{color: "white"}}>
                        {currentOrders.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{"&:last-child td, &:last-child th": {border: 0}}}
                            >
                                <TableCell component="th" scope="row">
                                     <span  className={cx("Details")}>#
                                         {row.id}
                                     </span>
                                </TableCell>
                                <TableCell align="left">
                                    <p>
                                        <b>Mã shop: </b>{row?.shopId}
                                    </p>
                                    <p>
                                        <b>Tên shop: </b>{row?.shopName}
                                    </p>
                                </TableCell>
                                <TableCell align="left">
                                    <p>
                                        <b>Tên: </b>{row?.nameCustomer}
                                    </p>
                                    <p>
                                        <b>SĐT: </b>{row?.phoneCustomer}
                                    </p>
                                    <p>
                                        <b>Email: </b>{row?.emailCustomer}
                                    </p></TableCell>
                                <TableCell align="center">{MoneyUtils.getMoney(row.totalPrice.amount)}</TableCell>
                                <TableCell align="center">
                                    <span className={cx("status", StatusColor.convert(row.status))}>
                                        {ConvertorStatus.convert(row.status)}</span>
                                </TableCell>
                                <TableCell align="center"
                                          >{DateUtils.convert(row.createdAt)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Grid item md={12} style={{paddingTop: "40px", display: "flex", justifyContent: "center"}}>
                <Pagination
                    count={Math.ceil(orderAll.length / rowsPerPage)}
                    page={page}
                    onChange={handleChangePage}
                    color="primary"
                    renderItem={(item) => (
                        <PaginationItem
                            slots={{
                                previous: ArrowLeft,
                                next: ArrowRight,
                            }}
                            {...item}
                        />
                    )}
                />
            </Grid>
        </div>
    );
}
