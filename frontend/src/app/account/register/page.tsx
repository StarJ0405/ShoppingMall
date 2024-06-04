'use client'
import { KeyDownCheck, Move } from '@/app/Global/Method';
import { useState } from 'react'


export default function Page(){
    const [focusing, setFocusing] = useState(0);
    const [preKey, setPreKey] = useState('');

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [nickanme, setNickanme] = useState('');
    const [phone, setPhone] = useState('');
    const [role, setRole] = useState(-1);
    const [birthday, setBirthday] = useState('');
    const [gender,setGender] = useState(-1);
    
    const input_name = ['이름을 입력해주세요','휴대폰 번호를 입력해주세요']
    function IsDisabled(){
        return username=='' || password=='' || email == ''||nickanme=='' || phone=='' ||role==-1 || birthday==''|| gender==-1;
    }
    function Submit(){
        console.log({username:username,password:password,email:email,nickanme:nickanme,phone:phone,role:role,birthday:birthday,gender:gender});
    }
    return <div className='w-full h-full flex justify-center'>
        <div className='flex flex-col w-[960px] pt-[20px] items-center'>
            <div className='flex items-center h-[85px] self-start p-2'>
                <a href="/account/login"><img src='/logo.png' style={{width:'69px', height:'30px'}}/></a>
                <label className='ml-3 text-3xl font-bold'>회원가입</label>
            </div>
            <div className='divider divider-neutral mt-0'></div>
            <div className='w-[400px] flex flex-col justify-center'>
                <label className='font-bold text-2xl mt-[80px]'>{input_name[focusing]}</label>
                <input id='username' type='text' className='w-[400px] text-xl mt-[24px]' autoFocus placeholder='이름' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='이름'} onKeyDown={e=>{ KeyDownCheck({preKey,setPreKey, e:e,next:()=>Move('password')});}} onChange={e=>setUsername(e.target.value)}/>
                <input id='password' type='password' className='w-[400px] text-xl mt-[24px]' placeholder='비밀번호' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='비밀번호'} onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('username'),next:()=>Move('email')})}} onChange={e=>setPassword(e.target.value)}/>
                <input id='email' type='email' className='w-[400px] text-xl mt-[24px]' placeholder='이메일' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='이메일'} onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('password'),next:()=>Move('nickname')})}} onChange={e=>setEmail(e.target.value)}/>
                <input id='nickname' type='text' className='w-[400px] text-xl mt-[24px]' placeholder='닉네임' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='닉네임'} onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('email'),next:()=>Move('phone')})}} onChange={e=>setNickanme(e.target.value)}/>
                <input id='phone' type='text' className='w-[400px] text-xl mt-[24px]' placeholder='전화번호' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='전화번호'} onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('nickname'),next:()=>Move('role')})}} onChange={e=>setPhone(e.target.value)}/>
                <select id='role' defaultValue={-1} className='w-[400px] text-xl mt-[24px]' onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('phone'),next:()=>Move('birthday')})}} onChange={e=>setRole(e.target.selectedIndex)}>
                    <option disabled value={-1}>역할을 골라주세요</option>
                    <option value={0}>판매자</option>
                    <option value={1}>구매자</option>
                </select>
                <input id='birthday' type='date' className='w-[400px] text-xl mt-[24px]' placeholder='생년월일' onFocus={e=>e.target.placeholder=''} onBlur={e=>e.target.placeholder='전화번호'} onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('role'),next:()=>Move('male')})}} onChange={e=>setBirthday(e.target.value)}/>
                <div className='flex w-[120px] justify-between mt-[24px]'>
                    <input type='radio' id='male' name='gender' onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('birthday'),next:()=>document.getElementById('submit')?.click()})}} onChange={()=>setGender(0)}/><label>남성</label>
                    <input type='radio' id='female' name='gender' onKeyDown={e=>{KeyDownCheck({preKey,setPreKey, e:e,pre:()=>Move('male'),next:()=>document.getElementById('submit')?.click()})}} onChange={()=>setGender(1)}/><label>여성</label>
                </div>
                <button id='submit' className='btn btn-error text-white w-[400px] mt-[50px] mx-0' disabled={IsDisabled()} onClick={()=>Submit()}>확인</button>
            </div>
        </div>
    </div>
}