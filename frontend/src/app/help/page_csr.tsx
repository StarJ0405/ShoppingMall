"use client";
import { useEffect, useState } from "react";
import Main from "../Global/Layout/MainLayout";
import { getRecent, getUser } from "../API/UserAPI";
import { redirect } from "next/navigation";

interface pageProps {
    categories: any[];
}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    return <Main categories={props.categories} recentList={recentList} setRecentList={setRecentList} user={user}>
        1
    </Main>
}