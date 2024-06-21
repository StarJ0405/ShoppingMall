"use client"

import { getRecent, getUser, postCartList, postWish } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

export default function Page() {
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
            redirect("/account/login");
    }, [ACCESS_TOKEN]);
    function SelectAll(e: any) {
        document.getElementsByName('check').forEach((check: any) => check.checked = e.target.checked);
    }
    function addCart(id: number) {
        if (confirm("선택하신 찜한 상품을 장바구니에 추가하시겠습니까?"))
          postCartList({ productId: id, optionIdList: [], count: 1 })
            .then(r => window.location.href = "/account/cart")
            .catch(e=>alert(e.response.data))
      }
      function addCartList() {
        if (confirm("선택하신 찜한 상품들을 장바구니에 추가하시겠습니까?"))
          document.getElementsByName('check').forEach((check: any) => check.checked ?
            (postCartList({ productId: check.value, optionIdList: [], count: 1 })
              .then(r => window.location.href = "/account/cart")
              .catch(e=>alert(e.response.data))
            )
            : null);
      }
    return <Profile user={user} recentList={recentList} setRecentList={setRecentList}>
        <label className="font-bold text-xl"><label className="text-red-500">찜한</label> 상품</label>
        <li className="list-disc text-xs">최근 상품은 등록일로부터 <label className="font-bold">최대 1년간</label> 저장됩니다.</li>
        <table>
            <thead className="text-center">
                <tr>
                    <th><input type="checkbox" className="w-[15px]" onChange={(e) => SelectAll(e)} /></th>
                    <th className="w-[690px]">상품명</th>
                    <th className="w-[110px]">가격</th>
                    <th className="w-[110px]">만족도</th>
                    <th className="w-[110px]">주문</th>
                </tr>
            </thead>
            <tbody className="text-center">
                {recentList?.map((recent, index) =>
                    <tr className="h-[64px] align-middle" key={index}>
                        <td><input name="check" type="checkbox" value={recent.id} /></td>
                        <td className="text-start px-2">
                            <a href={'/product/' + recent.id} className="flex">
                                <img src={recent.url ? recent.url : '/empty_product.png'} className="w-[60px] h-[60px]" alt="상품이미지" />
                                <label className="px-2 w-full cursor-pointer">{recent?.title}</label>
                            </a>
                        </td>
                        <td >{recent?.price?.toLocaleString('ko-KR') + '원'}</td>
                        <td >{recent?.price?.grade}</td>
                        <td className="text-xs">
                            <button className="px-2 border border-black mb-1 btn btn-xs" onClick={() => addCart(recent?.productId)}>장바구니</button>
                            <button className="px-2 border border-black btn btn-xs" onClick={() => { postWish(recent?.productId).catch(e => console.log(e))}}>찜하기</button>
                        </td>
                    </tr>
                )}
            </tbody>
        </table>
        <button className="mt-2 text-xs border border-black mb-1 w-[145px] h-[18px] btn btn-xs mr-2" onClick={() => addCartList()}>선택상품 장바구니 담기</button>

    </Profile>;
}