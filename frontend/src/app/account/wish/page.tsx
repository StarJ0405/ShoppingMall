"use client"
import { deleteWishList, deleteWishListMultiple, getUser, getWishList } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

export default function Page() {
  const [user, setUser] = useState(null as any);
  const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
  const [widhList, setWishList] = useState(null as unknown as any[]);
  useEffect(() => {
    if (ACCESS_TOKEN)
      getUser()
        .then(r => {
          setUser(r);
          getWishList()
            .then(r => {
              setWishList(r);
            })
            .catch(e => console.log(e));
        })
        .catch(e => console.log(e));
    else
      redirect("/account/login");
  }, [ACCESS_TOKEN]);
  function Delete(id: number) {
    if (confirm("선택하신 찜한 상품을 삭제 하시겠습니까?"))
      deleteWishList(id)
        .then(r => {
          setWishList(r);
          document.getElementsByName('check').forEach((check: any) => check.checked =false);
        })
        .catch(e => console.log(e));
  }
  function DeleteAll() {
    if (confirm("선택하신 찜한 상품들을 삭제 하시겠습니까?")) {
      const numbers = [] as unknown as number[];
      const checks = document.getElementsByName('check');
      checks.forEach((check: any) => check.checked ? numbers.push(check.value) : null);
      deleteWishListMultiple(numbers)
        .then(r => {
          setWishList(r);
          checks.forEach((check: any) => check.checked =false);
        }).catch(e => console.log(e))
    }
  }
  function SelectAll(e: any) {
    document.getElementsByName('check').forEach((check: any) => check.checked = e.target.checked);
  }
  return <Profile user={user}>
    <label className="font-bold text-xl"><label className="text-red-500">찜한</label> 상품</label>
    <li className="list-disc text-xs">찜한 상품은 등록일로부터 <label className="font-bold">최대 1년간</label> 저장됩니다.</li>
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
        {widhList?.map((wish, index) =>
          <tr className="h-[64px] align-middle" key={index}>
            <td><input name="check" type="checkbox" value={wish.id} /></td>
            <td className="text-start px-2">
              <a href={'/product/' + wish.id} className="flex">
                <img src={wish.url ? wish.url : '/empty_product.png'} className="w-[60px] h-[60px]" alt="상품이미지" />
                <label className="px-2 w-full cursor-pointer">{wish?.title}</label>
              </a>
            </td>
            <td >{wish.price.toLocaleString('ko-KR') + '원'}</td>
            <td >?</td>
            <td className="text-xs">
              <button className="px-2 border border-black mb-1 btn btn-xs">장바구니</button>
              <button className="px-2 border border-black btn btn-xs" onClick={() => Delete(wish.id)}>삭제하기</button>
            </td>
          </tr>
        )}
      </tbody>
    </table>
    <div className="text-xs flex mt-2">
      <button className="border border-black mb-1 w-[145px] h-[18px] btn btn-xs mr-2">선택상품 장바구니 담기</button>
      <button className="border border-black w-[66px] h-[18px] btn btn-xs" onClick={() => DeleteAll()}>삭제하기</button>
    </div>
  </Profile>
}