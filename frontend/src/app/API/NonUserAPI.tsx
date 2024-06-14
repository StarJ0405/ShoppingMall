import { getAPI } from './AxiosAPI';


export const NonUserApi = getAPI();
/** 게시글 불러오기 */

export const getCategories = async () => {
    const response = await NonUserApi.get('/api/category');
    return response.data;
}
export const getProductList = async () => {
    const response = await NonUserApi.get('/api/product/list');
    return response.data;
}
export const getProduct = async (data: number) => {
    const response = await NonUserApi.get('/api/product', { headers: { 'productId': data } });
    return response.data;
}
export const getWho = async (data: string) => {
    const response = await NonUserApi.get('/api/user/who', { headers: { 'Username': data } });
    return response.data;
}
