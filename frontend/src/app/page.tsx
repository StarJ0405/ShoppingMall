import { getCategories, getProductRecentList } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
  const productList = await getProductRecentList(0);
  const categories = await getCategories();
  return <Page productList={productList} categories={categories} />;
}
