import config from "~/config";
import Home from "~/pages/Home";
import NotFound_404 from "~/pages/404/404";
import ProductDetail from "~/pages/ProductDetail";
import AllProduct from "~/pages/SellerManager/AllProduct";
import Product from "~/pages/Product";
import AddProductManager from "~/pages/SellerManager/AddProduct";
import SellerManager from "~/pages/SellerManager";
import EditProductManager from "~/pages/SellerManager/EditProduct/EditProductManager";
import ShopProfileManager from "~/pages/SellerManager/Shop/ShopProfile";
import LoginLayout from "~/pages/auth/Login/LoginLayout";
import RegisterLayout from "~/pages/auth/register/RegisterLayout";
import VertifyCodeRegisterLayout from "~/pages/auth/VertifyCodeRegister/VertifyCodeRegisterLayout";
import CreatePasswordLayout from "~/pages/auth/CreatePassword/CreatePasswordLayout";
import InformationUser from "~/pages/InformationUser";
import ChatService from "~/pages/chat";
import CartDetail from "~/pages/payment/cart";
import InformationAddressManager from "~/pages/InformationUser/account/address";
import OrderDetail from "~/pages/payment/order";
import PasswordChangeManager from "~/pages/InformationUser/account/PasswordChange";
import PurchaseManager from "~/pages/InformationUser/Purchase";
import OrderAllManager from "~/pages/SellerManager/order/orderAll";
import OrderCancelManager from "~/pages/SellerManager/order/orderCancel";
import OrderReadyManager from "~/pages/SellerManager/order/orderReady";
import OrderDeliveryManager from "~/pages/SellerManager/order/orderDelivery";
import OrderDeliveredManager from "~/pages/SellerManager/order/orderDelivered";
import VertifyForgotPasswordLayout
    from "~/pages/auth/CreatePassword/VeritifyForgotPassword/VertifyForgotPasswordLayout";
import SendMailForgotLayout from "~/pages/auth/CreatePassword/SendMailForgotPassword";
import RegisterSeller from "~/pages/SellerManager/createSeller";
import ShopChatManager from "~/pages/SellerManager/chat";


const publicRoutes = [
    {path: '/', component: Home},
    {path: config.routes.notFound404, component: NotFound_404, layout: null},
    {path: config.routes.home, component: Home},
    {path: config.routes.login, component: LoginLayout, layout: null},
    {path: config.routes.register, component: RegisterLayout, layout: null},
    {path: config.routes.verifyCodeRegister, component: VertifyCodeRegisterLayout, layout: null},
    {path: config.routes.sendMailForgotPassword, component: SendMailForgotLayout, layout: null},
    {path: config.routes.verifyForgotPassword, component: VertifyForgotPasswordLayout, layout: null},
    {path: config.routes.createPassword, component: CreatePasswordLayout, layout: null},
    {path: config.routes.product, component: Product},
    {path: config.routes.sellerManager, component: SellerManager, layout: null},
    {path: config.routes.productListAll, component: AllProduct, layout: null},
    {path: config.routes.addProduct, component: AddProductManager, layout: null},
    {path: config.routes.editProduct, component: EditProductManager, layout: null},
    {path: config.routes.registerShop, component: RegisterSeller, layout: null},
    {path: config.routes.shopProfile, component: ShopProfileManager, layout: null},
    {path: config.routes.shopOrderAll, component: OrderAllManager, layout: null},
    {path: config.routes.shopOrderReady, component: OrderReadyManager, layout: null},
    {path: config.routes.shopOrderDelivering, component: OrderDeliveryManager, layout: null},
    {path: config.routes.shopOrderDelivered, component: OrderDeliveredManager, layout: null},
    {path: config.routes.shopOrderCancel, component: OrderCancelManager, layout: null},
    {path: config.routes.shopChat, component: ShopChatManager, layout: null},
    {path: config.routes.chatService, component: ChatService, layout: null},
    {path: config.routes.productDetail, component: ProductDetail},
    {path: config.routes.cartDetail, component: CartDetail},
    {path: config.routes.informationUser, component: InformationUser},
    {path: config.routes.informationAddress, component: InformationAddressManager},
    {path: config.routes.passwordChange, component: PasswordChangeManager},
    {path: config.routes.purchase, component: PurchaseManager},
    {path: config.routes.orderDetail, component: OrderDetail},


];
const privateRoutes = [];
export {publicRoutes, privateRoutes};
