import React from "react";
import styles from "./Cards.module.scss";
import { cardsData } from "../../../Data/Data";

import Card from "../Card/Card";
import classNames from 'classnames/bind';
const cx = classNames.bind(styles);

const Cards = () => {
  return (
    <div className={cx("Cards")}>
      {cardsData.map((card, id) => {
        return (
          <div className={cx("parentContainer" )}key={id}>
            <Card
              title={card.title}
              color={card.color}
              barValue={card.barValue}
              value={card.value}
              png={card.png}
              series={card.series}
            />
          </div>
        );
      })}
    </div>
  );
};

export default Cards;
