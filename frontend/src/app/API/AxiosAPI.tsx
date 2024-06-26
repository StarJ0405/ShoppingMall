import axios from 'axios';

export function getAPI() {
    const api = axios.create({
        baseURL: 'http://15.164.124.78:8080',
        headers: {
            'Content-Type': 'application/json;charset=utf-8;',
        },
    });
    return api;
}