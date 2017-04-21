//Node-JS Imports
var tmi = require("tmi.js");

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
        logger("Twitch has connected");
    });


	var push = function(msg, channel) {
		client.say(channel, msg);
	}



    //Listen For Messages
	client.on("chat", function(channel, user, message, self) {
		logger("[Twitch][" + user.username + "] : " + message);
		if (!self) {
			//Get User Level
			if (user.badges && user.badges.broadcaster == "1") {
				var ans = logger(JSON.stringify({rank: 4, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			}
			if (user.mod == "true") {
				var ans = logger(JSON.stringify({rank: 3, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			}
			if (user.subscriber == "true" && user.mod != "true" && user.badges.broadcaster != "1") {
				var ans = logger(JSON.stringify({rank: 2, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			}
			if (user.mod != 'true' && user.subscriber != 'true' && !user.badges) {
				var ans = logger(JSON.stringify({rank: 1, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			}
		}
	});
};

//Export The function
module.exports.chat = chat;
module.exports.push = push;
