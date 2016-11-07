//Node-JS Imports
var fs = require('fs');

var command = function(message, user, ul) {

    //User Levels
    //0 = Annon
    //1 = Normal user
    //2 = Sub
    //3 = Mod
    //4 = Owner

    //Load the custom commands file
    var file = fs.readFileSync("custom_commands", "UTF8");

    if (file.split("\n").length > 1) {
        for (var i = 0; file.split("\n").length > i; i++) {
            if (file.split("\n")[i].split(/ /)[0] == message.split(/ /)[0].toLowerCase()) {
                //We found the command!
                //Now check if we have premission to use it
                if (file.split("\n")[i].split(/ /)[1] <= ul) {
                    return file.split("\n")[i].replace(file.split("\n")[i].split(/ /)[0], "").replace(file.split("\n")[i].split(/ /)[1], "").replace(/  /, "");
                }
            }
        }
    } else {
        if (file.split("\n")[0].split(/ /)[1] <= ul) {
            return file.split("\n")[0].replace(file.split("\n")[0].split(/ /)[0], "").replace(file.split("\n")[0].split(/ /)[1], "").replace(/  /, "");
        }
    }
};

module.exports.command = command;
