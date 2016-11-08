//Node-JS Imports
var request = require('request');
var command_base = require('../chat/base.js');
var HitboxChatClient = require("hitbox-chat");

//Setup the export
var chat = function(user, password, chan) {

    var client = new HitboxChatClient({
        name: user,
        pass: password
    });
    client.on("connect", function() {
        // handle connect
        var channel = client.joinChannel(chan.toLowerCase());
        channel.on("login", function(name, role) {})

        channel.on("chat", function(name, message, role) {
            console.log("[Hitbox][" + name + "] : " + message);
            if (role == "guest") {
                var res = command_base.chat(message, name, 0);
                if (res) {
                    channel.sendMessage(res);
                }
            }
            if (role == "anon") {
                var res = command_base.chat(message, name, 1);
                if (res) {
                    channel.sendMessage(res);
                }
            }
            if (role == "user") {
                var res = command_base.chat(message, name, 3);
                if (res) {
                    channel.sendMessage(res);
                }
            }
            if (role == "admin") {
                var res = command_base.chat(message, name, 4);
                if (res) {
                    channel.sendMessage(res);
                }
            }
        })

    });
};

//Export The function
module.exports.chat = chat;
