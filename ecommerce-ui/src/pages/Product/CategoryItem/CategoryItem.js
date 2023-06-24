import React, {useContext} from "react";
import classNames from "classnames/bind";

import styles from "./CategoryItem.module.scss";
import {MdOutlineArrowRight} from "react-icons/md";
import {Link} from "react-router-dom";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function CategoryItem(props) {
    const {getData, data, activeCategoryId} = props;
    const {localeList} = useContext(LanguageContext);
    return (
        <div className={cx("category-content")}>
            {data.map((item, index) => {
                return (
                    <Link
                        to={`/product-industrial/${item.id}/${item.name}`}
                        onClick={() => getData(item)}
                        key={item.id}
                        className={cx({
                            "box-category": true,
                            active: item.id === activeCategoryId,
                            "open-icon": item.id === activeCategoryId,
                        })}
                    >
                        <MdOutlineArrowRight className={cx("arrow-icon")}/>
                        <h3 className={cx("category-item")}>{localeList['category_item_' + item?.id]}</h3>
                    </Link>
                );
            })}
        </div>
    );
}

export default CategoryItem;
