//Node-JS Extenral Imports

//Node-JS Internal Imports
var twitch_chat = require("./clients/twitch.js");
var beam_chat = require("./clients/beam.js");
var hitbox_chat = require("./clients/hitbox.js");
var logger = require('./functions/logger.js');

//Needed vars
var config = require("./config.json");

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
