//Node-JS Imports
var fs = require('fs');

var command = function(message, user, ul) {

    //Check to make sure it was written right
    if (message.split(/ /)[2] && message.split(/ /)[2].startsWith("~")) {

        var isFile = fs.accessSync("custom_commands", fs.F_OK);

        if (!isFile) {
            //Read the File
            var file = fs.readFileSync("custom_commands", "UTF8");

            //Run though the lines and try to find a match!
            //Check if file is empty
            if (file.split("\n").length > 1) {
                //Line is not empty!
                //Now check if we already have this command
                var final = "";
                for (var i = 0; file.split("\n").length > i; i++) {
                    if (file.split("\n")[i] != "") {
                        if (file.split("\n")[i].split(/ /)[0] != message.split(/ /)[2]) {
                            console.log(file.split("\n")[i]);
                            final += (file.split("\n")[i] + "\n");
                        }
                    }
                    if (file.split("\n").length == (i + 1)) {
                        fs.writeFileSync("custom_commands", final);
                        return "Command Removed"
                    }
                }
            } else {
                if (file.split("\n")[0].split(/ /)[0] == message.split(/ /)[2]) {
                    fs.writeFileSync("custom_commands", "");
                    return "Command Removed"
                } else {
                    return "Command Not Found!"
                }
            }
        } else {
            return "No Command File"
        }

    } else {
        return "Error in syntax";
    }

};

module.exports.command = command;
