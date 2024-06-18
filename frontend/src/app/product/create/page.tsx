'use client'
import { getCategories } from '@/app/API/NonUserAPI';
import { getRecent, getUser, productRegist, saveImage } from '@/app/API/UserAPI';
import Main from '@/app/Global/Layout/MainLayout';
import Modal from '@/app/Global/Modal';
import { redirect } from 'next/navigation';
import { useEffect, useMemo, useRef, useState } from 'react';
import 'react-quill/dist/quill.snow.css';
import QuillNoSSRWrapper from '@/app/Global/QuillNoSSRWrapper';
import ReactQuill from 'react-quill';
// const ReactQuill = dynamic(() => import('react-quill'), { ssr: false });


export default function Page() {
    const [user, setUser] = useState(null as any);
    const [categories, setCategories] = useState(null as unknown as any[]);
    const [isImageHover, setIsImageHover] = useState(false);

    const [firstCategory, setFirstCategory] = useState(-1);
    const [secondCategory, setSecondCategory] = useState(-1);
    const [category, setCategory] = useState(-1);
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
    const [options, setOptions] = useState([] as any[][])
    const [selectedOptionList, setSelectedOptionList] = useState([] as any[]);
    const [selectedOption, setSelectedOption] = useState(null as any);
    const [isModalOpen, setISModalOpen] = useState(-1);
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const quillInstance = useRef<ReactQuill>(null);

    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');

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
                const imgUrl = (await saveImage(formData)).url;
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
        getCategories()
            .then(r => setCategories(r))
            .catch(e => console.log(e));
    }, []);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    setAS(r.phoneNumber);
                    setBrand(r.nickname);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    function Regist() {
        if (category != -1)
            productRegist({ categoryId: category, price: price, description: simpleDescription, detail: detail, dateLimit: dateLimit, remain: remain, title: title, delivery: delivery, address: address, receipt: receipt, a_s: a_s, brand: brand, productTagList: tags, url: url, optionLists: options })
                .then(() => window.location.href = '/')
                .catch(e => console.log(e))
        else alert('카테고리를 선택해주세요.');
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
    function openModal(type: number) {
        setISModalOpen(type);
    }
    return <Main className='flex justify-center' user={user} recentList={recentList} setRecentList={setRecentList}>
        <div className='w-[1240px] min-h-[750px]'>
            <table>
                <tbody className='border border-black'>
                    <tr>
                        <th className='border border-black w-[150px]'>카테고리</th>
                        <td className='w-[1090px] px-2'>
                            <select defaultValue={-1} autoFocus onChange={e => { setFirstCategory(Number(e.target.selectedOptions[0].value)); setSecondCategory(-1); setCategory(-1); }}>
                                <option value={-1} disabled>최상위 카테고리를 골라주세요.</option>
                                {categories?.map((category, index) => <option key={index} value={index}>{category.name}</option>)}
                            </select>
                            {firstCategory >= 0 ?
                                <select defaultValue={0} className='ml-2' onChange={e => { setSecondCategory(Number(e.target.selectedOptions[0].value)); setCategory(-1) }}>
                                    <option value={0} disabled>상위 카테고리를 골라주세요.</option>
                                    {(categories[firstCategory].categoryResponseDTOList as any[])?.map((category, index) => <option key={index} value={index}>{category.name}</option>)}
                                </select>
                                :
                                <></>
                            }
                            {secondCategory >= 0 ?
                                <select defaultValue={0} className='ml-2' onChange={e => setCategory(Number(e.target.selectedOptions[0].value))}>
                                    <option value={0} disabled>하위 카테고리를 골라주세요.</option>
                                    {(categories[firstCategory].categoryResponseDTOList[secondCategory].categoryResponseDTOList as any[])?.map((category, index) => <option key={index} value={category.id}>{category.name}</option>)}
                                </select>
                                :
                                <></>
                            }
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
                        <td className='px-2'><input type='text' placeholder='제목..' defaultValue={title} onChange={e => setTitle(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>부가설명</th>
                        <td className='px-2'><input type='text' placeholder='간략내용..' defaultValue={simpleDescription} onChange={e => setSimpleDescription(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>판매 종료일</th>
                        <td className='px-2'><input type='datetime-local' placeholder='판매 기간' defaultValue={dateLimit} onChange={e => setDateLimit(e.target.value)} max="9999-12-31T23:59" /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>판매 수량</th>
                        <td className='px-2'><input type='number' placeholder='남은 수량..' defaultValue={remain} onChange={e => setRemain(Number(e.target.value))} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>가격</th>
                        <td className='px-2'><input type='number' placeholder='가격' defaultValue={price} onChange={e => setPrice(Number(e.target.value))} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>배송방법</th>
                        <td className='px-2'><input type='text' placeholder='배송 방법..' defaultValue={'택배'} onChange={e => setDelivery(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>배송가능지역</th>
                        <td className='px-2'><input type='text' placeholder='배송가능 지역 입력..' defaultValue={'전국(제주 도서산간지역 제외)'} onChange={e => setAddress(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>영수증발행</th>
                        <td className='px-2'>
                            <textarea placeholder='영수발행 방법..' className='w-full' defaultValue={'국내거주해외셀러 : 구매대행 수수료에 대한 현금영수증만 발행이 가능하며, 판매자에게 직접 발행 요청 필요(11번가 발행불가)\n해외거주해외셀러 : 온라인 현금영수증 발급 불가, 신용카드 전표 정보는 나의 11번가 PC참조'} onChange={e => setReceipt(e.target.value)}></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>A/S안내</th>
                        <td className='px-2'><input type='text' placeholder='A/S 관련 번호..' defaultValue={user?.phoneNumber} onChange={e => setAS(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>브랜드</th>
                        <td className='px-2'><input type='text' placeholder='브랜드..' defaultValue={user?.nickname} onChange={e => setBrand(e.target.value)} /></td>
                    </tr>
                    <tr>
                        <th className='border border-black'>태그</th>
                        <td className='px-2 flex flex-col'>
                            <div className='flex'>{tags?.map((tag, index) => <button className='btn btn-xs border border-black mr-1 mb-1' key={index} onClick={() => setTags(tags.filter((t) => t != tag))}>{tag}</button>)}</div>
                            <div className='flex'>
                                <input id="tag" type='text' placeholder='태그 작성..' onKeyDown={e => { if (e.key == 'Enter') document.getElementById('addTag')?.click() }} />
                                <button id='addTag' className='btn btn-xs ml-2' onClick={() => addTag()}>태그 추가</button>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>옵션</th>
                        <td className='px-2 flex items-center'>
                            <div className='flex flex-col'>
                                <select id="optionList" defaultValue={-1} onChange={e => { setSelectedOptionList(options[Number(e.target.selectedOptions[0].value)]); (document.getElementById('option') as HTMLSelectElement).selectedIndex = 0; }}>
                                    {options ?
                                        <option value={-1} disabled>등록된 옵션목록이 없습니다.</option>
                                        :
                                        <option value={-1} disabled>옵션 목록을 선택해주세요</option>
                                    }
                                    {options?.map((option: any, index) => <option key={index} value={index}>{option.name}</option>)}
                                </select>
                                <button className='btn btn-xs btn-info' onClick={() => openModal(0)}>옵션 목록 추가</button>
                                <button className='btn btn-xs btn-error' disabled={selectedOptionList.length <= 0} onClick={() => {
                                    const newOptions = options.filter((option: any) => option.name != (selectedOptionList as any).name);
                                    setSelectedOptionList([] as any[]);
                                    setSelectedOption(null as any);
                                    setOptions(newOptions);
                                    (document.getElementById('optionList') as HTMLSelectElement).selectedIndex = 0;
                                }}>옵션 목록 삭제</button>
                            </div>
                            <label className='text-2xl mx-4'>→</label>
                            <div className='flex flex-col'>
                                <select id="option" defaultValue={-1} onChange={e => { setSelectedOption((selectedOptionList as any).child[Number(e.target.selectedOptions[0].value)]) }}>
                                    {selectedOptionList.length > 0 ?
                                        options ?
                                            <option value={-1} disabled>등록된 옵션이 없습니다.</option>
                                            :
                                            <option value={-1} disabled>옵션을 선택해주세요</option>
                                        :
                                        <option value={-1} disabled>옵션 목록을 선택해주세요</option>
                                    }
                                    {((selectedOptionList as any)?.child as any[])?.map((option: any, index) => <option key={index} value={index}>{option.name + '(' + option.remain + ")" + ':' + option.price}</option>)}
                                </select>
                                <button className='btn btn-xs btn-info' disabled={selectedOptionList.length <= 0} onClick={() => openModal(1)}>옵션 추가</button>
                                <button className='btn btn-xs btn-error' disabled={!selectedOption} onClick={() => {
                                    (selectedOptionList as any).child = (selectedOptionList as any).child.filter((child: any) => child.name != selectedOption.name);
                                    setSelectedOption(null as any);
                                    setOptions([...options]);
                                    (document.getElementById('option') as HTMLSelectElement).selectedIndex = 0;
                                }}>옵션 삭제</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th className='border border-black'>상세설명</th>
                        <td className='px-2 flex pb-[50px] min-h-[350px]' >
                            {/* <ReactQuill modules={modules} formats={formats} className='min-h-[300px] w-full' placeholder='상세 설명 작성..' onChange={(e: any) => setDetail(e)} value={detail} /> */}
                            <QuillNoSSRWrapper
                                forwardedRef={quillInstance}
                                value={detail}
                                onChange={(e: any) => setDetail(e)}
                                modules={modules}
                                theme="snow"
                                placeholder="내용을 입력해주세요."
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className=''>
                <button className='btn btn-xs btn-info' onClick={() => Regist()}>상품 등록</button><button className='btn btn-xs btn-error'>취소</button>
            </div>
        </div>
        <Modal open={isModalOpen > -1} onClose={() => setISModalOpen(-1)} className='w-[300px] h-[150px] flex flex-col justify-center items-center' escClose={true} outlineClose={true} >
            <input id="add" type="text" maxLength={50} placeholder='작성..' autoFocus onKeyDown={e => {
                if (e.key == "Enter")
                    if (isModalOpen == 0)
                        document.getElementById("add_button")?.click();
                    else
                        document.getElementById("price")?.focus();
            }} />
            {isModalOpen == 1 ? <>
                <input type="number" id="price" min={0} placeholder='옵션 추가 비용' onKeyDown={e => { if (e.key == "Enter") document.getElementById('remain')?.focus() }} />
                <input type="number" id="remain" min={0} placeholder='수량 제한' onKeyDown={e => { if (e.key == "Enter") document.getElementById('add_button')?.click() }} />
            </>
                : <></>}
            <button id="add_button" className='btn btn-sm mt-1' onClick={() => {
                const value = (document.getElementById('add') as HTMLInputElement).value;
                if (value && value.length > 0)
                    if (isModalOpen == 0) {
                        // 옵션 목록
                        const now = { name: value, child: [] as any } as any;
                        if (options.filter((option: any) => option.name == value).length == 0) {
                            options.push(now);
                            setOptions([...options]);
                        }
                        setISModalOpen(-1);
                    } else {
                        // 옵션
                        const price = Number((document.getElementById('price') as HTMLInputElement).value);
                        const remain = Number((document.getElementById('remain') as HTMLInputElement).value);
                        const now = { name: value, price: price, remain: remain } as any;
                        const search = (selectedOptionList as any).child.filter((option: any) => option.name == value);
                        if (search.length == 0) {
                            (selectedOptionList as any).child.push(now);
                            setOptions([...options]);
                        } else search[0].price = price;
                        setISModalOpen(-1);
                    }
            }}>등록하기</button>
            <button className='btn btn-sm mt-1' onClick={() => setISModalOpen(-1)}>취소하기 </button>
        </Modal>
    </Main>;
}