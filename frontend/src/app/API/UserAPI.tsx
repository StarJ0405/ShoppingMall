import { getAPI } from './AxiosAPI';


export const UserApi = getAPI();

UserApi.interceptors.request.use(
    (config) => {
        const TOKEN_TYPE = localStorage.getItem('tokenType');
        const ACCESS_TOKEN = localStorage.getItem('accessToken');
        const REFRESH_TOKEN = localStorage.getItem('refreshToken');
        config.headers['Authorization'] = `${TOKEN_TYPE} ${ACCESS_TOKEN}`;
        config.headers['REFRESH_TOKEN'] = REFRESH_TOKEN;
        return config;
    },
    (error) => {
        console.log(error);
        return Promise.reject(error);
    }
);
// 토큰 유효성 검사
UserApi.interceptors.response.use((response) => {
    return response;
}, async (error) => {
    const originalRequest = error.config;
    if (!originalRequest._retry)
        if (error.response.status === 401) {
            await refreshAccessToken();
            return UserApi(originalRequest);
        } else if (error.response.status === 403) {
            localStorage.clear();
            window.location.href = `/account/login`;
            return;
        } else if (error.response.status === 500) {
            localStorage.clear();
            window.location.href = `/account/login`;
            return;
        }
    return Promise.reject(error);
});
// 토큰 갱신
const refreshAccessToken = async () => {
    const response = await UserApi.get(`/api/auth/refresh`);
    const TOKEN_TYPE = localStorage.getItem('tokenType');
    const ACCESS_TOKEN = response.data;
    localStorage.setItem('accessToken', ACCESS_TOKEN);
    UserApi.defaults.headers.common['Authorization'] = `${TOKEN_TYPE} ${ACCESS_TOKEN}`;
}

export const getUser = async () => {
    const response = await UserApi.get(`/api/user`);
    return response.data;
}

export const updateUser = async (data: any) => {
    const response = await UserApi.put(`/api/user`, data);
    return response.data;
}
export const deleteUser = async () => {
    await UserApi.delete(`/api/user`);
}
export const getWishList = async () => {
    const response = await UserApi.get(`/api/user/wishList`);
    return response.data;
}
export const deleteWishList = async (data: number) => {
    const response = await UserApi.delete(`/api/user/wishList`, { headers: { 'productId': data } });
    return response.data;
}
export const deleteWishListMultiple = async (data: any[]) => {
    const response = await UserApi.delete(`/api/user/wishList/Multi`, { headers: { 'productIdList': data } });
    return response.data;
}

interface productProps {
    categoryId: number,
    price: number,
    description: string,
    detail: string,
    dateLimit: Date,
    remain: number,
    title: string,
    delivery: string,
    address: string,
    receipt: string,
    a_s: string,
    brand: string,
    productTagList: string[],
    url:string
}
export const productRegist = async (data: productProps) => {
    const response = await UserApi.post(`/api/product`, data);
    return response.data;
}

export const saveImage = async(data:any)=>{
    const response = await UserApi.post(`/api/image`, data,{
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}