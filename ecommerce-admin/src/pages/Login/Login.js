import React, {useContext, useState} from 'react'
import classNames from 'classnames/bind'
import styles from './Login.module.scss'
import {useFormik} from 'formik';
import * as Yup from 'yup';
import config from "../../config";
import {getUserToken, loginAdmin} from "../../services/workspaces.sevices";
import {encode} from "base-64";
import {UserContext} from "../../config/provider/UserProvider";

const cx = classNames.bind(styles);

export default function Login() {
    const initialValues = {
        username: '',
        password: '',
    };
    const [page, setPage] = useState(true);
    const [errCM,setErrCM] = useState(false);
    const submitLogin = async () => {
        if (Object.keys(formik.errors).length === 0) {
            const type = "NORMALLY";
            const result = await loginAdmin(formik.values.username, encode(formik.values.password));
            const errMsg = result?.data.status !== 1;
            setErrCM(errMsg);
            if (result?.data.status === 1) {
                const user = await getUserToken(result?.data?.data, type);
                localStorage.clear();
                localStorage.setItem("ad-id", user?.data?.id);
                window.location.href = config.routes.dashboard;
            }
        }


    }
    const validationSchema = Yup.object().shape({
        username: Yup.string().required('Vui lòng nhập Email'),
        password: Yup.string().required('Vui lòng nhập mật khẩu'),
    });


    const formik = useFormik({
        initialValues,
        validationSchema,
        submitLogin,
    });
    return (
        <div className={cx("wap")}>
            <div className={cx("container")}>
                <div className={cx("form")}>
                    <div className={cx("form_front")}>
                        <div className={cx("form_details")}>Login</div>
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

                            <div className={cx("tx-err-lg")}>Tài khoản hoặc mật khẩu không đúng
                            </div>
                        </div>
                        <input type="text" className={cx("input")} placeholder="Email"
                               name="username"
                               value={formik.values.username}
                               onChange={formik.handleChange}/>
                        {formik.errors.username && (
                            <div style={{
                                color: 'red',
                                marginLeft: '-10rem',
                                marginTop: "-1.6rem"
                            }}>{formik.errors.username}</div>
                        )}
                        <input type="password" className={cx("input")}
                               placeholder="Mật khẩu"
                               name="password"
                               value={formik.values.password}
                               onChange={formik.handleChange}/>
                        {formik.errors.password && (
                            <div style={{
                                color: 'red',
                                marginLeft: '-8rem',
                                marginTop: "-1.6rem"
                            }}>{formik.errors.password}</div>
                        )}
                        <button className={cx("btn")} onClick={submitLogin}>Login</button>
                        <span className={cx("switch")}> Don't have an account?
                            <label htmlFor="signup_toggle" className={cx("signup_tog")}>Sign Up</label>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    )
}
