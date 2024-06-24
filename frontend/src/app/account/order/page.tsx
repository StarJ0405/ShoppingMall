import { getCategories } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home(){
    const categories = await getCategories();
    return <Page categories={categories} />
}