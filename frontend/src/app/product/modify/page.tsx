import { getCategories, getProduct } from "@/app/API/NonUserAPI";
import Page from "./page_csr";
import { redirect } from "next/navigation";

export default async function Home({ searchParams }: { searchParams: any }) {
    const categoreis = await getCategories();
    if (!searchParams?.id || searchParams?.id < 1)
        redirect('/');
    const product = await getProduct(searchParams?.id);

    return <Page categories={categoreis} product={product} />;
}