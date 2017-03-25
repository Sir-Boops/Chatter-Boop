//Node-JS Imports
var BeamClient = require('beam-client-node');
var BeamSocket = require('beam-client-node/lib/ws');
var command_base = require('../chat/base.js');

var chat = function(user, pass, channel, logger) {
    //Setup and connect to Beam
    var client = new BeamClient();

    client.use("password", {
            username: user,
            password: pass
        })
        .attempt()
        .then(response => {
            //console.log(response.body);
            // Store the logged in user's details for later refernece
            userInfo = response.body;
            // Returns a promise that resolves with our chat connection details.
            return client.chat.join(channel);
        })
        .then(response => {
            const body = response.body;
            //console.log(body);
            return createChatSocket(userInfo.id, channel, body.endpoints, body.authkey);
        })

    function createChatSocket(userId, channelId, endpoints, authkey) {
        const socket = new BeamSocket(endpoints).boot();

        // You don't need to wait for the socket to connect before calling methods,
        // we spool them and run them when connected automatically!
        socket.auth(channelId, userId, authkey)
            .then(() => {
                logger.log("Beam has connected");
            })
            .catch(error => {
                console.log('Oh no! An error occurred!', error);
            });

        // Listen to chat messages, note that you will also receive your own!
        socket.on('ChatMessage', data => {
            logger.log("[Beam][" + data.user_name + "] : " + data.message.message[0].text);

            //Check if it's us sending the message
            if (data.user_name.toLowerCase() != user.toLowerCase()) {

                //Get the user level
                var ul = 0;
                for (var i = 0; data.user_roles.length > i; i++) {
                    //Check For the owner tag
                    if (data.user_roles[i].toLowerCase() == "owner" && ul < 4) {
                        ul = 4;
			logger.log(JSON.stringify('{"rank":"4", "msg":"' + data.message.message[0].text + '", "name":"' + data.user_name + '", "UUID":"' + data.user_id + '"}'));
                    }
                    //Check for mod
                    if (data.user_roles[i].toLowerCase() == "mod" && ul < 3) {
                        ul = 3;
			logger.log(JSON.stringify('{"rank":"3", "msg":"' + data.message.message[0].text + '", "name":"' + data.user_name + '", "UUID":"' + data.user_id + '"}'));
                    }
                    //Check For user
                    if (data.user_roles[i].toLowerCase() == "user" && ul < 1) {
                        ul = 1;
			logger.log(JSON.stringify('{"rank":"1", "msg":"' + data.message.message[0].text + '", "name":"' + data.user_name + '", "UUID":"' + data.user_id + '"}'));
                    }
                }
            };
        });

        // Listen to socket errors, you'll need to handle these!
        socket.on('error', error => {
            console.error('Socket error', error);
        });
    }
};

module.exports.chat = chat;
