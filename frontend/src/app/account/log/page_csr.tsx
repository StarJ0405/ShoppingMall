"use client";

import { getPayment, getRecent, getUser } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { getDate, getDateTime } from "@/app/Global/Method";
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
    const [payments, setPlayments] = useState(null as unknown as any[]);
    const [selectPayment, setSelectPayment] = useState(null as any);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getPayment()
                        .then(r => setPlayments(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    console.log(payments);
    return <Profile categories={props.categories} recentList={recentList} setRecentList={setRecentList} user={user}>
        <label className="font-bold text-2xl"><label className="text-red-500">주문/배송</label>조회</label>
        <table>
            <thead>
                <tr>
                    <th className="w-[140px]">주문일자</th>
                    <th>상품정보</th>
                    <th className="w-[120px]">상품수량</th>
                    <th className="w-[120px]">주문상태</th>
                </tr>
            </thead>
            <tbody>
                {payments?.map((payment, index) => <tr key={index}>
                    <td className="text-center">
                        <div className="flex flex-col">
                            <label>{getDate(payment?.paymentDate)}</label>
                            <label className="text-xs">({getDateTime(payment?.paymentDate)})</label>
                        </div>
                    </td>
                    <td>
                        <div className="flex">
                            <img className="w-[80px] h-[80px] mr-2" src={payment.paymentProductResponseDTOList[0].imageUrl ? payment.paymentProductResponseDTOList[0].imageUrl : '/empty_product.png'} />
                            <label className="hover:underline cursor-pointer" onClick={() => setSelectPayment(payment)}>{payment.paymentProductResponseDTOList[0].title}{payment.paymentProductResponseDTOList.length > 1 ? ' ... 등 ' : ''}</label>
                        </div>
                    </td>
                    <td className="text-center">
                        <label>{payment.paymentProductResponseDTOList.length} 품목</label>
                    </td>
                    <td className="text-center">
                        {payment.paymentStatus}
                    </td>
                </tr>)}
            </tbody>
        </table>
        <Modal open={selectPayment != null} onClose={() => setSelectPayment(null)} outlineClose={true} escClose={true} className="">
            <div className="flex flex-col w-[744px] h-[552px]">
                <div className="text-white bg-red-500 h-[37px] py-2 px-4">구매 상세 기록</div>
                <div className="px-4 flex flex-col">
                    <label className="font-bold text-sm mt-2">배송 정보</label>
                    <div className="divider divider-neutral my-2"></div>
                    <table>
                        <tr>
                            <th className="w-[150px]">받는사람</th>
                            <td><label>{selectPayment?.recipient}</label></td>
                        </tr>
                        <tr>
                            <th>받는사람 번호</th>
                            <td><label>{selectPayment?.phoneNumber}</label></td>
                        </tr>
                        <tr>
                            <th>우편번호</th>
                            <td><label>{selectPayment?.postNumber}</label></td>
                        </tr>
                        <tr>
                            <th>기본주소</th>
                            <td><label>{selectPayment?.mainAddress}</label></td>
                        </tr>
                        <tr>
                            <th>상세주소</th>
                            <td><label>{selectPayment?.addressDetail}</label></td>
                        </tr>
                        <tr>
                            <th>상세주소</th>
                            <td><label>{selectPayment?.addressDetail}</label></td>
                        </tr>
                        <tr>
                            <th>배송메시지</th>
                            <td><label>{selectPayment?.deliveryMessage}</label></td>
                        </tr>
                        <tr>
                            <th>결제금액</th>
                            <td><label className="text-red-500 font-bold">{selectPayment?.totalPrice.toLocaleString('ko-kr')}</label> 원</td>
                        </tr>
                        <tr>
                            <th>사용포인트</th>
                            {/* <td><label className="text-red-500 font-bold">{selectPayment?.point.toLocaleString('ko-kr')}</label>P</td> */}
                            <td><label className="text-red-500 font-bold">{Number(0).toLocaleString('ko-kr')}</label> P</td>
                        </tr>
                    </table>
                </div>
                <div className="px-4 flex flex-col mt-8 ">
                    <label className="font-bold text-sm mt-2">상품 목록</label>
                    <div className="divider divider-neutral my-2"></div>
                    <div className="text-xs text-center overflow-y-scroll">
                        <div className="flex">
                            <div className="w-[500px]">상품명</div>
                            <div>가격</div>
                        </div>
                        <div>
                            {(selectPayment?.paymentProductResponseDTOList as any[])?.map((product, index) => <div key={index}>
                                <div className="flex w-[500px]">
                                    <img src={product?.imageUrl ? product?.imageUrl : '/empty_product.png'} className="w-[24px] h-[24px]" />
                                    <div className="flex flex-col">
                                        <label>{product?.title}</label>
                                        {(product?.paymentProductDetailResponseDTOList as any[]).map((option, index) => <label key={index}>
                                            {option.optionName}
                                        </label>)}
                                    </div>
                                </div>
                            </div>)}
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
    </Profile>;
}