import { getAPI } from './AxiosAPI';


export const NonUserApi = getAPI();
/** 게시글 불러오기 */

export const getCategories = async () => {
    const response = await NonUserApi.get(`/api/category`);
    return response.data;
}

