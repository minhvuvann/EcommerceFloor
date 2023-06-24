import React, {useContext, useEffect, useState} from 'react';
import classNames from "classnames/bind";
import styles from "./ChatService.module.scss";
import ChatPerson from "~/pages/chat/chat-person";
import {Send} from "@mui/icons-material";
import {getShopAll} from "~/services/workspaces.sevices";
import {BiExit} from "react-icons/bi";
import {UserContext} from "~/config/provider/UserProvider";
import DateUtils from "~/utils/DateUtils";

const cx = classNames.bind(styles);

function ChatService(props) {
    const [ticker, setTicker] = useState('');
    const [headerName, setHeaderName] = useState();
    const [headerImg, setHeaderImg] = useState();
    const [shopAll, setShopAll] = useState([]);
    const {setChatOpen} = props;
    const [messages, setMessages] = useState([]);
    const {customer} = useContext(UserContext);
    const [sendContent, setSendContent] = useState('');

    useEffect(() => {
            async function loadData() {
                const res = await getShopAll();
                setTicker(res.data[0].shopId);
                setHeaderName(res.data[0].name);
                setHeaderImg(res.data[0].imageUrl);
                setShopAll(res.data);
                const socketConnectS = new WebSocket(`ws://localhost:8202/ecommerce-floor/chat/${customer.id}_${res?.data[0]?.shopId}`);
                socketConnectS.onopen = () => {
                    socketConnectS.send('MESSAGES_LIST');
                };
                socketConnectS.onmessage = (event) => {
                    const response = JSON.parse(event.data);
                    setMessages(response);
                    console.log(response);
                };
                return () => {
                    socketConnectS.close();
                };
            }

            loadData();
        }
        , []
    );


    function activeTicker(id, name, img) {
        setTicker(id);
        setHeaderImg(img);
        setHeaderName(name);
        const socketConnect = new WebSocket(`ws://localhost:8202/ecommerce-floor/chat/${customer.id}_${id}`);
        socketConnect.onopen = () => {
            socketConnect.send('MESSAGES_LIST');
        }
        socketConnect.onmessage = (event) => {
            const response = JSON.parse(event.data);
            setMessages(response);
            console.log(response);
        };
        return () => {
            socketConnect.close();
        };
    }


    const exitChat = () => {
        setChatOpen(false);
    }
    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            sendMess(ticker);
        }
    };

    function sendMess(id) {
        const socketConnect = new WebSocket(`ws://localhost:8202/ecommerce-floor/chat/${customer.id}_${id}`);
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
                    shopAll?.map(({shopId, imageUrl, name, status}) => {
                            return (
                                <ChatPerson ticker={ticker} onClick={() => activeTicker(shopId, name, imageUrl)}
                                            key={shopId} id={shopId} img={imageUrl}
                                            name={name} statusActive={status === "ACTIVE"}/>
                            )
                        }
                    )
                }
                </ul>
            </aside>
            <main className={cx("main")}>
                <header className={cx("main-header")}>
                    <img className={cx("main-header-img")}
                         src={headerImg} alt=""/>
                    <div className={cx("main-header-title")}>
                        <h2 className={cx("main-header-title-h2")}>{headerName}</h2>
                    </div>
                    <img className={cx("main-header-img")}
                         src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_star.png" alt=""/>
                </header>
                <ul className={cx("chat")}>
                    {messages?.map(({userId, senderId, content, fromAt}) => (
                            <li className={cx(userId === customer.id ? "me" : "you", "chat-li")}>
                                <div className={cx("chat-entete")}>
                                    <span className={cx("status", "green")}/>
                                    <h2 className={cx("chat-h3")}>{DateUtils.convert(fromAt)}</h2>
                                </div>
                                <div
                                    className={cx(userId === customer.id ? "me-message" : "you-message", "chat-message")}>
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
                    <Send  onClick={() => sendMess(ticker)} sx={{
                        height: "1.3em",
                        marginLeft: "34.5rem"
                    }} style={{cursor: "pointer"}} color={"primary"}/>


                </footer>
            </main>
        </div>
    );
}

export default ChatService;