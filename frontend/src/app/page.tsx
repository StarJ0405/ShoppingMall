import { getProductRecentList } from "@/app/API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
  const productList = await getProductRecentList(0);
  return <Page productList={productList} />;
}
