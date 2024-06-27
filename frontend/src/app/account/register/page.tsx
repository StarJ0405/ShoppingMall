'use client'
import { SignUp } from '@/app/API/AuthAPI';
import { Check, KeyDownCheck, Move, PhoneNumberCheck, checkInput } from '@/app/Global/Method';

import { useState } from 'react'


export default function Page() {
    const [focusing, setFocusing] = useState(0);
    const [preKey, setPreKey] = useState('');

    const [username, setUsername] = useState('');
    const [password1, setPassword1] = useState('');
    const [password2, setPassword2] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [nickname, setNickname] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [role, setRole] = useState(-1);
    const [birthday, setBirthday] = useState('');
    const [gender, setGender] = useState(-1);

    const [error, setError] = useState('');

    const input_name = ['아이디를 입력해주세요', '비밀번호을 입력해주세요', '이름을 입력해주세요', '이메일을 입력해주세요', '닉네임을 입력해주세요', '전화번호를 입력해주세요', '역할을 골라주세요', '생일을 입력해주세요', '성별을 골라주세요']
    function IsDisabled() {
        return username == '' || password1 == '' || password2 == '' || name == '' || email == '' || nickname == '' || phoneNumber == '' || role == -1 || birthday == '' || gender == -1;
    }

    function Submit() {
        if (!Check('^([A-Za-z0-9_]){3,24}$', username))
            return setError('아이디는 3글자 이상의 영어 대/소문자와 숫자로만 구성 가능합니다.');
        if (!Check('^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-+={}~?:;`|/]).{6,24}$', password1))
            return setError('비밀번호는 최소 6, 최대 24자로 대문자, 소문자, 숫자, 특수문자(!@#$%^&*()-+={}~?:;`)가 각각 한개씩 들어가 있어야합니다.');
        if (password1 !== password2)
            return setError('비밀번호가 일치하지 않습니다.');
        if (!Check('^([가-힣]){2,}$', name))
            return setError('이름은 한글 2글자 이상으로 구성되어야 합니다.');
        if (birthday === '')
            return setError('생일을 입력해주세요.');
        if (!Check('^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$', email))
            return setError('이메일 형식이 맞지 않습니다.');
        if (!Check('^[A-Za-z가-힣0-9_\s]{2,24}$', nickname))
            return setError('닉네임은 한글,영어,숫자만 가능하며 최대 24자까지 가능합니다.');
        if (!Check('^0[0-9]{10}$', phoneNumber))
            return setError('전화번호 형식이 맞지 않습니다.(###-####-####)');
        if (role == -1)
            return setError('역할을 골라주세요.');
        if (gender == -1)
            return setError('성별을 골라주세요.');
        SignUp({ username: username, password: password1, name: name, email: email, nickname: nickname, phoneNumber: phoneNumber, role: role, birthday: birthday, gender: gender })
            .then(() => {
                window.location.href = "/account/login";
            }).catch(error => {
                switch (error.response.data) {
                    case 'username': { setError('유저네임 중복'); break; }
                    case 'email': { setError('이메일 중복'); break; }
                    case 'nickname': { setError('닉네임 중복'); break; }
                    case 'phone': { setError('전화번호 중복'); break; }
                    default:
                        console.log(error);
                }
            });
    }
    return <div className='w-full h-full flex justify-center'>
        <div className='flex flex-col w-[960px] pt-[20px] items-center'>
            <div className='flex items-center h-[85px] self-start p-2'>
                <a href='/account/login'><img src='/logo.png' style={{ width: '69px', height: '50px' }} /></a>
                <label className='ml-3 text-3xl font-bold'>회원가입</label>
            </div>
            <div className='divider divider-neutral mt-0'></div>
            <div className='w-[400px] flex flex-col justify-center'>
                <label className='font-bold text-2xl mt-[80px]'>{input_name[focusing]}</label>
                <label className='font-bold text-sm text-red-500'>{error}</label>
                <input id='username' type='text' className='w-[400px] text-xl mt-[24px]' autoFocus placeholder='아이디' onFocus={e => { e.target.placeholder = ''; setFocusing(0) }} onBlur={e => e.target.placeholder = '아이디'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, next: () => Move('password') }); }} onChange={e => setUsername(e.target.value)} maxLength={24} onKeyUp={e => checkInput(e, '^([A-Za-z0-9_]){3,24}$', () => setError(''), () => setError('아이디는 3글자 이상의 영어 대/소문자와 숫자로만 구성 가능합니다.'))} />
                <input id='password1' type='password' className='w-[400px] text-xl mt-[24px]' placeholder='비밀번호' onFocus={e => { e.target.placeholder = ''; setFocusing(1) }} onBlur={e => e.target.placeholder = '비밀번호'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('username'), next: () => Move('password2') }) }} onChange={e => setPassword1(e.target.value)} maxLength={24} onKeyUp={e => checkInput(e, '^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-+={}~?:;`|/]).{6,24}$', () => setError(''), () => setError('비밀번호는 최소 6, 최대 24자로 대문자, 소문자, 숫자, 특수문자(!@#$%^&*()-+={}~?:;`)가 각각 한개씩 들어가 있어야합니다.'))} />
                <input id='password2' type='password' className='w-[400px] text-xl mt-[24px]' placeholder='비밀번호 확인' onFocus={e => { e.target.placeholder = ''; setFocusing(1) }} onBlur={e => e.target.placeholder = '비밀번호'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('password1'), next: () => Move('name') }) }} onChange={e => setPassword2(e.target.value)} maxLength={24} onKeyUp={e => { if (password1 !== password2) setError('비밀번호가 일치하지 않습니다.'); else setError('') }} />
                <input id='name' type='text' className='w-[400px] text-xl mt-[24px]' autoFocus placeholder='이름' onFocus={e => { e.target.placeholder = ''; setFocusing(2) }} onBlur={e => e.target.placeholder = '이름'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('password2'), next: () => Move('birthday') }); }} onChange={e => setName(e.target.value)} maxLength={5} onKeyUp={e => checkInput(e, '^([가-힣]){2,}$', () => setError(''), () => setError('이름은 한글 2글자 이상으로 구성되어야 합니다.'))} />
                <input id='birthday' type='date' className='w-[400px] text-xl mt-[24px]' placeholder='생년월일' onFocus={e => { e.target.placeholder = ''; setFocusing(7) }} onBlur={e => e.target.placeholder = '생년월일'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('name'), next: () => Move('email') }) }} onChange={e => setBirthday(e.target.value)} max={"9999-12-31"} />
                <input id='email' type='email' className='w-[400px] text-xl mt-[24px]' placeholder='이메일' onFocus={e => { e.target.placeholder = ''; setFocusing(3) }} onBlur={e => e.target.placeholder = '이메일'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('birthday'), next: () => Move('nickname') }) }} onChange={e => setEmail(e.target.value)} onKeyUp={e => checkInput(e, '^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$', () => setError(''), () => setError('이메일 형식이 맞지 않습니다.'))} />
                <input id='nickname' type='text' className='w-[400px] text-xl mt-[24px]' placeholder='닉네임' onFocus={e => { e.target.placeholder = ''; setFocusing(4) }} onBlur={e => e.target.placeholder = '닉네임'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('email'), next: () => Move('phoneNumber') }) }} onChange={e => setNickname(e.target.value)} maxLength={24} onKeyUp={e => checkInput(e, '^[A-Za-z가-힣0-9_\s]{2,24}$', () => setError(''), () => setError('닉네임은 한글,영어,숫자만 가능하며 최대 24자까지 가능합니다.'))} />
                <input id='phoneNumber' type='text' className='w-[400px] text-xl mt-[24px]' placeholder='전화번호' onFocus={e => { e.target.placeholder = ''; setFocusing(5) }} onBlur={e => e.target.placeholder = '전화번호'} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('nickname'), next: () => Move('role') }) }} onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')) }} onKeyUp={e => { PhoneNumberCheck(e); checkInput(e, '^0[0-9]{2}-[0-9]{4}-[0-9]{4}$', () => setError(''), () => setError('전화번호 형식이 맞지 않습니다.(###-####-####)')); }} maxLength={13} />
                <select id='role' defaultValue={-1} className='w-[400px] text-xl mt-[24px]' onFocus={() => setFocusing(6)} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('phoneNumber'), next: () => Move('birthday') }) }} onChange={e => setRole(Number(e.target.selectedOptions[0].value))}>
                    <option disabled value={-1}>역할을 골라주세요</option>
                    <option value={0}>구매자</option>
                    <option value={1}>판매자</option>
                </select>
                <div className='flex w-[120px] justify-between mt-[24px]'>
                    <input type='radio' id='male' name='gender' onFocus={() => setFocusing(8)} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('birthday'), next: () => document.getElementById('submit')?.click() }) }} onChange={() => setGender(0)} /><label>남성</label>
                    <input type='radio' id='female' name='gender' onFocus={() => setFocusing(8)} onKeyDown={e => { KeyDownCheck({ preKey, setPreKey, e: e, pre: () => Move('male'), next: () => document.getElementById('submit')?.click() }) }} onChange={() => setGender(1)} /><label>여성</label>
                </div>
                <button id='submit' className='btn btn-error text-white w-[400px] mt-[50px] mx-0' disabled={IsDisabled()} onClick={() => Submit()}>확인</button>
            </div>
        </div>
    </div>
}