//Node-JS Imports
var fs = require('fs');

var command = function(message, user, ul) {

    //Check to make sure it was written right
    if (message.split(/ /)[2] && message.split(/ /)[2].startsWith("~") && message.split(/ /)[3] <= 4) {

        var isFile = fs.existsSync("custom_commands");

        if (isFile) {
            //Read the File
            var file = fs.readFileSync("custom_commands", "UTF8");

            //Run though the lines and try to find a match!
            //Check if file is empty
            if (file.split("\n").length > 1) {
                for (var i = 0;
                    (file.split("\n").length - 1) > i; i++) {
                    //Check for empty lines
                    if (file.split("\n")[i] != "") {
                        //Line is not empty!
                        //Now check if we already have this command
                        if (file.split("\n")[i].split(/ /)[0] == message.split(/ /)[2]) {
                            return "Command already in use"
                        } else {
                            if ((i + 1) == (file.split("\n").length - 1)) {
                                var res = message.replace(message.split(/ /)[0], "").replace(message.split(/ /)[1], "").replace(message.split(/ /)[2], "");
                                var res2 = res.replace(message.split(/ /)[3], "").replace(/    /, "");
                                fs.appendFileSync("custom_commands", message.split(/ /)[2] + " " + message.split(/ /)[3] + " " + res2 + "\n");
                                return "Command Added"
                            }
                        }
                    }
                }
            } else {
                var res = message.replace(message.split(/ /)[0], "").replace(message.split(/ /)[1], "").replace(message.split(/ /)[2], "");
                var res2 = res.replace(message.split(/ /)[3], "").replace(/    /, "");
                fs.appendFileSync("custom_commands", message.split(/ /)[2] + " " + message.split(/ /)[3] + " " + res2 + "\n", "UTF8");
                return "Command Added"
            }
        } else {
            var res = message.replace(message.split(/ /)[0], "").replace(message.split(/ /)[1], "").replace(message.split(/ /)[2], "");
            var res2 = res.replace(message.split(/ /)[3], "").replace(/    /, "");
            fs.writeFileSync("custom_commands", message.split(/ /)[2] + " " + message.split(/ /)[3] + " " + res2 + "\n");
            return "Command Added"
        }

    } else {
        return "Error in syntax";
    }
};

module.exports.command = command;
