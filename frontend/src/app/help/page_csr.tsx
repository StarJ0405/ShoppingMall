"use client";
import { useEffect, useState } from "react";
import Main from "../Global/Layout/MainLayout";
import { deleteArticle, getRecent, getUser, postArticle, updateArticle } from "../API/UserAPI";
import Modal from "../Global/Modal";
import { getDateTimeFormat } from "../Global/Method";
import { getArticleList, getCategories } from "../API/NonUserAPI";

interface pageProps {
    categories: any[];
    articleList: any[];
    maxPage: number;
}

export default function Page(props: pageProps) {
    const [user, setUser] = useState(null as any);
    const ACCESS_TOKEN = typeof window == 'undefined' ? null : localStorage.getItem('accessToken');
    const [recentList, setRecentList] = useState(null as unknown as any[]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [articleList, setArticleList] = useState(props.articleList);
    const [page, setPage] = useState(0);
    const [maxPage, setMaxPage] = useState(props.maxPage);
    const [focus, setFocus] = useState(-1);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [isModify, setIsModify] = useState(false);
    const [categories, setCategories] = useState(props.categories);
    useEffect(() => {
        if (ACCESS_TOKEN)
            getUser()
                .then(r => {
                    setUser(r);
                })
                .catch(e => console.log(e));
        getRecent()
            .then(r => setRecentList(r))
            .catch(e => console.log(e));
        getCategories().then(r => setCategories(r)).catch(e => console.log(e));
        getArticleList({ Type: 1, Page: 0 })
            .then(r => {
                setArticleList(r.content);
                setMaxPage(r.totalPages);
            }).catch(e => console.log(e));
    }, [ACCESS_TOKEN]);
    function openModal() {
        setIsModalOpen(true);
        setTitle('');
        setContent('');
        setIsModify(false);
    }
    function renew() {
        let newList = [] as any[];
        for (let i = 0; i <= page; i++) {
            getArticleList({ Type: 1, Page: i }).then(r => {
                newList = [...newList, ...r.content];
                setMaxPage(r.totalPages);
                setArticleList(newList);
            });
        }
    }
    return <Main categories={categories} recentList={recentList} setRecentList={setRecentList} user={user}>
        <div className="w-[1240px] flex flex-col">
            <div className="flex justify-between">
                <label className="font-bold text-xl">FAQ</label>
                <div className="flex items-center">
                    <button className="btn btn-info btn-sm w-[100px] text-white" onClick={() => {
                        location.href = "/account/chat/admin";
                    }}>문의하기</button>
                    {user?.role == "ADMIN" ? <button className="btn btn-outline btn-sm ml-2" onClick={openModal}>등록하기</button> : <></>}
                </div>
                <Modal open={isModalOpen} onClose={() => setIsModalOpen(false)} className="" escClose={true} outlineClose={true}>
                    <div className="flex flex-col w-[744px] h-[452px]">
                        <div className="text-white bg-red-500 h-[37px] py-2 px-4">FAQ 등록</div>
                        <div className="px-4 flex flex-col">
                            <input type="text" className="input input-bordered mt-4" placeholder="제목" defaultValue={title} onChange={e => setTitle(e.target.value)} />
                            <textarea className="input input-bordered mt-4 resize-none h-[250px] p-4" defaultValue={content} placeholder="내용" onChange={e => setContent(e.target.value)} />
                        </div>

                        <div className="flex justify-center mt-6">
                            {isModify ?
                                <button className="btn btn-info btn-sm mr-4 text-white" disabled={title == "" || content == ""} onClick={() => {
                                    if (title == '' || content == '')
                                        return;
                                    updateArticle({ articleId: articleList[focus]?.id, title: title, content: content, type: 1 }).then(r => {
                                        renew();
                                        setIsModalOpen(false);
                                    }).catch(e => console.log(e));
                                }}>수정</button>
                                :
                                <button className="btn btn-info btn-sm mr-4 text-white" disabled={title == "" || content == ""} onClick={() => {
                                    if (title == '' || content == '')
                                        return;
                                    postArticle({ title: title, content: content, type: 1 }).then(r => {
                                        renew();
                                        setIsModalOpen(false);
                                        setFocus(-1);
                                    }).catch(e => console.log(e));
                                }}>등록</button>
                            }
                            <button className="btn btn-error btn-sm text-white" onClick={() => setIsModalOpen(false)}>취소</button>
                        </div>
                    </div>
                </Modal>
            </div>
            <div className="flex flex-col items-center">
                {articleList?.map((article, index) => <div key={index} className="w-full flex flex-col">
                    <div className="p-4 cursor-pointer border-y border-gray-500 mt-4 flex justify-between" onClick={() => setFocus(focus == index ? -1 : index)}>
                        <label>{article?.title}</label>
                        <label>{getDateTimeFormat(article?.createDate)}</label>
                    </div>
                    {focus == index ?
                        <div className="resize-none pl-8 mt-2 w-full flex flex-col" >
                            <div className="border-gray-500 rounded-lg p-4 min-h-[200px] bg-yellow-100">{article?.content}</div>
                            <div className="self-end">
                                {user?.role == "ADMIN" ? <button className="btn btn-xs text-white btn-warning w-[100px] mr-2" onClick={() => {
                                    setIsModalOpen(true);
                                    setTitle(article?.title);
                                    setContent(article?.content);
                                    setIsModify(true);
                                }}>수정하기</button> : <></>}
                                {user?.role == "ADMIN" ? <button className="btn btn-xs text-white btn-error w-[100px]" onClick={() => {
                                    deleteArticle(article.id).then(() => {
                                        renew();
                                        setFocus(-1);
                                    }).catch(e => console.log(e));
                                }}>삭제하기</button> : <></>}
                            </div>
                        </div>
                        :
                        <></>
                    }
                </div>)}
                {maxPage != 0 && page != (maxPage - 1) ? <button className="btn text-lg btn-outline w-[150px] mt-4" onClick={() => {
                    setPage(page + 1);
                    getArticleList({ Type: 1, Page: (page + 1) }).then(r => {
                        const newList = [...articleList, ...r.content];
                        setMaxPage(r.totalPages);
                        setArticleList(newList);
                    })
                }}>더보기</button> : <></>}
            </div>
        </div>
    </Main>
}