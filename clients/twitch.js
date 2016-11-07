//Node-JS Imports
var tmi = require("tmi.js");
var command_base = require('../chat/base.js');

//Setup the export
var chat = function(user, pass, channel) {
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
        console.log("Twitch has connected");
    });



    //Listen For Messages
    client.on("chat", function(channel, user, message, self) {
        if (!self) {
            console.log("[Twitch][" + user.username + "] : " + message);
            //Get User Level
            if (user.badges.broadcaster == "1") {
                client.say(channel, command_base.chat(message, user.username, 4));
            } else {
                if (user.mod == "true") {
                    client.say(channel, command_base.chat(message, user.username, 3));
                }
                if (user.mod == "false") {
                    client.say(channel, command_base.chat(message, user.username, 1));
                }
            }
        };
    });
};

//Export The function
module.exports.chat = chat;
