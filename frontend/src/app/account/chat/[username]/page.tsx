import { getCategories } from "@/app/API/NonUserAPI"
import Page from "./page_csr";

export default async function Home({ params }: { params: any }) {
    const categories = await getCategories();
    return <Page categories={categories} target={params.username} />
}