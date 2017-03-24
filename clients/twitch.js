//Node-JS Imports
var tmi = require("tmi.js");
var command_base = require('../chat/base.js');

//Setup the export
var chat = function(user, pass, channel, logger) {
    //Setup The Client
    var options = {
        options: {
            debug: false
        },
        connection: {
            reconnect: true,
            secure: true
        },
        identity: {
            username: user,
            password: pass
        },
        channels: [channel]
    };
    var client = new tmi.client(options);

    // Connect to Twitch
    client.connect();

    //Once connected
    client.on("connected", function(address, port) {
        logger.log("Twitch has connected");
    });



    //Listen For Messages
    client.on("chat", function(channel, user, message, self) {
        logger.log("[Twitch][" + user.username + "] : " + message);
        if (!self) {
            //Get User Level
            if (user.badges) {
                if (user.badges.broadcaster == "1") {
                    var res = command_base.chat(message, user.username, 4);
                    if (res) {
                        client.say(channel, res);
                    }
                }
            } else {
                if (user.mod == "true") {
                    var res = command_base.chat(message, user.username, 3);
                    if (res) {
                        client.say(channel, res);
                    }
                }
                if (user.mod == "false") {
                    var res = command_base.chat(message, user.username, 1);
                    if (res) {
                        client.say(channel, res);
                    }
                }
            }
        }
    });
};

//Export The function
module.exports.chat = chat;
