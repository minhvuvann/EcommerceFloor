import React, {createContext, useEffect, useState} from 'react';
import {changeLanguage} from "~/services/workspaces.sevices";


export const LanguageContext = createContext([]);

function LanguageProvider(props) {
    const [localeList, setLocaleList] = useState({});
    const [syncUpdate, setSyncUpdate] = useState(false);
    const locale = localStorage.getItem('locale-language');

    async function load_language() {
        const languages = await changeLanguage(locale ?
            locale : 'vi');
        setLocaleList(languages?.data);
    }

    const setProperties = (params) => {
        localStorage.setItem('locale-language', params);
    }
    useEffect(() => {
        load_language();
    }, [syncUpdate]);

    return (
        <LanguageContext.Provider value={{localeList, setSyncUpdate, properties: locale, setProperties}}>
            {props.children}
        </LanguageContext.Provider>
    );
}

export default LanguageProvider;