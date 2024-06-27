"use client";

import Main from "../Global/Layout/MainLayout";
import { getRecent, getUser } from "../API/UserAPI";
import { useEffect, useState } from "react";
import { MonthDate } from "../Global/Method";
import DropDown, { Direcion } from "../Global/DropDown";
import { getCategories } from "../API/NonUserAPI";

interface pageProps {
    categories: any[];
    search: any;
    keyword: string;
    page: number;
    sort: number;
    CategoryId: number;
    topCategory: any;
    secondCategory: any;
    bottomCategory: any;
}
export default function Page(props: pageProps) {
    const [topCategory, setTopCategory] = useState(props.topCategory);
    const [secondCategory, setSecondCategory] = useState(props.secondCategory);
    const [selectSecondCategory, setSelectSecondCategory] = useState(props.secondCategory);
    const [bottomCategory, setBottomCategory] = useState(props.bottomCategory);
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const search = props.search;
    const [openSort, setOpenSort] = useState(false);
    const [categories, setCategories] = useState(props.categories);
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
        getCategories().then(r => {
            setCategories(r);
            (r as any[]).forEach(top =>
                (top.categoryResponseDTOList as any[])?.forEach(second =>
                    (second.categoryResponseDTOList as any[])?.forEach(bottom => {
                        if (bottom.id == props.CategoryId) {
                            setTopCategory(top);
                            setSecondCategory(second);
                            setBottomCategory(bottom);
                        };
                    })
                )
            );

        }).catch(e => console.log(e));
    }, [ACCESS_TOKEN]);
    function Pages() {
        const start = props.page - (props.page % 10);
        const value = [] as number[];
        for (let i = start; i < start + 10; i++) {
            if (i == search.totalPages)
                break;
            else
                value.push(i);
        }
        return value.map((v, index) => <button className="btn btn-xs btn-outline" key={index} disabled={v == props.page} onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + v + "&sort=" + props.sort}>{v + 1}</button>);
    }
    const sortName = ['최근 등록순', '높은 가격순', '낮은 가격순', '리뷰 많은순']
    return <Main categories={categories} user={user} recentList={recentList} setRecentList={setRecentList}>
        <div className="flex flex-col w-[1240px]">
            <div className="flex justify-between">
                <label>검색 결과 {search.totalElements.toLocaleString('ko-kr')}건</label>
                <button id="sort" className="self-end font-bold btn btn-sm" onClick={() => setOpenSort(!openSort)}>{sortName[props.sort]}</button>
                <DropDown open={openSort} onClose={() => setOpenSort(false)} background="main" button="sort" className="bg-white mt-[0.5px]" defaultDriection={Direcion.DOWN} height={100} width={100}>
                    <div className="flex flex-col">
                        <label className="hover:underline cursor-pointer" onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + 0 + "&sort=" + 0}>최근 등록순</label>
                        <label className="hover:underline cursor-pointer" onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + 0 + "&sort=" + 1}>높은 가격순</label>
                        <label className="hover:underline cursor-pointer" onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + 0 + "&sort=" + 2}>낮은 가격순</label>
                        <label className="hover:underline cursor-pointer" onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + 0 + "&sort=" + 3}>리뷰 많은순</label>
                    </div>
                </DropDown>
            </div>
            <label className="text-2xl font-bold">{topCategory?.name}</label>
            <div className="p-2 flex font-bold">
                <div className="flex flex-col h-[200px] w-full">
                    <label className="text-lg font-bold bg-gray-700 text-white p-2">중간 카테고리</label>
                    <div className="flex flex-col overflow-y-scroll">
                        {(topCategory?.categoryResponseDTOList as any[]).map((second, index) => <label key={index} className={"p-2 cursor-pointer" + (second.id == secondCategory.id ? ' text-red-500' : '') + (second?.id == selectSecondCategory?.id ? ' bg-orange-300' : '')} onClick={() => setSelectSecondCategory(second)}>
                            {second.name}
                        </label>)}
                    </div>
                </div>
                <div className="flex flex-col h-[200px] w-full">
                    <label className="text-lg font-bold bg-gray-700 text-white p-2">하위 카테고리</label>
                    <div className="flex flex-col overflow-y-scroll">
                        {(selectSecondCategory?.categoryResponseDTOList as any[]).map((bottom, index) => <label key={index} className={"p-2 cursor-pointer" + (bottom.id == bottomCategory.id ? ' text-red-500' : '')} onClick={() => location.href = '/category?CategoryId=' + bottom.id}>
                            {bottom.name}
                        </label>)}
                    </div>
                </div>
            </div>
            <div className='flex items-center border-2 border-gray-300 rounded-full px-5 ml-4 my-5 w-[350px] h-[48px]'>
                <input type="text" id="category_keyword" defaultValue={props.keyword} placeholder="카테고리 내 검색" className="bg-transparent w-[300px] outline-none" onKeyDown={e => { if (e.key == 'Enter') document.getElementById('category_search')?.click() }} />
                <button id='category_search' onClick={() => { const value = (document.getElementById('category_keyword') as HTMLInputElement)?.value; location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + (value ? value : '') + "&sort=" + props.sort }}>
                    <img src='/search.png' className='w-[24px] h-[24px]' />
                </button>
            </div>
            {search?.content?.length > 0 ? (search.content as any[])?.map((product, index) => <div key={index} className="flex group cursor-pointer mb-8" onClick={() => location.href = "/product/" + product.id}>
                <img src={product?.url ? product?.url : '/empty_product.png'} className="w-[120px] h-[120px]" />
                <div className="ml-4 flex flex-col w-[680px]">
                    <div>
                        <label className={"text-xl cursor-pointer" + (product?.remain > 0 ? ' group-hover:underline' : ' line-through')}>{product?.title ? product?.title : '제목 없음'}</label>
                        {product?.remain > 0 ? <></> : <label className="text-red-500 text-xs">품절</label>}</div>
                    <div className="flex">
                        <label className="text-lg text-red-500 mr-1 cursor-pointer">{(product?.discount).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label>
                        <label className="cursor-pointer"><label className="text-xl font-bold cursor-pointer">{product?.price.toLocaleString('ko-kr')}</label>원</label>
                    </div>
                    <div className="flex items-center">
                        <div className='rating rating-sm rating-half'>
                            <input type='radio' className='rating-hidden' defaultChecked={!product?.grade || (product?.grade == 0 && product?.grade < 0.25)} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.grade > 0.25 && product?.grade <= 0.75} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.grade > 0.75 && product?.grade <= 1.25} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.grade > 1.25 && product?.grade <= 1.75} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.grade > 1.75 && product?.grade <= 2.25} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.grade > 2.25 && product?.grade <= 2.75} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.grade > 2.75 && product?.grade <= 3.25} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.grade > 3.25 && product?.grade <= 3.75} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.grade > 3.75 && product?.grade <= 4.25} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.grade > 4.25 && product?.grade <= 4.75} onClick={e => e.preventDefault()} />
                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.grade > 4.75} onClick={e => e.preventDefault()} />
                        </div>
                        <label className="ml-2 mt-1 text-sm text-gray-500">({product?.reviewSize})</label>
                    </div>
                </div>
                <div className="flex flex-col w-[400px] ml-[40px] justify-between">
                    <label>{product?.authorUsername}</label>
                    <div className="flex flex-col text-sm">
                        <label>무료배송</label>
                        <label className="text-blue-500 font-bold">{MonthDate()} 도착</label>
                    </div>
                </div>
            </div>)
                :
                <div className="m-auto text-2xl text-gray-600 mb-16"><label className="font-bold text-black">{props.keyword != '' ? props.keyword : ""}</label>{props.keyword != '' ? "의 " : ""}검색 결과가 없습니다</div>
            }
        </div>
        <div className="flex">
            <button className="btn btn-xs btn-outline" disabled={props.page < 10} onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + (props.page - props.page % 10 - 10) + "&sort=" + props.sort}>
                {'<'}
            </button>

            <Pages />

            <button className="btn btn-xs btn-outline" disabled={search.totalPages - props.page < 10} onClick={() => window.location.href = '/category?CategoryId=' + props.CategoryId + "&keyword=" + props.keyword + "&page=" + (props.page - props.page % 10 + 10) + "&sort=" + props.sort}>
                {'>'}
            </button>
        </div>
    </Main>;
}