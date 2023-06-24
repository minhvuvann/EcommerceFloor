import {createTheme} from "@mui/material";

export const themeCustomer = createTheme({
    status: {
        danger: '#e53e3e',
    },
    breakpoints: {
        values: {
            xs: 0,
            sm: 600,
            md: 900,
            lg: 1200,
            xl: 1536,
        },
    },
    palette: {
        primary: {
            main: '#ff5588',
            darker: '#fff',
        },
        white:{
            main: '#fff',
            darker: '#ff5588',
        },
        neutral: {
            main: '#64748B',
            contrastText: '#fff',
        },
        danger: {
            main: '#f44336',
        },
        edit:{
            main:'#ffc107'
        }
    },
});
