import {createContext, useEffect, useState} from "react";

import {getError} from "~/utils/error";
import axios from "axios";

export const ProductContext = createContext({
    productFilter: {},
    loadProduct: () => {
    },
});
export const ProductContextFilter = {
    maxResult: 20,
    offset: 0,
    search: null,
    name: null,
    priceTo: null,
    priceFrom: null,
    productId: null,
    shopId: null,
    industrialId: null,
    tradeMarkId: null,

    getMaxResult: function () {
        return this.maxResult;
    },
    setMaxResult: function (maxResult) {
        this.maxResult = maxResult;
    },
    getOffset: function () {
        return this.offset;
    }, setOffset: function (offset) {
        this.offset = offset;
    }
    , getSearch: function () {
        return this.search;
    }
    , setSearch: function (search) {
        this.search = search;
    }, getName: function () {
        return this.name;
    }, setName: function (name) {
        this.name = name;
    }, getPriceTo: function () {
        return this.priceTo;
    }, setPriceTo: function (priceTo) {
        this.priceTo = priceTo;
    }, getPriceFrom: function () {
        return this.priceFrom;
    }, setPriceFrom: function (priceFrom) {
        this.priceFrom = priceFrom;
    }, getProductId: function () {
        return this.productId;
    }, setProductId: function (productId) {
        this.productId = productId;
    }, getShopId: function () {
        return this.shopId;
    }, setShopId: function (shopId) {
        this.shopId = shopId;
    }, getIndustrialId: function () {
        return this.industrialId;

    }, setIndustrialId: function (industrialId) {
        this.industrialId = industrialId;
    },
    getTradeMarkId: function () {
        return this.tradeMarkId;
    },
    setTradeMarkId: function (tradeMarkId) {
        this.tradeMarkId = tradeMarkId;
    },

}
export const ProductContextProvider = ({children}) => {
    const basUrl = process.env.REACT_APP_PUBLIC_API_URL;
    const [productFilter, setProductFilter] = useState({});

    useEffect(() => {
        const getProductFilter = async () => {
            try {
                await axios
                    .post(
                        `${basUrl}/product/1.0.0/product/filter`,
                        {
                            maxResult: ProductContextFilter.getMaxResult(),
                            offset: ProductContextFilter.getOffset(),
                            search: ProductContextFilter.getSearch(),
                            name: ProductContextFilter.getName(),
                            priceTo: ProductContextFilter.getPriceTo(),
                            priceFrom: ProductContextFilter.getPriceFrom(),
                            productId: ProductContextFilter.getProductId(),
                            shopId: ProductContextFilter.getShopId(),
                            industrialId: ProductContextFilter.getIndustrialId(),
                            tradeMarkId: ProductContextFilter.getTradeMarkId(),
                        },
                        {
                            headers: {
                                "Content-Type": "application/json",
                                charset: "utf-8",
                            },
                        }
                    )
                    .then(function (response) {
                        const {data} = response;
                        setProductFilter(data);
                    })
                    .catch(function (error) {
                        console.error(getError(error));
                    });
            } catch (error) {
                console.log(getError(error));
            }
        };
        getProductFilter();
    }, productFilter);

    const context = {
        productFilter,
    };
    return (
        <ProductContext.Provider value={context}>
            {children}
        </ProductContext.Provider>
    );
};

export default ProductContext;
