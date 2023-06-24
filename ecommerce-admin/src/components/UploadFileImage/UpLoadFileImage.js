import React, {useContext, useEffect} from "react";
import ImageUploading from "react-images-uploading";
import classNames from "classnames/bind";
import styles from "./UpLoadFileImage.module.scss";

const cx = classNames.bind(styles);

function UpLoadFileImage(props) {
  const {buttonChose, buttonUpdate, images, onChange, maxNumber, open, imageProduct, name, showButton} = props;

  return (
      <ImageUploading
          multiple
          value={images}
          onChange={onChange}
          maxNumber={maxNumber}
          dataURLKey="data_url"
      >
        {({
            imageList,
            onImageUpload,
            onImageRemoveAll,
            onImageUpdate,
            onImageRemove,
            isDragging,
            dragProps,
          }) => (
            // write your building UI
            <div className={cx("upload__image-wrapper")}>
              {imageList.length > 0 &&
              imageList.map((image, index) => (
                  <div key={index} className={cx("image-item")}>
                    <img
                        src={image.data_url}
                        className={cx("image-product")}
                        alt={name}
                    />
                    <div className={cx("image-item__btn-wrapper")}>
                      <button
                          className={cx("btn-update")}
                          onClick={() => onImageUpdate(index)}
                      >
                        {buttonUpdate}
                      </button>
                    </div>
                  </div>
              ))}
              {open && (
                  <div className={cx("image-item")}>
                    <img
                        src={imageProduct}
                        className={cx("image-product")}
                        alt={name}
                    />
                    {showButton && (
                        <div className={cx("image-item__btn-wrapper")}>
                          <button
                              className={cx("btn-update")}
                              style={isDragging ? {color: "red"} : undefined}
                              onClick={onImageUpload}
                              {...dragProps}
                          >
                            {buttonChose}
                          </button>
                        </div>

                    )}
                  </div>
              )}
            </div>
        )}
      </ImageUploading>
  );
}

export default UpLoadFileImage;
