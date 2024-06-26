"use client";

import { deleteImageList, getPayment, getRecent, getUser, postReview, putReview, saveImageList } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { getDate, getDateTime } from "@/app/Global/Method";
import Modal from "@/app/Global/Modal";
import QuillNoSSRWrapper from "@/app/Global/QuillNoSSRWrapper";
import { redirect } from "next/navigation";
import { useEffect, useMemo, useRef, useState } from "react";
import 'react-quill/dist/quill.snow.css';
import ReactQuill from "react-quill";
import { getCategories } from "@/app/API/NonUserAPI";

interface pageProps {
    categories: any[];
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [payments, setPlayments] = useState(null as unknown as any[]);
    const [selectPayment, setSelectPayment] = useState(null as any);
    const [review, setReview] = useState(null as any);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [grade, setGrade] = useState(0);
    const [categories, setCategories] = useState(props.categories);
    const quillInstance = useRef<ReactQuill>(null);
    const imageHandler = () => {
        const input = document.createElement('input') as HTMLInputElement;
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();


        input.addEventListener('change', async () => {
            const file = input.files?.[0];

            try {
                const formData = new FormData();
                formData.append('file', file as any);
                const imgUrl = (await saveImageList(formData)).url;
                const editor = (quillInstance?.current as any).getEditor();
                const range = editor.getSelection();
                editor.insertEmbed(range.index, 'image', imgUrl);
                editor.setSelection(range.index + 1);
            } catch (error) {
                console.log(error);
            }
        });
    };
    const modules = useMemo(
        () => ({
            toolbar: {
                container: [
                    [{ header: '1' }, { header: '2' }],
                    [{ size: [] }],
                    ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                    [{ list: 'ordered' }, { list: 'bullet' }, { align: [] }],
                    ['image'],
                ],
                handlers: { image: imageHandler },
            },
            clipboard: {
                matchVisual: false,
            },
        }),
        [],
    );

