import React, {useContext} from "react";
import classNames from "classnames/bind";

import {Link} from "react-router-dom";
import Slider from "react-slick";

import styles from "./CategoryItem.module.scss";
import config from "~/config";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function CategoryItem(props) {
    const {data} = props;
    const {localeList} = useContext(LanguageContext);

    const settings = {
        dots: true,
        infinite: true,
        speed: 500,
        initialSlide: 0,
        slidesToShow: 10,
        slidesToScroll: 4,
        rows: 2,
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3,
                    infinite: true,
                    dots: true,
                },
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 2,
                    initialSlide: 2,
                },
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                },
            },
        ],
    };
    return (
        <div>
            <Slider {...settings}>
                {data.map((item, index) => {
                    return (
                        <Link
                            to={`/product-industrial/${item.id}/${item.name}`}
                            key={item.id}
                            className={cx("category-item")}
                        >
                            <div className={cx("box-image")}>
                                <img className={cx("image")} alt="" src={item.iconUrl}/>
                            </div>
                            <h3 className={cx("category-name")}>{localeList['category_item_' + item.id]}</h3>
                        </Link>
                    );
                })}
            </Slider>
        </div>
    );
}

export default CategoryItem;
