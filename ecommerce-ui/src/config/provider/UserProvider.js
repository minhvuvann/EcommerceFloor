import React, {createContext, useEffect, useState} from 'react';

import { getUser} from "~/services/workspaces.sevices";


export const UserContext = createContext({});

function UserProvider(props) {
    const [customer, setCustomer] = useState({});
    const [shouldUpdate, setShouldUpdate] = useState(false);
    const user = JSON.parse(localStorage.getItem("cs-obj"));

    async function loadCustomer() {
        if (user !== null) {
            const data = await getUser(user.id);
            localStorage.setItem("cs-obj", JSON.stringify(data?.data));
            setCustomer(data?.data);
        }

    }

    useEffect(() => {
        loadCustomer()
    }, [shouldUpdate]);


    return (
        <UserContext.Provider value={{customer, setShouldUpdate}}>
            {props.children}
        </UserContext.Provider>
    );
}

export default UserProvider;