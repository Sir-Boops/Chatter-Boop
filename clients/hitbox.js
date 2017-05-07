//Node-JS Imports
var request = require('request');
var HitboxChatClient = require("hitbox-chat");

//Setup the export
var chat = function(user, password, chan, logger, plugins, api) {

    var client = new HitboxChatClient({
        name: user,
        pass: password
    });

    client.on("connect", function() {
        // handle connect
        var channel = client.joinChannel(chan.toLowerCase());
        channel.on("login", function(name, role) {
		logger.log("[Hitbox] Client has connected to Hitbox.tv");
        })

        channel.on("chat", function(name, message, role) {
		logger.log('[Hitbox][' + name  + '] : ' + message);
		if (name.toLowerCase() != user.toLowerCase()) {
			if (role == "guest") {
				var ans = api.trigger(JSON.stringify({rank: 0, msg: message, name: name, UUID: name.toLowerCase(), platform: "hitbox"}), logger, plugins);
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "anon") {
				var ans = api.trigger(JSON.stringify({rank: 1, msg: message, name: name, UUID: name.toLowerCase(), platform: "hitbox"}), logger, plugins);
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "user") {
				var ans = api.trigger(JSON.stringify({rank: 2, msg: message, name: name, UUID: name.toLowerCase(), platform: "hitbox"}), logger, plugins);
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "admin") {
				var ans = api.trigger(JSON.stringify({rank: 4, msg: message, name: name, UUID: name.toLowerCase(), platform: "hitbox"}), logger, plugins);
				if (ans) { channel.sendMessage(ans) }
			}
		}
	});
});
};

//Export The function
module.exports.chat = chat;
