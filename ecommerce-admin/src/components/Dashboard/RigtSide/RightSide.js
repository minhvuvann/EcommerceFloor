import React from "react";
import classNames from "classnames/bind";
import CustomerReview from "../CustomerReview/CustomerReview";
import Updates from "../Updates/Updates";
import styles from './RightSide.module.scss';
const cx = classNames.bind(styles)

const RightSide = () => {
  return (
    <div className={cx("RightSide")}>
      <div>
        <h3>Updates</h3>
        <Updates/>
      </div>
      <div>
        <h3>Customer Review</h3>
        <CustomerReview/>
      </div>
    </div>
  );
};

export default RightSide;
