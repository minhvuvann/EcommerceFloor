import React from "react";



export const LogoVietQR = ({width, height, className}) => (
    <img alt="Viet QR"
         style={{width: width, height: height}}
         src={"https://www.vietqr.io/img/vietqr%20api%20-%20payment%20kit.png"}
         className={className}/>
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
