

import { getCategories, getCategorySearch } from "@/app/API/NonUserAPI";
import Page from "./page_csr";


export default async function Home({ searchParams }: { searchParams: any }) {
    const categories = await getCategories() as any[];
    const CategoryId = searchParams.CategoryId ? searchParams.CategoryId : 0;
    const page = searchParams?.page ? searchParams?.page : 0;
    const sort = searchParams?.sort ? searchParams?.sort : 0;
    const keyword = searchParams?.keyword ? searchParams?.keyword : '';
    let topCategory = null;
    let secondCategory = null;
    let bottomCatrgory = null;
    categories.forEach(top =>
        (top.categoryResponseDTOList as any[])?.forEach(second =>
            (second.categoryResponseDTOList as any[])?.forEach(bottom => {
                if (bottom.id == CategoryId) {
                    topCategory = top;
                    secondCategory = second;
                    bottomCatrgory = bottom;
                };
            })
        )
    );

    const search = await getCategorySearch({ CategoryId: CategoryId, Page: page, Sort: sort, Keyword: encodeURIComponent(keyword) });
    return <Page categories={categories} search={search} keyword={keyword} page={page} sort={sort} CategoryId={CategoryId} topCategory={topCategory} secondCategory={secondCategory} bottomCatrgory={bottomCatrgory}/>;
}