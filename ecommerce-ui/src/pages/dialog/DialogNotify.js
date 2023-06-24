import React, {useState} from 'react';
import classNames from "classnames/bind";
import styles from "./DialogNotify.module.scss";
import {Dialog, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";


const cx = classNames.bind(styles);

function DialogNotify(props) {
    const {open, title, content} = props;
    return (

            <Dialog open={open} onClose={false}>
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {content}
                    </DialogContentText>
                </DialogContent>
            </Dialog>
    );
}

export default DialogNotify;
