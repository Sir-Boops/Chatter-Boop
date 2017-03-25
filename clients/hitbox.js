//Node-JS Imports
var request = require('request');
var command_base = require('../chat/base.js');
var HitboxChatClient = require("hitbox-chat");

//Setup the export
var chat = function(user, password, chan, logger) {

    var client = new HitboxChatClient({
        name: user,
        pass: password
    });
    client.on("connect", function() {
        // handle connect
        var channel = client.joinChannel(chan.toLowerCase());
        channel.on("login", function(name, role) {
		logger.log("Hitbox Connected");
        })

        channel.on("chat", function(name, message, role) {
            if (role == "guest") {
		logger.log(JSON.stringify('{"rank":"0", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}'));
            }
            if (role == "anon") {
		logger.log(JSON.stringify('{"rank":"1", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}'));
            }
            if (role == "user") {
		logger.log(JSON.stringify('{"rank":"2", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}'));
            }
            if (role == "admin") {
		logger.log(JSON.stringify('{"rank":"4", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}'));
            }
        })

    });
};

//Export The function
module.exports.chat = chat;
