

import { getCategories, getSearch } from "@/app/API/NonUserAPI";
import Page from "./page_csr";


export default async function Home({ searchParams }: { searchParams: any }) {
    const categories = await getCategories();
    const page= searchParams?.page ? searchParams?.page : 0;
    const sort = searchParams?.sort ? searchParams?.sort : 0;
    const keyword = searchParams?.keyword ? searchParams?.keyword : '' ;
    const search = await getSearch({ Page: page, Sort: sort, Keyword: encodeURIComponent(keyword)});
    return <Page categories={categories} search={search} keyword={keyword} page={page} sort={sort}/>;
}