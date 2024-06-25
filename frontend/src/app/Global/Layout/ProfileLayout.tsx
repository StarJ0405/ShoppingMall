import SideList from "./SideList";
import Main from "./MainLayout";

interface pageInterface {
    children: React.ReactNode,
    user: any
    recentList: any[];
    setRecentList: (data: any) => void;
    categories: any[];
}
export default function Profile(props: Readonly<pageInterface>) {
    const user = props?.user;
    return <Main user={user} recentList={props.recentList} setRecentList={props.setRecentList} categories={props.categories}>
        <div className="flex flex-col w-[1240px]">
            <div className="flex w-full items-end">
                <label className="font-bold text-2xl w-[185px] min-w-[185px]">나의 52번가</label>
                <label className="text-xs">52번가 속 내 정보를 한번에 확인하세요!</label>
            </div>
            <div className="flex">
                <SideList user={user} />
                <div className="flex flex-col mt-4 w-full">
                    {props.children}
                </div>
            </div>
        </div>
    </Main>
}