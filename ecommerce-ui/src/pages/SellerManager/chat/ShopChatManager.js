import React from 'react';


import SellerManagerWrapper from "~/layout/SellerManager";
import Chat from "~/pages/SellerManager/chat/Chat";

function ShopChatManager(props) {
    return (
        <SellerManagerWrapper child={<Chat/>}/>
    );
}
export default ShopChatManager;
