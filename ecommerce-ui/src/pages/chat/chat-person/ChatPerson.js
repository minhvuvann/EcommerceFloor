import React, {useState} from 'react';
import classNames from "classnames/bind";
import styles from "./ChatPerson.module.scss";

const cx = classNames.bind(styles);

function ChatPerson(props) {
    const {ticker, id, onClick, img, name, statusActive} = props;
    return (
        <div onClick={onClick} className={cx('wrapper', ticker === id ? "ticker" : "")}>
            <li className={cx("li-ps")}>
                <img className={cx("img-ps")} src={img} alt={name}/>
                <div className={cx("av-ps")}>
                    <div className={cx("h2-ps")}>{name.length > 10 ? name.substring(0, 10) + "..." : name}</div>
                    <div className={cx("at-ps")}>
                        <span className={cx(statusActive ? "green" : "orange", "status")}/>
                        {statusActive ? "online" : "offline"}
                    </div>
                </div>
            </li>
        </div>
    );
}

export default ChatPerson;