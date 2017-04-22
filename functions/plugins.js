// Node-JS Imports
var fs = require('fs');

var load_plugins = function () {

	// Read the plugins dir
	var plugin_dir = fs.readdirSync('./plugins/');

	// Define arrays
	var plugins_to_load = [];
	var plugins = [];

	// Find plugins to load
	for (var i = 0; i < plugin_dir.length; i++) {

		// Check if this is a dir
		if (fs.statSync('./plugins/' + plugin_dir[i]).isDirectory()) {

			// Make sure a plugin.json file is there and readable
			if (JSON.parse(fs.readFileSync('./plugins/' + plugin_dir[i] + '/plugin.json', 'utf8')).main_file) {
				plugins_to_load.push(plugin_dir[i]);
			};
		};
	};

	// Register pluigins
	for (var i = 0; i < plugins_to_load.length; i++) {

		// Read the plugin.json
		var plugin_json = JSON.parse(fs.readFileSync('./plugins/' + plugins_to_load[i] + '/plugin.json'));

		// Create a new array to register the plugin with
		// plugin_command:plugin_file:plugin_folder
		var new_arr = "";
		new_arr += (plugin_json.command + ':');
		new_arr += ('../plugins/' + plugins_to_load[i] + '/' + plugin_json.main_file + ':');
		new_arr += ('../plugins/' + plugins_to_load[i] + '/' + ':');
		plugins.push(new_arr);
	};

	// Return the list of plugins
	return plugins;
};

module.exports.load = load_plugins;
