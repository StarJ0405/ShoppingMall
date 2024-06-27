'use client'

import { Login } from '@/app/API/AuthAPI';
import { KeyDownCheck, Move } from '@/app/Global/Method';
import { useState } from 'react'


export default function Page() {
    const [canSee, setCanSee] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [preKey, setPreKey] = useState('');
    function Sumbit() {
        Login({ username, password })
            .then((response) => {
                localStorage.clear();
                localStorage.setItem('tokenType', response.tokenType);
                localStorage.setItem('accessToken', response.accessToken);
                localStorage.setItem('refreshToken', response.refreshToken);
                window.location.href = `/`;
            }).catch((error) => {
                switch (error.response.data) {
                    case 'username': { setError('아이디가 잘못되었습니다.'); break; }
                    case 'password': { setError('잘못된 비밀번호입니다.'); break; }
                    default:
                        console.log(error);
                }
                
            });
    }

    return <div className='w-full h-screen'>
        <div className='flex flex-col items-center justify-center h-full relative'>
            <a href='/'><img src='/logo.png' className='w-[75px] h-[48px] mb-[40px]' /></a>
            <label className='text-xs text-red-500 text-start w-[396px]'>{error}</label>
            <input id='username' type='text' className='w-[396px] h-[46px] input input-bordered rounded-[0]' style={{ outline: '0px' }} placeholder='아이디 입력' onFocus={e => e.target.style.border = '2px solid red'} onBlur={e => e.target.style.border = ''} onChange={e => setUsername(e.target.value)} onKeyDown={e => KeyDownCheck({ preKey, setPreKey, e: e, next: () => Move('password') })} />
            <input id='password' type={canSee ? 'text' : 'password'} className='w-[396px] h-[46px] input input-bordered rounded-[0]' style={{ outline: '0px' }} placeholder='비밀번호 8자~20자' onFocus={e => e.target.style.border = '2px solid red'} onBlur={e => e.target.style.border = ''} onChange={e => setPassword(e.target.value)} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('username'), next: () => document.getElementById('submit')?.click() }) }} />
            <div className='w-[396px] mt-1'><input type='checkbox' onChange={() => setCanSee(!canSee)} /> <label>비밀번호 확인</label></div>
            <button id='submit' className='btn btn-error text-white text-lg w-[396px] mt-[24px]' onClick={() => Sumbit()}>로그인</button>
            <div className='flex justify-evenly w-[396px] mt-[12px]'>
                <a href='/'>아이디찾기</a>
                <a href='/'>비밀번호찾기</a>
                <a href='/account/register'>회원가입</a>
            </div>
            <label className='text-gray-400 text-sm mt-[50px]'>Copyright © 2019 11Street Co.,Ltd. All Rights Reserved.</label>
        </div>
    </div>
}
