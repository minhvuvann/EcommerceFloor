import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";

import {UserContext} from "~/config/provider/UserProvider";
import {getShopAll, getUserAll} from "~/services/workspaces.sevices";
import {BiExit} from "react-icons/bi";
import DateUtils from "~/utils/DateUtils";
import {Send} from "@mui/icons-material";
import styles from "./Chat.module.scss"
import {useParams} from "react-router-dom";
import ChatPerson from "~/pages/SellerManager/chat/chat-customer";
const cx = classNames.bind(styles);

function Chat(props) {
    const [ticker, setTicker] = useState('');
    const [headerName, setHeaderName] = useState();
    const [headerImg, setHeaderImg] = useState();
    const [customerAll, setCustomerAll] = useState([]);
    const {setChatOpen} = props;
    const [messages, setMessages] = useState([]);
    const { shopId} =useParams();
    const [sendContent, setSendContent] = useState('');
    const exitChat = () => {
        setChatOpen(false);
    }
    useEffect(() => {
            async function loadData() {
                const res = await getUserAll();
                console.log(res);
                setTicker(res.data[0].id);
                setHeaderName(res.data[0].fullName);
                setHeaderImg(res.data[0].imageUrl);
                setCustomerAll(res.data);
                const socketConnectS = new WebSocket(`ws://localhost:8202/mellow-ecommerce-floor/chat/${shopId}_${res?.data[0]?.id}`);
                socketConnectS.onopen = () => {
                    socketConnectS.send('MESSAGES_LIST');
                };
                socketConnectS.onmessage = (event) => {
                    const response = JSON.parse(event.data);
                    setMessages(response);
                };
                return () => {
                    socketConnectS.close();
                };
            }

            loadData();
        }
        , []
    );


    function activeTicker(customerId, name, img) {
        setTicker(customerId);
        setHeaderImg(img);
        setHeaderName(name);
        const socketConnect = new WebSocket(`ws://localhost:8202/mellow-ecommerce-floor/chat/${shopId}_${customerId}`);
        socketConnect.onopen = () => {
            socketConnect.send('MESSAGES_LIST');
        }
        socketConnect.onmessage = (event) => {
            const response = JSON.parse(event.data);
            setMessages(response);
        };
        return () => {
            socketConnect.close();
        };
    }
    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            sendMess(ticker);
        }
    };

    function sendMess(customerId) {
        const socketConnect = new WebSocket(`ws://localhost:8202/mellow-ecommerce-floor/chat/${shopId}_${customerId}`);
        socketConnect.onopen = () => {
            socketConnect.send(sendContent);
        }
        socketConnect.onmessage = (event) => {
            const response = JSON.parse(event.data);
            console.log(response);
            setMessages(prevState => response);
            setSendContent('');
        };

    }

    return (
        <div className={cx("container")}>
            <aside className={cx("aside")}>
                <header className={cx("aside-header")}>
                    <BiExit onClick={exitChat} style={{color: "#ff5588", marginRight: "2px", cursor: "pointer"}}/>
                    <input className={cx("aside-input")} type="text" placeholder="Tìm kiếm"/>
                </header>
                <ul className={cx("aside-ul")}>{
                    customerAll?.map(({ id, imageUrl,  fullName, userStatus}) => {
                            return (
                                <ChatPerson ticker={ticker} onClick={() => activeTicker(id, fullName, imageUrl)}
                                            key={id} id={id} img={imageUrl}
                                            name={fullName} statusActive={userStatus === "ACTIVE"}/>
                            )
                        }
                    )
                }
                </ul>
            </aside>
            <main className={cx("main")}>
                <header className={cx("main-header")}>
                    <img className={cx("main-header-img")}
                         src={headerImg} />
                    <div className={cx("main-header-title")}>
                        <h2 className={cx("main-header-title-h2")}>{headerName}</h2>
                    </div>
                    <img className={cx("main-header-img")}
                         src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_star.png" alt=""/>
                </header>
                <ul className={cx("chat")}>
                    {messages?.map(({userId, senderId, content, fromAt}) => (
                         <li className={cx(userId == shopId ? "me" : "you", "chat-li")}>
                                <div className={cx("chat-entete")}>
                                    <span className={cx("status", "green")}/>
                                    <h2 className={cx("chat-h3")}>{DateUtils.convert(fromAt)}</h2>
                                </div>
                                <div
                                    className={cx(userId ==shopId ? "me-message" : "you-message", "chat-message")}>
                                    {content}
                                </div>
                            </li>
                        )
                    )}

                </ul>
                <footer className={cx("footer")}>
                    <div className={cx("textarea-container")}>
                        <textarea onKeyPress={handleKeyPress}
                            value={sendContent} onChange={event => setSendContent(event.target.value)}
                                  className={cx("footer-textarea")} placeholder="Nhập nội dung tin nhắn"/>
                    </div>
                    <img className={cx("footer-img")}
                         src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_picture.png" alt=""/>
                    <img className={cx("footer-img")}
                         src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_file.png" alt="">

                    </img>
                    <Send onClick={() => sendMess(ticker)} sx={{
                        height: "1.3em",
                        marginLeft: "75.5rem"
                    }} style={{cursor: "pointer"}} color={"primary"}/>


                </footer>
            </main>
        </div>
    );
}

export default Chat;
