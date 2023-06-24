import config from "../config";
import Dashboard from '../pages/Dashboard/Dashboard'
import Shops from "../pages/Shops/Shops";
import AccountManager from "../pages/Account/AccountManager";
import AddCustomner from "../components/Account/add/AddAccount";
import EditCustomner from "../components/Account/Edit/EditAccount";
import AddShop from "../components/Shop/add/Add";
import EditShop from "../components/Shop/Edit/Edit";
import OrderManager from "../pages/Order/OrderManager";
import AddOrder from "../components/Order/add/Add";
import EditOrder from "../components/Order/Edit/Edit";
import Login from "../pages/Login/Login";


const publicRoutes = [
    {path: '/', component: Dashboard},
    {path: config.routes.dashboard, component: Dashboard},
    {path: config.routes.accountManager, component: AccountManager},
    {path: config.routes.addAccount, component: AddCustomner},
    {path: config.routes.editAccount, component: EditCustomner},
    {path: config.routes.shops, component: Shops},
    {path: config.routes.addShop, component: AddShop},
    {path: config.routes.editShop, component: EditShop},
    {path: config.routes.orders, component: OrderManager},
    {path: config.routes.addOrder, component: AddOrder},
    {path: config.routes.editOrder, component: EditOrder},
    {path: config.routes.login, component: Login, layout: null},



];
const privateRoutes = [];
export {publicRoutes, privateRoutes};
