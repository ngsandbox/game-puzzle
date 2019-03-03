"use strict";
let gameListenerClient;
$(function () {
});

function initStompClient(onConnection) {
    const client = Stomp.over(new SockJS('/game-websocket'));
    client.connect({}, function (frame) {
        console.info('Connected to the system ', frame);
        onConnection(client);
    });

    return client;
}

function disconnect() {
    if (gameListenerClient) {
        console.warn("Disconnect for the system");
        gameListenerClient.disconnect();
    }
}

function connect() {
    initStompClient(function (client) {
        gameListenerClient = client;
        gameListenerClient.subscribe('/topic/game', function (pushMessage) {
            renderMessage(JSON.parse(pushMessage.body));
        });
    });
}

function renderMessage(message) {
    console.log("Received message", message);
    if (message.id && message.status) {
    }
}