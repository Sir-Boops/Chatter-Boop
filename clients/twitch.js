//Node-JS Imports
var tmi = require("tmi.js");

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
    client.on("chat", function(channel, userstate, message, self) {
        if (!self) {
            console.log("[Twitch][" + userstate.username + "] : " + message);
        };
    });
};

//Export The function
module.exports.chat = chat;
