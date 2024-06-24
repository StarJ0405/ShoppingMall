"use client";
import { getAddress, getRecent, getUser } from "@/app/API/UserAPI";
import Main from "@/app/Global/Layout/MainLayout";
import { PhoneNumberCheck } from "@/app/Global/Method";
import Modal from "@/app/Global/Modal";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

interface pageProps {
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const ORDER = typeof window == 'undefined' ? null : JSON.parse(localStorage.getItem('order') as string);
    const price = 0;
    const discountedPrice = 0;
    const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
    const [addresses, setAddresses] = useState(null as unknown as any[]);
    const [selectAddress, setSelectAddress] = useState(null as any);
    const [who, setWho] = useState('');
    const [phoneNumber, setPhoneNumber] = useState("");
    const [postNumber, setPostNumber] = useState(-1);
    const [address, setAddress] = useState('');
    const [detail, setDetail] = useState('');
    const [delivery, setDelivery] = useState('');
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    if (!ORDER) {
                        window.location.href = "/";
                        alert('구매 중인 상품 정보가 삭제되어 메인페이지로 이동합니다.');
                    }
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getAddress()
                        .then(r => setAddresses(r))
                        .catch(e => console.log(e))
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);

    return <Main categories={props.categories} recentList={recentList} setRecentList={setRecentList} user={user} >
        <div className='flex flex-col w-[1240px]'>
            <div className='divider'></div>
            <div className='flex justify-between'>
                <label className='text-3xl font-bold'>주문결제</label>
                <div className='flex px-2 items-center justify-center text-sm'>
                    <a href="/account/cart"><div className='w-[110px] h-[42px] flex items-center justify-center border border-gray-300 rounded-l-full'>
                        <label className='font-bold mr-1'>01</label>장바구니
                    </div></a>
                    <div className='w-[110px] h-[42px] text-white bg-gray-800 flex items-center justify-center font-bold'>02 주문서</div>
                    <div className='w-[110px] h-[42px] flex items-center justify-center border border-gray-300 rounded-r-full'>
                        <label className='font-bold mr-1'>03</label>주문완료
                    </div>
                </div>
            </div>
            <div className='divider'></div>
            <div className='flex'>
                <div className='w-[880px] flex-col'>
                    <div className="flex">
                        <label className="text-2xl font-bold mr-4">배송정보</label>
                        <button className="btn btn-sm" onClick={() => setIsAddressModalOpen(true)}>배송지목록</button>
                        <Modal open={isAddressModalOpen} onClose={() => setIsAddressModalOpen(false)} className="" escClose={true} outlineClose={true}>
                            <div className="flex flex-col w-[744px] h-[552px]">
                                <div className="text-white bg-red-500 h-[37px] py-2 px-4">나의 배송지 관리</div>
                                <div className="px-4 flex flex-col">
                                    <label className="font-bold text-sm mt-2">배송지 목록</label>
                                    <div className="divider"></div>
                                    <table className="text-xs text-center">
                                        <thead>
                                            <tr className="bg-gray-300">
                                                <th className="w-[110px] border-r border-y border-gray-400">별칭</th>
                                                <th className="w-[100px] border-r border-y border-gray-400">받는사람</th>
                                                <th className="w-[231px] border-r border-y border-gray-400">주소</th>
                                                <th className="w-[231px] border-r border-y border-gray-400">배송메시지</th>
                                                <th className="w-[40px] border-y border-gray-400">선택</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {addresses?.map((address, index) => <tr key={index}>
                                                <td className="border-r border-b border-gray-400">{address?.title}</td>
                                                <td className="border-r border-b border-gray-400">
                                                    <div className="flex flex-col">
                                                        <label>{address?.recipient}</label>
                                                        <label style={{ fontSize: '0.6rem' }}>{address?.phoneNumber}</label>
                                                    </div>
                                                </td>
                                                <td className="border-r border-b border-gray-400">
                                                    <div className="flex flex-col">
                                                        <label>({address.postNumber}){address?.mainAddress}</label>
                                                        <label>{address?.addressDetail}</label>
                                                    </div>
                                                </td>
                                                <td className="border-r border-b border-gray-400">{address?.deliveryMessage}</td>
                                                <td className="border-b border-gray-400">
                                                    <button className="btn btn-xs w-[38px] m-0 p-0" onClick={() => {
                                                        setSelectAddress(null);
                                                        const interval = setInterval(() => { setSelectAddress(address); clearInterval(interval) }, 100);
                                                        setIsAddressModalOpen(false);
                                                    }}>선택</button>
                                                </td>
                                            </tr>)}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </Modal>
                    </div>
                    <div className="flex flex-col">
                        <input className="input input-bordered w-[375px] mt-4" type="text" defaultValue={selectAddress?.recipient} placeholder="받는 사람" onChange={e => setWho(e.target.value)} />
                        <input className="input input-bordered w-[375px] mt-4" type="number" defaultValue={selectAddress?.postNumber} placeholder="우편번호" onChange={e => setPostNumber(Number(e.target.value))} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" defaultValue={selectAddress?.mainAddress} placeholder="기본 주소" onChange={e => setAddress(e.target.value)} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" defaultValue={selectAddress?.addressDetail} placeholder="상세 주소" onChange={e => setDetail(e.target.value)} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" defaultValue={selectAddress?.phoneNumber} placeholder="전화번호" onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')); setPhoneNumber(e.target.value); }} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" defaultValue={selectAddress?.phoneNumber} placeholder="배송메시지" onChange={e => setDelivery(e.target.value)} />
                        {/* <div><input type="checkbox" /><label>배송지 저장</label></div> */}
                    </div>
                </div>
                <div className='w-[300px] min-h-[750px] ml-[60px] relative'>
                    <div className='fixed flex flex-col w-[300px]'>
                        <label className='text-xl font-bold'>적립혜택</label>
                        <label className='text-lg mt-2'>적립 혜택이 없습니다.</label>
                        <div className='divider'></div>
                        <label className='text-xl font-bold'>결제 예정금액</label>
                        <div className='flex justify-between mt-2'>
                            <label>상품금액</label>
                            <label>{price?.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label>할인금액</label>
                            <label className='text-red-500'>{discountedPrice?.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </Main>;
}