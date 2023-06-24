import * as React from 'react';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {
  Avatar,
  Button,
  createTheme,
  Dialog, DialogActions,
  DialogContent,
  DialogContentText, DialogTitle,
  InputBase,
  tableCellClasses, Tooltip, tooltipClasses
} from '@mui/material';
import classNames from 'classnames/bind'
import styles from './OrderList.module.scss'
import styled from "@emotion/styled";
import {useEffect, useState} from "react";
import DateUtils from "../../../utils/DateUtils";
import {BiSearch} from "react-icons/bi";
import {filterOrder, filterUser} from "../../../services/workspaces.sevices";
import {Link} from "react-router-dom";
import {ThemeProvider} from "@emotion/react";
import IconButton from "@mui/material/IconButton";
import {AiFillDelete, AiTwotoneEdit} from "react-icons/ai";
import {themeCustomer} from "../../CustomerMaterial";
import config from "../../../config";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import MoneyUtils from "../../../utils/MoneyUtils";
import StatusColor from "../../../utils/order/StatusColor";
import ConvertorStatus from "../../../utils/order/ConvertorStatus";

const cx = classNames.bind(styles);

const LightTooltip = styled(({className, ...props}) => (
    <Tooltip {...props} classes={{popper: className}}/>
))(({theme}) => ({
  [`& .${tooltipClasses.tooltip}`]: {
    fontSize: 16,
  },
}));
export default function OrderList(props) {
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

  const StyledTableRow = styled(TableRow)(({theme}) => ({
    '&:nth-of-type(odd)': {
      backgroundColor: '#F5F5F5',
    },
    // hide last border
    '&:last-child td, &:last-child th': {
      border: 0,
    },
  }));
  const theme = createTheme(themeCustomer)
  const [page, setPage] = React.useState(0);
  const [textSearch, setTextSearch] = useState("");
  const [rowsPerPage, setRowsPerPage] = React.useState(4);
  const [showDialog, setShowDialog] = useState(false);
  const [showDelete, setShowDelete] = useState(false);
  const [idDelete, setIdDelete] = useState('');
  const [orderList, setOrderList] = useState([]);
  const enterSearch = (event) => {
    if (event.key === "Enter") {
      const body = {
        search: textSearch,
      };
      filterOrder(body).then((res) => setOrderList(res.data.resultList));
      setTextSearch('');
    }

  }
  const clickSearch = () => {
    const body = {
      search: textSearch,
    };
    filterOrder(body).then((res) => setOrderList(res.data.resultList));
    setTextSearch('');

  }

  useEffect(() => {
    const body = {
      maxResult: 1000,
    };
    filterOrder(body).then((res) => setOrderList(res.data.resultList));
  }, []);


  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const actionDelete = (id_row) => {
    setShowDelete(true);
    setIdDelete(id_row);
  }

  function submitDelete() {

  }

//
  return (
      <div className={cx('content-wrapper')}>
        <div className={cx('search')}>
          <InputBase
              fullWidth
              style={{fontSize: "1.6rem"}}
              spellCheck={false}
              value={textSearch}
              onChange={(event) => setTextSearch(event.target.value)}
              onKeyPress={enterSearch}
              placeholder={
                'Tìm kiếm tài khoản'
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
          <Dialog
              open={showDelete}
              onClose={() => setShowDelete(false)}
              aria-labelledby="alert-dialog-title"
              aria-describedby="alert-dialog-description"
          >
            <DialogTitle id="alert-dialog-title" sx={{fontSize: '16px', color: 'black'}}>
              {"Yêu cầu xoá đơn hàng?"}
            </DialogTitle>
            <DialogContent>
              <DialogContentText id="alert-dialog-description"
                                 sx={{fontSize: '17px', color: 'black'}}>
                Bạn có chắc là muốn xoá đơn hàng {idDelete}
              </DialogContentText>
            </DialogContent>
            <DialogActions>
              <Button sx={{fontSize: '15px'}} onClick={() => setShowDelete(false)}>Hủy</Button>
              <Button sx={{fontSize: '15px'}} onClick={submitDelete} autoFocus>
                Đồng ý
              </Button>
            </DialogActions>
          </Dialog>
          <div className={cx('tab-header')}>
            <Box sx={{width: '100%', typography: 'body1'}}>

              <Paper sx={{width: '100%', overflow: 'hidden'}}>
                <TableContainer>
                  <Table sx={{minWidth: 768}} stickyHeader aria-label="sticky table">
                    <TableHead>
                      <TableRow>
                        <TableCell>Mã đơn hàng</TableCell>
                        <TableCell align="left">Người bán</TableCell>
                        <TableCell align="left">Người mua</TableCell>
                        <TableCell align="center">Tổng tiền</TableCell>
                        <TableCell align="center">Thanh toán</TableCell>
                        <TableCell align="center">Trạng thái</TableCell>
                        <TableCell align="center">Ngày tạo</TableCell>
                        <StyledTableCell
                            align="center">Thao tác</StyledTableCell>
                      </TableRow>
                    </TableHead>
                    <Dialog open={showDialog}>
                      <DialogContent>
                        <div style={{fontSize: "1.4rem", display: "flex"}}>
                          <CheckBoxIcon style={{color: "#22c55e"}}/>
                          <span style={{paddingTop: "4px"}}>
                                          Xoá đơn hàng thành công
                                      </span>
                        </div>
                      </DialogContent>
                    </Dialog>
                    <TableBody>
                      {orderList?.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)?.map((row) => (
                          <StyledTableRow key={row.id}>
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
                            <TableCell align="center">{row.payment?"QR":"Thanh toán khi nhận hàng"}</TableCell>

                            <TableCell align="center">
                                    <span className={cx("status", StatusColor.convert(row.status))}>
                                        {ConvertorStatus.convert(row.status)}</span>
                            </TableCell>
                            <TableCell align="center"
                            >{DateUtils.convert(row.createdAt)}</TableCell>
                            <StyledTableCell align="center">
                              <div className={cx('function')}>
                                <ThemeProvider theme={theme}>
                                  <LightTooltip title="Xoá">
                                    <IconButton onClick={
                                      () => actionDelete(row.id)
                                    } color='danger' size="large"
                                                aria-label="delete">
                                      <AiFillDelete/>
                                    </IconButton>
                                  </LightTooltip>

                                  <Link to={`/edit-account/${row.id}`}>
                                    <LightTooltip title="Sửa">
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
                      fontSize: 10,
                    }}
                    rowsPerPageOptions={[4, 8, 16, 32, 100]}

                    component="div"
                    count={orderList?.length || 0}
                    rowsPerPage={rowsPerPage}
                    labelRowsPerPage={"Số lượng tài khoản"}
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