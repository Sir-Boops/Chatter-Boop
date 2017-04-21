// Node-JS Imports
var tmi = require('tmi.js');

// Global functions
var client;

var create_client = function(user, pass, channel, logger) {

	// Set client options
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

	// Create the client
	client = new tmi.client(options);

	// Connect
	client.connect();
};

var listen_chat = function(logger, api) {

	// Listen to chat log and dump to the API
	client.on('chat', function(channel, user, message, self) {

		// Log the chat message to console
		logger("[Twitch][" + user.username + "] : " + message);

		// Parse the message and push to the API
		if (!self){

			// Find the user level
			if (user.badges && user.badges.broadcaster == "1") {

				// Translate to json string and send to
				// the API function
				var ans = api(JSON.stringify({rank: 4, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			};

			if (user.mod == "true") {
				var ans = api(JSON.stringify({rank: 3, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			};

			if (user.subscriber == "true" && user.mod != "true" && user.badges.broadcaster != "1") {
				var ans = api(JSON.stringify({rank: 2, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			};

			if (user.mod != 'true' && user.subscriber != 'true' && !user.badges) {
				var ans = api(JSON.stringify({rank: 1, msg: message, name: user['display-name'], UUID: user['user-id']}));
				if (ans) { client.say(channel, ans); };
			};
		};
	});
};

// Exports
module.exports.create = create_client;
module.exports.chat = listen_chat;
