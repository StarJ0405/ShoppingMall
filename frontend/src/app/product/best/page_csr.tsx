'use client'
import { useEffect, useState } from 'react';
import Main from '@/app/Global/Layout/MainLayout';
import { getRecent, getUser } from '@/app/API/UserAPI';

interface pageProps {
    bestList: any[];
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window === 'undefined' ? null : localStorage.getItem('accessToken');
    const bestList = props.bestList;
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
    }, [ACCESS_TOKEN]);

    return <Main user={user} recentList={recentList} setRecentList={setRecentList} categories={props.categories}>
        <div className='w-full h-full flex justify-center'>
            <div className='flex flex-wrap w-[1240px]'>
                {bestList.map((product, index) =>
                    <a href={'/product/' + product.id} key={index} className='mb-5 mr-8 relative'>
                        <label className='absolute text-white bg-gray-700 w-[38px] h-[28px] flex items-center justify-center border-solid border-r-4'>{index + 1}</label>
                        <div className='w-[275px] h-[450px] flex flex-col p-4 hover:border border-gray-500'>
                            <img src={product?.url ? product.url : '/empty_product.png'} className='w-[275px] h-[275px]' />
                            <label className='mt-5 hover:underline'>{product?.title}</label>
                            <div className='flex mt-2 items-center'>
                                <label className='text-red-500 font-bold text-xl'><label className='text-3xl'>22</label>%</label>
                                <div className='flex flex-col justify-center ml-4'>
                                    <label className='line-through text-gray-500 text-sm'>{Number(product?.price).toLocaleString('ko-kr')}원</label>
                                    <label className='font-bold text-lg'>{Number(product?.price * 0.78).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                                </div>
                            </div>
                            <label className='text-gray-500 text-sm mt-5'>무료배송</label>
                        </div>
                    </a>
                )}
            </div>
        </div>
    </Main>;
}