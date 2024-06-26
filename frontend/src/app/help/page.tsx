import { getArticleList, getCategories } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
    const categories = await getCategories();
    const articleList = await getArticleList({ Type: 1, Page: 0 });
    return <Page categories={categories} articleList={articleList.content} maxPage={articleList.totalPages} />
}