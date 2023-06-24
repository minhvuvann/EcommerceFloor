import React, {useState} from 'react';
import classNames from "classnames/bind";
import styles from "./FilterItem.module.scss";


const cx = classNames.bind(styles);

function FilterItem(props) {
    const {data,handleGetFilterItem} = props;
    const [appState, changeState] = useState({
        activeObject: null,
        object: data,

    })

    function toggleActive(index) {
        changeState({...appState, activeObject: appState.object[index]});
        handleGetFilterItem(appState.object[index])

    }


    const toggleActiveStyles = (index) => {
        if (appState.object[index] === appState.activeObject) {
            return cx('filter-item', 'active')

        } else {
            return cx('filter-item')
        }
    }
    return (
        data.map((item,index)=>{
            return (
                <span key={index}
                      onClick={() => toggleActive(index)}
                      className={toggleActiveStyles(index)}>
                    {item.name}
                </span>
            )
        })
    );
}

export default FilterItem;
