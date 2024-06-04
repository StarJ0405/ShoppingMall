import axios from 'axios';

export function getAPI(){
    const api = axios.create({
        baseURL: 'http://www.starj.o-r.kr',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    return api;
}