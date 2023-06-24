import React, {useState} from "react";
import classNames from "classnames/bind";
import {
    Container,
    Grid,
    FormControl,
    Button,
} from "@mui/material";

import styles from "./CreatePassword.module.scss";
import config from "~/config";
import {MdOutlineKeyboardBackspace} from "react-icons/md";
import {activeUser, updatePasswordUser} from "~/services/workspaces.sevices";
import {useParams} from "react-router-dom";
import {encode} from "base-64";
import {Dialog, DialogContent, DialogContentText} from "@material-ui/core";
import CheckBoxIcon from "@mui/icons-material/CheckBox";

const cx = classNames.bind(styles);

function CreatePassword(props) {
    const {id} = useParams();
    const [password, setPassword] = useState("");
    const [errorP, setErrorP] = useState(false);
    const [opacity, setOpacity] = useState('0.7');
    const [confirmed, setConfirmed] = useState(false);


    const handlePasswordChange = (event) => {
        if (event.target.value?.length >= 6) {
            setErrorP(false);
            setOpacity('1');
        } else {
            setErrorP(true);
            setOpacity('0.7');
        }
        setPassword(event.target.value);

    };

    const keyPressP = () => {

    }


    async function confirmPass() {
        if (!errorP) {
            const updatePassword = await updatePasswordUser(id, encode(password));
            if (updatePassword) {
                setConfirmed(true);
                localStorage.clear();
                localStorage.setItem("cs-id", id);
                localStorage.setItem("cs-obj", JSON.stringify(updatePassword?.data));
                setTimeout(() => {
                    setConfirmed(false);
                    window.location.href = config.routes.home;
                }, 5000);
            }
        }
    }

    return (
        <div className={cx("wrapper")}>
            <Container>
                <Grid container justifyContent={"center"}>
                    <Grid container item md={7} justifyContent={"center"}>
                        <Dialog open={confirmed}>
                            <DialogContent>
                                <DialogContentText>
                                    <div style={{fontSize: "1.4rem", display: "flex"}}>
                                        <CheckBoxIcon style={{color: "#22c55e"}}/>
                                        <span style={{paddingTop: "4px"}}>
                                         Đặt lại mật khẩu thành công
                                      </span>
                                    </div>
                                    <a href={config.routes.home}>
                                        <Button style={{
                                            width: "100%",
                                            backgroundColor: "#ff5588",
                                            color: "#FFFFFF",
                                            marginTop: "3rem",
                                        }}
                                        >
                                            Đăng nhập
                                        </Button>
                                    </a>
                                </DialogContentText>
                            </DialogContent>
                        </Dialog>
                        <form className={cx("form-vertify")}>
                            <FormControl className={cx("input")}>
                                <div className={cx("form-header")}>
                                    <a href={config.routes.verifyForgotPassword}
                                       style={{color: "#ff5588", marginRight: "6rem"}}>
                                        <MdOutlineKeyboardBackspace size={30}/>
                                    </a>
                                    <div className={cx("label-header")}>
                                        Đặt lại mật khẩu
                                    </div>

                                </div>
                                <div className={cx("bo-verify")}>
                                    <input
                                        style={{
                                            background: errorP ? "#fff6f7" : "",
                                            borderColor: errorP ? "#ff424f" : ""
                                        }}
                                        type={"text"}
                                        className={cx("verification-code")}
                                        value={password}
                                        onInput={handlePasswordChange}
                                        placeholder={"Vui lòng nhập mật khẩu"}
                                    />
                                    <div style={{
                                        display: !errorP ? 'none' : 'flex'
                                    }} className={cx("err-lg")}>
                                        Mật khẩu phải có độ dài từ 6 kí tự
                                    </div>
                                </div>
                            </FormControl>

                            <Button
                                onClick={confirmPass}
                                style={{
                                    marginLeft: "51px", backgroundColor: "#ff5588", borderRadius: 0,
                                    marginBottom: "50px", fontFamily: "inherit", marginTop: "20px", opacity: opacity
                                }}
                                className={cx("btn-verify")}
                                variant="contained"
                                color="primary"

                                sx={{fontSize: "1.6rem"}}
                            >
                                XÁC NHẬN
                            </Button>

                        </form>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}

export default CreatePassword;
