import React, {useContext, useEffect, useRef, useState} from "react";
import classNames from "classnames/bind";
import {Grid} from "@mui/material";
import {useMediaQuery} from "react-responsive";
import {Link} from "react-router-dom";
import styles from "./Login.module.scss";
import config from "~/config";
import Background from "~/assets/logo/Mellow_bg.png";
import {FaFacebook} from "react-icons/fa";
import {FcGoogle} from "react-icons/fc";
import {BsApple} from "react-icons/bs";
import {activeUser, getUserToken, loginCustomer, sendEmail} from "~/services/workspaces.sevices";
import {RingLoader} from "react-spinners";
import {encode} from "base-64";
import {auth, ggProvider} from "~/firebase";
import {signInWithPopup} from "firebase/auth";
import {LanguageContext} from "~/config/provider/LanguageProvider";
import {useFormik} from 'formik';
import * as Yup from 'yup';

const cx = classNames.bind(styles);

function Login(props) {
    const isTablet = useMediaQuery({maxWidth: 875});

    const [loadL, setLoadL] = useState(false);
    const [errCM, setErrCM] = useState(false);
    const [isG, setIsG] = useState(false);
    const [fn, setFn] = useState(null);
    const [im, setIm] = useState(null);
    const {localeList} = useContext(LanguageContext);

    const initialValues = {
        email: '',
        password: '',
    };

    const handleLogin = async () => {
        const type = isG ? "GOOGLE" : "NORMALLY";
        if (Object.keys(formik.errors).length === 0) {
            setLoadL(true);
            const result = await loginCustomer(formik.values.email, encode(formik.values.password),
                type, fn, im
            );
            const errMsg = result?.data.status !== 1;
            setErrCM(errMsg);
            if (result?.data.status === 1) {
                const user = await getUserToken(result?.data.data, type);
                localStorage.clear();
                localStorage.setItem("em", user?.data.email);
                localStorage.setItem("cs-id", user?.data.id);
                localStorage.setItem("cs-obj", JSON.stringify(user?.data));
                window.location.href = config.routes.home;
            }
            setLoadL(false);

        }

    }
    const validationSchema = Yup.object().shape({
        email: Yup.string().required(localeList['valid_email']),
        password: Yup.string().required(localeList['valid_password']),
    });

    const formik = useFormik({
        initialValues,
        validationSchema,
        handleLogin,
    });

    const loginGG = () => {
        signInWithPopup(auth, ggProvider).then(async function (firebaseRes) {
            setLoadL(true);
            const authData = firebaseRes._tokenResponse;
            const type = "GOOGLE";
            const result = await loginCustomer(authData.email, encode(authData.localId),
                type, authData.fullName, authData.photoUrl
            );
            const errMsg = result?.data.status !== 1;
            setErrCM(errMsg);
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
            setLoadL(false);

        }).catch(function (error) {
            window.location.reload();
        });

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
                            <h1 className={cx("login-text")}>{localeList['header_login']}</h1>
                            <div style={{
                                display: errCM ? 'block' : 'none'
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

                                <div className={cx("tx-err-lg")}>{localeList['login_error']}
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

                            <button onClick={handleLogin} className={cx("btn-function")}>{loadL ? (
                                <RingLoader
                                    size={20}
                                    color={"#ffffff"}
                                    loading={true}
                                />
                            ) : localeList['header_login']}</button>
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
                            <h4 className={cx("notify")}>
                                {localeList['login_new_mellow']}
                                <Link to={config.routes.register}>{localeList['header_signup']}</Link>
                            </h4>
                        </div>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}

export default Login;
