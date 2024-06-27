"use client";

import { getCategories } from "@/app/API/NonUserAPI";
import { getChatRoomList, getRecent, getUser } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { getDateTimeFormat } from "@/app/Global/Method";
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
    const [chatRooms, setChatRooms] = useState([] as any[]);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getCategories().then(r => setCategories(r)).catch(e => console.log(e));
                    getChatRoomList().then(r => setChatRooms(r)).catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    console.log(chatRooms);
    return <Profile user={user} categories={categories} recentList={recentList} setRecentList={setRecentList} >
        <label className='text-3xl font-bold'><label className="text-red-500">채팅방</label> 목록</label>
        <table>
            <thead>
                <tr>
                    <th className="w-[150px]">
                        상대방
                    </th>
                    <th>
                        메시지
                    </th>
                    <th className="w-[150px]">
                        마지막 채팅 시간
                    </th>
                </tr>
            </thead>
            <tbody className="text-center">
                {chatRooms?.map((chatRoom, index) => <tr key={index}>
                    <td>
                        <div className="flex items-center" >
                            <img src={chatRoom?.acceptUsername_url ? chatRoom?.acceptUsername_url : '/base_profile.png'} className="w-[36px] h-[36px] mr-2" />
                            <label className="hover:underline cursor-pointer" onClick={() => location.href = "/account/chat/" + chatRoom?.acceptUsername}>{chatRoom?.acceptUsername}</label>
                        </div>
                    </td>
                    <td>{chatRoom?.type == 1 ? '<사진>' : chatRoom?.lastMessage}</td>
                    <td>{getDateTimeFormat(chatRoom?.modifyDate)}</td>
                </tr>)}
            </tbody>
        </table>
    </Profile>
}