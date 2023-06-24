import React, {useState} from "react";
import classNames from "classnames/bind";
import {
    Container,
    Grid,
    FormControl,
    Button,
} from "@mui/material";
import styles from "./SendMailForgot.module.scss";
import {filterUser, sendEmail} from "~/services/workspaces.sevices";
import {encode} from "base-64";
import config from "~/config";
import {MdOutlineKeyboardBackspace} from "react-icons/md";
import isEmail from "validator/es/lib/isEmail";

const cx = classNames.bind(styles);

function SendMailForgot(props) {
    const [error, setError] = useState(false);
    const [notFound, setNotFound] = useState(false);
    const [email, setEmail] = useState("");
    const [opacity, setOpacity] = useState('0.7');


    const handleEmailChange = (event) => {
        if (isEmail(event.target.value)) {
            setOpacity('1');
            setError(false);
        } else {
            setOpacity('0.7');
            setError(true);
        }
        setEmail(event.target.value);
    };

    async function confirmMail() {
        if (!error) {
            const body_1 = {
                roleType: "PERSONAL",
                email: email
            }
            const body_2 = {
                roleType: "PERSONAL_STORE",
                email: email
            }
            const userNot_1 = await filterUser(body_1);
            const userNot_2 = await filterUser(body_2);
            if (userNot_1?.data?.resultList?.length !== 0 || userNot_2?.data?.resultList?.length) {
                const id = userNot_1?.data?.resultList?.length !== 0 ? userNot_1?.data?.resultList[0]?.id :
                    userNot_2?.data?.resultList[0]?.id;
                setNotFound(false);
                const self = await sendEmail(email);
                if (self.data.status === 1) {
                    localStorage.setItem("vr-co", encode(self.data.data));
                }
                window.location.href = `/verify-forgot-password/${email}/id/${id}`;
            } else {
                setNotFound(true);
            }

        }


    }



    return (
        <div className={cx("wrapper")}>
            <Container>
                <Grid container justifyContent={"center"}>
                    <Grid container item md={7} justifyContent={"center"}>

                        <form className={cx("form-vertify")}>
                            <FormControl className={cx("input")}>
                                <div className={cx("form-header")}>
                                    <a href={config.routes.login} style={{color: "#ff5588", marginRight: "6rem"}}>
                                        <MdOutlineKeyboardBackspace size={30}/>
                                    </a>
                                    <div className={cx("label-header")}>
                                        Đặt lại mật khẩu
                                    </div>

                                </div>
                                <div style={{
                                    display: !notFound ? 'none' : 'block'
                                }} className={cx("err-lg-not-found")}>
                                    <div className={cx("ic-err-lg")}>
                                        <svg viewBox="0 0 16 16" className="fJb11i">
                                            <path fill="none" stroke="#FF424F" d="M8 15A7 7 0 108 1a7 7 0 000 14z"
                                                  clipRule="evenodd"/>
                                            <rect stroke="none" width="7" height="1.5" x="6.061" y="5" fill="#FF424F"
                                                  rx=".75"
                                                  transform="rotate(45 6.06 5)"/>
                                            <rect stroke="none" width="7" height="1.5" fill="#FF424F" rx=".75"
                                                  transform="scale(-1 1) rotate(45 -11.01 -9.51)"/>
                                        </svg>
                                    </div>

                                    <div className={cx("tx-err-lg")}>Không tìm thấy tài khoản
                                    </div>
                                </div>
                                <div className={cx("bo-verify")}>
                                    <input
                                        style={{
                                            background: error ? "#fff6f7" : "",
                                            borderColor: error ? "#ff424f" : ""
                                        }}
                                        type={"text"}
                                        className={cx("verification-code")}
                                        value={email}
                                        onChange={handleEmailChange}
                                        placeholder={"Vui lòng nhập Email"}
                                    />
                                    <div style={{
                                        display: !error ? 'none' : 'flex'
                                    }} className={cx("err-lg")}>
                                        Email không hợp lệ
                                    </div>
                                </div>
                            </FormControl>

                            <Button
                                onClick={confirmMail}
                                style={{
                                    marginLeft: "51px", backgroundColor: "#ff5588", borderRadius: 0,
                                    marginBottom: "50px", fontFamily: "inherit", marginTop: "20px", opacity: opacity
                                }}
                                className={cx("btn-verify")}
                                variant="contained"
                                color="primary"

                                sx={{fontSize: "1.6rem"}}
                            >
                                TIẾP THEO
                            </Button>

                        </form>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default SendMailForgot;
