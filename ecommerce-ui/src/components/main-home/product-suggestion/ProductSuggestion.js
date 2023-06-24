import React, {useContext, useEffect, useState} from "react";
import classNames from "classnames/bind";
import {Container, Grid} from "@mui/material";
import {useMediaQuery} from "react-responsive";

import styles from "./ProductSuggestion.module.scss";
import ProductSuggestionItem from "~/pages/product-suggestion-item";
import {getProductFilter} from "~/services/workspaces.sevices";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function ProductSuggestion(props) {
    const [productSuggestions, setProductSuggestions] = useState([]);
    const {localeList} = useContext(LanguageContext);

    useEffect(() => {
        const body = {
            maxResult: 48
        };
        getProductFilter(body).then((res) => setProductSuggestions(res?.data?.resultList));
    }, []);
    const isSmallDesktop = useMediaQuery({maxWidth: 1250});

    return (
        <div className={cx("wrapper")}>
            <Container style={{padding: 0}}>
                <div className={cx("category-header")}>
                    <h3 className={cx("category-header-name")}>{localeList['daily_discover']}</h3>
                </div>
                <div className={cx("category-content")}>
                    <Grid container spacing={2}>
                        {productSuggestions?.map(({id, name, featuredImageUrl, mediumPrice}) => (
                            <Grid key={id} item container sm={6} md={isSmallDesktop ? 3 : 2}>
                                <ProductSuggestionItem
                                    id={id}
                                    length={productSuggestions.length}
                                    name={name}
                                    key={id}
                                    featuredImageUrl={featuredImageUrl}
                                    mediumPrice={mediumPrice}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </div>
            </Container>
        </div>
    );
}

export default ProductSuggestion;
