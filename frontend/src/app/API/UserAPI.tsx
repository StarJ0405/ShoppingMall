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
        if (error.response.status === 401 && (error.response.data == '' || error.response.data == null)) {
            await refreshAccessToken();
            return UserApi(originalRequest);
        } else if (error.response.status === 403 && (error.response.data == '' || error.response.data == null)) {
            localStorage.clear();
            window.location.href = '/account/login';
            return;
        }
    return Promise.reject(error);
});
// 토큰 갱신
const refreshAccessToken = async () => {
    const response = await UserApi.get('/api/auth/refresh');
    const TOKEN_TYPE = localStorage.getItem('tokenType');
    const ACCESS_TOKEN = response.data;
    localStorage.setItem('accessToken', ACCESS_TOKEN);
    UserApi.defaults.headers.common['Authorization'] = `${TOKEN_TYPE} ${ACCESS_TOKEN}`;
}

export const getUser = async () => {
    const response = await UserApi.get('/api/user');
    return response.data;
}
interface UpdateProps {
    name: string,
    email: string,
    phoneNumber: string,
    nickname: string,
    password: string,
    newPassword: string,
    url: string
}
export const updateUser = async (data: UpdateProps) => {
    const response = await UserApi.put('/api/user', data);
    return response.data;
}
export const updateUserPassword = async (data: UpdateProps) => {
    const response = await UserApi.put('/api/user/password', data);
    return response.data;
}
export const deleteUser = async () => {
    await UserApi.delete('/api/user');
}

export const checkWish = async (data: number) => {
    const response = await UserApi.get('/api/user/wishList/check', { headers: { 'ProductId': data } });
    return response.data;
}
export const postWish = async (data: number) => {
    const response = await UserApi.post('/api/user/wishList', { productId: data });
    return response.data;
}
export const getWishList = async () => {
    const response = await UserApi.get('/api/user/wishList');
    return response.data;
}
export const deleteWish = async (data: number) => {
    const response = await UserApi.delete('/api/user/wishList', { headers: { 'productId': data } });
    return response.data;
}
export const deleteWishList = async (data: any[]) => {
    const response = await UserApi.delete('/api/user/wishList/Multi', { headers: { 'productIdList': data } });
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
    tagList: string[],
    url: string,
    optionLists: any
}
export const productRegist = async (data: productProps) => {
    const response = await UserApi.post('/api/product', data);
    return response.data;
}
export const deleteImage = async () => {
    const response = await UserApi.delete('/api/image');
    return response.data;
}
export const saveImage = async (data: any) => {
    const response = await UserApi.post('/api/image', data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}
export const saveImageList = async (data: any) => {
    const response = await UserApi.post('/api/image/list', data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
}
export const deleteImageList = async () => {
    const response = await UserApi.delete('/api/image/list');
    return response.data;
}
export const getRecent = async () => {
    const response = await UserApi.get('/api/recent');
    return response.data;
}
export const postRecent = async (data: number) => {
    const response = await UserApi.post('/api/recent', { productId: data });
    return response.data;
}
export const deleteRecent = async (data: number) => {
    const response = await UserApi.delete('/api/recent', { headers: { 'RecentId': data } });
    return response.data;
}

export const getCartList = async () => {
    const response = await UserApi.get('/api/cart/cartList');
    return response.data;
}
interface cartProps {
    productId: number;
    optionIdList: number[];
    count: number;
}

export const postCartList = async (data: cartProps) => {
    const response = await UserApi.post('/api/cart/cartList', data);
    return response.data;
}

export const deleteCartList = async (data: number) => {
    const response = await UserApi.delete('/api/cart/cartList', { headers: { cartItemId: data } });
    return response.data;
}

export const updateCartList = async (id: number, count: number) => {
    const response = await UserApi.put('/api/cart/cartList', { cartItemId: id, count: count })
    return response.data;
}
interface addressProps {
    title: string;
    recipient: string;
    phoneNumber: string;
    mainAddress: string;
    addressDetail: string;
    postNumber: number;
    deliveryMessage: string;
}
export const postAddress = async (data: addressProps) => {
    const response = await UserApi.post('/api/address', data);
    return response.data;
}
export const getAddress = async () => {
    const response = await UserApi.get('/api/address');
    return response.data;
}

export const deleteAddress = async (numbers: number[]) => {
    const response = await UserApi.delete('/api/address', { headers: { AddressIdList: numbers as any } })
    return response.data;
}
export const updateAddress = async (id: number, data: addressProps) => {
    const response = await UserApi.put('/api/address', { addressId: id, ...data });
    return response.data;
}
interface postPayment {
    cartItemIdList: number[];
    recipient: string;
    phoneNumber: string;
    mainAddress: string;
    addressDetail: string;
    postNumber: string;
    deliveryMessage: string;
}
export const postPayment = async (data: postPayment) => {
    const response = await UserApi.post('/api/payment/logList', data);
    return response.data;
}