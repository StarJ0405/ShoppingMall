'use client'

import { useState } from 'react';
import Side from '../Side';
import DropDown, { Direcion } from '../DropDown';
import { deleteRecent } from '@/app/API/UserAPI';
import { getDate } from '../Method';



interface pageInterface {
  children: React.ReactNode,
  className?: string
  user: any
  recentList: any[];
  setRecentList: (data: any) => void;
  categories: any[];
}

export default function Main(props: Readonly<pageInterface>) {
  const className = props.className;
  const user = props.user;
  const recentList = props.recentList;
  const [isRecentOpen, setIsRecentOpen] = useState(false);
  const [hover, setHover] = useState(-1);
  const [isSideOpen, setIsSideOpen] = useState(false);
  const [userHover, setUserHover] = useState(false);
  const [userHoverInterval, setUserHoverInterval] = useState(null as any);
  const categories = props.categories
  const [topCategoryHover, setTopCategoryHover] = useState(null as any);
  const [topCategoryHoverInterval, setCategoryHoverInterval] = useState(null as any);
  function openUserHover() {
    clearInterval(userHoverInterval);
    setUserHover(true);
  }
  function closeUserHover() {
    if (userHoverInterval)
      clearInterval(userHoverInterval);
    const interval = setInterval(() => { setUserHover(false); clearInterval(interval); }, 100);
    setUserHoverInterval(interval);
  }

  function openTopCategoryHover(category: any) {
    clearInterval(topCategoryHoverInterval);
    setTopCategoryHover(category);
  }
  function closeTopCategoryHover() {
    if (topCategoryHoverInterval)
      clearInterval(topCategoryHoverInterval);
    const interval = setInterval(() => { setTopCategoryHover(null); clearInterval(interval) }, 500);
    setCategoryHoverInterval(interval);
  }
  return (
    <main id='main' className={'min-h-screen flex flex-col items-center realtive ' + className}>
      <header className='flex w-[1240px] items-center h-[120px]'>
        <button onClick={() => setIsSideOpen(true)}><img src='/expand_button.png' className='w-[36px] h-[36px]' /></button>
        <a className='ml-10' href='/'><img src='/logo.png' className='w-[64px] h-[40px]' /></a>
        <div className='flex items-center border-2 border-gray-300 rounded-full px-5 ml-4'>
          <input id="keyword" type='text' className='text-xl bg-transparent w-[480px] mr-[20px] outline-none' placeholder='검색' onKeyDown={e => { if (e.key == 'Enter') document.getElementById('search')?.click() }}></input>
          <button id='search' onClick={() => { const value = (document.getElementById('keyword') as HTMLInputElement)?.value; location.href = '/search?keyword=' + (value ? value : '') }}>
            <img src='/search.png' className='w-[50px] h-[50px]' />
          </button>
        </div>
        <div className='justify-self-end flex ml-[150px] justify-between w-[300px]' >
          <a href='/account/profile'><img id='user' alt='user' src='/user.png' className='w-[48px] h-[48px]' onMouseEnter={e => { (e.target as any).src = '/user_red.png'; openUserHover(); }} onMouseLeave={e => { (e.target as any).src = '/user.png'; closeUserHover(); }}></img></a>
          <a href='/account/log'><img alt='delivery' src='/delivery.png' className='w-[60px] h-[48px]' onMouseEnter={e => (e.target as any).src = '/delivery_red.png'} onMouseLeave={e => (e.target as any).src = '/delivery.png'}></img></a>
          <a href='/account/cart'><img alt='cart' src='/cart.png' className='w-[48px] h-[48px]' onMouseEnter={e => (e.target as any).src = '/cart_red.png'} onMouseLeave={e => (e.target as any).src = '/cart.png'}></img></a>
          <a className='cursor-pointer' onClick={() => setIsRecentOpen(true)}><img alt='recent' src='/recent.png' className='w-[48px] h-[48px]' onMouseEnter={e => (e.target as any).src = '/recent_red.png'} onMouseLeave={e => (e.target as any).src = '/recent.png'}></img></a>
        </div>
      </header>
      <DropDown open={userHover} onClose={() => closeUserHover()} className='bg-white' background='main' button='user' defaultDriection={Direcion.DOWN} height={205} width={170}>
        <div className='h-full w-full flex flex-col justify-between py-4 px-2 text-lg' onMouseEnter={() => openUserHover()} onMouseLeave={() => closeUserHover()}>
          <a href='' className='hover:text-red-500'>주문/배송조회</a>
          <a href='' className='hover:text-red-500'>취소/반품/교환</a>
          <a href='' className='hover:text-red-500'>고객센터</a>
          <a href='/account/profile' className='hover:text-red-500'>회원정보</a>
          {user?.role == 'SELLER' || user?.role == 'ADMIN' ? <a href='/product/create' className='hover:text-red-500'>상품 등록</a> : <></>}
          {user ? <a href='/' onClick={e => { e.preventDefault(); localStorage.clear(); window.location.reload(); }} className='hover:text-red-500'>로그아웃</a> : <></>}
        </div>
      </DropDown>
      <Side open={isSideOpen} onClose={() => setIsSideOpen(false)} className='pl-4 py-4 left-0 top-0 h-full flex' outlineClassName='bg-opacity-5' escClose={true} outlineClose={true}>
        <div className='w-[300px]'>
          <div className='flex justify-between items-center text-2xl font-bold'>
            {user ?
              <a href='' className='hover:underline'>{user?.nickname}</a>
              :
              <a href='/account/login' className='hover:underline'>로그인</a>
            }
            <button onClick={() => setIsSideOpen(false)}><img alt='x' src='/x.png' className='w-[24px] h-[24px] mr-6' /></button>
          </div>
          <div className='overflow-y-scroll h-full'>
            <div className='mt-[20px] flex flex-col' onMouseLeave={() => closeTopCategoryHover()}>
              <label className='text-xl font-bold'>카테고리</label>
              <div className='flex flex-col'>
                {categories?.map((category, index) => <div key={index} className='pl-5 text-lg mt-1 hover:bg-red-500 hover:text-white w-full h-full cursor-pointer' onMouseEnter={() => openTopCategoryHover(category)}>{category.name}</div>)}
              </div>
            </div>
          </div>
        </div>
        <div className={('flex flex-col justif w-[200px] h-full pl-4 overflow-y-scroll') + (topCategoryHover != null ? '' : ' hidden')} onMouseEnter={() => openTopCategoryHover(topCategoryHover)} onMouseLeave={() => closeTopCategoryHover()}>
          {(topCategoryHover?.categoryResponseDTOList as any[])?.map((category, index) => <div key={index} className='flex flex-col mt-8'>
            <label className='text-gray-500 text-lg font-bold'>{category.name}</label>
            {(category.categoryResponseDTOList as any[])?.map((category, index) =>
              <label key={index} className='hover:text-red-500 cursor-pointer my-1' onClick={()=>location.href = '/search?keyword='+category.name}>{category.name}</label>
            )}
          </div>)}
        </div>
      </Side>
      <Side open={isRecentOpen} onClose={() => setIsRecentOpen(false)} className='px-4 py-4 w-[400px] right-0 top-0 h-full' outlineClassName='bg-opacity-5' escClose={true} outlineClose={true}>
        <div className='flex justify-between items-center text-2xl font-bold'>
          <div className='flex justify-between w-full items-center'>
            <div>
              <label className='text-xl font-bold'>최근 본 상품</label>
              <a href="/account/wish/" className='text-sm text-gray-500 ml-4 hover:underline'>{'찜 목록 보기 >'}</a>
            </div>
            <button className='text-2xl cursor-pointer' onClick={() => setIsRecentOpen(false)}>X</button>
          </div>
        </div>
        <div className='divider'></div>
        <ul>
          {recentList?.map((recent, index) => <li className='list-disc ml-4' key={index}>
            <label>{getDate(recent?.createDate)}</label>
            <div className='relative w-[104px]'>
              <img onClick={() => window.location.href = '/product/' + recent.productId} src={recent?.url ? recent.url : '/empty_product.png'} className={'w-[104px] h-[104px] cursor-pointer ' + (hover == index ? ' border-2 border-black' : '')} onMouseEnter={() => setHover(index)} onMouseLeave={() => setHover(-1)} />
              <button className={'text-sm absolute font-bold right-0 top-0 text-white bg-black w-[14px] z-[1] text-center' + (hover != index ? ' hidden' : '')} onClick={() => {
                deleteRecent(recent.recentId).then(r => { props.setRecentList(r); console.log(r) }).catch(e => console.log(e))
              }} onMouseEnter={() => setHover(index)} onMouseLeave={() => setHover(-1)} >X</button>
            </div>
          </li>)}
        </ul>
      </Side>
      <nav className='flex w-[1240px] h-[66px] items-center justify-between'>
        <div className='flex items-center'>
          <a href='/product/best' className='text-lg border-red-500 hover:border-b-2'>베스트</a>
        </div>
        {user ?
          <div><a href='/account/profile/' className='font-bold hover:underline'>{user?.nickname}</a> <label className='text-red-500 font-bold'>{user?.point.toLocaleString('ko-kr')} P</label></div>
          :
          <a href='/account/login' className='font-bold hover:underline'>로그인</a>
        }
      </nav>
      {props.children}
      <footer className='flex flex-col w-[1240px]'>
        <div className='flex justify-between'>
          <label className='font-bold'>상호명 및 호스팅 서비스 제공 : 십일번가(주)</label><label className='w-[360px] cursor-pointer'>고객센터</label>
        </div>
        <label className='text-xs'>대표이사 : 홍성재, 이동원, 황준하, 이순재, 주소: 대전광역시 서구 둔산로 52, Tel: 042-369-5890</label>
        <label className='text-xs'>사업자등록번호 : 889-86-02332, 통신판매업신고 : 제 2021-대전서구-1956호</label>
      </footer>
    </main>
  );
}
