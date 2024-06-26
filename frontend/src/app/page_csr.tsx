'use client'
import { useEffect, useState } from 'react';
import Main from './Global/Layout/MainLayout';
import { getRecent, getUser } from './API/UserAPI';
import { MonthDate } from './Global/Method';
import { getCategories, getProductRecentList } from './API/NonUserAPI';

interface pageProps {
    productList: any;
    categories: any[];
}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const [productList, setProductList] = useState(props.productList.content);
    const [maxPage, setMaxPage] = useState(props.productList.totalPages);
    const ACCESS_TOKEN = typeof window === 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [page, setPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [categories, setCategories] = useState(props.categories);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getProductRecentList(0).then(r => {setProductList(r.content); setMaxPage(r.totalPages)}).catch(e => console.log(e));
                    getCategories().then(r => setCategories(r)).catch(e => console.log(e));
                })
                .catch(e => console.log(e));
    }, [ACCESS_TOKEN]);
    useEffect(() => {
        const loadPage = () => {
            const scrollLocation = document.documentElement.scrollTop;
            const windowHeight = window.innerHeight;
            const fullHeight = document.body.scrollHeight;

            if (!isLoading && scrollLocation + windowHeight >= fullHeight && page < maxPage - 1) {
                setIsLoading(true);
                getProductRecentList(page + 1)
                    .then(response => {
                        if (response.size > 0) {
                            const newlist = [...productList, ...response.content];
                            setProductList(newlist);
                            setMaxPage(response.totalPages);
                            setPage(page + 1);
                        }
                        setIsLoading(false);
                    })
                    .catch(error => {
                        console.log(error);
                        setIsLoading(false);
                    });
            }
        };
        window.addEventListener('scroll', loadPage);
        return () => window.removeEventListener('scroll', loadPage);
    }, [page]);
    return <Main user={user} recentList={recentList} setRecentList={setRecentList} categories={categories}>
        <div className='w-full h-full flex justify-center'>
            <div className='flex flex-wrap w-[1240px]'>
                {(productList as any[]).map((product, index) =>
                    <a href={'/product/' + product.id} key={index} className='mr-4'>
                        <div className='w-[394px] h-[431px] flex flex-col p-4 hover:border border-gray-500'>
                            <img src={product?.url ? product.url : '/empty_product.png'} className='w-[190px] h-[190px]' />
                            <label className='text-lg mt-2'>{product?.title ? product?.title : '제목 없음'}</label>
                            {/* <span className='text-xl mt-2'><label className='text-red-500 text-2xl'>9% </label> <label className='font-bold text-2xl'>10,400원</label>~ <label className='text-gray-300 line-through'>11,550원</label></span> */}
                            <label className='text-xl mt-2 font-bold text-2xl'>{product?.price.toLocaleString('ko-KR')}원</label>
                            <div className='mt-2 flex'>
                                <div className='rating rating-xs rating-half'>
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
                                <label className='text-xs self-center ml-2'>{product?.reviewSize.toLocaleString("ko-kr")}</label></div>
                            <label className='mt-1 text-sm'>포인트 최대 <label className='text-blue-400'>{(product?.price / 100).toLocaleString("ko-kr", { maximumFractionDigits: 0 })}P</label> 적립</label>
                            <div className='text-sm flex justify-between w-full mt-auto'>
                                <label>무료배송 <label className='text-blue-400'>{MonthDate()} 도착</label></label>
                                <label>{product?.remain.toLocaleString('ko-KR')}개 남음</label>
                            </div>
                        </div>
                    </a>
                )}
                {/* <a href='/'>
                        <div className='w-[394px] h-[431px] flex flex-col p-4 hover:border border-gray-500'>
                            <img src='/empty_product.png' className='w-[190px] h-[190px]' />
                            <label className='text-lg mt-2'>제목</label>
                            <span className='text-xl mt-2'><label className='text-red-500 text-2xl'>9%</label> <label className='font-bold text-2xl'>10,400원</label>~ <label className='text-gray-300 line-through'>11,550원</label></span>
                            <div className='mt-4'>
                                <div className='rating rating-xs'>
                                    <input type='radio' name='rating-5' className='mask mask-star-2 bg-orange-400' />
                                    <input type='radio' name='rating-5' className='mask mask-star-2 bg-orange-400' checked />
                                    <input type='radio' name='rating-5' className='mask mask-star-2 bg-orange-400' />
                                    <input type='radio' name='rating-5' className='mask mask-star-2 bg-orange-400' />
                                    <input type='radio' name='rating-5' className='mask mask-star-2 bg-orange-400' />
                                </div>
                                <label className='text-xs'> 6,123</label></div>
                            <label className='mt-1 text-sm'>포인트 최대 <label className='text-blue-400'>150P</label> 적립</label>
                            <div className='text-sm flex justify-between w-full mt-auto'>
                                <label>무료배송 <label className='text-blue-400'>6/15(토) 도착</label></label>
                                <label>1,523개 남음</label>
                            </div>
                        </div>
                    </a> */}
                {isLoading ? <div className='w-full flex justify-center'> <img src="/loading.png" style={{ width: 50 + 'px', height: 50 + 'px' }} /> </div> : null}
            </div>
        </div>
    </Main>

}