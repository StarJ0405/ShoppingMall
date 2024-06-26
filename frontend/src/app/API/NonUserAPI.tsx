import { getAPI } from './AxiosAPI';


export const NonUserApi = getAPI();
/** 게시글 불러오기 */

export const getCategories = async () => {
    const response = await NonUserApi.get('/api/category');
    return response.data;
}
export const getProductRecentList = async (data: number) => {
    const response = await NonUserApi.get('/api/product/latest', { headers: { 'page': data } });
    return response.data;
}
export const getProductList = async () => {
    const response = await NonUserApi.get('/api/product/list');
    return response.data;
}
export const getProductBest = async () => {
    const response = await NonUserApi.get('/api/product/best');
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
export const getReviews = async (data: number) => {
    const response = await NonUserApi.get('/api/review', { headers: { 'ProductId': data } });
    return response.data;
}

interface searchProps {
    Page: number;
    Sort: number
    Keyword: string;
}
export const getSearch = async (data: searchProps) => {
    const response = await NonUserApi.get('/api/search', { headers: { ...data } });
    return response.data;
}
interface categorySearchProps {
    CategoryId: number;
    Page: number;
    Sort: number
    Keyword: string;
}
export const getCategorySearch = async (data: categorySearchProps) => {
    const response = await NonUserApi.get('/api/category/search', { headers: { ...data } });
    return response.data;
}
export const getProductQAList = async (data: number) => {
    const response = await NonUserApi.get('/api/product/question', { headers: { ProductId: data } });
    return response.data;
}

interface getArticleList {
    Type: number;
    Page: number;
}
export const getArticleList = async (data: getArticleList) => {
    const response = await NonUserApi.get('/api/article', { headers: { ...data } });
    return response.data;
}