"use client";
import { Client, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export function getSocket(username: string) {
    const Socket = new Client({
        webSocketFactory: () => {
            return new SockJS("http://localhost:8080/ws-stomp");
        },
        beforeConnect: () => {
            console.log("beforeConnect");
        },
        debug(str) {
            console.log(`debug`, str);
        },
        reconnectDelay: 50000, // 자동 재연결
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: () => {
            Socket.subscribe("/sub/alram/" + username, () => { console.log(username + " recive alarm") })
        }
    });
    Socket.activate();
    return Socket;
}
export function unsubscribe(Socket: any, destination: string) {
    try {
        Socket.unsubscribe(destination);
    } catch (ex) {
        console.log("실패..");
        const time = setInterval(() => {
            unsubscribe(Socket, destination);
            clearInterval(time);
        }, 1000);
    }
}
export function subscribe(Socket: any, destination: string, onActive: (e: any) => void) {
    try {
        Socket.subscribe(destination, (e: any) => onActive(e));
    } catch (ex) {
        const time = setInterval(() => {
            subscribe(Socket, destination, onActive);
            clearInterval(time);
        }, 1000);
    }
}
export function publish(Socket: any, destination: string, message: any) {
    try {
        Socket.publish({ destination: destination, body: JSON.stringify(message) })
    } catch (ex) {
        const time = setInterval(() => {
            publish(Socket, destination, message);
            clearInterval(time);
        }, 1000);
    }
}
