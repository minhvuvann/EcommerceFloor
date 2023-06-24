import React ,{useState} from 'react';

import classNames from "classnames/bind";

import styles from "./PasswordChange.module.scss";
import {Button, Container, createTheme, ThemeProvider} from "@mui/material";
import PasswordForm from "~/pages/InformationUser/account/PasswordChange/PasswordForm/PasswordForm";



const cx = classNames.bind(styles);

function PasswordChange(props) {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState({});

    async function handleSubmit(event) {
        event.preventDefault();
        const errors = validateForm();
        if (Object.keys(errors).length === 0) {
            // Form input is valid, continue with password change
            // try {
            //   const response = await fetch('/api/update-password', {
            //     method: 'POST',
            //     headers: {
            //       'Content-Type': 'application/json'
            //     },
            //     body: JSON.stringify({
            //       oldPassword,
            //       newPassword
            //     })
            //   });
            //   if (response.ok) {
            //     // Password change succeeded, reset form fields
            //     setOldPassword('');
            //     setNewPassword('');
            //     setConfirmPassword('');
            //     setFormErrors({});
            //     alert('Password updated successfully!');
            //   } else {
            //     // Password change failed, display error message
            //     const errorMessage = await response.text();
            //     setFormErrors({ general: errorMessage });
            //   }
            // } catch (error) {
            //   // Network error or other exception, display error message
            //   setFormErrors({ general: error.message });
            // }
        } else {
            // Form input is invalid, display error messages
            setErrors(errors);
        }
    }

    function validateForm() {
        const errors = {};
        if (oldPassword.trim() === '') {
            errors.oldPassword = 'Nhập mật khẩu hiện tại';
        }
        if (newPassword.trim() === '') {
            errors.newPassword = 'Nhập mật khẩu mới';
        } else if (newPassword.length <= 8) {
            errors.newPassword = 'Mật khẩu ít nhất 8 ký tự ';
        }
        if (confirmPassword.trim() === '') {
            errors.confirmPassword = '';
        } else if (newPassword !== confirmPassword) {
            errors.confirmPassword = 'Passwords do not match';
        }
        return errors;
    }

    function handleChange(event) {
        const { name, value } = event.target;
        switch (name) {
            case 'oldPassword':
                setOldPassword(value);
                break;
            case 'newPassword':
                setNewPassword(value);
                break;
            case 'confirmPassword':
                setConfirmPassword(value);
                break;
            default:
                break;
        }
    }


    const theme = createTheme({
        palette: {
            primary: {
                // Purple and green play nicely together.
                main:'#ff5588 ',
            },
            secondary: {
                // This is green.A700 as hex.
                main: '#e3f2fd',
            },
        },
    });
    return (
        <div className={cx('wrapper')}>
            <Container>
                <div className={cx('password')}>
                    <div className={cx('content')}>
                        <h5>Đổi Mật Khẩu</h5>
                        <p>Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho người khác.</p>
                    </div>
                    <div className={cx("form")}>
                        <form onSubmit={handleSubmit}>
                            <PasswordForm htmlFor="oldPassword"   text = 'Mật Khẩu Hiện Tại' id="oldPassword"
                                          name="oldPassword"
                                          value={oldPassword}
                                          onChange={handleChange} />
                            {errors.oldPassword && <div className={cx("error")}>{errors.oldPassword}</div>}
                            <a >Quên mật khẩu?</a>
                            <PasswordForm text = 'Mật Khẩu Mới' htmlFor="newPassword"    id="newPassword"
                                          name="newPassword"
                                          value={newPassword}
                                          onChange={handleChange}  />
                            {errors.newPassword && <div className={cx("error")}>{errors.newPassword}</div>}
                            <PasswordForm  text ='Xác Nhận Mật Khẩu' htmlFor="confirmPassword" nid="confirmPassword"
                                           name="confirmPassword"
                                           value={confirmPassword}
                                           onChange={handleChange} />
                            {errors.confirmPassword && <div className={cx("error")}>{errors.confirmPassword}</div>}
                            <div className={cx('btn')}>
                                <ThemeProvider theme={theme}>
                                    <Button  type="submit" className={cx('btnAdd')} sx={{fontSize: 15}} color= 'primary' variant="contained" >
                                        Xác nhận
                                    </Button>
                                </ThemeProvider>
                            </div>
                        </form>
                    </div>
                </div>
            </Container>
            </div>
    );
}

export default PasswordChange;
