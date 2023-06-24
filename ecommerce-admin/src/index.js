import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {ProSidebarProvider} from 'react-pro-sidebar';
import UserProvider from "./config/provider/UserProvider";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <ProSidebarProvider>
            <UserProvider>
                <App/>

            </UserProvider>
        </ProSidebarProvider>
    </React.StrictMode>
);


