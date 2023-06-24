import React, { useEffect, useState } from "react";
import classNames from "classnames/bind";
import { Grid } from "@mui/material";
import { useParams } from "react-router-dom";

import styles from "~/pages/SellerManager/AddProduct/AddEditProduct.module.scss";
import { getProductDetailById } from "~/services/workspaces.sevices";
import UpLoadFileImage from "~/components/UploadFileImage";

const cx = classNames.bind(styles);

function EditProduct(props) {
  const [images, setImages] = useState([]);
  const [open, setOpen] = useState(true);
  const [showButton, setShowButton] = useState(true);
  const maxNumber = 1;
  const onChange = (imageList, addUpdateIndex) => {
    // data for submit
    // console.log(imageList, addUpdateIndex);
    setShowButton(false)
    setImages(imageList);
    setOpen(false);
  };
  let { id } = useParams();
  const [product, setProduct] = useState();
  useEffect(() => {
    async function fetchData() {
      const response = await getProductDetailById(id);
      const data = response?.data.product;
      if (data) {
        setProduct(data);
      }
    }

    fetchData();
  }, []);

  return (
    <div className={cx("wrapper")}>
      <h3 className={cx("header")}>Sửa thông tin sản phẩm</h3>
      <div className={cx("form-function")}>
        <div className={cx("box-image")}>
          <h3 className={cx("label")}>
            <span className={cx("requirement")}>*</span> Hình ảnh sản phẩm
          </h3>
          <UpLoadFileImage
            imageProduct={product?.featuredImageUrl}
            showButton={showButton}
            open={open}
            images={images}
            name={product?.name}
            maxNumber={maxNumber}
            onChange={onChange}
          />
        </div>
        <Grid container>
          <Grid container item xs={12} md={12}>
            <div className={cx("form-item")}>
              <h3 className={cx("label")}>
                <span className={cx("requirement")}>* </span>Tên sản phẩm
              </h3>
              <input
                className={cx("input-item")}
                defaultValue={product?.name}
                type="text"
              />
            </div>
          </Grid>
          <Grid container item xs={12} md={12}>
            <div className={cx("form-item")}>
              <h3 className={cx("label")}>
                <span className={cx("requirement")}>* </span>Mô tả
              </h3>
              <textarea
                className={cx("input-area")}
                defaultValue={product?.description}
                rows="4"
                cols="50"
              />
            </div>
          </Grid>
          <Grid container item xs={12} md={12}>
            <div className={cx("form-item")}>
              <h3 className={cx("label")}>
                <span className={cx("requirement")}>* </span>Giá bán
              </h3>
              <input
                defaultValue={product?.mediumPrice.amount}
                className={cx("input-item")}
                type="text"
              />
            </div>
          </Grid>
        </Grid>
        <button style={{ marginTop: "20px" }} className="btn-add">
          Lưu sản phẩm
        </button>
      </div>
    </div>
  );
}

export default EditProduct;
