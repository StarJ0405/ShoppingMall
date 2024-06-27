"use client";

import { getCategories, getWho } from "@/app/API/NonUserAPI";
import { Subscribe, getSocket } from "@/app/API/SocketAPI";
import { getChatList, getChatRoom, getRecent, getUser, saveChatImage } from "@/app/API/UserAPI";
import Main from "@/app/Global/Layout/MainLayout";
import { getDateTimeFormat } from "@/app/Global/Method";
import Modal from "@/app/Global/Modal";
import { redirect } from "next/navigation";
import { use, useEffect, useState } from "react";

interface pageProps {
    target: string;
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [categories, setCategories] = useState(props.categories);
    const [socket, setSocket] = useState(null as any);
    const [isReady, setIsReady] = useState(false);
    const [target, setTarget] = useState(null as any);
    const [chatroom, setChatroom] = useState(null as any);
    const [chatList, setChatList] = useState([] as any[]);
    const [selectedUrl, setSelectedUrl] = useState('');
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getCategories().then(r => setCategories(r)).catch(e => console.log(e));
                    getWho(props.target)
                        .then(r => {
                            setTarget(r);
                            if (user?.username == r?.username)
                                redirect('/');
                        })
                        .catch(e => console.log(e));
                    getChatRoom(props.target)
                        .then(r => {
                            setChatroom(r);
                            const subs = [] as Subscribe[];
                            subs.push({
                                location: "/api/sub/chat/" + r.id, active: () => getChatList(r.id).then(r => {
                                    setChatList(r);
                                    const timer = setInterval(() => {
                                        document.getElementById((r?.length - 1).toString())?.scrollIntoView();
                                        clearInterval(timer);
                                    }, 100);
                                }).catch(e => console.log(e))
                            });
                            setSocket(getSocket(subs, () => setIsReady(true)));
                            getChatList(r.id).then(r => {
                                setChatList(r);
                                const timer = setInterval(() => {
                                    document.getElementById((r?.length - 1).toString())?.scrollIntoView();
                                    clearInterval(timer);
                                }, 100);
                            }).catch(e => console.log(e));
                        })
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    return <Main user={user} categories={categories} recentList={recentList} setRecentList={setRecentList} >
        <div className="flex flex-col w-[1240px]">
            <div className="flex flex-col w-full h-[500px] bg-gray-300 p-4 overflow-y-scroll">
                {chatList?.length == 0 ? <label className="self-center text-2xl my-auto font-bold">문의하실 내용을 입력해주세요.</label> : <></>}
                {chatList?.map((chat, index) => {
                    if (chat.sender == user?.username)
                        return <div id={index.toString()} key={index} className="self-end py-2 px-4 flex items-center justify-end">
                            <div className="flex flex-col border border-gray-500 rounded-l-full py-4 pr-4 pl-8 break-words text-start">
                                {chat?.type == 0 ?
                                    <div className="self-start max-w-[600px]">{chat?.message}</div>
                                    :
                                    <img src={chat?.message} onClick={() => setSelectedUrl(chat?.message)} className="w-[64px] h-[64px] self-center cursor-pointer mb-2" />
                                }
                                <label className="self-start text-xs">{getDateTimeFormat(chat?.createDate)}</label>
                            </div>
                            <div className="flex flex-col items-center ml-2">
                                <img src={user?.url ? user?.url : '/base_profile.png'} className="w-[36px] h-[36px]" />
                                <label>{user?.nickname}</label>
                            </div>
                        </div>
                    else
                        return <div id={index.toString()} key={index} className="self-start py-2 px-4 flex items-center">
                            <div className="flex flex-col items-center mr-2">
                                <img src={target?.url ? target?.url : '/base_profile.png'} className="w-[36px] h-[36px]" />
                                <label>{target?.nickname}</label>
                            </div>
                            <div className="flex flex-col border border-gray-500 rounded-r-full py-4 pr-4 pl-8 break-words text-end">
                                {chat?.type == 0 ?
                                    <div className="self-end max-w-[600px]">{chat?.message}</div>
                                    :
                                    <img src={chat?.message} onClick={() => setSelectedUrl(chat?.message)} className="w-[64px] h-[64px] self-center cursor-pointer mb-2" />
                                }
                                <label className="self-end text-xs">{getDateTimeFormat(chat?.createDate)}</label>
                            </div>
                        </div>
                })}
                <Modal open={selectedUrl != ''} onClose={() => setSelectedUrl('')} className="bg-opacity-0" escClose={true} outlineClose={true}>
                    <img src={selectedUrl} onClick={() => setSelectedUrl('')} className="cursor-pointer" />
                </Modal>
            </div>
            <div className="w-[1225px] py-2 px-4 border border-gray-500 rounded-lg flex">
                <input id="text" type="text" className="outline-none w-full" placeholder="채팅.." autoFocus onKeyDown={e => {
                    if (e.key == "Enter" && isReady) document.getElementById('submit')?.click();
                }} />
                <button id="submit" className="w-[75px]" onClick={() => {
                    const text = document.getElementById('text') as HTMLInputElement;
                    if ((text.value as string) == "" || text.value == null)
                        return;
                    socket.publish({ destination: "/api/pub/chat/" + chatroom?.id, body: JSON.stringify({ sender: user?.username, message: text.value, type: 0 }) });
                    socket.publish({ destination: "/api/pub/alarm/" + target?.username, body: JSON.stringify({ sender: user?.username, message: text.value, url: "/account/chat/" + user?.username }) });
                    text.value = '';
                }}>보내기</button>
                <img src="/image.png" className="w-[24px] h-[24px] cursor-pointer ml-2" onClick={() => document.getElementById('file')?.click()} />
                <input id="file" type="file" hidden onChange={(e) => {
                    const formData = new FormData();
                    formData.append('file', e.target.files?.[0] as any);
                    formData.append('roomId', chatroom?.id);
                    saveChatImage(formData).then(r => {
                        socket.publish({ destination: "/api/pub/chat/" + chatroom?.id, body: JSON.stringify({ sender: user?.username, message: r, type: 1 }) });
                        socket.publish({ destination: "/api/pub/alarm/" + target?.username, body: JSON.stringify({ sender: user?.username, message: "사진", url: "/account/chat/" + user?.username }) });
                    }).catch(e => console.log(e));
                }} />
            </div>
        </div>
    </Main>
}