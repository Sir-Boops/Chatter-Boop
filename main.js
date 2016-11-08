//Node-JS Extenral Imports

//Node-JS Internal Imports
var twitch_chat = require("./clients/twitch.js");
var beam_chat = require("./clients/beam.js");
var hitbox_chat = require("./clients/hitbox.js");

//Needed vars
var config = require("./config.json");

//Start Twitch
twitch_chat.chat(config.twitch.username, config.twitch.oauth, config.twitch.channel);

//Start Beam
beam_chat.chat(config.beam.username, config.beam.password, config.beam.channelID);

//Start Hitbox
hitbox_chat.chat(config.hitbox.username, config.hitbox.password, config.hitbox.channel);
