'use client'

import { deleteCartList, getCartList, getRecent, getUser, updateCartList } from '@/app/API/UserAPI';
import Main from '@/app/Global/Layout/MainLayout';
import { redirect } from 'next/navigation';
import { useEffect, useState } from 'react';
import { json } from 'stream/consumers';
interface pageProps {
    categories: any[]
}
export default function Page(props: pageProps) {
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
                    localStorage.removeItem('order');
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
                const index = Number(check.id);
                price += getPrice(cartList[index]);
                discountedPrice += getDiscountPrice(cartList[index]);
            }
        });
        setPrice(price);
        setDisCountedPrice(discountedPrice);
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
    function order() {
        const order = [] as any[];
        document.getElementsByName('check').forEach((check: any) => {
            if (check.checked) {
                const index = Number(check.id);
                order.push(cartList[index]);
            }
        });
        if (order.length > 0) {
            localStorage.setItem('order', JSON.stringify(order));
            window.location.href = '/account/order';
        }
    }
    function getPrice(cart: any) {
        let price = cart?.productPrice;
        (cart?.cartItemDetailResponseDTOList as any[]).forEach(option => {
            price += option.optionPrice;
        });
        return price * cart.count;
    }
    function getDiscountPrice(cart: any) {
        let price = cart?.productPrice * (100 - cart.discount) / 100;
        (cart?.cartItemDetailResponseDTOList as any[]).forEach(option => {
            price += option.optionPrice;
        });
        return price * cart.count;
    }
    return <Main recentList={recentList} setRecentList={setRecentList} user={user} categories={props.categories}>
        <div className='flex flex-col w-[1240px]'>
            <div className='divider'></div>
            <div className='flex justify-between'>
                <label className='text-3xl font-bold'>장바구니</label>
                <div className='flex px-2 items-center justify-center text-sm'>
                    <div className='w-[110px] h-[42px] text-white bg-gray-800 flex items-center justify-center font-bold rounded-l-full'>01 장바구니</div>
                    <div className='w-[110px] h-[42px] flex items-center justify-center border border-gray-300'>
                        <label className='font-bold mr-1'>02</label>주문서
                    </div>
                    <div className='w-[110px] h-[42px] flex items-center justify-center border border-gray-300 rounded-r-full'>
                        <label className='font-bold mr-1'>03</label>주문완료
                    </div>
                </div>
            </div>
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
                                <td><input name="check" type="checkbox" id={index.toString()} onChange={e => select(e, getPrice(cart), getDiscountPrice(cart))} /></td>
                                <td className='flex items-center'>
                                    <img src={cart?.imageUrl ? cart.imageUrl : '/empty_product.png'} className='w-[120px] h-[120px] mr-2' />
                                    <div className='flex flex-col items-start'>
                                        <a className='hover:underline' href={'/product/' + cart.productId}>{cart.productTitle}</a>
                                        {(cart?.cartItemDetailResponseDTOList as any[]).map((option, index) => <label className='text-xs' key={index}>
                                            {option.optionName} ( <label className='font-bold'>{option.optionPrice.toLocaleString('ko-kr')}</label>원)
                                            </label>)}
                                        <input className='input input-info input-sm w-[124px] mt-2' type='number' defaultValue={cart.count} onChange={(e) => updateCartList(cart.cartItemId, Number(e.target.value)).then(r => setCartList(r)).catch(error => {
                                            if (error.response.status == 403 && (error.response.data != "")) {
                                                alert(error.response.data);
                                                e.target.value = cart.remain;
                                            }
                                        })} min={1} />
                                    </div>
                                </td>
                                <td>
                                    {cart.discount > 0 ?
                                        <div className='flex flex-col'>
                                            <label><label className='text-lg font-bold'>{getDiscountPrice(cart).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                                            <label className='text-gray-500 line-through text-sm'>{getPrice(cart).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                                        </div>
                                        :
                                        <label><label className='text-lg font-bold'>{getPrice(cart).toLocaleString('ko-kr', { maximumFractionDigits: 0 })
                                        }</label>원</label>
                                    }
                                </td>
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
                            <label><label className='font-bold text-lg'>{price.toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label>할인금액</label>
                            <label className='text-red-500'><label className='font-bold text-lg'>{(discountedPrice - price).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <div className='flex justify-between mt-2'>
                            <label className='text-red-500'>합계</label>
                            <label className='text-red-500'><label className='font-bold text-2xl'>{(discountedPrice).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                        </div>
                        <button className='btn btn-error text-white mt-5 text-lg' onClick={order}>주문하기</button>
                    </div>
                </div>
            </div>
        </div>
    </Main>
}