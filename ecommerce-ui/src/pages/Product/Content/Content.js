import React, {useState, useEffect, Fragment, useContext} from "react";
import classNames from "classnames/bind";
import {
    Grid,
    Pagination,
    PaginationItem,
    createTheme,
    ThemeProvider,
} from "@mui/material";
import {useMediaQuery} from "react-responsive";
import {Link, useParams} from "react-router-dom";
import {Slider} from "@material-ui/core";

import styles from "./Content.module.scss";
import FilterItem from "~/pages/Product/FilterItem";
import {getProductFilter} from "~/services/workspaces.sevices";
import {themeCustomer} from "~/components/CustomerMaterial";
import {ArrowLeft, ArrowRight} from "~/components/Icon";
import MoneyUtils from "~/utils/MoneyUtils";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function Content(props) {
    const theme = createTheme(themeCustomer);
    let {id, name} = useParams();
    const [product, setProduct] = useState([]);
    const [priceRange, setPriceRange] = useState([0, 0]);
    const [page, setPage] = useState(1);
    const [rowsPerPage] = useState(10);
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const startIndex = (page - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;
    const currentProducts = product.slice(startIndex, endIndex);
    const {localeList} = useContext(LanguageContext);

    useEffect(() => {
        const body = {
            priceFrom: priceRange[0],
            priceTo: priceRange[1],
            maxResult: 100,
            search: name,
        };
        getProductFilter(body).then((res) => setProduct(res.data.resultList));
    }, [priceRange]);

    const handlePriceRangeChange = (event, newValue) => {
        setPriceRange(newValue);
    };
    useEffect(() => {
        const fetchData = async () => {
            if (id !== "search") {
                const body = {
                    industrialId: id,
                    maxResult: 100,
                };
                setPriceRange([0, 0]);
                const result = await getProductFilter(body);
                const data = result?.data.resultList;
                setProduct(data);
            } else {
                const body = {
                    search: name,
                    maxResult: 100,
                };
                setPriceRange([0, 0]);
                const result = await getProductFilter(body);
                const data = result?.data.resultList;
                setProduct(data);
            }
        };
        fetchData();
    }, [id, name]);
    console.log(product);
    const filterItem = [
        {id: 1, name: localeList['filter_latest']},
        {id: 2, name: localeList['filter_selling']},
    ];
    const formatCurrency = (currency) => {
        currency = currency.toLocaleString("vi", {
            style: "currency",
            currency: "VND",
        });
        return currency;
    };
    const desktopSmall = useMediaQuery({maxWidth: 1080});

    const handleGetFilterItem = (filterItem) => {
    };
    return (
        <div className={cx("wrapper")}>
            <div className={cx("filter")}>
                <h3 className={cx("filter-header")}>{localeList['filter_sort']} </h3>
                <FilterItem
                    handleGetFilterItem={handleGetFilterItem}
                    data={filterItem}
                />
                <div className={cx("filter-price")}>
                    <p>{localeList['filter_price_from']}: {formatCurrency(priceRange[0])}</p>
                </div>

                <div className={cx("range-price")}>

                    <Slider
                        value={priceRange}
                        onChange={handlePriceRangeChange}
                        valueLabelDisplay="auto"
                        color="secondary"
                        min={0}
                        max={1000000}
                    />
                </div>
                <div className={cx("filter-price")}>
                    <p>{localeList['filter_price_to']}: {formatCurrency(priceRange[1])}</p>
                </div>
            </div>
            <div className={cx("box-product")}>
                {product.length > 0 ? (
                    <Grid container direction="row" spacing={2}>
                        {currentProducts?.map((item, index) => {
                            return (
                                <Grid
                                    style={{width: "100%"}}
                                    key={item?.id}
                                    item
                                    md={desktopSmall ? 3 : 12 / 5}
                                    sm={6}
                                >
                                    <Link to={`/product-detail/${item.id}/product`}>
                                        <div className={cx("product-item")}>
                                            <div className={cx("product-image")}>
                                                <img src={item?.imageUrls} alt={item?.name}/>
                                            </div>
                                            <div className={cx("product-info")}>
                                                <h3 className={cx("description")}>{item?.name}</h3>
                                                <div className={cx("price")}>
                                                    <span
                                                        className={cx("old-price")}>  {MoneyUtils.getMoney(
                                                        item?.mediumPrice.amount +
                                                        (Math.floor(Math.random() * 10) + 1) * 10000
                                                    )}</span>
                                                    <span className={cx("new-price")}>
                            {MoneyUtils.getMoney(item?.mediumPrice.amount)}
                          </span>
                                                </div>
                                            </div>
                                        </div>
                                    </Link>
                                </Grid>
                            );
                        })}
                        <Grid item md={12} style={{paddingTop: "40px"}}>
                            <Pagination
                                count={Math.ceil(product.length / rowsPerPage)}
                                page={page}
                                onChange={handleChangePage}
                                color="primary"
                                renderItem={(item) => (
                                    <PaginationItem
                                        slots={{
                                            previous: ArrowLeft,
                                            next: ArrowRight,
                                        }}
                                        {...item}
                                    />
                                )}
                            />
                        </Grid>
                    </Grid>
                ) : (
                    <Grid container>
                        <Grid style={{width: "100%"}} item md={12}>
                            <div className={cx("notify")}>
                                <h4 className={cx("notify-text")}>{localeList['filter_no_product']}</h4>
                            </div>
                        </Grid>
                    </Grid>
                )}
            </div>
        </div>
    );
}

export default Content;
