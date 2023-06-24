import React from "react";
import classNames from "classnames/bind";
import {animateScroll as scroll} from "react-scroll";

import styles from "./ChatButton.module.scss";
import {useScrollY} from "../Hooks/useScrollY";
import {BiChat} from "react-icons/bi";

const cx = classNames.bind(styles);

function ChatButton(props) {
    const {setChatOpen} = props;

    const ScrollToTop = () => {
        setChatOpen(true);
    };
    return (
        <div
            className={cx("wrapper")}
            onClick={() => ScrollToTop()}
        >
      <span className="go-top">
        <BiChat className={cx("icon")}/>
        Chat
      </span>
        </div>
    );
}

export default ChatButton;
