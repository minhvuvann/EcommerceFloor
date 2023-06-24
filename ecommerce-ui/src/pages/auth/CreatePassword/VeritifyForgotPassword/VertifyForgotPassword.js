import React, {useEffect, useState} from "react";
import classNames from "classnames/bind";
import {
    Container,
    Grid,
    FormControl,
    Button,
} from "@mui/material";
import {useParams} from 'react-router-dom'

import styles from "./VertifyForgotPassword.module.scss";
import {activeUser, sendEmail} from "~/services/workspaces.sevices";
import {encode} from "base-64";
import config from "~/config";
import {MdOutlineKeyboardBackspace} from "react-icons/md";


const cx = classNames.bind(styles);

function VerifyCodeRegister(props) {
    const {email, id} = useParams();
    const [code, setCode] = useState();
    const [timeLeft, setTimeLeft] = useState(60);
    const [error, setError] = useState(false);

    useEffect(() => {
        const countdownTimer = setInterval(() => {
            // Trừ đi 1 giây sau mỗi lần cập nhật
            setTimeLeft(timeLeft - 1);
            if (timeLeft === 0) {

            }
        }, 1000);

        // Xóa interval khi component bị unmount
        return () => clearInterval(countdownTimer);
    }, [timeLeft]);

    const handleCodeChange = (event) => {
        // if (/^[0-9\b]+$/.test(event.target.value)) {
        setCode(event.target.value);
        // }
    };

    function handleCodeKeyPress(event) {
        const keyCode = event.keyCode || event.which;
        const keyValue = String.fromCharCode(keyCode);
        // Chỉ cho phép nhập số từ 0-9 và phím điều khiển
        if (!/^[0-9\b]+$/.test(keyValue)) {
            event.preventDefault();
        }
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        // Handle form submission here
    };

    async function submitCode() {
        const codeSession = localStorage.getItem("vr-co");
        console.log(atob(codeSession) + "\n" + code);
        if (codeSession != null && code === atob(codeSession)) {
            window.location.href = `/create-password/id/${id}`;
        }else {
            setError(true);
        }
    }

    async function mailReturn() {
        const self = await sendEmail(email);
        if (self.data.status === 1) {
            setTimeLeft(60);
            localStorage.setItem("vr-co", encode(self.data.data));
        }
    }


    return (
        <div className={cx("wrapper")}>
            <Container>
                <Grid container justifyContent={"center"}>
                    <Grid container item md={7} justifyContent={"center"}>

                        <form className={cx("form-vertify")} onSubmit={handleSubmit}>
                            <FormControl className={cx("input")}>
                                <div className={cx("form-header")}>
                                    <a href={config.routes.sendMailForgotPassword}
                                       style={{color: "#ff5588", marginRight: "5rem"}}>
                                        <MdOutlineKeyboardBackspace size={30}/>
                                    </a>
                                    <h2 className={cx("label-header")}>
                                        Vui lòng nhập mã xác minh
                                    </h2>
                                </div>
                                <div style={{
                                    display: !error ? 'none' : 'block'
                                }} className={cx("err-lg")}>
                                    <div className={cx("ic-err-lg")}>
                                        <svg viewBox="0 0 16 16" className="fJb11i">
                                            <path fill="none" stroke="#FF424F" d="M8 15A7 7 0 108 1a7 7 0 000 14z"
                                                  clipRule="evenodd"/>
                                            <rect stroke="none" width="7" height="1.5" x="6.061" y="5"
                                                  fill="#FF424F"
                                                  rx=".75"
                                                  transform="rotate(45 6.06 5)"/>
                                            <rect stroke="none" width="7" height="1.5" fill="#FF424F" rx=".75"
                                                  transform="scale(-1 1) rotate(45 -11.01 -9.51)"/>
                                        </svg>
                                    </div>

                                    <div className={cx("tx-err-lg")}>Mã xác minh không hợp lệ
                                    </div>
                                </div>
                                <div className={cx("notify-send")}>
                                    <p>Mã xác nhận được gửi qua Email</p>
                                    <b>{email}</b>
                                </div>
                                <div className={cx("bo-verify")}>
                                    <input
                                        type={"tel"}
                                        maxLength={6}
                                        className={cx("verification-code")}
                                        value={code}
                                        onChange={handleCodeChange}
                                        onKeyPress={handleCodeKeyPress}
                                    />
                                </div>
                                <div style={{
                                    alignItems: "center",
                                    boxSizing: "border-box",
                                    display: "flex",
                                    justifyContent: "center",
                                    width: "100%"
                                }}>
                                    <div className={cx("line-veri")}/>
                                    <div className={cx("line-veri")}/>
                                    <div className={cx("line-veri")}/>
                                    <div className={cx("line-veri")}/>
                                    <div className={cx("line-veri")}/>
                                    <div className={cx("line-veri")}/>


                                </div>
                            </FormControl>
                            {timeLeft <= 0 ? <div className={cx("send-repeat")}>
                                <p>Bạn vẫn chưa nhận được?<span onClick={mailReturn}
                                                                className={cx("order")}>Gửi lại</span></p>

                            </div> : <div style={{color: "rgba(0, 0, 0, 0.26)"}} className={cx("send-repeat")}>
                                Nếu bạn chưa nhận được, vui lòng chờ trong {timeLeft} giây để gửi lại.
                            </div>}

                            <Button
                                onClick={submitCode}
                                style={{marginLeft: "66px", backgroundColor: "#ff5588"}}
                                className={cx("btn-vertify")}
                                type="submit"
                                variant="contained"
                                color="primary"
                                sx={{fontSize: "1.6rem"}}
                            >
                                Xác nhận
                            </Button>

                        </form>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default VerifyCodeRegister;
