import { getCategories, getProductBest } from "@/app/API/NonUserAPI";
import Page from "./page_csr";
import { getCartList } from "@/app/API/UserAPI";


export default async function Home({ params }: { params: any }) {
    const bestList = await getProductBest();
    const categories = await getCategories();
    return <Page bestList={bestList} categories={categories} />;
}