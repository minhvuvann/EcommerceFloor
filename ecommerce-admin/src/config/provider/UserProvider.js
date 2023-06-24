import React, {createContext, useEffect, useState} from 'react';
import {getUser} from "../../services/workspaces.sevices";


export const UserContext = createContext({});
const admin_id = localStorage.getItem("ad-id");

function UserProvider(props) {
    const [admin, setAdmin] = useState({});
    const [load_update, setLoad_update] = useState(false);

    async function providerAdmin() {
        if (admin_id) {
            const data = await getUser(admin_id);
            setAdmin(data?.data);
        }

    }

    useEffect(() => {
        providerAdmin();
    }, [load_update]);


    return (
        <UserContext.Provider value={{admin, setLoad_update}}>
            {props.children}
        </UserContext.Provider>
    );
}

export default UserProvider;