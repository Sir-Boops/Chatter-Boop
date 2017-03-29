//Node-JS Imports
var request = require('request');
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
		logger("Hitbox Connected");
        })

        channel.on("chat", function(name, message, role) {
		logger('[Hitbox][' + name  + ']:' + message);
		if (name.toLowerCase() != user.toLowerCase()) {
			if (role == "guest") {
				var ans = logger('{"rank":"0", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}');
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "anon") {
				var ans = logger('{"rank":"1", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}');
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "user") {
				var ans = logger('{"rank":"2", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}');
				if (ans) { channel.sendMessage(ans) };
			}
			if (role == "admin") {
				var ans = logger('{"rank":"4", "msg":"' + message + '", "name":"' + name + '", "UUID":"' + name.toLowerCase() + '"}');
				if (ans) { channel.sendMessage(ans) }
			}
		}
	});
});
};

//Export The function
module.exports.chat = chat;
