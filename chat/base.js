//Node-JS Imports

//Command Imports
var bot = require("./commands/bot.js");
var command = require("./commands/command.js");
var custom_listener = require("./commands/custom_listener.js");

var chat = function(message, user, ul) {

    //User Levels
    //0 = Annon
    //1 = Normal user
    //2 = Sub
    //3 = Mod
    //4 = Owner

    //Check What command they are running
    //The ~bot Command
    if (message.split(/ /)[0].toLowerCase() == "~bot" && ul > 0) {
        return bot.command(message, user, ul);
    };

    //The ~command Command
    if (message.split(/ /)[0].toLowerCase() == "~command" && ul > 3) {
        return command.command(message, user, ul);
    }

    //The Custom command listener
    if (message.split(/ /)[0].startsWith("~")) {
        return custom_listener.command(message, user, ul);
    }
};

module.exports.chat = chat;
