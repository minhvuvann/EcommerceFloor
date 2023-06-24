import React, {useState} from "react";
import classNames from "classnames/bind";
import {Popover, Typography, Box} from "@material-ui/core";
import {Link} from "react-router-dom";

import styles from "./UserDropdown.module.scss";
import avatar from "~/assets/avatar/avatar.jpg";
import config from "~/config";
import routes from "~/config/routes";

const cx = classNames.bind(styles);

function UserDropdown(props) {
    const [anchorEl, setAnchorEl] = useState(null);
    const {imgUrl, userName, id, locale} = props;
    const handlePopoverOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handlePopoverClose = () => {
        setAnchorEl(null);
    };
    const logOut = () => {
        localStorage.clear();
        window.location.replace(config.routes.home);
    }


    const open = Boolean(anchorEl);
    return (
        <div className={cx("wrapper")}>
            <div className={cx("user-info")} onMouseEnter={handlePopoverOpen}>
                <img className={cx("avatar")} src={imgUrl} alt={userName}/>
                <span className={cx("username")}>{userName}</span>
            </div>
            <Popover
                open={open}
                onMouseLeave={handlePopoverClose}
                anchorEl={anchorEl}
                onClose={handlePopoverClose}
                anchorOrigin={{
                    vertical: "bottom",
                    horizontal: "right",
                }}
                transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                }}
                PaperProps={{
                    style: {
                        backgroundColor: "transparent",
                        boxShadow: "none",
                        borderRadius: 0,
                    },
                }}
            >
                <Box
                    sx={{
                        position: "relative",
                        mt: "10px",
                        "&::before": {
                            backgroundColor: "white",
                            content: '""',
                            display: "block",
                            position: "absolute",
                            width: 12,
                            height: 12,
                            top: -6,
                            transform: "rotate(45deg)",
                            right: `calc(20% - 6px)`,
                        },
                    }}
                />
                <Typography>
                    <div className={cx("dropdown-user")}>
                        <Link to={config.routes.informationUser} className={cx("dropdowm-item")}>
                            {locale['header_user_down_account']}
                        </Link>
                        <Link to={'/purchase'} className={cx("dropdowm-item")}>
                            {locale['header_user_down_purchase']}
                        </Link>
                        <Link to={config.routes.home} onClick={logOut}
                              className={cx("dropdowm-item")}>
                            {locale['header_user_down_logout']}
                        </Link>
                    </div>
                </Typography>
            </Popover>

        </div>
    );
}

export default UserDropdown;
