import React, {useState} from 'react';
import classNames from "classnames/bind";
import styles from "./ColorSize.module.scss";


const cx = classNames.bind(styles);

function ColorSize(props) {
    const {data,getColor,title} = props;
    console.log(data)
    const [appState, changeState] = useState({
        activeObject: null,
        object: data
    })

    function toggleActive(index) {
        changeState({...appState, activeObject: appState.object[index]});
        console.log(index)
        getColor(appState.object[index])
        console.log(appState.object[index])

    }

    function toggleActiveStyles(index) {
        if (appState.object[index] === appState.activeObject) {
            return cx('color-size__item', 'active')
        } else {
            return cx('color-size__item')
        }
    }
    const colors = [{name:data}]
    console.log(colors)

    return (
        <div className={cx('color-size')}>
            <h3 className={cx('title')}>{title}</h3>
            {
                colors.map((item, index) => {
                    return (
                        <span onClick={() => toggleActive(index)}
                              key={index}
                              className={toggleActiveStyles(index)}>
                                                    {item.name}
                                                </span>

                    )
                })
            }

        </div>
    );
}

export default ColorSize;
