import { getProductBest } from "@/app/API/NonUserAPI";
import Page from "./page_csr";


export default async function Home({ params }: { params: any }) {
    const bestList = await getProductBest();
    
    return <Page bestList={bestList}></Page>;
}