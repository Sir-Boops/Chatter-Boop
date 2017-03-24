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
		console.log(user);
		if (!self) {
			//Get User Level
			if (user.badges && user.badges.broadcaster == "1") {
				logger.log(JSON.stringify('{"rank":"4", "msg":"' + message + '", "name":"' + user['display-name'] + '", "UUID":"' + user['user-id']  + '"}'));
			}
			if (user.mod == "true") {
				logger.log(JSON.stringify('{"rank":"3", "msg":"' + message + '", "name":"' + user['display-name'] + '", "UUID":"' + user['user-id']  + '"}'));
			}
			if (user.subscriber == "true") {
				logger.log(JSON.stringify('{"rank":"2", "msg":"' + message + '", "name":"' + user['display-name'] + '", "UUID":"' + user['user-id']  + '"}'));
			}
			if (user.mod != 'true' && user.subscriber != 'true') {
				logger.log(JSON.stringify('{"rank":"1", "msg":"' + message + '", "name":"' + user['display-name'] + '", "UUID":"' + user['user-id']  + '"}'));
			}
		}
	});
};

//Export The function
module.exports.chat = chat;
