'use client'
import { getUser, productRegist, saveImage } from '@/app/API/UserAPI';
import Main from '@/app/Global/Layout/MainLayout';
import dynamic from 'next/dynamic';
import { redirect } from 'next/navigation';
import { describe } from 'node:test';
import { useEffect, useState } from 'react';
import 'react-quill/dist/quill.snow.css';

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
    const [a_s, setAS] = useState('');
    const [brand, setBrand] = useState('');
    const [tags, setTags] = useState([] as string[]);
    const [detail, setDetail] = useState('');
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');

    useEffect(() => {

    }, []);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    setAS(r?.phoneNumber);
                    setBrand(r?.nickname);
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    function Regist() {
        // productRegist({ category: category?.id, price: price, description: simpleDescription, detail: detail, dateLimit: dateLimit, remain: remain, title: title, delivery: delivery, address: address, receipt: receipt, a_s: a_s, brand: brand, productTagList: tags, url:url })
        productRegist({ categoryId: 1, price: price, description: simpleDescription, detail: detail, dateLimit: dateLimit, remain: remain, title: title, delivery: delivery, address: address, receipt: receipt, a_s: a_s, brand: brand, productTagList: tags, url:url })
            .then(() => window.location.href='/')
            .catch(e => console.log(e))
        // console.log({ categoryId: 1, price: price, description: simpleDescription, detail: detail, dateLimit: dateLimit, remain: remain, title: title, delivery: delivery, address: address, receipt: receipt, a_s: a_s, brand: brand, productTagList: tags,url:url });
    }
    function Change(file: any) {
        const formData = new FormData();
        formData.append('file', file);
        saveImage(formData)
            .then(r => setUrl(r?.url))
            .catch(e => console.log(e))
    }
    function addTag() {
        const tag = document.getElementById('tag') as HTMLInputElement;
        if (tag?.value) {
            const value = tag.value.replaceAll(' ', '');
            if (!tags.includes(value)) {
                tags.push(value);
                setTags([...tags]);
            }
            tag.value = '';
        }
    }
    return <Main className='flex justify-center' user={user}>
        <div className='w-[1240px] min-h-[750px]'>
            <table>
                <tbody className='border border-black'>
                    <tr>
                        <th className='border border-black w-[150px]'>카테고리</th>
                        <td className='w-[1090px] px-2'>
                            <select defaultValue={0} autoFocus>
                                <option value={0} disabled>최상위 카테고리를 골라주세요.</option>
                            </select>
                            <select defaultValue={0} className='ml-2'>
                                <option value={0} disabled>상위 카테고리를 골라주세요.</option>
                            </select>
                            <select defaultValue={0} className='ml-2'>
                                <option value={0} disabled>하위 카테고리를 골라주세요.</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>배경 사진</th>
                        <td className='px-2 flex relative'>
                            <div className={'w-[256px] h-[256px] bg-black absolute z-[1] text-gray-400 flex items-center opacity-20 text-center ' + (isImageHover ? '' : 'hidden')} onMouseEnter={() => setIsImageHover(true)} onMouseLeave={() => setIsImageHover(false)} onClick={() => document.getElementById('file')?.click()}><label className='w-[256px]'>클릭시 이미지 변경</label></div>
                            <img src={url != '' ? url : '/white.png'} alt='main Image' className='w-[256px] h-[256px]' onMouseEnter={() => setIsImageHover(true)} onMouseLeave={() => setIsImageHover(false)} />
                            <input id='file' hidden type='file' onChange={e => Change(e.target.files?.[0])} />
                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>제목</th>
                        <td className='px-2'><input type='text' placeholder='제목..' defaultValue={title} onChange={e => setTitle(e.target.value)} maxLength={100} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>부가설명</th>
                        <td className='px-2'><input type='text' placeholder='간략내용..' defaultValue={simpleDescription} onChange={e => setSimpleDescription(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>판매 종료일</th>
                        <td className='px-2'><input type='datetime-local' placeholder='판매 기간' defaultValue={dateLimit} onChange={e => setDateLimit(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>판매 수량</th>
                        <td className='px-2'><input type='number' placeholder='남은 수량..' defaultValue={remain} onChange={e => setRemain(Number(e.target.value))} min={0} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>가격</th>
                        <td className='px-2'><input type='number' placeholder='가격' defaultValue={price} onChange={e => setPrice(Number(e.target.value))} min={0} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>배송방법</th>
                        <td className='px-2'><input type='text' placeholder='배송 방법..' defaultValue={delivery} onChange={e => setDelivery(e.target.value)} maxLength={100} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>배송가능지역</th>
                        <td className='px-2'><input type='text' placeholder='배송가능 지역 입력..' defaultValue={address} onChange={e => setAddress(e.target.value)} maxLength={100} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>영수증발행</th>
                        <td className='px-2'>
                            <textarea placeholder='영수발행 방법..' className='w-full' defaultValue={receipt} onChange={e => setReceipt(e.target.value)} maxLength={100}></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>A/S안내</th>
                        <td className='px-2'><input type='text' placeholder='A/S 관련 번호..' defaultValue={a_s} onChange={e => setAS(e.target.value)} maxLength={100} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>브랜드</th>
                        <td className='px-2'><input type='text' placeholder='브랜드..' defaultValue={brand} onChange={e => setBrand(e.target.value)} maxLength={50} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>태그</th>
                        <td className='px-2 flex flex-col'>
                            <div className='flex'>{tags?.map((tag, index) => <button className='btn btn-xs border border-black mr-1 mb-1' key={index} onClick={() => setTags(tags.filter((t) => t != tag))}>{tag}</button>)}</div>
                            <div className='flex'>
                                <input id="tag" type='text' placeholder='태그 작성..' onKeyDown={e => { if (e.key == 'Enter') document.getElementById('addTag')?.click() }} maxLength={50} />
                                <button id='addTag' className='btn btn-xs ml-2' onClick={() => addTag()}>태그 추가</button>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>상세설명</th>
                        <td className='px-2 flex pb-[50px]'>
                            <ReactQuill className='min-h-[300px] w-full' placeholder='상세 설명 작성..' onChange={(e: any) => setDetail(e)} />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className=''>
                <button className='btn btn-xs' onClick={() => Regist()}>상품 등록</button><button className='btn btn-xs'>취소</button>
            </div>
        </div>

    </Main>;
}