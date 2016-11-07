//Node-JS Imports
var package_json = require("../../package.json");
var command = function(message, user, user_level) {

    //User Levels
    //0 = Annon
    //1 = Normal user
    //2 = Sub
    //3 = Mod
    //4 = Owner

    return package_json.name + " @ " + package_json.homepage;
};

module.exports.command = command;
