import React from "react";
import classNames from "classnames/bind";
import {Link} from "react-router-dom";
import MoneyUtils from "~/utils/MoneyUtils";



import styles from "./ProductSuggestionItem.module.scss";


const cx = classNames.bind(styles);

function ProductSuggestionItem(props) {

    return (
        <Link to={`/product-detail/${props.id}/product`}>
            <div className={cx("product-item")}>
                <img
                    className={cx("product-image")}
                    alt={props.name}
                    src={props.featuredImageUrl}
                />
                <div className={cx("product-info")}>
                    <h3 className={cx("product-name")}>{props.name}</h3>
                    <div className={cx("product-footer")}>
                        <span className={cx("price")}>{MoneyUtils.getMoney(props.mediumPrice.amount)}</span>
                        <span className={cx("sold")}>
                Đã bán:
                            {Math.floor(Math.random() * 100)}
            </span>
                    </div>
                </div>
            </div>
        </Link>
    );
}

export default ProductSuggestionItem;
