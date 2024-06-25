"use client";

import { use, useEffect, useState } from "react";
import Main from "../Global/Layout/MainLayout";
import { getMyProducts, getRecent, getUser } from "../API/UserAPI";
import { redirect } from "next/navigation";
import { getDateTimeFormat } from "../Global/Method";
import Modal from "../Global/Modal";

interface pageProps {
    categories: any[];
    eventList: any[];

}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [eventList, setEventList] = useState(props.eventList);
    const [isEventModal, setIsEventModal] = useState(false);
    const [productList, setProductList] = useState(null as unknown as any[]);
    const [selectedList, setSelectedList] = useState(null as unknown as any[]);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getMyProducts()
                        .then(r => setProductList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);

    return <Main categories={props.categories} user={use} setRecentList={setRecentList} recentList={recentList}>
        <div className="w-[1240px] flex flex-col">
            <table className="mb-4">
                <thead>
                    <tr>
                        <th>이벤트ID</th>
                        <th>시작일</th>
                        <th>종료일</th>
                        <th>할인율</th>
                        <th>상품보기</th>
                    </tr>
                </thead>
                <tbody>
                    {eventList?.map((event, index) => <tr key={index} className="text-center">
                        <td>{event?.eventId}</td>
                        <td>{getDateTimeFormat(event?.startDate)}</td>
                        <td>{getDateTimeFormat(event?.endDate)}</td>
                        <td>{event?.discount}%</td>
                        <td><button className="btn btn-xs">상품보기</button></td>
                    </tr>)}
                </tbody>
            </table>
            <button className="btn btn-info btn-sm w-[100px] self-end text-white mb-4" onClick={() => setIsEventModal(true)}>이벤트 등록</button>
            <Modal open={isEventModal} onClose={() => setIsEventModal(false)} className="" escClose={true} outlineClose={true}>
                <div className="flex flex-col w-[744px] h-[752px]">
                    <div className="text-white bg-red-500 h-[37px] py-2 px-4 font-bold">이벤트 등록</div>
                    <div className="px-4 flex flex-col text-center">
                        <div className="flex mt-5">
                            <div className="w-[100px]">시작일</div>
                            <input className="" type="datetime-local" />
                        </div>
                        <div className="flex mt-5">
                            <div className="w-[100px]">종료일</div>
                            <input className="" type="datetime-local" />
                        </div>
                        <div className="flex mt-5">
                            <div className="w-[100px]">할인율</div>
                            <input className="" type="number" />
                        </div>
                        <label className="font-bold text-2xl">상품 목록</label>
                        <div className="flex mt-5 h-[500px]">

                            <div className="w-[100px] flex flex-col items-center">
                                전체선택
                                <input type="checkbox" className="mr-2" onClick={(e) => {
                                    document.getElementsByName('check').forEach(check => {
                                        const value = (e.target as HTMLInputElement).checked;
                                        (check as HTMLInputElement).checked = value;
                                        if (value) {

                                        } else {

                                        }
                                    })
                                }} />
                            </div>
                            <div className="w-[500px] h-[500px] overflow-y-scroll">
                                {!productList || productList?.length == 0 ? <div>
                                    <div className="flex items-center justify-center h-[300px] text-3xl font-bold">
                                        등록된 상품이 없습니다.
                                    </div>
                                </div> : <></>}
                                {productList?.map((product, index) =>
                                    <div key={index} className="flex items-center mb-2">
                                        <input name='check' type="checkbox" id={index.toString()} className="mr-2" />
                                        <img src={product?.url ? product?.url : '/empty_product.png'} className="w-[50px] h-[50px] mr-4" />
                                        <label>({product?.id}) {product?.title} </label>
                                    </div>)}
                            </div>
                        </div>
                    </div>
                </div>
            </Modal>
        </div>
    </Main>
}