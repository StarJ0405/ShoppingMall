import { getCategories } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
    const categoreis = await getCategories();
    return <Page categories={categoreis} />;
}