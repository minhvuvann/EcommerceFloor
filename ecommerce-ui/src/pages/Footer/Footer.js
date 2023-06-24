import React from 'react';
import classNames from 'classnames/bind';
import {Container, Grid} from "@mui/material";


import styles from './Footer.module.scss';
import CirclePay from '~/assets/payment/circle.png';
import ShopeePay from '~/assets/payment/shopeePay.png';
import JcbPay from '~/assets/payment/jcb.png';
import {useMediaQuery} from "react-responsive";

const cx = classNames.bind(styles);


function Footer(props) {
    const isTablet = useMediaQuery({maxWidth: 600})
    return (
        <div className={cx("wrapper")}>
            <Container>
                <Grid container spacing={2} justifyContent={isTablet ? 'center' : ''}>
                    <Grid item md={12 / 5}>
                        <div className={cx('footer-wrapper')}>
                            <h1 className={cx('title')}>Chăm sóc khách hàng</h1>
                            <a className={cx('footer-item')}>Trung tâm trợ giúp</a>
                            <a className={cx('footer-item')}>Mellow Blog</a>
                            <a className={cx('footer-item')}>Mellow Mall</a>
                            <a className={cx('footer-item')}>Hướng Dẫn Mua Hàng</a>
                            <a className={cx('footer-item')}>Hướng Dẫn Bán Hàng</a>
                            <a className={cx('footer-item')}>Thanh Toán</a>


                        </div>
                    </Grid>
                    <Grid item md={12 / 5}>
                        <div className={cx('footer-wrapper')}>
                            <h1 className={cx('title')}>Về Mellow</h1>
                            <a className={cx('footer-item')}>Giới Thiệu Về Mellow Việt Nam</a>
                            <a className={cx('footer-item')}>Tuyển Dụng</a>
                            <a className={cx('footer-item')}>Điều Khoản Mellow</a>
                            <a className={cx('footer-item')}>Chính Sách Bảo Mật</a>
                            <a className={cx('footer-item')}>Chính Hãng</a>
                            <a className={cx('footer-item')}>Kênh Người Bán</a>
                        </div>
                    </Grid>
                    <Grid item md={12 / 5}>
                        <div className={cx('footer-wrapper')}>
                            <h1 className={cx('title')}>Theo Dõi Chúng tôi trên</h1>
                            <ul className={cx("SOb2n7")}>
                                <li className={cx("o8Gbgv")}><a href="https://www.facebook.com/ShopeeVN"
                                                                className="FA0WjS"
                                                                title="" target="_blank" rel="noopener noreferrer"><img
                                    className="_6ZyW8Y"
                                    src="https://down-vn.img.susercontent.com/file/2277b37437aa470fd1c71127c6ff8eb5"/><span
                                    className={cx("xTjlXx")}>Facebook</span></a></li>
                                <li className={cx("o8Gbgv")}><a href="https://instagram.com/Shopee_VN"
                                                                className="FA0WjS" title=""
                                                                target="_blank" rel="noopener noreferrer">
                                    <img className="_6ZyW8Y"
                                         src="https://down-vn.img.susercontent.com/file/5973ebbc642ceee80a504a81203bfb91"/><span
                                    className={cx("xTjlXx")}>Instagram</span></a></li>
                                <li className={cx("o8Gbgv")}><a href="https://www.linkedin.com/company/shopee"
                                                                className="FA0WjS"
                                                                title="" target="_blank" rel="noopener noreferrer"><img
                                    className="_6ZyW8Y"
                                    src="https://down-vn.img.susercontent.com/file/f4f86f1119712b553992a75493065d9a"/><span
                                    className={cx("xTjlXx")}>LinkedIn</span></a></li>
                            </ul>
                        </div>
                    </Grid>
                    <Grid item md={12 / 5}>
                        <div className={cx('footer-wrapper')}>
                            <h1 className={cx('title')}>Tải ứng dụng Mellow</h1>
                            <div className="pkg67p"><a href="https://shopee.vn/web" target="_blank"
                                                       rel="noopener noreferrer"><img
                                src="https://down-vn.img.susercontent.com/file/a5e589e8e118e937dc660f224b9a1472"
                                alt="download_qr_code" className="ebQ6br"/></a>
                                <div className="zLPzwH"><a href="https://shopee.vn/web" target="_blank"
                                                           rel="noopener noreferrer" className="W4jGm6"><img
                                    src="https://down-vn.img.susercontent.com/file/ad01628e90ddf248076685f73497c163"
                                    alt="app"/></a><a href="https://shopee.vn/web" target="_blank"
                                                      rel="noopener noreferrer" className="W4jGm6"><img
                                    src="https://down-vn.img.susercontent.com/file/ae7dced05f7243d0f3171f786e123def"
                                    alt="app"/></a><a href="https://shopee.vn/web" target="_blank"
                                                      rel="noopener noreferrer" className="W4jGm6"><img
                                    src="https://down-vn.img.susercontent.com/file/35352374f39bdd03b25e7b83542b2cb0"
                                    alt="app"/></a></div>
                            </div>
                        </div>
                    </Grid>
                    <Grid item md={12 / 5}>
                        <div className={cx('footer-wrapper')}>
                            <h1 className={cx('title')}>Thanh toán</h1>
                            <ul className={cx("D7qxb9")}>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/d4bbea4570b93bfd5fc652ca82a262a8"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/a0a9062ebe19b45c1ae0506f16af5c16"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/38fd98e55806c3b2e4535c4e4a6c4c08"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/bc2a874caeee705449c164be385b796c"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/2c46b83d84111ddc32cfd3b5995d9281"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/5e3f0bee86058637ff23cfdf2e14ca09"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/9263fa8c83628f5deff55e2a90758b06"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/0217f1d345587aa0a300e69e2195c492"
                                    alt="logo"/></a></li>
                            </ul>
                            <h1 className={cx('title')}>Đơn Vị Vận CHuyển</h1>
                            <ul className={cx("D7qxb9")}>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/5e7282bd0f7ee0872fdb0bd1d40fbe9e"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/d10b0ec09f0322f9201a4f3daf378ed2"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/77bf96a871418fbc21cc63dd39fb5f15"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/59270fb2f3fbb7cbc92fca3877edde3f"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/957f4eec32b963115f952835c779cd2c"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/0d349e22ca8d4337d11c9b134cf9fe63"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/3900aefbf52b1c180ba66e5ec91190e5"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/6e3be504f08f88a15a28a9a447d94d3d"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/b8348201b4611fc3315b82765d35fc63"
                                    alt="logo"/></a></li>
                                <li className={cx("uqMJQA")}><a target="_blank" rel="noopener noreferrer"
                                                                className="_2pbE-b"><img
                                    src="https://down-vn.img.susercontent.com/file/0b3014da32de48c03340a4e4154328f6"
                                    alt="logo"/></a></li>
                            </ul>
                        </div>
                    </Grid>


                </Grid>
            </Container>
        </div>
    );
}

export default Footer;
