import { getAPI } from './AxiosAPI';


export const NonUserApi = getAPI();
/** 게시글 불러오기 */
interface testProps {
    id: number
    name: string
}
export const test = async (props: testProps) => {
    const response = await NonUserApi.get(`/api/article`, { headers: { ...props } });
    return response.data;
}

