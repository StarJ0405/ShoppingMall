"use client"

interface sideListProps {
    user: any;
}
export default function SideList(props: sideListProps) {
    const user = props.user;
    return <div className="w-[165px] mr-[20px]">
        <div className="flex flex-col items-center border-2 border-gray-400 py-2 px-4 w-full">
            <img src="/logo.png" className="w-[58px] h-[58px]" />
            <a href="/account/profile">{user?.nickname}</a>
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">나의 쇼핑 내역</label>
            <a href="/account/log" className="text-sm text-gray-500 hover:underline">주문/배송조회</a>
            <a href="/account/reviews" className="text-sm text-gray-500 hover:underline">내 리뷰 목록</a>
            {user?.role == "SELLER" ? <a href="/account/productList" className="text-sm text-gray-500 hover:underline">내 상품 목록</a> : <></>}
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">나의 관심 목록</label>
            <a href="/account/wish" className="text-sm text-gray-500 hover:underline">찜한 상품</a>
            <a href="/account/recent" className="text-sm text-gray-500 hover:underline">최근본 상품</a>
            <a href="/account/chatList" className="text-sm text-gray-500 hover:underline">내 채팅 목록</a>
        </div>
        <div className="flex flex-col border-x-2 border-b-2 border-gray-400 py-2 px-4 w-full">
            <label className="font-bold mb-3">회원 정보 변경</label>
            <a href="/account/address" className="text-sm text-gray-500 hover:underline">나의 배송지 관리</a>
            <a href="" className="text-sm text-gray-500 hover:underline" onClick={() => alert('다시 한번 생각해주세요.')}>회원 탈퇴</a>
        </div>
    </div>
}


