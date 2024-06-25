"use client";

import { getRecent, getUser } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

interface pageProps {
    categories: any[]
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
    return <Profile categories={props.categories} user={user} recentList={recentList} setRecentList={setRecentList}>
        1
    </Profile>;
}