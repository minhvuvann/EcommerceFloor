import React from "react";
import Cards from "../Cards/Cards";
import Table from "../Table/Table";
import classNames from "classnames/bind";
import styles from "./MainDash.module.scss";
const cx = classNames.bind(styles);
const MainDash = () => {
  return (
    <div className={cx("MainDash")}>
      <Cards />
      <Table />
    </div>
  );
};

export default MainDash;
