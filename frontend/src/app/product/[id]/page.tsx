import { getCategories, getProduct, getWho } from "@/app/API/NonUserAPI";
import Page from "./page_csr";


export default async function Home({ params }: { params: any }) {
    const product = await getProduct(params.id);
    const seller = await getWho(product.authorUsername);
    const categories = await getCategories();
    const topCategory = categories.filter((cateogry:any) => cateogry.name == product.topCategoryName)[0]
    const middleCateogry =  topCategory?.categoryResponseDTOList.filter((category:any)=>category.name == product.middleCategoryName)[0];
    return <Page product={product} seller={seller} topCategory={topCategory} middleCategory={middleCateogry} categories={categories}></Page>;
}