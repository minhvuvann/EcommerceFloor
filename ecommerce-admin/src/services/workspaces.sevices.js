import instance from "../config/interceptors/axios";

export const changeLanguage = async (locale) => {
    return instance.get("/language/1.0.0/language/change?locale=" + locale);
}
export const sendEmail = async (email) => {
    return await instance.post("/mail/1.0.0/send?email=" + email);
}
export const getCategory = async () => {
    return await instance.get("/product/1.0.0/product/industrials");
};
export const getProductDetailById = async (id) => {
    return await instance.get(`/product/1.0.0/product/${id}/detail`);
};
export const createShop = async (userId, body) => {
    return await instance.post(`/user/1.0.0/shop/create?user-id=${userId}`, body);
};
export const getShopById = async (id) => {
    return await instance.get(`/user/1.0.0/shop/shop/${id}`);
};
export const getIndustrials = async () => {
    return await instance.get(`/product/1.0.0/product/industrials`);
};
export const getTrackMarks = async (id) => {
    return await instance.get(`/product/1.0.0/product/trademark-list/${id}/industrial`);
};
export const getProductFilter = async (body) => {
    return await instance.post("/product/1.0.0/product/filter", body);
};
export const registerUser = async (body) => {
    return await instance.post("/user/1.0.0/register/user", body);
};
export const loginCustomer = async (email, password, serviceType, fullName, imgUrl) => {
    return await instance.post("/user/1.0.0/login/customer?email=" + email +
        "&password=" + password + "&service-type=" + serviceType + "&full-name=" + fullName +
        "&image=" + imgUrl);
};
export const loginAdmin = async (email, password) => {
    return await instance.post("/user/1.0.0/login/admin?email=" + email +
        "&password=" + password);
};
export const getUserToken = async (token, type) => {
    return await instance.get("/user/1.0.0/login/info?code-token=" +
        token + "&service-type=" + type);
};
export const getUser = async (id) => {
    return await instance.get(`/user/1.0.0/user/${id}`);
};
export const getShopAll = async () => {
    return await instance.get('user/1.0.0/shop/shop/all');
};
export const getAllCarrier = async () => {
    return await instance.get('shipment/1.0.0/shipment/carrier-all');
}
export const getFeeShipment = async (shopId, body) => {
    return await instance.post('shipment/1.0.0/shipment/shipping-fee?shop-id=' + shopId, body);
}

export const getCartDetail = async (userId) => {
    return await instance.get(`cart/1.0.0/cart/${userId}/detail`);
}
export const addCart = async (cartItem) => {
    return await instance.post('cart/1.0.0/cart-item/create', cartItem);
}
export const removeCart = async (cartItemId, quantity) => {
    return await instance.delete(`cart/1.0.0/cart-item/${cartItemId}/deleted/${quantity}`);
}
export const getProvinces = async () => {
    return await instance.post('geo/1.0.0/province-list');
}
export const getDistricts = async (provinceId) => {
    return await instance.post('geo/1.0.0/district-list?province-id=' + provinceId);
}
export const getWards = async (districtId) => {
    return await instance.post('geo/1.0.0/ward-list?district-id=' + districtId);
}
export const updateShopAddress = async (province, district, ward, address, shopId) => {
    return await instance.put("/user/1.0.0/shop/shop/update/address?shop-id=" + shopId
        + "&province-code=" + province + "&district-code=" + district + "&ward-code=" + ward + "&address=" + address);

}
export const updateShop = async (userId, shopId, body) => {
    return await instance.put("/user/1.0.0/shop/shop/update/info?user-id=" + userId + "&shop-id=" + shopId, body);

}
export const getQRImage = async (amount) => {
    return await instance.post("/bank/1.0.0/qr-code-info/"
        + null + "/account-no/" + null + "/template/" + null + "?account-name="
        + null + "&addInfo=" + null + "&amount=" + amount);
}
export const createOrder = async (body) => {
    return await instance.post("/order/1.0.0/order", body);
}
export const getOrderDetails = async (id) => {
    return await instance.get("/order/1.0.0/order-detail/" + id);
}
export const filterOrder = async (body) => {
    return await instance.post("/order/1.0.0/order/filter", body);
}
export const confirmStatusSequence = async (orderId, byUser) => {
    return await instance.put("/order/1.0.0/order/" + orderId + "/confirm-sequence", byUser);
}
export const cancelOrder = async (orderId, body) => {
    return await instance.put("/order/1.0.0/order/" + orderId + "/cancel", body);
}
export const updateAddressUser = async (userId, address) => {
    return await instance.put("/user/1.0.0/user/" + userId + "/info-address", address);
}
export const updateInfoUser = async (userId, info) => {
    return await instance.put("/user/1.0.0/user/" + userId + "/info-basic", info);
}
export const activeUser = async (userId, info) => {
    return await instance.put("/user/1.0.0/user/" + userId + "/active/" + info, info);
}
export const updatePasswordUser = async (userId, pw) => {
    return await instance.put("/user/1.0.0/user/" + userId + "/info-password?pwd=" + pw);
}
export const filterUser = async (body) => {
    return await instance.post("/user/1.0.0/user/filter", body);
}


