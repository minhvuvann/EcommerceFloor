import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
const WebSocketService = {
    connect: (userId, onMessageReceived) => {
        const socket = new SockJS('http://localhost:8202/ecommerce-floor/chat');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            stompClient.subscribe(`/user/${userId}/queue/messages`, (message) => {
                onMessageReceived(JSON.parse(message.body));
            });
        });

        return stompClient;
    },

    disconnect: (stompClient) => {
        if (stompClient) {
            stompClient.disconnect();
        }
    },

    sendMessage: (stompClient, userId, message) => {
        stompClient.send(`/app/chat/${userId}`, {}, JSON.stringify(message));
    },
};

export default WebSocketService;