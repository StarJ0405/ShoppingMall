"use client";

import { getMyReviews, getRecent, getUser } from "@/app/API/UserAPI";
import Profile from "@/app/Global/Layout/ProfileLayout";
import { getDateTimeFormat } from "@/app/Global/Method";
import Modal from "@/app/Global/Modal";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

interface pageProps {
    categories: any[]
}
export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [reviewList, setReviewList] = useState(null as unknown as any[]);
    const [review, setReview] = useState(null as any);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                    getRecent()
                        .then(r => setRecentList(r))
                        .catch(e => console.log(e));
                    getMyReviews()
                        .then(r => setReviewList(r))
                        .catch(e => console.log(e));
                })
                .catch(e => console.log(e));
        else
            redirect('/account/login');
    }, [ACCESS_TOKEN]);
    console.log(reviewList);
    return <Profile categories={props.categories} user={user} recentList={recentList} setRecentList={setRecentList}>
        <div className='flex items-end'>
            <label className='text-xl font-bold'>내 <label className='text-xl text-red-500 font-bold'>상품</label>목록</label>
            <label className='text-xs h-[14px] border-l-2 border-gray-400 ml-2 mb-[5px] pl-2'>고객님의 상품으로 들어가서 상품을 수정하실 수 있습니다.</label>
        </div>
        <table className="text-center">
            <thead>
                <tr>
                    <th className="w-[600px]">상품명</th>
                    <th className="w-[200px]">리뷰제목</th>
                    <th className="w-[100px]">평점</th>
                    <th className="w-[150px]">작성일</th>
                    <th className="w-[100px]">내용</th>
                </tr>
            </thead>
            <tbody>
                {reviewList?.map((review, index) => <tr key={index}>
                    <td>
                        <a href={"/product/" + review?.productId}>{review?.productTitle}</a>
                    </td>
                    <td>{review?.title}</td>
                    <td>{review?.grade}점</td>
                    <td>{getDateTimeFormat(review?.createDate)}</td>
                    <td>
                        <button className="btn btn-xs" onClick={()=>setReview(review)}>내용</button>
                    </td>
                </tr>)}
            </tbody>
        </table>
        <Modal open={review !=null} onClose={()=>setReview(null)} escClose={true} outlineClose={true} className="">
        <div className="flex flex-col w-[744px] h-[752px]">
                <div className="text-white bg-red-500 h-[37px] py-2 px-4">리뷰</div>
                <div className="px-4 flex flex-col">
                    {/* <label className="font-bold text-sm mt-2">상품 정보</label>
                    <div className="divider divider-neutral my-2"></div> */}
                    <div className="flex font-bold text-2xl mt-4">
                        <label className="font-bold text-2xl min-w-[75px]">제목</label>
                        <label className="input input-bordered w-full">{review?.title}</label>
                    </div>
                    <div className="flex font-bold text-2xl mt-4">
                        <label className="font-bold text-2xl min-w-[75px]">내용</label>
                        <div className="input input-bordered w-full overflow-scroll h-[500px]" dangerouslySetInnerHTML={{ __html: review?.content }} />
                    </div>
                    <button className="btn btn-error btn-sm self-center mt-4 text-white" onClick={()=>setReview(null)}>닫기</button>
                </div>                
            </div>
        </Modal>
    </Profile>;
}