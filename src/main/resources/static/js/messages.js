"use strict";
let gameListenerClient;
let login;
$(function () {
    login = $(".user").text();
    connect();
    $.get("/api/v1/listen");
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

let messages = {};

function renderMessage(message) {
    console.log("Received message", message);
    if (message.id && message.login) {
        if (messages[message.id]) {
            console.log("Skip message", message.id);
            return;
        }

        messages[message.id] = message;
        let name = (login === message.login) ? login : "Enemy"
        $("#messagesContainer").prepend("" +
            "<div class='col-12'>" +
            name + ": " + message.message +
            "</div>");
    }
}