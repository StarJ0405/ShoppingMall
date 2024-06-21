'use client'

import { deleteCartList, getCartList, getRecent, getUser, updateCartList } from '@/app/API/UserAPI';
import Main from '@/app/Global/Layout/MainLayout';
import { redirect } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function Page() {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [cartList, setCartList] = useState(null as unknown as any[]);
    const [price, setPrice] = useState(0);
    const [discountedPrice, setDisCountedPrice] = useState(0);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getCartList()
                        .then(r => setCartList(r))
                        // .then(r => { setCartList(r); console.log(r) })
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    function SelectAll(e: any) {
        let price = 0;
        let discountedPrice = 0;
        document.getElementsByName('check').forEach((check: any) => {
            check.checked = e.target.checked;
            if (e.target.checked) {
                const index = Number(e.target.id);
                setPrice(price + cartList[index]?.totalPrice)
                setDisCountedPrice(discountedPrice + 0);
            } else {
                setPrice(0);
                setDisCountedPrice(0);
            }
        });
    }
    function select(e: any, p: number, dp: number) {
        if (e.target.checked) {
            setPrice(price + p)
            setDisCountedPrice(discountedPrice + dp);
        } else {
            setPrice(price - p)
            setDisCountedPrice(discountedPrice - dp);
        }
    }


    return <Main recentList={recentList} setRecentList={setRecentList} user={user}>
        <div className='flex flex-col w-[1240px]'>
            <div className='divider'></div>
            <label className='text-3xl font-bold'>장바구니</label>
            <div className='divider'></div>
            <div className='flex'>
                <div className='w-[880px] flex-col'>
                    <table>
                        <thead>
                            <tr>
                                <th><input type="checkbox" className="w-[15px]" onChange={(e) => SelectAll(e)} /></th>
                                <th className='w-[500px]'>상품명</th>
                                <th className='w-[190px]'>가격</th>
                                <th className='w-[190px]'>배송정보</th>
                            </tr>
                        </thead>
                        <tbody className='text-center'>
                            {cartList?.map((cart, index) => <tr key={index} className='min-h-[104px]'>
                                <td><input name="check" type="checkbox" id={index.toString()} onChange={e => select(e, cart.totalPrice, 0)} /></td>
                                <td className='flex items-center'>
                                    <img src={cart?.productUrl ? cart.productUrl : '/empty_product.png'} className='w-[120px] h-[120px] mr-2' />
                                    <div className='flex flex-col items-start'>
                                        <a className='hover:underline' href={'/product/' + cart.productId}>{cart.productTitle}</a>
                                        {(cart?.cartItemDetailResponseDTOList as any[]).map((option, index) => <label key={index}>{option.optionName}</label>)}
                                        <input className='input input-info input-sm w-[124px]' type='number' defaultValue={cart.count} onChange={(e) => updateCartList(cart.cartItemId,Number(e.target.value)).then(r=>setCartList(r)).catch(error=>{
                                            if(error.response.status==403 && (error.response.data!="")){
                                                alert(error.response.data);
                                                e.target.value=cart.remain;                                                
                                            }
                                        })} min={1}/>
                                    </div>
                                </td>
                                <td>{cart?.totalPrice.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</td>
                                <td >
                                    <div className='flex'>
                                        <label className='w-[166px]'>무료배송</label>
                                        <button className='w-[24px]' onClick={() => { deleteCartList(cart.cartItemId).then(r => setCartList(r)).catch(e => console.log(e)) }}>X</button>
                                    </div>
                                </td>
                            </tr>)}
                        </tbody>
                    </table>
                </div>
                <div className='w-[300px] min-h-[750px] ml-[60px] relative'>
                    <div className='fixed flex flex-col w-[300px]'>
                        <label className='text-xl font-bold'>적립혜택</label>
                        <label className='text-lg mt-2'>적립 혜택이 없습니다.</label>
                        <div className='divider'></div>
                        <label className='text-xl font-bold'>결제 예정금액</label>
                        <div className='flex justify-between mt-2'>
                            <label>상품금액</label>
                            <label>{price.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label>할인금액</label>
                            <label className='text-red-500'>{discountedPrice.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                        </div>
                        <button className='btn btn-error text-white mt-5 text-lg'>주문하기</button>
                    </div>
                </div>
            </div>
        </div>
    </Main>
}