"use client";

import { getMyProducts, getRecent, getUser } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import {getDateTimeFormat } from "@/app/Global/Method";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

interface pageProps {
    categories: any[]
}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [productList, setProductList] = useState(null as unknown as any[]);
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

    return <Profile categories={props.categories} user={user} recentList={recentList} setRecentList={setRecentList}>
        <div className='flex items-end'>
            <label className='text-xl font-bold'>내 <label className='text-xl text-red-500 font-bold'>상품</label>목록</label>
            <label className='text-xs h-[14px] border-l-2 border-gray-400 ml-2 mb-[5px] pl-2'>고객님의 상품으로 들어가서 상품을 수정하실 수 있습니다.</label>
        </div>
        <table>
            <thead>
                <tr>
                    <th className="w-[600px]">상품명</th>
                    <th className="w-[200px]">기본가격</th>
                    <th className="w-[100px]">수량</th>
                    <th className="w-[150px]">판매기한</th>
                </tr>
            </thead>
            <tbody>
                {productList?.map((product, index) =>
                    <tr key={index} className="text-center">
                        <td>
                            <div className="flex">
                                <img src={product?.imageUrl ? product?.imageUrl : '/empty_product.png'} className="w-[24px] h-[24px]" />
                                <div className="flex flex-col px-2 text-start">
                                    <a href={'/product/' + product?.id} className="hover:underline">{product?.title ? product?.title : '제목 없음'}</a>
                                </div>
                            </div>
                        </td>
                        <td >
                            <label className="font-bold">{(product?.price)?.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원
                        </td>
                        <td>
                            <label className="font-bold">{product?.remain.toLocaleString('ko-kr')}</label> 개
                        </td>
                        <td>
                            <label className="font-bold">{getDateTimeFormat(product?.dateLimit)}</label>
                        </td>
                    </tr>)}
            </tbody>
        </table>
    </Profile>;
}