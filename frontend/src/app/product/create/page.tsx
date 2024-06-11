'use client'
import { getUser, productRegist } from "@/app/API/UserAPI";
import Main from "@/app/Global/Layout/MainLayout";
import dynamic from "next/dynamic";
import { redirect } from "next/navigation";
import { describe } from "node:test";
import { useEffect, useState } from "react";
import "react-quill/dist/quill.snow.css";

const ReactQuill = dynamic(() => import('react-quill'), { ssr: false });

export default function Page() {
    const [user, setUser] = useState(null as any);
    const [categories, setCategories] = useState(null as unknown as any[]);
    const [isImageHover, setIsImageHover] = useState(false);

    const [category, setCategory] = useState(null as any);
    const [url, setUrl] = useState('');
    const [title, setTitle] = useState('');
    const [simpleDescription, setSimpleDescription] = useState('');
    const [dateLimit, setDateLimit] = useState(null as any);
    const [remain, setRemain] = useState(0);
    const [price, setPrice] = useState(0);
    const [delivery, setDelivery] = useState('택배');
    const [address, setAddress] = useState('전국(제주 도서산간지역 제외)');
    const [receipt, setReceipt] = useState('국내거주해외셀러 : 구매대행 수수료에 대한 현금영수증만 발행이 가능하며, 판매자에게 직접 발행 요청 필요(11번가 발행불가)\n해외거주해외셀러 : 온라인 현금영수증 발급 불가, 신용카드 전표 정보는 나의 11번가 PC참조');
    const [as, setAS] = useState(user?.phoneNumber);
    const [brand, setBrand] = useState(user?.nickname);
    const [tags, setTags] = useState(null as unknown as string[]);
    const [detail, setDetail] = useState('');
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');


    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                })
                .catch(e => console.log(e));
        else
            redirect("/account/login");
    }, [ACCESS_TOKEN]);
    function Regist(){
        productRegist({category:category?.id,price:price,description:simpleDescription,detail:detail,dateLimit:dateLimit,remain:remain,title:title,delivery:delivery,address:address,receipt:receipt,a_s:as,brand:brand,productTagList:tags})
        .then(()=>redirect('/'))
        .catch(e=>console.log(e))
    }
    return <Main className="flex justify-center" user={user}>
        <div className="w-[1240px] min-h-[750px]">
            <table>
                <tbody className="border border-black">
                    <tr>
                        <th className="border border-black w-[150px]">카테고리</th>
                        <td className="w-[1090px] px-2">
                            <select defaultValue={0} autoFocus>
                                <option value={0} disabled>최상위 카테고리를 골라주세요.</option>
                            </select>
                            <select defaultValue={0} className="ml-2">
                                <option value={0} disabled>상위 카테고리를 골라주세요.</option>
                            </select>
                            <select defaultValue={0} className="ml-2">
                                <option value={0} disabled>하위 카테고리를 골라주세요.</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th className="border border-black">배경 사진</th>
                        <td className="px-2 flex relative">
                            <div className={"w-[256px] h-[256px] bg-gray-300 absolute z-[1] text-gray-400 flex items-center text-center " + (isImageHover ? '' : 'hidden')} onMouseEnter={() => setIsImageHover(true)} onMouseLeave={() => setIsImageHover(false)} onClick={() => document.getElementById('file')?.click()}><label className="w-[256px]">클릭시 이미지 변경</label></div>
                            <img src={url != '' ? url : '/white.png'} alt="main Image" className="w-[256px] h-[256px]" onMouseEnter={() => setIsImageHover(true)} onMouseLeave={() => setIsImageHover(false)} />
                            <input id='file' hidden type="file" />
                        </td>
                    </tr>
                    <tr>
                        <th className="border border-black">제목</th>
                        <td className="px-2"><input type="text" placeholder="제목.." defaultValue={title} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">부가설명</th>
                        <td className="px-2"><input type="text" placeholder="간략내용.." defaultValue={simpleDescription} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">판매 종료일</th>
                        <td className="px-2"><input type="datetime-local" placeholder="판매 기간" defaultValue={dateLimit} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">판매 수량</th>
                        <td className="px-2"><input type="number" placeholder="남은 수량.." defaultValue={remain} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">가격</th>
                        <td className="px-2"><input type="number" placeholder="가격" defaultValue={price} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">배송방법</th>
                        <td className="px-2"><input type="text" placeholder="배송 방법.." defaultValue={'택배'} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">배송가능지역</th>
                        <td className="px-2"><input type="text" placeholder="배송가능 지역 입력.." defaultValue={'전국(제주 도서산간지역 제외)'} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">영수증발행</th>
                        <td className="px-2">
                            <textarea placeholder="영수발행 방법.." className="w-full" defaultValue={'국내거주해외셀러 : 구매대행 수수료에 대한 현금영수증만 발행이 가능하며, 판매자에게 직접 발행 요청 필요(11번가 발행불가)\n해외거주해외셀러 : 온라인 현금영수증 발급 불가, 신용카드 전표 정보는 나의 11번가 PC참조'}></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th className="border border-black">A/S안내</th>
                        <td className="px-2"><input type="text" placeholder="A/S 관련 번호.." defaultValue={user?.phoneNumber} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">브랜드</th>
                        <td className="px-2"><input type="text" placeholder="브랜드.." defaultValue={user?.nickname} /></td>
                    </tr>
                    <tr>
                        <th className="border border-black">태그</th>
                        <td className="px-2 flex flex-col">
                            <div className="flex">{tags?.map((tag,index)=><button key={index}>tag</button>)}</div>
                            <div className="flex">
                                <input type="text" placeholder="태그 작성.." onKeyDown={e=>{if(e.key=="Enter")document.getElementById('addTag')?.click()}}/>
                                <button id="addTag" className="btn btn-xs ml-2">태그 추가</button>
                            </div>
                            
                        </td>
                    </tr>
                    <tr>
                        <th className="border border-black">상세설명</th>
                        <td className="px-2 flex pb-[50px]">
                            <ReactQuill className="min-h-[300px] w-full" placeholder="상세 설명 작성.." />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className="">
                <button className="btn btn-xs" onClick={()=>Regist()}>상품 등록</button><button className="btn btn-xs">취소</button>
            </div>
        </div>

    </Main>;
}