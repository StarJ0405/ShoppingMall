'use client'
import { getUser } from '@/app/API/UserAPI';
import { PhoneString } from '@/app/Global/Method';
import { redirect } from 'next/navigation';
import { useEffect, useState } from 'react';
import Profile from '@/app/Global/Layout/ProfileLayout';

export default function Home() {
  const [user, setUser] = useState(null as any);
  const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
  useEffect(() => {
    if (ACCESS_TOKEN)
      getUser()
        .then(r => {
          setUser(r);
        })
        .catch(e => console.log(e));
    else
      redirect('/account/login');
  }, [ACCESS_TOKEN]);
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
      <div>
        <label className='font-bold text-lg'>{user?.nickname}님의 11번가 기본정보</label>
      </div>
      <table className='text-left text-base'>
        <tbody>
          <tr>
            <th className='w-[159px]'>이름</th>
            <td>{user?.name}</td>
          </tr>
          <tr>
            <th>비밀번호</th>
            <td>변경하기</td>
          </tr>
          <tr>
            <th>생년월일</th>
            <td>{user?.birthday}</td>
          </tr>
          <tr>
            <th>휴대전화</th>
            <td>{PhoneString(user?.phoneNumber)}</td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>{user?.email}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </Profile>;
}
