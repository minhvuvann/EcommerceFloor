import React, { useEffect, useState } from "react";
import { Button, Avatar, InputBase } from "@mui/material";
import classNames from "classnames/bind";
import { useMediaQuery } from "react-responsive";
import Tippy from "@tippyjs/react/headless";
import { Link, useNavigate } from "react-router-dom";

import { BiSearch } from "react-icons/bi";
import styles from "./Search.module.scss";
import { getProductFilter } from "~/services/workspaces.sevices";
import useDebounce from "~/components/Hooks/useDebounce";

const cx = classNames.bind(styles);

function Search(props) {
  const tablet = useMediaQuery({ maxWidth: 768 });
  const [searchValue, setSearchValue] = useState("");
  const [showResult, setShowResult] = useState(false);
  const [product, setProduct] = useState([]);
  const navigate = useNavigate();
  const handleClick = () => {
    if (searchValue.length > 0) {
      navigate(`/product-industrial/${"search"}/${searchValue}`);
      setSearchValue("");

    }
  };
  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      setShowResult(false);
      navigate(`/product-industrial/${"search"}/${searchValue}`);
      setSearchValue("");
    }
  };
  const handleChange = (event) => {
    setSearchValue(event.target.value);
  };
  const handleHideResult = () => {
    setShowResult(false);
  };
  const debouncedValue = useDebounce(searchValue, 700);

  const body = {
    search: debouncedValue,
    maxResult: 5,
  };

  useEffect(() => {
    getProductFilter(body).then((res) => setProduct(res.data.resultList));
  }, [debouncedValue]);

  return (
    <div className={cx("search")}>
      <Tippy
        interactive
        popperOptions={{
          strategy: "fixed",
        }}
        placement="bottom-start"
        visible={showResult && searchValue.length > 0}
        offset={[-12, 10]}
        onClickOutside={handleHideResult}
        render={(attrs) => (
          <div
            style={{ width: tablet ? "100%" : "500px" }}
            className={cx("search-result")}
            tabIndex="-1"
            {...attrs}
          >
            {product.length > 0 ? (
              product.map((item, index) => {
                return (
                  <Link
                    key={item.id}
                    to={`/product-detail/${item.id}/product`}
                    className={cx("result-item")}
                    onClick={() => setShowResult(false)}
                  >
                    <Avatar
                      variant="square"
                      sx={{ width: 24, height: 24 }}
                      alt="Avatar"
                      src={item.featuredImageUrl}
                    />
                    <h3 className={cx("product-name")}>{item.name}</h3>
                  </Link>
                );
              })
            ) : (
              <p className={cx("empty")}>Không tìm thấy sản phẩm</p>
            )}
          </div>
        )}
      >
        <InputBase
          fullWidth
          style={{ fontSize: "1.6rem" }}
          spellCheck={false}
          value={searchValue}
          onChange={handleChange}
          onFocus={()=> setShowResult(true)}
          onKeyPress={handleKeyPress}
          placeholder={
            tablet ? "Tìm kiếm" : "Đăng ký và nhận voucher bạn mới đến 70k!"
          }
        />
      </Tippy>

      <Button
        onClick={handleClick}
        className={cx("btn-search")}
        size={"medium"}
        variant="contained"
      >
        <BiSearch className={cx("search-icon")} />
      </Button>
    </div>
  );
}

export default Search;
