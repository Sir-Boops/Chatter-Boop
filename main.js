//Node-JS Extenral Imports
var fs = require('fs');

//Node-JS Internal Imports
var twitch_chat = require("./clients/twitch.js");
//var beam_chat = require("./clients/beam.js");
//var hitbox_chat = require("./clients/hitbox.js");
var logger = require('./functions/logger.js');
var api = require('./functions/api.js');
var plugin_api = require('./functions/plugins.js');

//Needed vars
var config = require("./config.json");

// Create plugin list
var plugins = plugin_api.load();

// Load clients
if (config.twitch.enabled) {
	twitch_chat.create(config.twitch.username, config.twitch.oauth, config.twitch.channel, logger);
	twitch_chat.chat(logger, api, plugins);
};