    const formats = [
        'header',
        'font',
        'size',
        'bold',
        'italic',
        'underline',
        'strike',
        'blockquote',
        'list',
        'bullet',
        'align',
        'image',
    ];
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getPayment()
                        .then(r => setPlayments(r))
                        .catch(e => console.log(e));
                    getCategories().then(r => setCategories(r)).catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    // function getPrice(product: any) {
    //     let price = product?.productPrice;
    //     (product?.paymentProductDetailResponseDTOList as any[]).forEach(option => {
    //         price += option.optionPrice;
    //     });
    //     return price * product.count;
    // }
    function getDiscountPrice(product: any) {
        let price = product?.productPrice * (100 - product?.discount) / 100;
        (product?.paymentProductDetailResponseDTOList as any[])?.forEach(option => {
            price += option.optionPrice;
        });
        return price * product?.count;
    }
    function openPayment(payment: any) {
        setSelectPayment(payment)
        setReview(null);
    }
    function submit() {
        if (title == '' || content == '')
            return;
        postReview({ paymentProductId: review.paymentProductId, productId: review.productId, title: title, content: content, grade: grade }).then(r => { setReview(null); setPlayments(r) }).catch(e => console.log(e));
    }
    function modify() {
        if (title == '' || content == '')
            return;
        putReview({ reviewId: review?.reviewResponseDTO?.id, paymentProductId: review.paymentProductId, title: title, content: content, grade: grade }).then(r => { setReview(null); setPlayments(r) }).catch(e => console.log(e));
    }
    return <Profile categories={categories} recentList={recentList} setRecentList={setRecentList} user={user}>
        <label className="font-bold text-2xl"><label className="text-red-500">주문/배송</label>조회</label>
        <table>
            <thead>
                <tr>
                    <th className="w-[140px]">주문일자</th>
                    <th>상품정보</th>
                    <th className="w-[120px]">상품수량</th>
                    <th className="w-[120px]">주문상태</th>
                </tr>
            </thead>
            <tbody>
                {payments?.map((payment, index) => <tr key={index}>
                    <td className="text-center">
                        <div className="flex flex-col">
                            <label>{getDate(payment?.paymentDate)}</label>
                            <label className="text-xs">({getDateTime(payment?.paymentDate)})</label>
                        </div>
                    </td>
                    <td>
                        <div className="flex">
                            <img className="w-[80px] h-[80px] mr-2" src={payment.paymentProductResponseDTOList[0]?.imageUrl ? payment.paymentProductResponseDTOList[0].imageUrl : '/empty_product.png'} />
                            <label className="hover:underline cursor-pointer" onClick={() => openPayment(payment)}>{payment.paymentProductResponseDTOList[0]?.title}{payment.paymentProductResponseDTOList.length > 1 ? ' ... 등 ' : ''}</label>
                        </div>
                    </td>
                    <td className="text-center">
                        <label>{payment.paymentProductResponseDTOList.length} 품목</label>
                    </td>
                    <td className="text-center">
                        {payment.paymentStatus}
                    </td>
                </tr>)}
            </tbody>
        </table>
        <div className="flex justify-center font-bold text-2xl mt-8">
            {payments?.length == 0 ? <label>구매 내역이 없습니다.</label> : <></>}
        </div>
        <Modal open={selectPayment != null} onClose={() => setSelectPayment(null)} outlineClose={true} escClose={true} className="">
            <div className="flex flex-col w-[744px] h-[752px]">
                <div className="text-white bg-red-500 h-[37px] py-2 px-4">구매 상세 기록</div>
                <div className="px-4 flex flex-col">
                    <label className="font-bold text-sm mt-2">배송 정보</label>
                    <div className="divider divider-neutral my-2"></div>
                    <table>
                        <tbody>
                            <tr>
                                <th className="w-[150px]">받는사람</th>
                                <td><label>{selectPayment?.recipient}</label></td>
                            </tr>
                            <tr>
                                <th>받는사람 번호</th>
                                <td><label>{selectPayment?.phoneNumber}</label></td>
                            </tr>
                            <tr>
                                <th>우편번호</th>
                                <td><label>{selectPayment?.postNumber}</label></td>
                            </tr>
                            <tr>
                                <th>기본주소</th>
                                <td><label>{selectPayment?.mainAddress}</label></td>
                            </tr>
                            <tr>
                                <th>상세주소</th>
                                <td><label>{selectPayment?.addressDetail}</label></td>
                            </tr>
                            <tr>
                                <th>상세주소</th>
                                <td><label>{selectPayment?.addressDetail}</label></td>
                            </tr>
                            <tr>
                                <th>배송메시지</th>
                                <td><label>{selectPayment?.deliveryMessage}</label></td>
                            </tr>
                            <tr>
                                <th>결제금액</th>
                                <td><label className="text-red-500 font-bold">{selectPayment?.totalPrice.toLocaleString('ko-kr')}</label> 원</td>
                            </tr>
                            <tr>
                                <th>사용포인트</th>
                                <td><label className="text-red-500 font-bold">{Number(selectPayment?.usedPoint).toLocaleString('ko-kr')}</label> P</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="px-4 flex flex-col mt-8">
                    <label className="font-bold text-sm mt-2">상품 목록</label>
                    <div className="divider divider-neutral my-2"></div>
                    <div className="text-sm text-center">
                        <div className="flex">
                            <div className="w-[450px]">상품명</div>
                            <div className="w-[145px]">가격</div>
                            <div className="w-[50px]">수량</div>
                            <div className="w-[50px]">리뷰</div>
                        </div>
                        <div className="overflow-y-scroll h-[290px]">
                            {(selectPayment?.paymentProductResponseDTOList as any[])?.map((product, index) => <div key={index} className="flex mb-4">
                                <div className="flex w-[450px]">
                                    <img src={product?.imageUrl ? product?.imageUrl : '/empty_product.png'} className="w-[24px] h-[24px]" />
                                    <div className="flex flex-col px-2 text-start">
                                        <a href={'/product/' + product?.productId} className="hover:underline">{product?.title ? product?.title : '제목 없음'}</a>
                                        {(product?.paymentProductDetailResponseDTOList as any[]).map((option, index) =>
                                            <label className="text-xs" key={index}>
                                                {option.optionListName} : {option.optionName} (<label className="font-bold">{option.optionPrice.toLocaleString('ko-kr')}</label>원)
                                            </label>)}
                                    </div>
                                </div>
                                <div className="w-[145px]">
                                    <label className="font-bold">{getDiscountPrice(product).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원
                                </div>
                                <div className="w-[50px]">
                                    <label className="font-bold">{product.count.toLocaleString('ko-kr')}</label> 개
                                </div>
                                <div className="w-[50px]">
                                    <button className="btn btn-xs" onClick={() => {
                                        setReview(product); setSelectPayment(null); deleteImageList();
                                        if (product?.reviewStatus && product?.reviewResponseDTO) {
                                            setTitle(product?.reviewResponseDTO?.title);
                                            setContent(product?.reviewResponseDTO?.content);
                                            setGrade(product?.reviewResponseDTO?.grade);
                                        } else {
                                            setTitle('');
                                            setContent('');
                                            setGrade(0);
                                        }
                                    }}>리뷰</button>
                                </div>
                            </div>)}
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
        <Modal open={review != null} onClose={() => setReview(null)} className="" escClose={true} outlineClose={true}>
            <div className="flex flex-col w-[744px] h-[752px]">
                <div className="text-white bg-red-500 h-[37px] py-2 px-4">리뷰</div>
                <div className="px-4 flex flex-col">
                    <label className="font-bold text-sm mt-2">상품 정보</label>
                    <div className="divider divider-neutral my-2"></div>
                    <table>
                        <thead>
                            <tr>
                                <th className="w-[450px]">상품명</th>
                                <th className="w-[145px]">가격</th>
                                <th className="w-[50px]">수량</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="text-center">
                                <td>
                                    <div className="flex">
                                        <img src={review?.imageUrl ? review?.imageUrl : '/empty_product.png'} className="w-[24px] h-[24px]" />
                                        <div className="flex flex-col px-2 text-start">
                                            <a href={'/product/' + review?.productId} className="hover:underline">{review?.title}</a>
                                            {(review?.paymentProductDetailResponseDTOList as any[])?.map((option, index) =>
                                                <label className="text-xs" key={index}>
                                                    {option.optionListName} : {option.optionName} (<label className="font-bold">{option.optionPrice.toLocaleString('ko-kr')}</label>원)
                                                </label>)}
                                        </div>
                                    </div>
                                </td>
                                <td >
                                    <label className="font-bold">{getDiscountPrice(review).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원
                                </td>
                                <td>
                                    <label className="font-bold">{review?.count}</label> 개
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div className="px-4 flex flex-col mt-8">
                    <label className="font-bold text-sm mt-2" autoFocus>리뷰 작성</label>
                    <div className="divider divider-neutral my-2"></div>
                    <input id="title" type="text" className="input input-bordered mb-4" placeholder="리뷰 제목.." defaultValue={title} onChange={e => setTitle(e.target.value)} />
                    <QuillNoSSRWrapper
                        forwardedRef={quillInstance}
                        defaultValue={content}
                        onChange={(e: any) => setContent(e)}
                        modules={modules}
                        theme="snow"
                        className='w-full h-[300px]'
                        placeholder="리뷰 내용을 입력해주세요."
                    />
                    <div className="flex justify-between mt-14 items-center">
                        <div className='rating rating-lg rating-half'>
                            <input name="grade" type='radio' className='rating-hidden' onClick={() => setGrade(0)} defaultChecked={grade == 0} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' onClick={() => setGrade(0.5)} defaultChecked={grade == 0.5} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' onClick={() => setGrade(1)} defaultChecked={grade == 1} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' onClick={() => setGrade(1.5)} defaultChecked={grade == 1.5} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' onClick={() => setGrade(2)} defaultChecked={grade == 2} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' onClick={() => setGrade(2.5)} defaultChecked={grade == 2.5} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' onClick={() => setGrade(3)} defaultChecked={grade == 3} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' onClick={() => setGrade(3.5)} defaultChecked={grade == 3.5} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' onClick={() => setGrade(4)} defaultChecked={grade == 4} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' onClick={() => setGrade(4.5)} defaultChecked={grade == 4.5} />
                            <input name="grade" type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' onClick={() => setGrade(5)} defaultChecked={grade == 5} />
                        </div>
                        {review?.reviewStatus ?
                            <button className="btn btn-xs btn-info" onClick={modify}>수정하기</button>
                            :
                            <button className="btn btn-xs btn-info" onClick={submit}>작성하기</button>
                        }

                    </div>
                </div>
            </div>
        </Modal>
    </Profile>;
}