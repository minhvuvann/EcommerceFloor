const routes = {
    home: "/home",
    login: "/login",
    register: "/register",
    registerShop: "/register-shop",
    sellerManager: "/seller",
    productDetail: "/product-detail/:id/product",
    product: "/product-industrial/:id/:name",
    productList: "/product-list",
    productListAll: "/product/list/all-seller/:id/shop",
    addProduct: "/product/new",
    editProduct: "/product/edit/:shopId/product",
    shopProfile: "/shop/profile/:shopId",
    shopOrderAll: "/shop/order/all",
    shopOrderReady: "/shop/order/ready",
    shopOrderDelivering: "/shop/order/delivering",
    shopOrderDelivered: "/shop/order/delivered",
    shopOrderCancel: "/shop/order/cancel",
    verifyCodeRegister: "/register-verify-code/:email/id/:id",
    createPassword: "/create-password/id/:id",
    informationUser: "/info-user",
    chatService: "/chat-mg",
    cartDetail: "/cart-detail",
    informationAddress: "/info-address",
    passwordChange: "/password-change",
    purchase: "/purchase",
    orderDetail: "/order-detail/:id/sample",
    verifyForgotPassword: "/verify-forgot-password/:email/id/:id",
    sendMailForgotPassword: "/send-mail-forgot-password",
    notFound404: '/not-found',
    shopChat: "/shop/chat/:shopId"


};
export default routes;
