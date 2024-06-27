"use client";
import { getCategories } from "@/app/API/NonUserAPI";
import { getAddress, getRecent, getUser, postPayment } from "@/app/API/UserAPI";
import Main from "@/app/Global/Layout/MainLayout";
import { MonthDate, PhoneNumberCheck } from "@/app/Global/Method";
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
    const ORDER = (typeof window == 'undefined' ? [] : JSON.parse(localStorage.getItem('order') as string)) as any[];
    const [price, setPrice] = useState(0);
    const [discountedPrice, setDiscountedPrice] = useState(0);
    const [isAddressModalOpen, setIsAddressModalOpen] = useState(false);
    const [addresses, setAddresses] = useState(null as unknown as any[]);
    const [who, setWho] = useState('');
    const [phoneNumber, setPhoneNumber] = useState("");
    const [postNumber, setPostNumber] = useState(-1);
    const [address, setAddress] = useState('');
    const [detail, setDetail] = useState('');
    const [delivery, setDelivery] = useState('');
    const [mounted, setMounted] = useState(false);
    const [point, setPoint] = useState(0);
    const [categories, setCategories] = useState(props.categories);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    if (!ORDER) {
                        window.location.href = "/";
                        alert('구매 중인 상품 정보가 삭제되어 메인페이지로 이동합니다.');
                    } else {
                        let discountedPrice = 0;
                        let price = 0;
                        (ORDER as any[]).forEach(order => {
                            discountedPrice += getDiscountPrice(order);
                            price += getPrice(order);
                        });
                        setPrice(price);
                        setDiscountedPrice(discountedPrice);
                    }
                    setMounted(true);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getAddress()
                        .then(r => setAddresses(r))
                        .catch(e => console.log(e))
                    getCategories()
                        .then(r => setCategories(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    function pay() {
        if (who == '' || phoneNumber == '' || address == '' || detail == '' || postNumber == -1 || delivery == '')
            return;
        let list = [] as number[];
        ORDER?.forEach(order => list.push(order.cartItemId));
        postPayment({ cartItemIdList: list, recipient: who, phoneNumber: phoneNumber, mainAddress: address, addressDetail: detail, postNumber: postNumber.toString().padStart(5, '0'), deliveryMessage: delivery, point: point }).then(() => window.location.href = "/account/log").catch(e => console.log(e));
    }
    function getPrice(order: any) {
        let price = order?.productPrice;
        (order?.cartItemDetailResponseDTOList as any[]).forEach(option => {
            price += option.optionPrice;
        });
        return price * order.count;
    }
    function getDiscountPrice(order: any) {
        let price = order?.productPrice * (100 - order.discount) / 100;
        (order?.cartItemDetailResponseDTOList as any[]).forEach(option => {
            price += option.optionPrice;
        });
        return price * order.count;
    }
    function getMaxPoint() {
        return user?.point > discountedPrice ? discountedPrice : user?.point;
    }
    return <Main categories={categories} recentList={recentList} setRecentList={setRecentList} user={user} >
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
                                    <div className="divider divider-neutral"></div>
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
                                                    <button className="btn btn-outline btn-xs w-[38px] m-0 p-0" onClick={() => {
                                                        setWho(address.recipient);
                                                        (document.getElementById('who') as HTMLInputElement).value = address.recipient;
                                                        setPostNumber(address.postNumber);
                                                        (document.getElementById('postNumber') as HTMLInputElement).value = address.postNumber;
                                                        setAddress(address.mainAddress);
                                                        (document.getElementById('address') as HTMLInputElement).value = address.mainAddress;
                                                        setDetail(address.addressDetail);
                                                        (document.getElementById('detail') as HTMLInputElement).value = address.addressDetail;
                                                        setPhoneNumber(address.phoneNumber);
                                                        (document.getElementById('phoneNumber') as HTMLInputElement).value = address.phoneNumber;
                                                        setDelivery(address.deliveryMessage);
                                                        (document.getElementById('delivery') as HTMLInputElement).value = address.deliveryMessage;

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
                        <input className="input input-bordered w-[375px] mt-4" type="text" id="who" placeholder="받는 사람" onChange={e => setWho(e.target.value)} />
                        <input className="input input-bordered w-[375px] mt-4" type="number" id="postNumber" placeholder="우편번호" onChange={e => setPostNumber(Number(e.target.value))} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" id="address" placeholder="기본 주소" onChange={e => setAddress(e.target.value)} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" id="detail" placeholder="상세 주소" onChange={e => setDetail(e.target.value)} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" id="phoneNumber" placeholder="전화번호" onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')); setPhoneNumber(e.target.value); }} />
                        <input className="input input-bordered w-[642px] mt-4" type="text" id="delivery" placeholder="배송메시지" onChange={e => setDelivery(e.target.value)} />
                        {/* <div><input type="checkbox" /><label>배송지 저장</label></div> */}
                    </div>
                    <div className="flex mt-20 items-end">
                        <label className="text-2xl font-bold mr-4">주문 상품</label>
                        <label className="text-sm">상품수량 및 옵션변경은 상품상세 또는 장바구니에서 가능합니다.</label>
                    </div>
                    <div className="divider"></div>
                    <div className="flex flex-col">
                        {mounted && ORDER?.map((order, index) =>
                            <div key={index} className="flex mt-4">
                                <img src={order?.imageUrl ? order?.imageUrl : '/empty_product.png'} className="w-[120px] h-[120px]" alt="order" />
                                <div className="flex flex-col w-[376px] ml-2 justify-between pb-4">
                                    <a href={"/product/" + order?.productId} className="text-xl hover:underline">{order?.productTitle}</a>
                                    {(order.cartItemDetailResponseDTOList as any[])?.map((option, index) => <label key={index}>{option?.optionName}</label>)

                                    }
                                    <label><label className="font-bold text-blue-500">{MonthDate()}</label> 도착</label>
                                </div>
                                <label className="w-[90px] text-center">{order?.count}개</label>
                                {order?.discount > 0 ?
                                    <div className="flex flex-col w-[155px] text-center">
                                        <label><label className="text-lg text font-bold">{getDiscountPrice(order).toLocaleString('ko-kr')}</label>원</label>
                                        <label className="line-through text-gray-500 text-sm">{getPrice(order).toLocaleString('ko-kr')}원</label>
                                    </div>
                                    :
                                    <label className="w-[155px]"><label >{(order?.productPrice * order?.count).toLocaleString('ko-kr')}</label>원</label>
                                }
                                <label className="w-[129px] text-center">무료배송</label>
                            </div>
                        )}
                    </div>
                    <div className="text-2xl font-bold mt-20">포인트</div>
                    <div className="divider"></div>
                    <div className="flex items-center mb-16">
                        <label className="text-lg font-bold">포인트</label>
                        <div className="flex border border-black ml-4 items-center px-2 ">
                            <input type="number" className="input input-sm" defaultValue={0} min={0} max={getMaxPoint()} onChange={e => { let value = Number(e.target.value); if (value < 0) value = 0; else if (value > getMaxPoint()) value = getMaxPoint(); e.target.value = value?.toString(); setPoint(value); }} />
                            <label>원</label>
                        </div>
                        <label className="ml-4">사용가능<label className="text-red-500 font-bold ml-2">{(user?.point ? user?.point : 0).toLocaleString('ko-kr')}P</label></label>
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
                            <label><label className='font-bold text-lg'>{price.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label>할인금액</label>
                            <label className='text-red-500'><label className='font-bold text-lg'>{(discountedPrice - price - point).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label className='text-red-500'>합계</label>
                            <label className='text-red-500'><label className='font-bold text-2xl'>{(discountedPrice - point).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <button className='btn btn-error text-white mt-5 text-lg' onClick={pay}>결제하기</button>
                    </div>
                </div>
            </div>
        </div>
    </Main>;
}