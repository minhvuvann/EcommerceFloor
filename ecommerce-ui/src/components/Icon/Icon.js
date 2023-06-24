import React from "react";

import logo from "~/assets/logo/Mellow.png";
import logo_reverse from "~/assets/logo/Mellow_revese.png";
import {ResponsiveImage} from 'react-responsive-image';

export const Logo = ({width = "162px", height = "50px", className}) => (
    <img
        alt="Mellow"
        src={logo}
        className={className}
    />
);
export const LogoReverse = ({width = "123", height = "40", className}) => (
    <img alt="Mellow"
         src={logo_reverse}
         className={className}/>
);
export const LogoVietQR = ({width, height, className}) => (
    <img alt="Viet QR"
         style={{width: width, height: height}}
         src={"https://www.vietqr.io/img/vietqr%20api%20-%20payment%20kit.png"}
         className={className}/>
);
export const MessageIcon = ({width = "30px", height = "30px", className}) => (
    <svg
        stroke="currentColor"
        fill="#fff"
        strokeWidth="0"
        viewBox="0 0 1024 1024"
        className={className}
        width={width}
        height={height}
        xmlns="http://www.w3.org/2000/svg"
    >
        <path
            d="M464 512a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm200 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm-400 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm661.2-173.6c-22.6-53.7-55-101.9-96.3-143.3a444.35 444.35 0 0 0-143.3-96.3C630.6 75.7 572.2 64 512 64h-2c-60.6.3-119.3 12.3-174.5 35.9a445.35 445.35 0 0 0-142 96.5c-40.9 41.3-73 89.3-95.2 142.8-23 55.4-34.6 114.3-34.3 174.9A449.4 449.4 0 0 0 112 714v152a46 46 0 0 0 46 46h152.1A449.4 449.4 0 0 0 510 960h2.1c59.9 0 118-11.6 172.7-34.3a444.48 444.48 0 0 0 142.8-95.2c41.3-40.9 73.8-88.7 96.5-142 23.6-55.2 35.6-113.9 35.9-174.5.3-60.9-11.5-120-34.8-175.6zm-151.1 438C704 845.8 611 884 512 884h-1.7c-60.3-.3-120.2-15.3-173.1-43.5l-8.4-4.5H188V695.2l-4.5-8.4C155.3 633.9 140.3 574 140 513.7c-.4-99.7 37.7-193.3 107.6-263.8 69.8-70.5 163.1-109.5 262.8-109.9h1.7c50 0 98.5 9.7 144.2 28.9 44.6 18.7 84.6 45.6 119 80 34.3 34.3 61.3 74.4 80 119 19.4 46.2 29.1 95.2 28.9 145.8-.6 99.6-39.7 192.9-110.1 262.7z"/>
    </svg>
);
export const MenuCategoryIcon = ({
                                     width = "12px",
                                     height = "22px",
                                     className,
                                 }) => (
    <svg viewBox="0 0 12 10" className={className} width={width} height={height}>
        <g fillRule="evenodd" stroke="none" strokeWidth="1">
            <g transform="translate(-373 -208)">
                <g transform="translate(155 191)">
                    <g transform="translate(218 17)">
                        <path d="m0 2h2v-2h-2zm4 0h7.1519633v-2h-7.1519633z"/>
                        <path d="m0 6h2v-2h-2zm4 0h7.1519633v-2h-7.1519633z"/>
                        <path d="m0 10h2v-2h-2zm4 0h7.1519633v-2h-7.1519633z"/>
                    </g>
                </g>
            </g>
        </g>
    </svg>
);
export const ArrowLeft = ({width = "20px", height = "20px", className}) => (
    <svg
        stroke="currentColor"
        fill="currentColor"
        strokeWidth="0"
        viewBox="0 0 24 24"
        className={className}
        width={width}
        height={height}
        xmlns="http://www.w3.org/2000/svg"
    >
        <path fill="none" d="M0 0h24v24H0V0z"/>
        <path d="M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z"/>
    </svg>
);
export const ArrowRight = ({width = "20px", height = "20px", className}) => (
    <svg
        stroke="currentColor"
        fill="currentColor"
        strokeWidth="0"
        viewBox="0 0 24 24"
        className={className}
        width={width}
        height={height}
        xmlns="http://www.w3.org/2000/svg"
    >
        <path fill="none" d="M0 0h24v24H0V0z"/>
        <path d="M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z"/>
    </svg>
);
