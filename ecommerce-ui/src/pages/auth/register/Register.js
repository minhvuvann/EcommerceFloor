import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {Grid} from "@mui/material";
import {useMediaQuery} from "react-responsive";

import styles from "~/pages/auth/Login/Login.module.scss";
import config from "~/config";
import Background from "~/assets/logo/Mellow_bg.png";
import {FaFacebook} from "react-icons/fa";
import {FcGoogle} from "react-icons/fc";
import {BsApple} from "react-icons/bs";
import {activeUser, filterUser, loginCustomer, registerUser} from "~/services/workspaces.sevices";
import {encode} from "base-64";
import {RingLoader} from "react-spinners";
import {Link} from "react-router-dom";
import {signInWithPopup} from "firebase/auth";
import {auth, ggProvider} from "~/firebase";
import {LanguageContext} from "~/config/provider/LanguageProvider";
import * as Yup from "yup";
import {useFormik} from "formik";


const cx = classNames.bind(styles);

function Register(props) {
    const isTablet = useMediaQuery({maxWidth: 875});
    const [err, setErr] = useState(false);
    const [loadR, setLoadR] = useState(false);
    const {localeList} = useContext(LanguageContext);
    const initialValues = {
        email: '',
        password: '',
    };
    const getDataR = () => {
        return {
            password: {
                password: encode(formik.values.password),
                passwordStatus: "NEW",
            },
            role: {
                roleType: "PERSONAL",
            },
            user: {
                email: formik.values.email,
                serviceType: "NORMALLY",
                userStatus: "INACTIVE",
            },
        };
    }

    const handleRegister = async () => {
        let data = getDataR();
        if (Object.keys(formik.errors).length === 0) {
            setLoadR(true);
            const result = await registerUser(data);
            const errMsg = result?.data.status !== 1 ? result?.data.message : "";
            setLoadR(false);
            setErr(errMsg);
            if (result?.data.status === 1) {
                localStorage.setItem("vr-co", encode(result?.data.data.code));
                window.location.href = `/register-verify-code/${formik.values.email}/id/${result?.data.data.userId}`;
            }
        }

    }
    const loginGG = () => {
        signInWithPopup(auth, ggProvider).then(async function (firebaseRes) {
            setLoadR(true);
            const authData = firebaseRes._tokenResponse;
            console.log(authData);
            const type = "GOOGLE";
            const result = await loginCustomer(authData.email, encode(authData.localId),
                type, authData.fullName, authData.photoUrl
            );
            const errMsg = result?.data.status !== 1;
            if (result?.data.status === 1) {
                if (result?.data.data.status !== "ACTIVE") {
                    await activeUser(result?.data.data.id, authData.fullName);
                }
                localStorage.clear();
                localStorage.setItem("em", result?.data.data.email);
                localStorage.setItem("cs-id", result?.data.data.id);
                localStorage.setItem("cs-obj", JSON.stringify(result?.data.data));
                window.location.href = config.routes.home;
            }
            setLoadR(false);

        }).catch(function (error) {
            window.location.reload();
        });

    }
    const validationSchema = Yup.object().shape({
        email: Yup.string().required(localeList['valid_email']),
        password: Yup.string().required(localeList['valid_password']),
    });

    const formik = useFormik({
        initialValues,
        validationSchema,
        handleRegister,
    });

    async function checkExists() {
        const body = {
            email: formik.values.email,
            serviceType: "NORMALLY",

        }
        await filterUser(body).then(response => {
            if (response.data.total === 1)
                setErr(true);
            else
                setErr(false);
        })

    }

    return (
        <div className={cx("wrapper")}>
            <div
                style={{
                    backgroundImage: `url(${isTablet ? "" : Background})`,
                    height: "600px",
                    backgroundSize: "cover",
                    backgroundRepeat: "no-repeat",
                }}
                className={cx("content-wrapper")}
            >
                <Grid container direction="row" justifyContent="flex-end">
                    <Grid item xs={12} md={5}>
                        <div className={cx("form-login")}>
                            <h1 className={cx("login-text")}>{localeList['header_signup']}</h1>
                            <div className={cx("box-input")}>
                                <div style={{
                                    display: err ? 'block' : 'none'
                                }} className={cx("err-lg")}>
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
                                    <div className={cx("tx-err-lg")}>{localeList['register_check_exist']}
                                    </div>
                                </div>
                                <div className={cx("container-input")}>
                                    <input
                                        className={cx("input")}
                                        type="text"
                                        name={"email"}
                                        placeholder=" Email"
                                        value={formik.values.email}
                                        onChange={formik.handleChange}
                                        onBlur={checkExists}
                                    />
                                    {formik.errors.email && (
                                        <span className={cx("error")}>{formik.errors.email}</span>
                                    )}
                                </div>
                                <div className={cx("container-input")}>
                                    <input
                                        type="password"
                                        name={"password"}
                                        className={cx("input")}
                                        placeholder={localeList['placeholder_pwd']}
                                        value={formik.values.password}
                                        onChange={formik.handleChange}
                                    />
                                    {formik.errors.password && (
                                        <span className={cx("error")}>{formik.errors.password}</span>
                                    )}
                                </div>
                            </div>
                            <button onClick={handleRegister}
                                    onTouchStart={handleRegister}
                                    className={cx("btn-function")}>  {loadR ? (
                                <RingLoader
                                    size={20}
                                    color={"#ffffff"}
                                    loading={true}
                                />
                            ) : localeList['button_signup']}</button>
                            <div className={cx("link-function")}>
                                <Link className={cx("link-item")} to={config.routes.sendMailForgotPassword}>
                                    {localeList['login_forgot']}
                                </Link>
                                <a className={cx("link-item")} href="">
                                    {localeList['login_sms']}
                                </a>
                            </div>
                            <div className={cx("separate")}>
                                <div className={cx("separate-left")}/>
                                <span className={cx("separate-text")}>{localeList['header_or']}</span>
                                <div className={cx("separate-right")}/>
                            </div>
                            <div className={cx("outside")}>
                                <button className={cx("btn-outside")}>
                                    <FaFacebook/> Facebook
                                </button>
                                <button onClick={loginGG} className={cx("btn-outside")}>
                                    <FcGoogle/> Google
                                </button>
                                <button className={cx("btn-outside")}>
                                    <BsApple style={{color: "#333"}}/> Apple
                                </button>
                            </div>
                            <div className={cx("security")}>
                                <div className={cx("security-text")}>
                                    {localeList['signup_yes']}
                                    <div>
                                        <a href="">{localeList['signup_terms']}</a> &
                                        <a href="">{localeList['signup_policy']}</a>
                                    </div>
                                </div>
                            </div>

                            <h4 className={cx("notify")}>
                                {localeList['signup_have']}
                                <Link to={config.routes.login}> {localeList['header_login']} </Link>
                            </h4>
                        </div>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}

export default Register;
