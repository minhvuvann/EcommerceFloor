import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import { Grid, Container } from "@mui/material";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";


import styles from "./Category.module.scss";
import { getError } from "~/utils/error";
import { getIndustrials } from "~/services/workspaces.sevices";
import CategoryItem from "~/components/main-home/category/category-item";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function Category(props) {
  const [industrialList, setIndustrialList] = useState([]);
  const {localeList} = useContext(LanguageContext);
  useEffect(() => {
    getIndustrials()
      .then((res) => setIndustrialList(res.data))
      .catch((error) => console.log(getError(error)));
  }, []);
  return (
    <div className={cx("wrapper")}>
      <Container style={{ padding: 0 }}>
        <div className={cx("category-header")}>
          <div className={cx("category-header-name")}>{localeList['header_category_name']}</div>
        </div>
        <div className={cx("category-content")}>
            <CategoryItem data={industrialList}/>
        </div>
      </Container>
    </div>
  );
}

export default Category;
