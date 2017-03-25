//Node-JS Extenral Imports
var fs = require('fs');

//Node-JS Internal Imports
var twitch_chat = require("./clients/twitch.js");
var beam_chat = require("./clients/beam.js");
var hitbox_chat = require("./clients/hitbox.js");
var log = require('./functions/logger.js');

//Needed vars
var config = require("./config.json");

// Find plugins to load
var plugin_dir = fs.readdirSync('./plugins/');
var plugins_to_load = [];
var plugins = [];

for(var i = 0; i < plugin_dir.length; i++) {
	if (fs.statSync('./plugins/' + plugin_dir[i]).isDirectory()) {
		// Verify that the plugin.json is there
		if(JSON.parse(fs.readFileSync('./plugins/' + plugin_dir[i] + '/plugin.json', 'utf8')).main_file) {
			plugins_to_load.push(plugin_dir[i]);
		};
	};
};

// Load and register plugins
for(var i = 0; i < plugins_to_load.length; i++) {
	//Read the plugin.json file
	// Create an array entry command:file
	var plugin_json = JSON.parse(fs.readFileSync('./plugins/' + plugins_to_load[i] + '/plugin.json'));
	var new_arr = "";
	new_arr += (plugin_json.command + ':');
	new_arr += ('./plugins/' + plugins_to_load[i] + '/' + plugin_json.main_file + ':');
	plugins.push(new_arr);
};

//Start Twitch
if (config.twitch.enabled) {
	twitch_chat.chat(config.twitch.username, config.twitch.oauth, config.twitch.channel, logger);
}

//Start Beam
if (config.beam.enabled) {
	beam_chat.chat(config.beam.username, config.beam.password, config.beam.channelID, logger);
}

//Start Hitbox
if (config.hitbox.enabled) {
	hitbox_chat.chat(config.hitbox.username, config.hitbox.password, config.hitbox.channel, logger);
}


// Message parser
function logger(msg) {
	// Check if JSON or not
	var error;
	try {
		JSON.parse(msg);
	} catch(e) {
		error = e;
	};

	if (error) {
		log.log(msg);
	} else {
		var string = JSON.parse(msg);
		// Check for command
		for(var i = 0; i < plugins.length; i++) {
			if(plugins[i].split(":")[0].toLowerCase() == string.msg.split(" ")[0].toLowerCase()) {
				var func = require(plugins[i].split(":")[1]);
				return func.main(msg);
			};
		};
	};
};
