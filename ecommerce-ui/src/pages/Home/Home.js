import React from "react";
import classNames from "classnames/bind";
import {Container} from '@mui/material'


import styles from "./Home.module.scss";
import MainHome from "~/components/main-home";
const cx = classNames.bind(styles);

function Home(props) {
  return (
    <div className={cx("wrapper")}>
      <Container>
        <MainHome />
      </Container>
    </div>
  );
}

export default Home;
