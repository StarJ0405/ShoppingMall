"use client"
import { useEffect, useState } from "react";
import Main from "./Global/main";
import { fetchUser } from "./API/UserAPI";

interface pageProps{

}
export default function Page(props : pageProps){
    const[user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window === 'undefined' ? null :  localStorage.getItem('accessToken');
    useEffect(()=>{
        if(ACCESS_TOKEN)
            fetchUser()
                .then(r=>{
                    setUser(r);
                })
                .catch(e=>console.log(e));
    },[ACCESS_TOKEN]);
    return <>
        <Main className="" user={user} >
            a
        </Main>
    </>
}