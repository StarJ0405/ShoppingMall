'use client'
import { getUser } from '@/app/API/UserAPI';
import { PhoneNumberCheck, PhoneString, checkInput } from '@/app/Global/Method';
import { redirect } from 'next/navigation';
import { useEffect, useState } from 'react';
import Profile from '@/app/Global/Layout/ProfileLayout';

export default function Home() {
  const [user, setUser] = useState(null as any);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [birthday, setBirthday] = useState('');
  const [error, setError] = useState('');

  const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
  useEffect(() => {
    if (ACCESS_TOKEN)
      getUser()
        .then(r => {
          setUser(r);
          setName(r.name);
          setEmail(r.email);
          setNickname(r.nickname);
          setPhoneNumber(r.phoneNumber);
          setBirthday(r.birthday);
        })
        .catch(e => console.log(e));
    else
      redirect('/account/login');
  }, [ACCESS_TOKEN]);
  function IsDisabled() {
    return name != user?.name || email != user?.email || nickname != user?.nickname || phoneNumber != user?.phoneNumber || birthday != user?.birthday;
  }
  function Check(pattern: string, test: string) {
    return new RegExp(pattern).test(test);
  }
  function Submit() {
    if (!Check('^([가-힣]){2,}$', name))
      return setError('이름은 한글 2글자 이상으로 구성되어야 합니다.');
    if (!Check('^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$', email))
      return setError('이메일 형식이 맞지 않습니다.');
    if (!Check('^[A-Za-z가-힣0-9_\s]{2,24}$', nickname))
      return setError('닉네임은 한글,영어,숫자만 가능하며 최대 24자까지 가능합니다.');
    if (!Check('^0[0-9]{10}$', phoneNumber))
      return setError('전화번호 형식이 맞지 않습니다.(###-####-####)');
  }
  return <Profile user={user}>
    <div className='flex items-end'>
      <label className='text-xl font-bold'><label className='text-xl text-red-500 font-bold'>회원정보</label> 변경</label>
      <label className='text-xs h-[14px] border-l-2 border-gray-400 ml-2 mb-[5px] pl-2'>고객님의 회원정보를 수정하실 수 있습니다. 회원정보를 변경하시고 반드시 하단에 있는 <label className='font-bold'>확인</label> 버튼을 클릭해 주셔야 합니다.</label>
    </div>
    <div className='divider divider-neutral m-0'></div>
    <ul className='list-disc text-xs ml-5'>
      <li>11번가는 회원님의 개인정보를 신중히 취급하며, 회원님의 동의 없이는 기재하신 회원정보가 공개되지 않습니다.</li>
      <li>보다 다양한 서비스를 받으시려면 정확한 정보를 항상 유지해 주셔야합니다.</li>
      <li>전문적인 영업 목적의 경우나 사업자등록증을 보유한 경우에는 반드시 사업자셀러 회원으로 전환하여 판매하셔야 합니다.</li>
      <li>사업자셀러 회원은 전환 신청 후 증빙서류를 업로드 하셔야하며, 구비서류 승인전까지 개인셀러 회원으로 판매 가능합니다.</li>
      <li>타인의 개인정보를 도용한 피해방지 및 개인 셀러의 개인정보 보호를 위해 본인확인 과정을 실시하고 있습니다.</li>
      <li className='text-red-500'>행정구역이 변경되어 사용할 수 없는 주소는 회원정보에서 삭제됩니다.</li>
    </ul>
    <div className='mt-3 flex flex-col'>
      <label className='font-bold text-lg'>{user?.nickname}님의 11번가 기본정보</label>
      <label className='text-xs font-bold text-red-500'>{error}</label>
      <table className='text-left text-base'>
        <tbody>
        <tr className='h-[40px]'>
            <th className='w-[159px]'>프로필 이미지</th>
            <td><img src={user?.url ? user.url : '/white.png'} alt="프로필 이미지" className='w-[128px] h-128px]'/></td>
          </tr>
          <tr className='h-[40px]'>
            <th className='w-[159px]'>아이디</th>
            <td><input type="text" className='input input-bordered input-sm' defaultValue={user?.username} readOnly maxLength={24} /></td>
          </tr>
          <tr className='h-[40px]'>
            <th>비밀번호</th>
            <td><button className='btn border border-gray-300 btn-sm'>변경하기</button></td>
          </tr>
          <tr className='h-[40px]'>
            <th>이름</th>
            <td><input type="text" className='input input-bordered input-sm' defaultValue={name} onChange={e => setName(e.target.value)} maxLength={5} onFocus={e => checkInput(e, '^([가-힣]){2,}$', () => setError(''), () => setError('이름은 한글 2글자 이상으로 구성되어야 합니다.'))} onKeyUp={e => checkInput(e, '^([가-힣]){2,}$', () => setError(''), () => setError('이름은 한글 2글자 이상으로 구성되어야 합니다.'))} /></td>
          </tr>
          <tr className='h-[40px]'>
            <th>생년월일</th>
            <td><input type="date" className='input input-bordered input-sm' defaultValue={birthday} onChange={e => setBirthday(e.target.value)} max={"9999-12-31"} /></td>
          </tr>
          <tr className='h-[40px]'>
            <th>이메일</th>
            <td><input type="text" className='input input-bordered input-sm' defaultValue={email} minLength={3} onChange={e => setEmail(e.target.value)} onFocus={e => checkInput(e, '^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$', () => setError(''), () => setError('이메일 형식이 맞지 않습니다.'))} onKeyUp={e => checkInput(e, '^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$', () => setError(''), () => setError('이메일 형식이 맞지 않습니다.'))} /></td>
          </tr>
          <tr className='h-[40px]'>
            <th>닉네임</th>
            <td><input type="text" className='input input-bordered input-sm' defaultValue={nickname} maxLength={24} onChange={e => setNickname(e.target.value)} onFocus={e => checkInput(e, '^[A-Za-z가-힣0-9_\s]{2,24}$', () => setError(''), () => setError('닉네임은 한글,영어,숫자만 가능하며 최대 24자까지 가능합니다.'))} onKeyUp={e => checkInput(e, '^[A-Za-z가-힣0-9_\s]{2,24}$', () => setError(''), () => setError('닉네임은 한글,영어,숫자만 가능하며 최대 24자까지 가능합니다.'))} /></td>
          </tr>
          <tr className='h-[40px]'>
            <th>전화번호</th>
            <td><input type="text" className='input input-bordered input-sm' defaultValue={PhoneString(phoneNumber)} maxLength={13} onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')) }} onFocus={e => { PhoneNumberCheck(e); checkInput(e, '^0[0-9]{2}-[0-9]{4}-[0-9]{4}$', () => setError(''), () => setError('전화번호 형식이 맞지 않습니다.(###-####-####)')); }} onKeyUp={e => { PhoneNumberCheck(e); checkInput(e, '^0[0-9]{2}-[0-9]{4}-[0-9]{4}$', () => setError(''), () => setError('전화번호 형식이 맞지 않습니다.(###-####-####)')); }} /></td>
          </tr>
        </tbody>
      </table>
      <button id='submit' className='btn btn-error btn-sm text-white w-[400px] mx-0 mt-2' disabled={!IsDisabled()} onClick={() => Submit()}>변경하기</button>
    </div>
  </Profile>;
}
