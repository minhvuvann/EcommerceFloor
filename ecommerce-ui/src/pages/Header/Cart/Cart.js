import React, {useContext} from "react";
import classNames from "classnames/bind";
import {
    Badge,
    createTheme, Grid,
} from "@mui/material";
import {useMediaQuery} from "react-responsive";
import {Link} from "react-router-dom";
import Avatar from "@material-ui/core/Avatar";
import Tippy from "@tippyjs/react/headless";

import styles from "./Cart.module.scss";
import {BsCart} from "react-icons/bs";
import Notify from "~/assets/notify/notify-empty.png";
import config from "~/config";
import MoneyUtils from "~/utils/MoneyUtils";
import {CartContext} from "~/config/provider/CartProvider";
import {LanguageContext} from "~/config/provider/LanguageProvider";

const cx = classNames.bind(styles);

function Cart(props) {
    const maxWidth600 = useMediaQuery({maxWidth: 600});
    const {cartItems, cart} = useContext(CartContext);
    const {localeList} = useContext(LanguageContext);


    return (
        <div className={cx("cart-wrapper")}>
            <Tippy
                interactive
                popperOptions={{
                    strategy: "fixed",
                }}
                placement="bottom-start"
                offset={[40, 10]}
                render={(attrs) => (
                    <div
                        className={cx("box")}
                        style={{background: "#fff"}}
                        tabIndex="-1"
                        {...attrs}
                    >
                        {cartItems?.length === 0 ? (
                            <div className={cx("box-cart")}>
                                <img
                                    className={cx("cart-image")}
                                    src={Notify}
                                    alt="notify image"
                                />
                                <span className={cx("cart-notify")}>{localeList['header_cart_title_no_product']}</span>
                            </div>
                        ) : (
                            <div className={cx("box-cart-item")}>
                                <h3 className={cx("title")}>{localeList['header_cart_title_product']}</h3>
                                {cartItems?.map(({productVariant, id}) => (
                                    <div key={id} className={cx("cart-item")}>
                                        <Grid container>
                                            <Grid xs={2}>
                                                <Avatar
                                                    alt="Avatar"
                                                    src={productVariant.imageUrl}
                                                    variant="square"
                                                    style={{width: "41", height: "41"}}
                                                />
                                            </Grid>
                                            <Grid xs={10}>
                                                <div className={cx("product-info")}>


                                            <span className={cx("product-name")}>
                                            {
                                                productVariant.productName.length > 40 ?
                                                    productVariant.productName.substring(0, 40) + "..." :
                                                    productVariant.productName
                                            }
                                            </span>
                                                    <span className={cx("product-price")}>{MoneyUtils
                                                        .getMoney(productVariant.price.amount)}</span>

                                                </div>
                                            </Grid>
                                        </Grid>
                                    </div>
                                ))}
                                <div className={cx("checkout")}>
                                    <a href={config.routes.cartDetail}
                                       className={cx("btn-checkout")}>{localeList['header_cart_view']}</a>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            >
                {maxWidth600 ? (
                    <Link to={config.routes.cartDetail}>
                        <Badge color="secondary" badgeContent={cartItems?.length ? cartItems?.length : 0} showZero>
                            <BsCart className={cx("cart-icon")}/>
                        </Badge>
                    </Link>
                ) : (
                    <Link to={config.routes.cartDetail}>
                        <Badge color="secondary" badgeContent={cartItems?.length ? cartItems?.length : 0} showZero>
                            <BsCart className={cx("cart-icon")}/>
                        </Badge>
                    </Link>
                )}
            </Tippy>
        </div>
    );
}

export default Cart;
