/*
 * org.ezcomputing.lang
 */
function include(module){
	//console.log("load: "+ module + " module.");
}



function assert(condition, message ="assertTrue failed."){
	if(!condition){
		throw message;
	}
}


function call(api, input){
	
	return JSON.parse(lang.call(api, JSON.stringify(input)));
	//console.log("load: "+ module + " module.");
}

function loadRemoteFunction(functionName, version, json) {

	var MyJavaClass = Java
			.type('org.ezcomputing.server.manager.RemoteLoaderManager');

	var resultString = MyJavaClass.loadRemoteFunction(functionName, version,
			JSON.stringify(json));

	var json = JSON.parse(resultString);

	// print("loadRemoteFunction:"+ result)

	if (json.error) {
		// TODO handle error
	}

	return json;
}

function wait(ms) {
	var start = new Date().getTime();
	var end = start;
	while (end < start + ms) {
		end = new Date().getTime();
	}
}



