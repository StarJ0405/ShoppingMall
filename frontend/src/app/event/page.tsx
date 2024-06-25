import { getCategories, getEventList, getProductList } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
    const categories = await getCategories();
    const evnetList = await getEventList();
    return <Page categories={categories} eventList={evnetList}/>
}