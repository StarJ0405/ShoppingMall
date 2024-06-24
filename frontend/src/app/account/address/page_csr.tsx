"use client"

import { deleteAddress, getAddress, getRecent, getUser, postAddress, updateAddress } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { PhoneNumberCheck } from "@/app/Global/Method";
import Modal from "@/app/Global/Modal";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react"



interface pageProps {
    categories: any[];
}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [createModal, setCreateModal] = useState(false);
    const [name, setName] = useState('');
    const [who, setWho] = useState('');
    const [phoneNumber, setPhoneNumber] = useState("");
    const [postNumber, setPostNumber] = useState(-1);
    const [address, setAddress] = useState('');
    const [detail, setDetail] = useState('');
    const [addresses, setAddresses] = useState(null as unknown as any[]);
    const [delivery, setDelivery] = useState('');
    const [selectAddress, setSelectAddress] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getAddress()
                        .then(r => setAddresses(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    function openCreateModal() {
        setName('');
        setWho('');
        setPhoneNumber('');
        setPostNumber(-1);
        setAddress('');
        setDetail('');
        setDelivery('');
        setCreateModal(true);
    }
    function openModifyModal(target: any) {
        setName(target.title);
        setWho(target.recipient);
        setPhoneNumber(target.phoneNumber);
        setPostNumber(target.postNumber);
        setAddress(target.mainAddress);
        setDetail(target.addressDetail);
        setDelivery(target.deliveryMessage)
        setSelectAddress(target);
    }
    return <Profile recentList={recentList} categories={props.categories} setRecentList={setRecentList} user={user}>
        <label className="text-xl font-bold"><label className="text-red-500">배송지 </label>관리</label>
        <table className="text-center">
            <thead>
                <tr>
                    {/* <th className="w-[24px]"><input type="checkbox" /> </th> */}
                    <th className="w-[100px]">별칭</th>
                    <th className="w-[100px]">받는사람</th>
                    <th className="w-[125px]">받는사람 번호</th>
                    <th className="w-[80px]">우편번호</th>
                    <th>주소</th>
                    <th>상세주소</th>
                    <th>배송메시지</th>
                    <th className="w-[40px]"> </th>
                </tr>
            </thead>
            <tbody>
                {addresses?.map((address, index) => <tr key={index}>
                    {/* <td><input name="check" type="checkbox" /></td> */}
                    <td>{address.title}</td>
                    <td>{address.recipient}</td>
                    <td>{address.phoneNumber}</td>
                    <td>{address.postNumber}</td>
                    <td>{address.mainAddress}</td>
                    <td>{address.addressDetail}</td>
                    <td>{address.deliveryMessage}</td>
                    <td>
                        <button className="btn btn-sm btn-warning p-1 text-white hover:text-black" onClick={() => openModifyModal(address)}>수정</button>
                        <button className="btn btn-sm btn-error p-1 text-white hover:text-black" onClick={() => { if (confirm(address.title + '를 삭제하시겠습니까?')) deleteAddress([address.addressId]).then(r => setAddresses(r)).catch(e => console.log(e)) }}>삭제</button>
                    </td>
                </tr>)}
            </tbody>
        </table>
        <button className="btn btn-info btn-xs w-[120px] text-white" onClick={() => setCreateModal(true)}>배송지 추가하기</button>
        <Modal open={createModal} onClose={() => setCreateModal(false)} escClose={true} outlineClose={true} className="">
            <table className="mt-8">
                <tbody>
                    <tr className="h-[50px]">
                        <th className="w-[150px] min-w-[150px]">별칭</th>
                        <td className="w-full">
                            <input className="input input-sm input-info" autoFocus onChange={e => setName(e.target.value)} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>받는사람</th>
                        <td>
                            <input type="text" className="input input-sm input-info" onChange={e => setWho(e.target.value)} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>받는사람 번호</th>
                        <td>
                            <input type="text" className="input input-sm input-info" onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')); setPhoneNumber(e.target.value); }} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>우편번호</th>
                        <td>
                            <input type="number" className="input input-sm input-info" onChange={e => { let value = Number(e.target.value); value = value < 0 ? 0 : (value > 99999 ? 99999 : value); setPostNumber(value); e.target.value = value.toString() }} max={99999} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>주소</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setAddress(e.target.value)} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>상세주소</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setDetail(e.target.value)} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>배송 메시지</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setDelivery(e.target.value)} />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className="flex ml-10 mb-10">
                <button onClick={() => { setCreateModal(false); postAddress({ title: name, recipient: who, phoneNumber: phoneNumber, mainAddress: address, addressDetail: detail, postNumber: postNumber, deliveryMessage: delivery }).then(r => setAddresses(r)).catch(e => console.log(e)) }} className="text-white cursor-pointer btn btn-info mr-2 btn-sm">저장</button>
                <button onClick={() => setCreateModal(false)} className="text-white cursor-pointer btn btn-info btn-sm">취소</button>
            </div>
        </Modal>
        <Modal open={selectAddress != null} onClose={() => setSelectAddress(null)} escClose={true} outlineClose={true} className="">
            <table className="mt-8">
                <tbody>
                    <tr className="h-[50px]">
                        <th className="w-[150px] min-w-[150px]">별칭</th>
                        <td className="w-full">
                            <input className="input input-sm input-info" autoFocus onChange={e => setName(e.target.value)} defaultValue={selectAddress?.title} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>받는사람</th>
                        <td>
                            <input type="text" className="input input-sm input-info" onChange={e => setWho(e.target.value)} defaultValue={selectAddress?.recipient} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>받는사람 번호</th>
                        <td>
                            <input type="text" className="input input-sm input-info" onChange={e => { PhoneNumberCheck(e); setPhoneNumber(e.target.value.replaceAll('-', '')); setPhoneNumber(e.target.value); }} defaultValue={selectAddress?.phoneNumber} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>우편번호</th>
                        <td>
                            <input type="number" className="input input-sm input-info" onChange={e => { let value = Number(e.target.value); value = value < 0 ? 0 : (value > 99999 ? 99999 : value); setPostNumber(value); e.target.value = value.toString() }} max={99999} defaultValue={selectAddress?.postNumber} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>주소</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setAddress(e.target.value)} defaultValue={selectAddress?.mainAddress} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>상세주소</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setDetail(e.target.value)} defaultValue={selectAddress?.addressDetail} />
                        </td>
                    </tr>
                    <tr className="h-[50px]">
                        <th>배송 메시지</th>
                        <td>
                            <input type="text" className="input input-sm input-info w-1/2" onChange={e => setDelivery(e.target.value)} defaultValue={selectAddress?.deliveryMessage} />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className="flex ml-10 mb-10">
                <button onClick={() => { setSelectAddress(null); updateAddress(selectAddress.addressId, { title: name, recipient: who, phoneNumber: phoneNumber, mainAddress: address, addressDetail: detail, postNumber: postNumber, deliveryMessage: delivery }).then(r => setAddresses(r)).catch(e => console.log(e)) }} className="text-white cursor-pointer btn btn-info mr-2 btn-sm">저장</button>
                <button onClick={() => setSelectAddress(null)} className="text-white cursor-pointer btn btn-info btn-sm">취소</button>
            </div>
        </Modal>
    </Profile>
}