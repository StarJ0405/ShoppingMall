'use client'
import { useEffect, useState } from 'react';
import Main from '@/app/Global/Layout/MainLayout';
import { checkWish, deleteWish, getUser, postWish } from '@/app/API/UserAPI';
import DropDown, { Direcion } from '@/app/Global/DropDown';
import { MonthDate } from '@/app/Global/Method';

interface pageProps {
    product: any;
    seller: any;
    topCategory: any;
    middleCategory: any;
}

export default function Page(props: pageProps) {
    const ACCESS_TOKEN = typeof window === 'undefined' ? null : localStorage.getItem('accessToken');
    const [user, setUser] = useState(null as any);
    const [middleCategoryDropdown, setMiddleCategoryDropdown] = useState(false);
    const [bottomCategoryDropdownm, setBottomCategoryDropdown] = useState(false);
    const [love, setLove] = useState(false);
    const [focus, setFocus] = useState(0);
    //const [product,setProduct] = useState(props.product);
    const product = props.product;
    const seller = props.seller;
    const topCategory = props.topCategory;
    const middleCategory = props.middleCategory;
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    checkWish(product.id)
                        .then(r => setLove(r))
                        .catch(e => console.log(e));
                    console.log(product, seller);
                })
                .catch(e => console.log(e));
    }, [ACCESS_TOKEN]);
    function Move(data: number) {
        setFocus(data);

    }
    function Wish() {
        if (love)
            deleteWish(product.id);
        else
            postWish(product.id);
        setLove(!love);
    }
    return <Main user={user} >
        <div className='flex flex-col w-[1240px] min-h-[670px]'>
            <div className='text-sm flex'>
                <label className='text-2xl font-bold'>{props.seller.nickname}</label>
                <label className='ml-2 text-gray-500'>긍정 리뷰</label>
                <label className='ml-2'>93.1%</label>
                <div className="divider divider-horizontal h-[20px] mx-0"></div>
                <label className='text-gray-500'>응답률 </label>
                <label className='ml-2'>100%</label>
            </div>
            <div className='mt-10 flex'>
                <div className='w-[880px] h-full'>
                    <div className='flex'>
                        <button id="middle" className='flex' onClick={() => setMiddleCategoryDropdown(!middleCategoryDropdown)}>{product.middleCategoryName}<img src={middleCategoryDropdown ? '/up.png' : '/down.png'} className='ml-1 w-[24px] h-[24px]' alt="dropdown" /></button>
                        <button id="bottom" className='flex ml-4' onClick={() => setBottomCategoryDropdown(!bottomCategoryDropdownm)}>{product.categoryName}<img src={bottomCategoryDropdownm ? '/up.png' : '/down.png'} className='ml-1 w-[24px] h-[24px]' alt="dropdown" /></button>
                        <DropDown open={middleCategoryDropdown} onClose={() => setMiddleCategoryDropdown(false)} background='main' button='middle' className='overflow-y-scroll bg-white' defaultDriection={Direcion.DOWN} width={140} height={180}>
                            <div className='max-w-[120px] max-h-[180px] flex flex-col'>
                                {(topCategory?.categoryResponseDTOList as any[])?.map((middle, index) => <a key={index} href={'/'} className={'' + (middle.name == product.middleCategoryName ? ' text-red-500' : '')}>{middle.name}</a>)}
                            </div>
                        </DropDown>
                        <DropDown open={bottomCategoryDropdownm} onClose={() => setBottomCategoryDropdown(false)} background='main' button='bottom' className='overflow-y-scroll bg-white' defaultDriection={Direcion.DOWN} width={200} height={180}>
                            <div className='max-w-[180px] max-h-[200px] flex flex-col'>
                                {(middleCategory?.categoryResponseDTOList as any[])?.map((bottom, index) => <a key={index} href={'/'} className={'' + (bottom.name == product.categoryName ? ' text-red-500' : '')}>{bottom.name}</a>)}
                            </div>
                        </DropDown>
                    </div>
                    <div className='flex mt-2'>
                        <div className='min-w-[410px] min-h-[410px] w-[410px] h-[410px] flex items-center justify-center'><img src={product?.url ? product.url : "/empty_product.png"} className='w-[300px] h-[300px]' /></div>
                        <div className='flex flex-col ml-2 w-full'>
                            <div>
                                <div className='rating rating-sm rating-half'>
                                    <input type='radio' readOnly className='rating-hidden' defaultChecked={!product?.score || (product?.score == 0 && product?.score < 0.25)} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.score > 0.25 && product?.score <= 0.75} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.score > 0.75 && product?.score <= 1.25} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.score > 1.25 && product?.score <= 1.75} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.score > 1.75 && product?.score <= 2.25} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.score > 2.25 && product?.score <= 2.75} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.score > 2.75 && product?.score <= 3.25} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.score > 3.25 && product?.score <= 3.75} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.score > 3.75 && product?.score <= 4.25} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={product?.score > 4.25 && product?.score <= 4.75} />
                                    <input type='radio' readOnly className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={product?.score > 4.75} />
                                </div>
                                <a href="/" className='ml-2'> {'37 리뷰보기 >'}</a>
                            </div>
                            <div className='mt-4 flex justify-between w-full'>
                                <label className='text-3xl'>{product?.title}</label>
                                <button className='min-w-[44px] min-h-[44px] w-[44px] h-[44px] border border-gray-500 rounded-full flex items-center justify-center' onClick={() => Wish()}><img src={love ? '/heart_on.png' : '/heart_off.png'} className='w-[24px] h-[24px]' /></button>
                            </div>
                            <label className='text-gray-500 text-lg'>원산지:상세설명 참조</label>
                            <label className='text-xl mt-2'><label className='text-2xl font-bold'>{product?.price.toLocaleString('ko-kr')}</label>원</label>
                            <div className='flex justify-between mt-auto'>
                                <label className='flex items-center'><div className='border border-black rounded-full w-[20px] h-[20px] flex items-center justify-center mr-2'>p</div>최대 적립 포인트</label>
                                <label className='font-bold'>{(product?.price / 100).toLocaleString("ko-kr", { maximumFractionDigits: 0 })}P</label>
                            </div>
                            <label className='mt-auto flex items-center'><div className='border border-black rounded-full w-[20px] h-[20px] flex items-center justify-center mr-2'>%</div>최대 22개월 무이자 할부</label>
                            <label className='mt-auto'>무료배송 | CJ대한통운</label>
                            <label className=''><label className='text-blue-400'>{MonthDate()}</label> 이내 도착 예정</label>
                        </div>
                    </div>
                    <div className='flex text-xl font-bold mt-4'>
                        <button className={'w-[220px] h-[60px]' + (focus == 0 ? ' text-white bg-red-500' : '')} onClick={() => Move(0)}>상품정보</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 1 ? ' text-white bg-red-500' : '')} onClick={() => Move(1)}>리뷰</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 2 ? ' text-white bg-red-500' : '')} onClick={() => Move(2)}>Q&A</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 3 ? ' text-white bg-red-500' : '')} onClick={() => Move(3)}>판매자정보<label className={'text-base font-normal' + (focus == 3 ? ' text-white' : ' text-gray-600')}>(반품/교환)</label></button>
                    </div>
                </div>
                <div className='w-[300px] h-full ml-[60px] relative'>
                    <div className='fixed'>
                        {

                        }
                    </div>
                </div>
            </div>
        </div>
    </Main>

}