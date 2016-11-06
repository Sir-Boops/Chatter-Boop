//Node-JS Imports
var BeamClient = require('beam-client-node');
var BeamSocket = require('beam-client-node/lib/ws');

var chat = function(user, pass, channel) {
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
              console.log("Beam has connected");
            })
            .catch(error => {
                console.log('Oh no! An error occurred!', error);
            });

        // Listen to chat messages, note that you will also receive your own!
        socket.on('ChatMessage', data => {
            console.log("[Beam][" + data.user_name + "] : " + data.message.message[0].text);
            //return socket.call('msg', ['Hello world!']);
        });

        // Listen to socket errors, you'll need to handle these!
        socket.on('error', error => {
            console.error('Socket error', error);
        });
    }
};

module.exports.chat = chat;
