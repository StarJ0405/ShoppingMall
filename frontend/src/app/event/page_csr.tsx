"use client";

import { use, useEffect, useState } from "react";
import Main from "../Global/Layout/MainLayout";
import { getEventList, getMyProducts, getRecent, getUser, postEvent, updateEvent } from "../API/UserAPI";
import { redirect } from "next/navigation";
import { getDateTimeFormat, getDateTimeFormatInput } from "../Global/Method";
import Modal from "../Global/Modal";
import { getCategories } from "../API/NonUserAPI";

interface pageProps {
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [eventList, setEventList] = useState(null as unknown as any[]);
    const [isEventModal, setIsEventModal] = useState(false);
    const [selectEvent, setSelectEvent] = useState(null as any);
    const [productList, setProductList] = useState(null as unknown as any[]);
    const [startDate, setStartDate] = useState(null as any);
    const [endDate, setEndDate] = useState(null as any);
    const [selectedList, setSelectedList] = useState([] as number[]);
    const [discount, setDiscount] = useState(0);
    const [isModify, setIsModify] = useState(false);
    const [eventId, setEventId] = useState(-1);
    const [categories, setCategories] = useState(props.categories);

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
                    getEventList()
                        .then(r => setEventList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
        getCategories()
            .then(r => setCategories(r))
            .then(e => console.log(e));
    }, [ACCESS_TOKEN]);
    return <Main categories={categories} user={use} setRecentList={setRecentList} recentList={recentList}>
        <div className="w-[1240px] flex flex-col">
            <table className="mb-4">
                <thead>
                    <tr>
                        <th>이벤트ID</th>
                        <th>시작일</th>
                        <th>종료일</th>
                        <th>할인율</th>
                        <th>상품보기</th>
                        {/* <th>수정/삭제</th> */}
                        <th>수정</th>
                    </tr>
                </thead>
                <tbody>
                    {eventList?.map((event, index) => <tr key={index} className="text-center">
                        <td>{event?.eventId}</td>
                        <td>{getDateTimeFormat(event?.startDate)}</td>
                        <td>{getDateTimeFormat(event?.endDate)}</td>
                        <td>{event?.discount}%</td>
                        <td><button className="btn btn-xs" onClick={() => setSelectEvent(event)}>상품보기</button></td>
                        <td>
                            {/* <div className="flex justify-center">
                                <button className="btn btn-warning btn-xs text-white mr-1">수정</button>
                                <button className="btn btn-error btn-xs text-white">삭제</button>
                            </div> */}
                            <button className="btn btn-warning btn-xs text-white" onClick={() => {
                                setIsEventModal(true);
                                setStartDate(getDateTimeFormatInput(event?.startDate));
                                setEndDate(getDateTimeFormatInput(event?.endDate));
                                setDiscount(event?.discount);
                                setSelectedList((event?.productResponseDTOList as any[])?.map(product => product.id));
                                setIsModify(true);
                                setEventId(event.eventId);
                            }}>수정</button>
                        </td>
                    </tr>)}
                </tbody>
            </table>
            <button className="btn btn-info btn-sm w-[100px] self-end text-white mb-4" onClick={() => { setIsEventModal(true); setSelectedList([]); setStartDate(new Date()); setEndDate(new Date()); setDiscount(0); setIsModify(false); setEventId(-1); }}>이벤트 등록</button>
            <Modal open={isEventModal} onClose={() => setIsEventModal(false)} className="" escClose={true} outlineClose={true}>
                <div className="flex flex-col w-[744px] h-[752px]">
                    <div className="text-white bg-red-500 h-[37px] py-2 px-4 font-bold">이벤트 등록</div>
                    <div className="px-4 flex flex-col text-center">
                        <div className="flex mt-5">
                            <div className="w-[100px]">시작일</div>
                            <input className="input input-bordered input-sm" type="datetime-local" defaultValue={getDateTimeFormatInput(startDate)} onChange={e => {
                                let value = e.target.value;
                                if (value == '')
                                    return;
                                value = value.split(":")[0] + ":00"
                                e.target.value = value;
                                setStartDate(value)
                            }} max="9999-12-31T23:59" />
                        </div>
                        <div className="flex mt-5">
                            <div className="w-[100px]">종료일</div>
                            <input className="input input-bordered input-sm" type="datetime-local" defaultValue={getDateTimeFormatInput(endDate)} onChange={e => {
                                let value = e.target.value;
                                if (value == '')
                                    return;
                                console.log(value);
                                value = value.split(":")[0] + ":00"
                                e.target.value = value;
                                setEndDate(e.target.value);
                            }} max="9999-12-31T23:59" />
                        </div>
                        <div className="flex mt-5">
                            <div className="w-[100px]">할인율</div>
                            <input className="input input-bordered input-sm" min={0} max={100} defaultValue={discount} onChange={(e) => {
                                let value = Number(e.target.value);
                                if (value < 0)
                                    value = 0;
                                if (value > 100)
                                    value = 100;
                                e.target.value = value.toString();
                                setDiscount(value);
                            }} type="number" />
                        </div>
                        <label className="font-bold text-2xl">상품 목록</label>
                        <div className="flex mt-5 h-[400px]">
                            <div className="w-[100px] flex flex-col items-center">
                                전체선택
                                <input type="checkbox" className="mr-2" defaultChecked={selectedList?.length == productList?.length} onClick={(e) => {
                                    const value = (e.target as HTMLInputElement).checked;
                                    document.getElementsByName('check').forEach(check => {
                                        (check as HTMLInputElement).checked = value;
                                    })
                                    if (value) {
                                        setSelectedList(productList.map(product => product.id));
                                    } else
                                        setSelectedList([]);
                                }} />
                            </div>
                            <div className="w-[500px] h-[400px] overflow-y-scroll">
                                {!productList || productList?.length == 0 ? <div>
                                    <div className="flex items-center justify-center h-[300px] text-3xl font-bold">
                                        등록된 상품이 없습니다.
                                    </div>
                                </div> : <></>}
                                {productList?.map((product, index) =>
                                    <div key={index} className="flex items-center mb-2">
                                        <input name='check' type="checkbox" id={index.toString()} defaultChecked={selectedList.includes(product?.id)} className="mr-2" onClick={e => {
                                            if ((e.target as HTMLInputElement).checked) {
                                                selectedList.push(productList[index].id);
                                                setSelectedList([...selectedList]);
                                            } else
                                                setSelectedList([...selectedList.filter(product => product != productList[index].id)])
                                        }} />
                                        <img src={product?.url ? product?.url : '/empty_product.png'} className="w-[50px] h-[50px] mr-4" />
                                        <label>({product?.id}) {product?.title} </label>
                                    </div>)}
                            </div>
                        </div>
                        <div className="flex justify-center">
                            {isModify ?
                                <button className="btn btn-warning btn-sm mr-2 text-white" disabled={startDate == null || endDate == null || discount == 0 || selectedList.length == 0} onClick={() => {
                                    if (startDate == null || endDate == null || discount == 0 || selectedList.length == 0)
                                        return;
                                    updateEvent({ eventId: eventId, startDate: startDate, endDate: endDate, discount: discount, productIdList: selectedList }).then(r => setEventList(r)).catch(e => console.log(e)); setIsEventModal(false);
                                }}>수정</button>
                                :
                                <button className="btn btn-info btn-sm mr-2 text-white" disabled={startDate == null || endDate == null || discount == 0 || selectedList.length == 0} onClick={() => {
                                    if (startDate == null || endDate == null || discount == 0 || selectedList.length == 0)
                                        return;
                                    postEvent({ startDate: startDate, endDate: endDate, discount: discount, productIdList: selectedList }).then(r => setEventList(r)).catch(e => console.log(e)); setIsEventModal(false);
                                }}>등록</button>

                            }

                            <button className="btn btn-error btn-sm text-white" onClick={() => setIsEventModal(false)}>취소</button>
                        </div>
                    </div>
                </div>
            </Modal>
            <Modal open={selectEvent != null} onClose={() => setSelectEvent(null)} className="" escClose={true} outlineClose={true}>
                <div className="flex flex-col w-[744px] h-[452px]">
                    <div className="text-white bg-red-500 h-[37px] py-2 px-4 font-bold">등록된 상품 목록</div>
                    <div className="px-4 flex flex-col text-center overflow-y-scroll h-full">
                        {(selectEvent?.productResponseDTOList as any[])?.map((product, index) => <div key={index} className="flex text-center mt-2">
                            <div className="w-[100px]">
                                {product?.id}
                            </div>
                            <div className="w-[300px]">
                                <a className="hover:underline" href={"/product/" + product?.id}>{product?.title}</a>
                            </div>
                            <div className="w-[300px]">
                                {product?.description}
                            </div>
                        </div>)}
                    </div>
                </div>
            </Modal>
        </div>
    </Main>
}