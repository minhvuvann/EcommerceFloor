import React, {useContext, useState} from 'react';
import classNames from "classnames/bind";


import styles from "./MainHome.module.scss";
import Banner from "~/components/main-home/banner-slider";
import Category from "~/components/main-home/category";
import ProductSuggestion from "~/components/main-home/product-suggestion";
import BackTop from "~/components/BackTop";
import {Dialog, DialogContent, DialogContentText} from "@material-ui/core";
import ChatService from "~/pages/chat";
import ChatButton from "~/components/chatButton";
import {UserContext} from "~/config/provider/UserProvider";

const cx = classNames.bind(styles);

function MainHome(props) {
    const [chatOpen, setChatOpen] = useState(false);
    const {customer} = useContext(UserContext);
    return (
        <div className={cx('wrapper')}>
            <Banner/>
            <Category/>
            <ProductSuggestion/>
            <Dialog open={chatOpen&&(customer !== undefined && Object.keys(customer).length > 0)}
                    PaperProps={{
                        style: {
                            position: 'absolute',
                            bottom: '0',
                            right: '0',

                        },
                    }}>
                <DialogContent style={{padding: 0}}>
                    <DialogContentText style={{marginBottom: "7px", fontFamily: "inherit"}}>
                        <ChatService setChatOpen={setChatOpen}/>
                    </DialogContentText>
                </DialogContent>
            </Dialog>
            <ChatButton setChatOpen={setChatOpen}/>
            <BackTop/>
        </div>
    );
}

export default MainHome;
