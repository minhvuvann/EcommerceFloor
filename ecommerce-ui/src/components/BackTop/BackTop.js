import React from "react";
import classNames from "classnames/bind";
import { animateScroll as scroll } from "react-scroll";

import styles from "./BackTop.module.scss";
import { RiArrowUpSLine } from "react-icons/ri";
import { useScrollY } from "../Hooks/useScrollY";

const cx = classNames.bind(styles);
function BackTop(props) {
  const ScrollToTop = () => {
    scroll.scrollToTop();
  };
  const [scrollY] = useScrollY();
  return (
    <div
      className={cx("wrapper")}
      onClick={() => ScrollToTop()}
      style={{ visibility: `${scrollY > 600 ? "visible" : "hidden"}` }}
    >
      <span className="go-top">
        <RiArrowUpSLine className={cx("icon")} />
      </span>
    </div>
  );
}

export default BackTop;
