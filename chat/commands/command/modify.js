//Node-JS Imports
var fs = require('fs');

var command = function(message, user, ul) {

    //Check to make sure it was written right
    if (message.split(/ /)[2] && message.split(/ /)[2].startsWith("~") && message.split(/ /)[3] <= 4) {

        var isFile = fs.accessSync("custom_commands", fs.F_OK);

        if (!isFile) {
            //Read the File
            var file = fs.readFileSync("custom_commands", "UTF8");

            //Run though the lines and try to find a match!
            //Check if file is empty
            if (file.split("\n").length > 1) {
                //Line is not empty!
                //Now check if we already have this command
                for (var i = 0; file.split("\n").length > i; i++) {
                    if (file.split("\n")[i].split(/ /)[0] == message.split(/ /)[2]) {
                        var res = message.replace(message.split(/ /)[0], "").replace(message.split(/ /)[1], "").replace(message.split(/ /)[2], "");
                        var res2 = res.replace(message.split(/ /)[3], "").replace(/    /, "");
                        var final = message.split(/ /)[2] + " " + message.split(/ /)[3] + " " + res2;
                        var list_arr = file.split("\n");
                        list_arr[i] = final;
                        for (var i2 = 0;
                            (list_arr.length - 1) > i2; i2++) {
                            list_arr[i2] = list_arr[i2] + "\n"
                        }
                        fs.writeFileSync("custom_commands", list_arr.join(",").replace(/,/g, ""));
                        return "Command Modified"
                    }
                }
            } else {
                var res = message.replace(message.split(/ /)[0], "").replace(message.split(/ /)[1], "").replace(message.split(/ /)[2], "");
                var res2 = res.replace(message.split(/ /)[3], "").replace(/    /, "");
                fs.writeFileSync("custom_commands", message.split(/ /)[2] + " " + res2 + " " + message.split(/ /)[3] + "\n");
                return "Command Modified"
            }
        } else {
            return "No Command File"
        }

    } else {
        return "Error in syntax";
    }

};

module.exports.command = command;
