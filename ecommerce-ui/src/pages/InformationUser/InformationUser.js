import React, {useEffect, useState} from "react";
import classNames from "classnames/bind";
import styles from "./InformationUser.module.scss";

import YourProfile from "~/pages/InformationUser/YourProfile";
import InformationUserWrapper from "~/pages/InformationUser/wrapper";
import {filterOrder, getOrderDetails} from "~/services/workspaces.sevices";

const cx = classNames.bind(styles);

function InformationUser(props) {
    const rsUser = JSON.parse(localStorage.getItem("cs-obj"));
    const [userId, setUserId] = useState();

    useEffect(() => {
        setUserId(rsUser.id);

    }, []);
    return (
        <div className={cx('wrapper')}>
            <InformationUserWrapper userId={userId} child={<YourProfile/>}/>
        </div>
    );
}

export default InformationUser;
