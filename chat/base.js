//Node-JS Imports

//Command Imports
var bot = require("./commands/bot.js");

var chat = function(message, user, ul) {

    //User Levels
    //0 = Annon
    //1 = Normal user
    //2 = Sub
    //3 = Mod
    //4 = Owner

    //Define The Answer
    var res;

    //Check What command they are running
    if (message.split(/ /)[0].toLowerCase() == "~bot" && ul > 0) {
        res = bot.command(message, user, ul);
    };

    //Return The Answer
    return res;
};

module.exports.chat = chat;
