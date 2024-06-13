import axios from 'axios';

export function getAPI() {
    const api = axios.create({
        baseURL: 'http://localhost:8080',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    return api;
}