var api = function (msg, logger, plugins) {

	// Final ans
	var ans = null;

	// Make sure it's JSON
	var error = null;

	try {
		JSON.parse(msg);
	} catch (e) {
		error = e;
	};

	// If error print the error and be done
	if (error) { logger.log('!! ERROR !! - ' + msg) };

	// If there is no error pass the msg to the plugins
	if (!error) { trigger(); };


	// Trigger the plugins
	function trigger () {

		// Parse the MSG for the plugins
		var pmsg = JSON.parse(msg);

		// Loop over the plugins
		for (var i = 0; i < plugins.length; i++) {
			if (plugins[i].split(":")[0].toLowerCase() == pmsg.msg.split(" ")[0].toLowerCase() || plugins[i].split(":")[0].toLowerCase() == '*') {
				var func = require(plugins[i].split(":")[1]);
				ans = func.main(msg, plugins[i].split(':')[2]);
			};
		};
	};

	// Return the final answer
	return ans;
};

module.exports.trigger = api;
