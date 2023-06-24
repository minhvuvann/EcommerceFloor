import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";

import styles from "./Category.module.scss";
import {MenuCategoryIcon} from "~/components/Icon";
import CategoryItem from "~/pages/Product/CategoryItem";
import { getIndustrials } from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);
function Category(props) {
    const [industrialList, setIndustrialList] = useState([]);
    const [activeCategoryId, setActiveCategoryId] = useState(null);
    const {localeList} = useContext(LanguageContext);

    useEffect(() => {
    getIndustrials()
      .then((res) => setIndustrialList(res.data))
      
  }, []);

    const handleGetData = (data) => {
        console.log(data);
        setActiveCategoryId(data.id)

    } 
    return (
        <div className={cx('wrapper')}>
            <div className={cx('category-header')}>
                <MenuCategoryIcon className={cx('menu-icon')}/>
                <h3 className={cx('header-text')}>{localeList['category_all_name']}</h3>
            </div>
            <div className={cx('divider')}></div>
            <CategoryItem activeCategoryId={activeCategoryId}  getData={handleGetData} data={industrialList}/>
        </div>
    );
}

export default Category;
