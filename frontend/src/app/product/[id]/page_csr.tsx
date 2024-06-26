'use client'
import { useEffect, useMemo, useRef, useState } from 'react';
import Main from '@/app/Global/Layout/MainLayout';
import { checkWish, deleteWish, getUser, postAnswer, postCartList, postQuestion, postRecent, postWish } from '@/app/API/UserAPI';
import DropDown, { Direcion } from '@/app/Global/DropDown';
import { MonthDate, PhoneString, getDateTime, getDateTimeFormat } from '@/app/Global/Method';
import { getCategories, getProduct, getProductQAList, getReviews, getWho } from '@/app/API/NonUserAPI';
import Modal from '@/app/Global/Modal';
import 'react-quill/dist/quill.snow.css';
import QuillNoSSRWrapper from '@/app/Global/QuillNoSSRWrapper';
import ReactQuill from 'react-quill';

interface pageProps {
    product: any;
    seller: any;
    categories: any[]
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
    const [reviews, setReviews] = useState(null as unknown as any[]);
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [productQAList, setProductQAList] = useState(null as unknown as any[]);
    const [categories, setCategories] = useState(props.categories);
    const [product, setProduct] = useState(props.product);
    const [seller, setSeller] = useState(props.seller);
    const [topCategory, setTopCategory] = useState(props.topCategory);
    const [middleCategory, setMiddleCategory] = useState(props.middleCategory);
    const grade0 = product?.numOfGrade['0'];
    const grade1 = product?.numOfGrade['0.5~1'];
    const grade2 = product?.numOfGrade['1.5~2'];
    const grade3 = product?.numOfGrade['2.5~3'];
    const grade4 = product?.numOfGrade['3.5~4'];
    const grade5 = product?.numOfGrade['4.5~5'];
    const [options, setOptions] = useState(null as unknown as number[])
    const [option, setOption] = useState(-1);
    const [count, setCount] = useState(product?.remain >= 1 ? 1 : product?.remain);
    const [mounted, setMounted] = useState(false);
    const [isQOpen, setIsQOpen] = useState(false);
    const [isAOpen, setIsAOpen] = useState(false);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [answer, setAnswer] = useState('');
    const [focusQA, setFocustQA] = useState(-1);
    const quillInstance = useRef<ReactQuill>(null);
    const [expiration, setExpiration] = useState(new Date() > new Date(product?.dateLimit));
    const modules = useMemo(
        () => ({
            toolbar: {
                container: [
                    [{ header: '1' }, { header: '2' }],
                    [{ size: [] }],
                    ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                    [{ list: 'ordered' }, { list: 'bullet' }, { align: [] }],

                ],

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
                    checkWish(product.id)
                        .then(r => setLove(r))
                        .catch(e => console.log(e));
                    setMounted(true);
                    postRecent(product.id)
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        getProductQAList(product.id)
            .then(r => setProductQAList(r))
            .catch(e => console.log(e));
        getProduct(product?.id).then(r => {
            setProduct(r);
            setExpiration(new Date() > new Date(r.dateLimit));
            getWho(r?.authorUsername).then(r => setSeller(r)).catch(e => console.log(e));
        })
            .catch(e => console.log(e));

        getCategories().then(r => {
            setCategories(r);
            const topCategory = r.filter((cateogry: any) => cateogry.name == product.topCategoryName)[0]
            setTopCategory(topCategory);
            setMiddleCategory(topCategory?.categoryResponseDTOList.filter((category: any) => category.name == product.middleCategoryName)[0]);
        }).catch(e => console.log(e));
        getReviews(product.id)
            .then(r => setReviews(r))
            .catch(e => console.log(e));
    }, [ACCESS_TOKEN]);

    function Move(data: number) {
        setFocus(data);
        document.getElementById(data.toString())?.scrollIntoView();
    }
    function getDate(data: any) {
        const date = new Date(data);
        return date.getFullYear() + '.' + (date.getMonth() + 1) + '.' + date.getDate();
    }
    function Wish() {
        if (love)
            deleteWish(product.id);
        else
            postWish(product.id);
        setLove(!love);
    }
    function updateOptions() {
        const options = [] as number[];
        (product?.optionListResponseDTOList as any[])?.forEach(optionList => {
            document.getElementsByName(optionList.optionListName).forEach((option) => {
                if ((option as HTMLInputElement).checked && (option as HTMLInputElement)?.id.includes('§'))
                    options.push(Number((option as HTMLInputElement)?.id.split('§')[1]));
            })
        });
        setOptions(options);

        const count = (document.getElementById('count') as HTMLInputElement);
        let value = Number(count.value);
        let max = product.remain;
        (product?.optionListResponseDTOList as any[])?.forEach(optionList => {
            (optionList.optionResponseDTOList as any[])?.forEach(option => {
                if (options?.includes(option.optionId))
                    max = max > option.optionRemain ? option.optionRemain : max;
            })
        });
        value = value > max ? max : value;
        count.value = value.toString();
        setCount(value);
    }
    function getPrice() {
        let optionPrice = 0;
        (product?.optionListResponseDTOList as any[])?.forEach(optionList => {
            (optionList.optionResponseDTOList as any[])?.forEach(option => {
                if (options?.includes(option.optionId))
                    optionPrice += option.optionPrice;
            })
        });
        return product.price + optionPrice;
    }
    function getDiscountPrice() {
        let optionPrice = 0;
        (product?.optionListResponseDTOList as any[])?.forEach(optionList => {
            (optionList.optionResponseDTOList as any[])?.forEach(option => {
                if (options?.includes(option.optionId))
                    optionPrice += option.optionPrice;
            })
        });
        return product.price * (100 - product?.discount) / 100 + optionPrice;
    }
    function getMax() {
        let max = product.remain;
        (product?.optionListResponseDTOList as any[])?.forEach(optionList => {
            (optionList.optionResponseDTOList as any[])?.forEach(option => {
                if (options?.includes(option.optionId))
                    max = max > option.optionRemain ? option.optionRemain : max;
            })
        });
        return max;
    }
    function getGood() {
        return product?.reviewSize > 0 ? (product?.numOfGrade['3.5~4'] + product?.numOfGrade['4.5~5']) / product?.reviewSize * 100 : 0;
    }
    function openQModal() {
        setIsQOpen(true);
        setTitle('');
        setContent('');
    }
    function openAModal() {
        setIsAOpen(true);
        setAnswer('');
    }
    function getPercent() {
        let answer = 0;
        productQAList?.forEach(productQA => {
            if (productQA?.answer != null)
                answer++;
        })
        return productQAList?.length > 0 ? answer / productQAList.length * 100 : 0;
    }


    return <Main user={user} recentList={recentList} setRecentList={setRecentList} categories={categories}>
        <div className='flex flex-col w-[1240px] min-h-[670px]'>
            <div className='text-sm flex'>
                <label className='text-2xl font-bold'>{props.seller.nickname}</label>
                <label className='ml-2 text-gray-500'>긍정 리뷰</label>
                <label className='ml-2'>{getGood().toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label>
                <div className='divider divider-horizontal h-[20px] mx-0'></div>
                <label className='text-gray-500'>응답률 </label>
                <label className='ml-2'>{getPercent().toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label>
            </div>
            <div className='mt-10 flex'>
                <div className='w-[880px] h-full'>
                    <div className='flex justify-between'>
                        <div className='flex'>
                            <button id='middle' className='flex' onClick={() => setMiddleCategoryDropdown(!middleCategoryDropdown)}>{product.middleCategoryName}<img src={middleCategoryDropdown ? '/up.png' : '/down.png'} className='ml-1 w-[24px] h-[24px]' alt='dropdown' /></button>
                            <button id='bottom' className='flex ml-4' onClick={() => setBottomCategoryDropdown(!bottomCategoryDropdownm)}>{product.categoryName}<img src={bottomCategoryDropdownm ? '/up.png' : '/down.png'} className='ml-1 w-[24px] h-[24px]' alt='dropdown' /></button>
                        </div>
                        {seller?.username == user?.username ? <a href={"/product/modify?id=" + product?.id} className='btn btn-error btn-xs text-white'>수정하기</a> : <></>}
                    </div>
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
                    <div className='flex mt-2'>
                        <div className='min-w-[410px] min-h-[410px] w-[410px] h-[410px] flex items-center justify-center'><img src={product?.url ? product.url : '/empty_product.png'} className='w-[300px] h-[300px]' /></div>
                        <div className='flex flex-col ml-2 w-full'>
                            <div>
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
                                <div className='flex justify-between'>
                                    <a onClick={() => Move(1)} className='ml-2 cursor-pointer'>{product?.reviewSize.toLocaleString("ko-kr") + ' 리뷰보기 >'}</a>
                                    <label>{getDateTimeFormat(product?.dateLimit)}까지</label>
                                </div>
                            </div>
                            <label className='mt-4 text-sm'>{product?.description}</label>
                            <div className='flex justify-between w-full'>
                                <div>
                                    <label className={'text-3xl' + (product?.remain > 0 ? '' : ' line-through text-gray-500')}>{product?.title ? product?.title : '제목 없음'}</label>
                                </div>
                                <button className='min-w-[44px] min-h-[44px] w-[44px] h-[44px] border border-gray-500 rounded-full flex items-center justify-center' onClick={() => Wish()}><img src={love ? '/heart_on.png' : '/heart_off.png'} className='w-[24px] h-[24px]' /></button>
                            </div>
                            <label className='text-gray-500 text-lg'>원산지:상세설명 참조</label>
                            {product?.discount > 0 ?
                                <div className='flex flex-col'>
                                    <label className='text-sm mt-2 text-gray-500 line-through'>{product?.price.toLocaleString('ko-kr')}원</label>
                                    <label className='text-xl mt-2 font-bold'><label className='text-2xl'>{product?.discount}% {(product?.price * (100 - product?.discount) / 100).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}</label>원</label>
                                </div>
                                :
                                <label className='text-xl mt-2 font-bold'><label className='text-2xl font-bold'>{product?.price.toLocaleString('ko-kr')}</label>원</label>
                            }

                            <div className='flex justify-between mt-auto'>
                                <label className='flex items-center'><div className='border border-black rounded-full w-[20px] h-[20px] flex items-center justify-center mr-2'>p</div>최대 적립 포인트</label>
                                <label className='font-bold'>{(getDiscountPrice() / 100).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}P</label>
                            </div>
                            <label className='mt-auto flex items-center'><div className='border border-black rounded-full w-[20px] h-[20px] flex items-center justify-center mr-2'>%</div>최대 22개월 무이자 할부</label>
                            <label className='mt-auto'>무료배송 | CJ대한통운</label>
                            <label className=''><label className='text-blue-400'>{MonthDate()}</label> 이내 도착 예정</label>
                        </div>
                    </div>
                    <div className='flex text-xl font-bold mt-4'>
                        <button id='0' className={'w-[220px] h-[60px]' + (focus == 0 ? ' text-white bg-red-500' : '')} onClick={() => Move(0)}>상품정보</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 1 ? ' text-white bg-red-500' : '')} onClick={() => Move(1)}>리뷰</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 2 ? ' text-white bg-red-500' : '')} onClick={() => Move(2)}>Q&A</button>
                        <button className={'w-[220px] h-[60px]' + (focus == 3 ? ' text-white bg-red-500' : '')} onClick={() => Move(3)}>판매자정보<label className={'text-base font-normal' + (focus == 3 ? ' text-white' : ' text-gray-600')}>(반품/교환)</label></button>
                    </div>
                    <div>
                        <table >
                            <tbody>
                                <tr className='h-[47px]'>
                                    <th className='w-[158px]'>상품상태</th><td className='w-[281px]'>새상품</td>
                                    <th className='w-[158px]'>상품번호</th><td className='w-[281px]'>{product?.id}</td>
                                </tr>
                                <tr className='h-[47px]'>
                                    <th>배송방법</th><td>{product?.delivery}</td>
                                    <th>배송가능지역</th><td>{product?.address}</td>
                                </tr>
                            </tbody>
                        </table>
                        <table>
                            <tbody>
                                <tr className='h-[47px]'>
                                    <th className='w-[158px]'>영수증발행</th>
                                    <td className='w-[701px]'>{product?.receipt}</td>
                                </tr>
                                <tr className='h-[47px]'>
                                    <th>A/S안내</th>
                                    <td>{product?.a_s}</td>
                                </tr>
                                <tr className='h-[47px]'>
                                    <th>브랜드</th>
                                    <td>{product?.brand}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div className='w-[860px] h-[100px] flex justify-center items-center text-gray-600 text-xl bg-gray-200 font-bold'>
                            <img src={'/exclamation.png'} className='w-[30px] h-[30px] mr-2' />
                            판매자가<label className='text-black ml-2'>현금결제를 요구하면 거부</label>하시고 즉시 <a href='?' className='underline'>52번가로 신고</a>해 주세요.
                        </div>
                        <div className='p-4'>
                            <div dangerouslySetInnerHTML={{ __html: product.detail }} />
                        </div>
                        <label id='1' className='font-bold text-2xl'>상품리뷰</label>
                        <div className='divider'></div>
                        <div className='flex'>
                            <div className='flex flex-col items-center justify-center w-[400px] h-[196px]'>
                                <label className='font-bold text-lg mb-1'>구매만족도</label>
                                <label className='font-bold text-3xl mb-3'>{product.grade}</label>
                                <div className='rating rating-lg rating-half'>
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
                                <label className='mt-3'>{product?.reviewSize.toLocaleString("ko-kr")}건</label>
                            </div>
                            <div className='divider divider-horizontal'></div>
                            <div className='flex items-center flex-col justify-center'>
                                <label className='flex items-center text-gray-500'>5점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade5} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade5 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                                <label className='flex items-center text-gray-500'>4점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade4} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade4 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                                <label className='flex items-center text-gray-500'>3점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade3} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade3 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                                <label className='flex items-center text-gray-500'>2점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade2} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade2 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                                <label className='flex items-center text-gray-500'>1점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade1} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade1 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                                <label className='flex items-center text-gray-500'>0점<progress className='progress progress-info w-[300px] h-[18px] mx-3' value={grade0} max={product?.reviewSize} /><label className='text-blue-400 w-[32px]'>{(product?.reviewSize > 0 ? grade0 / product?.reviewSize * 100 : 0).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}%</label></label>
                            </div>
                        </div>
                        <div className='divider'></div>
                        <label className='font-bold text-2xl'>전체리뷰<label className='font-normal text-sm ml-2'>{product?.reviewSize.toLocaleString("ko-kr")}건</label></label>
                        {reviews?.length == 0 ?
                            <div className='h-[100px] flex items-center justify-center'>등록된 리뷰가 없습니다.</div>
                            : <></>}
                        {reviews?.map((review, index) => <div key={index} className='w-full'>
                            <div className='flex'>
                                <img className='min-w-[52px] w-[52px] min-h-[52px] h-[52px]' src={review?.profileUrl ? review.profileUrl : '/base_profile.png'} />
                                <div className='flex flex-col p-2 w-full'>
                                    <div className='flex justify-between w-full items-center'>
                                        <label className='font-bold text-lg'>{review?.nickname}</label>
                                        <a className='text-gray-500 text-sm'>{getDate(review?.createDate)}</a>
                                    </div>
                                    <div>
                                        <div className='rating rating-sm rating-half'>
                                            <input type='radio' className='rating-hidden' defaultChecked={!review?.grade || (review?.grade == 0 && review?.grade < 0.25)} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={review?.grade > 0.25 && review?.grade <= 0.75} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={review?.grade > 0.75 && review?.grade <= 1.25} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={review?.grade > 1.25 && review?.grade <= 1.75} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={review?.grade > 1.75 && review?.grade <= 2.25} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={review?.grade > 2.25 && review?.grade <= 2.75} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={review?.grade > 2.75 && review?.grade <= 3.25} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={review?.grade > 3.25 && review?.grade <= 3.75} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={review?.grade > 3.75 && review?.grade <= 4.25} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-1' defaultChecked={review?.grade > 4.25 && review?.grade <= 4.75} onClick={e => e.preventDefault()} />
                                            <input type='radio' className='bg-orange-500 mask mask-star-2 mask-half-2' defaultChecked={review?.grade > 4.75} onClick={e => e.preventDefault()} />
                                        </div>
                                        <label className='ml-2'>{review?.grade}</label>
                                        <div><div dangerouslySetInnerHTML={{ __html: review?.content }} /></div>
                                        {/* <button className='btn btn-info btn-sm'>추천</button> */}
                                    </div>
                                </div>
                            </div>
                            <div className='divider'></div>
                        </div>)}
                    </div>
                    <label className='font-bold text-2xl'>문의/답변<label className='font-normal text-sm ml-2'>{product?.reviewSize.toLocaleString("ko-kr")}건</label></label>
                    <div id="2" className='flex text-center mb-2'>
                        <div className='w-[604px]'>문의/답변</div>
                        <div className='w-[96px]'>작성자</div>
                        <div className='w-[150px]'>작성일</div>
                    </div>
                    {productQAList?.map((productQA, index) => <div key={index} className='flex'>
                        <div className='flex flex-col text-center mb-2'>
                            <div className='flex items-center'>
                                <div className='w-[604px] text-start flex items-center'>
                                    {productQA.answer != null ? <label className='text-blue-500 border border-blue-500 p-1 text-xs mr-2'>답변완료</label> : <label className='text-red-500 border border-red-500 p-1 text-xs mr-2'>확인중</label>}
                                    <label className='hover:underline cursor-pointer' onClick={() => setFocustQA(index)}>{productQA?.title}</label>
                                </div>
                                <div className='w-[96px]'>{productQA?.author}</div>
                                <div className='w-[150px]'>{getDateTimeFormat(productQA?.createDate)}</div>
                            </div>
                            {focusQA == index ?
                                <div className='flex flex-col'>
                                    <div className='flex items-center'>
                                        <label className='text-blue-500 font-bold text-3xl mr-2'>Q</label>
                                        <label dangerouslySetInnerHTML={{ __html: productQA?.content }} />
                                    </div>
                                    {productQA.answer != null ?
                                        <>
                                            <div className='flex items-center mt-4'>
                                                <label className='text-red-500 font-bold text-3xl mr-2'>A</label>
                                                <label dangerouslySetInnerHTML={{ __html: productQA?.answer }} />
                                            </div>
                                            {/* {user.unsername == seller.unsername ? <button className='self-end btn btn-xs btn-error text-white w-[100px]' onClick={openAModal}>수정하기</button> : <></>} */}
                                        </>
                                        :
                                        <>
                                            {user.unsername == seller.unsername ? <button className='self-end btn btn-xs btn-error text-white w-[100px]' onClick={openAModal}>답변하기</button> : <></>}
                                        </>
                                    }

                                </div>
                                : <></>
                            }
                        </div>
                    </div>)}
                    <div className='min-h-[100px] w-full flex flex-col items-center justify-center'>
                        {productQAList && productQAList.length <= 0 ? <label>등록된 Q&A가 없습니다.</label> : <></>}
                        <button className='btn btn-warning ml-auto btn-sm text-white' onClick={openQModal}>상품 문의하기</button>
                    </div>
                    <label id="3" className='font-bold text-2xl'>판매자정보<label className='font-normal text-sm ml-2'>{product?.reviewSize.toLocaleString("ko-kr")}건</label></label>
                    <div className='flex text-center mb-2'>
                        <table>
                            <tbody>
                                <tr>
                                    <th className='w-[150px]'>반품/교환 배송비</th>
                                    <td className='w-[750px] p-2 text-start'>(구매자귀책) 2,500원/5,000원 초기배송비 무료시 반품배송비 부과방법 : 왕복(편도x2)</td>
                                </tr>
                                <tr>
                                    <th>반품/교환 문의</th>
                                    <td className='text-start p-2'>{seller?.email}</td>
                                </tr>
                                <tr>
                                    <th>반품/교환 안내</th>
                                    <td className='text-start p-2'>{PhoneString(seller?.phoneNumber)}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <Modal open={isQOpen} onClose={() => setIsQOpen(false)} className='' escClose={true} outlineClose={true}>
                        <div className="flex flex-col w-[744px] h-[552px]">
                            <div className="text-white bg-red-500 h-[37px] py-2 px-4">상품 문의하기</div>
                            <div className="px-4 flex flex-col">
                                <input id="title" type="text" className="input input-bordered mt-4" placeholder="문의 제목.." defaultValue={title} onChange={e => setTitle(e.target.value)} />
                                <QuillNoSSRWrapper
                                    forwardedRef={quillInstance}
                                    defaultValue={content}
                                    onChange={(e: any) => setContent(e)}
                                    modules={modules}
                                    theme="snow"
                                    className='w-full h-[300px] mt-2'
                                    placeholder="문의 내용을 입력해주세요."
                                />
                                <div className='flex justify-center mt-16'>
                                    <button className='btn btn-info btn-xs mr-2 text-white' disabled={title == "" || content == ""} onClick={() => {
                                        postQuestion({ productId: product.id, title: title, content: content }).then(r => { setProductQAList(r); setIsQOpen(false) }).catch(e => console.log(e));
                                    }}>등록</button>
                                    <button className='btn btn-error btn-xs text-white' onClick={() => setIsQOpen(false)}>취소</button>
                                </div>
                            </div>
                        </div>
                    </Modal>
                    <Modal open={isAOpen} onClose={() => setIsAOpen(false)} className='' escClose={true} outlineClose={true}>
                        <div className="flex flex-col w-[744px] h-[752px]">
                            <div className="text-white bg-red-500 h-[37px] py-2 px-4">상품 문의하기</div>
                            <div className="px-4 flex flex-col">
                                <div className="w-[712px] h-[48px] p-2 border border-black mt-4 rounded-lg flex items-center">{focusQA > 0 ? productQAList[focusQA]?.title : ''}</div>
                                <div className='w-[712px] h-[300px] p-2 border border-black mt-4 rounded-lg'> {focusQA > 0 ? <div dangerouslySetInnerHTML={{ __html: productQAList[focusQA]?.content }} ></div> : <></>}</div>
                                <QuillNoSSRWrapper
                                    forwardedRef={quillInstance}
                                    defaultValue={answer}
                                    onChange={(e: any) => setAnswer(e)}
                                    modules={modules}
                                    theme="snow"
                                    className='w-full h-[200px] mt-2'
                                    placeholder="답변 내용을 입력해주세요."
                                />
                                <div className='flex justify-center mt-16'>
                                    <button className='btn btn-info btn-xs mr-2 text-white' disabled={answer == ""} onClick={() => {
                                        postAnswer({ productId: product.id, answer: answer, productQAId: productQAList[focusQA].productQAId }).then(r => { setProductQAList(r); setIsAOpen(false) }).catch(e => console.log(e));
                                    }}>등록</button>
                                    <button className='btn btn-error btn-xs text-white' onClick={() => setIsQOpen(false)}>취소</button>
                                </div>
                            </div>
                        </div>
                    </Modal>
                </div>
                <div className='w-[300px] h-full ml-[60px] relative' >
                    <div className='w-[300px] fixed h-[650px]'>
                        <div className='h-[550px] overflow-y-scroll pr-1'>
                            {(product.optionListResponseDTOList as any[])?.map((list, index) =>
                                <div key={index} className={'border border-black mb-2'}>
                                    <div className={'font-bold p-2 border-black cursor-pointer flex flex-col' + (option != index ? " " : " border-4 bg-red-500 text-white")} onClick={() => {
                                        if (option == index)
                                            setOption(-1);
                                        else
                                            setOption(index);
                                    }}>
                                        {list.optionListName}
                                        {(list.optionResponseDTOList as any[]).filter(opt => options?.includes(opt.optionId)).map((option, index) => <label className='text-xs' key={index}>선택: {option?.optionName}</label>)}
                                    </div>
                                    {mounted ? <div className={(option == index ? '' : ' hidden')}>
                                        <div className={'flex relative flex-col mt-2' + (typeof window !== "undefined" && (document?.getElementById(list.optionListName) as HTMLInputElement)?.checked ? " bg-red-500 text-white" : "")}>
                                            <label className='mt-2 px-2'>{'옵션 미선택'}</label>
                                            <input type='radio' id={list.optionListName} name={list.optionListName} className='opacity-0 z-1 absolute w-full h-full cursor-pointer' onChange={updateOptions} defaultChecked={true} />
                                        </div>
                                        {(list.optionResponseDTOList as any[])?.map((opt, opt_index) =>
                                            <div key={opt_index} className={'h-full flex relative flex-col mt-2' + (options?.includes(opt.optionId) ? " bg-red-500 text-white" : "")}>
                                                <label className='mt-2 px-2'>{opt.optionName}</label>
                                                <label className='mt-2 px-4'><label className='font-bold'>{opt.optionPrice.toLocaleString('ko-kr')}</label> 원</label>
                                                <input type='radio' id={list.optionListName + '§' + opt.optionId} name={list.optionListName} className='opacity-0 z-1 absolute w-full h-full cursor-pointer' onChange={updateOptions} />
                                            </div>)}
                                    </div>
                                        : <></>
                                    }
                                </div>)}
                        </div>
                        <div className='flex justify-between'>
                            <label>총 <input type='number' id="count" className='[appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none m-0 input input-sm min-w-[30px]' defaultValue={product?.remain >= 1 ? 1 : product?.remain} min={product?.remain >= 1 ? 1 : product?.remain} max={getMax()} onChange={(e) => { let value = Number(e.target.value); if (value > getMax()) value = getMax(); else if (value < 0) value = 0; e.target.value = value.toString(); setCount(value); }} />개</label>
                            <label>{(getDiscountPrice() * count).toLocaleString('ko-kr', { maximumFractionDigits: 0 })}원</label>
                        </div>
                        {expiration ?
                            <button className='btn w-full mt-2' disabled>판매 기한 만료</button>
                            :
                            product?.remain > 0 ?
                                <button className='btn btn-error text-white w-full mt-2' onClick={() =>
                                    postCartList({ productId: product.id, optionIdList: options, count: count }).then(() => window.location.href = "/account/cart").catch(e => console.log(e))
                                }>장바구니 담기</button>
                                :
                                <button className='btn w-full mt-2' disabled>재입고 예정</button>
                        }
                        <button className='btn btn-sm btn-warning text-white w-full mt-1' onClick={() => location.href = "/account/chat/" + seller?.username}>실시간 상담</button>
                    </div>
                </div>
            </div>
            <div className='w-[880px] text-xs flex justify-end'>
                {(product?.tagList as String[])?.map((tag, index) => <label key={index} className='btn btn-xs' onClick={() => location.href = "/search?keyword=" + tag}>{tag}</label>)}
            </div>
        </div>
    </Main>
}