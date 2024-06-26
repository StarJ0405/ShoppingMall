"use client";

import { getCategories } from "@/app/API/NonUserAPI";
import { getRecent, getUser } from "@/app/API/UserAPI";
import Main from "@/app/Global/Layout/MainLayout";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

interface pageProps {
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [categories, setCategories] = useState(props.categories);

    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getCategories().then(r => setCategories(r)).catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    return <Main user={user} categories={categories} recentList={recentList} setRecentList={setRecentList} >
        <div className="flex flex-col w-[1240px]">
            <div className="flex flex-col w-full h-[500px] bg-gray-300 p-4 overflow-y-scroll">
                <div className="self-start py-2 px-4 flex items-center">
                    <div className="flex flex-col items-center mr-2">
                        <img src={user?.url ? user?.url : '/base_profile.png'} className="w-[36px] h-[36px]" />
                        <label>어드민</label>
                    </div>
                    <div className="flex flex-col border border-gray-500 rounded-r-full py-4 pl-4 pr-8 break-words text-start">
                        <div className="self-start max-w-[600px]">AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</div>
                        <label className="self-start text-xs">2024.06.27 00:25</label>
                    </div>
                </div>
                <div className="self-end py-2 px-4 flex items-center">
                    <div className="flex flex-col border border-gray-500 rounded-l-full py-4 pr-4 pl-8 break-words text-end">
                        <div className="self-end max-w-[600px]">AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA</div>
                        <label className="self-end text-xs">2024.06.27 00:25</label>
                    </div>
                    <div className="flex flex-col items-center ml-2">
                        <img src={user?.url ? user?.url : '/base_profile.png'} className="w-[36px] h-[36px]" />
                        <label>어드민</label>
                    </div>
                </div>
            </div>
            <div className="w-[1225px] py-2 px-4 border border-gray-500 rounded-lg flex">
                <input type="text" className="outline-none w-full" placeholder="채팅.." autoFocus />
                <img src="/image.png" className="w-[24px] h-[24px] cursor-pointer ml-2" onClick={() => document.getElementById('file')?.click()} />
                <input id="file" type="file" hidden onChange={() => {alert('이미지 시도')}} />
            </div>
        </div>
    </Main>
}