import { getProductList } from "./API/NonUserAPI";
import Page from "./page_csr";

export default async function Home() {
  const productList = await getProductList();
  return (
    <>
      <Page productList={productList}/>
    </>
  );
}
