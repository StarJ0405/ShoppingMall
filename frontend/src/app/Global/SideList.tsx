"use client"

interface sideListProps {
    user: any;
}
export default function SideList(props: sideListProps) {
    const user = props.user;

    return <div className="w-[165px] mr-[20px]">
        <div className="flex flex-col items-center border-2 border-gray-400 py-2 px-4 w-full">
            <img src="/sun.png" className="w-[58px] h-[58px]" />
            <a href="/account/profile">{user?.nickname}</a>
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">나의 쇼핑 내역</label>
            <a href="" className="text-sm text-gray-500 hover:underline">주문/배송조회</a>
            <a href="" className="text-sm text-gray-500 hover:underline">취소/반품/교환 신청</a>
            <a href="" className="text-sm text-gray-500 hover:underline">취소/반품/교환 현황</a>
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">나의 관심 목록</label>
            <a href="/account/wish" className="text-sm text-gray-500 hover:underline">찜한 상품</a>
            <a href="" className="text-sm text-gray-500 hover:underline">최근본 상품</a>
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">회원 정보 변경</label>
            <a href="" className="text-sm text-gray-500 hover:underline">나의 배송지 관리</a>
            <a href="" className="text-sm text-gray-500 hover:underline">회원 탈퇴</a>
        </div>
    </div>
}


