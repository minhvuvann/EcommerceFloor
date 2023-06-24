import * as React from 'react';
import {styled, useTheme} from '@mui/material/styles';
import Box from '@mui/material/Box';
import MuiDrawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import CssBaseline from '@mui/material/CssBaseline';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItemText from '@mui/material/ListItemText';
import classNames from 'classnames/bind'
import styles from './Navbar.module.scss'
import {Menu, MenuItem} from 'react-pro-sidebar';
import {UilSignOutAlt} from "@iconscout/react-unicons";
import Logo from "../../assets/logo/Mellow.png";
import {Avatar, Badge, Grid} from '@mui/material';
import NotificationsIcon from '@mui/icons-material/Notifications';
import SettingsIcon from '@mui/icons-material/Settings';
import {SidebarData} from '../../Data/Data';
import {Link} from 'react-router-dom';
import {useContext} from "react";
import {UserContext} from "../../config/provider/UserProvider";

const cx = classNames.bind(styles);
const drawerWidth = 240;

const openedMixin = (theme) => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme) => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({theme, open}) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

const Drawer = styled(MuiDrawer, {shouldForwardProp: (prop) => prop !== 'open'})(
    ({theme, open}) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        ...(open && {
            ...openedMixin(theme),
            '& .MuiDrawer-paper': openedMixin(theme),
        }),
        ...(!open && {
            ...closedMixin(theme),
            '& .MuiDrawer-paper': closedMixin(theme),
        }),
    }),
);

export default function Navbar(props) {
    const {children} = props;
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);
    const {admin} = useContext(UserContext);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    return (
        <Box sx={{display: 'flex'}} width={"100%"}>
            <CssBaseline/>
            <AppBar position="fixed" open={open}>
                <Toolbar width={"100%"} sx={{background: '#222222'}}>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        sx={{
                            marginRight: 5,
                            ...(open && {display: 'none'}),
                        }}
                    >
                        <MenuIcon/>
                    </IconButton>

                    <Typography width={"100%"} variant="h6" noWrap component="div">
                        <div className={cx('navbar-right')}>
                            <div style={{
                                width: "20%",
                                justifyContent: "flex-end",
                                marginRight: "-5rem"
                            }}>
                                <Grid style={{}} container rowSpacing={1} columnSpacing={{xs: 1, sm: 2, md: 3}}>
                                    <Grid item xs={2}>

                                    </Grid>
                                    <Grid item xs={4} sx={{marginTop: 1}}>
                                    </Grid>
                                    <Grid style={{display:"flex",justifyContent:"flex-end"}} item xs={3}>
                                        <Avatar
                                            alt="Remy Sharp"
                                            src={admin?.imageUrl}
                                            sx={{width: 45, height: 45}}
                                        />
                                    </Grid>

                                </Grid>

                            </div>

                        </div>
                    </Typography>
                </Toolbar>
            </AppBar>
            <Drawer style={{width: open ? "20rem" : "0"}} variant="permanent" open={open}>
                <DrawerHeader>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                    </IconButton>
                </DrawerHeader>
                <Divider/>
                <div className={cx("logo")}>
                    <img src={Logo}/>
                    <span>
            <ListItemText primary={'MELLOW'} sx={{opacity: open ? 1 : 0}}/>

            </span>
                </div>
                <div className={cx('avatar')}>
                    <Avatar
                        variant="rounded"
                        src={admin?.imageUrl}
                        sx={{width: 75, height: 75, marginLeft: 10, marginTop: 5, display: open ? 'block' : 'none'}}
                    />
                    <ListItemText primary={admin?.fullName} sx={{opacity: open ? 1 : 0, marginLeft: 11.5}}/>


                </div>

                <Divider/>
                <div className={cx("menu")}>
                    <Menu className={SidebarData}>
                        {SidebarData.map((item, index) => {
                            return (
                                <MenuItem
                                    className={cx("menuItem")}
                                    key={item.id}
                                    component={<Link to={item.to}/>}
                                    active={window.location.pathname === item.to}

                                >
                                    <item.icon/>
                                    <ListItemText style={{width: "15rem"}} primary={item.heading}
                                                  sx={{opacity: open ? 1 : 0,}}/>

                                </MenuItem>
                            );
                        })}

                        <div className={cx("menuItem")}>

                        </div>
                    </Menu>

                </div>
            </Drawer>
            <Box component="main" sx={{
                flexGrow: 1, p: 3

            }} style={{
                width: open ? "116rem" : "132rem", marginLeft: open ? "3.5rem" : "6rem",
                height: "99.8vh", padding:'24px 24px 0 24px'
            }}>
                <DrawerHeader/>
                {children}
            </Box>
        </Box>
    );
}
