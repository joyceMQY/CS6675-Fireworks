/**
 * Request handler to define handlers for different functions
 */

var fs = require("fs");
var url = require("url");
var queryString = require("querystring");
var util = require("util");
var db = require("./db");

function portal(response, request) {
	fs.readFile('index.html', function(error, data) {
		response.writeHead(200, {
			'content-type' : 'text/html'
		});
		response.end(data);
	});
}

function map(response, request) {
/*	var results = new Object();
	db.posts.find({},{latitude:1, longitude:1}, function(error, recordings){
		if(error || !recordings){
			console.log("No recordings are found.");
		}else{
			console.log(recordings.length);
			results.type = "FeatureCollection";
			var features = [];

			recordings.forEach(function(item){
				var object = new Object();
				object.type = "Feature";
				var point = new Object();
				point.type = "Point";
				var coordinates = [];
				coordinates.push(item.longitude);
				coordinates.push(item.latitude);
				point.coordinates = coordinates;
				object.geometry = point;

				features.push(object);
			});

			results.features = features;
		}

		// Write to json file
		var outputFile = "map/data.json";
		fs.writeFile(outputFile, JSON.stringify(results), function(err){
			if(err){
				console.log(err);
			}else{
				console.log("JSON saved to " + outputFile);
			}
		});
*/
		fs.readFile('map.html', function(error, data) {
			response.writeHead(200, {
				'content-type' : 'text/html'
			});
			response.end(data);
		});

//	});
}

function map1(response, request) {
	fs.readFile('map1.html', function(error, data) {
		response.writeHead(200, {
			'content-type' : 'text/html'
		});
		response.end(data);
	});
}

// Open javascript file if requested
function serveFileJS(response, request) {
	var pathname = url.parse(request.url).pathname;
	fs.readFile(pathname.substring(1, pathname.length), function(error, data) {
		response.writeHead(200, {
			'content-type' : 'text/javascript'
		});
		response.end(data);
	});
}

// Open json file if requested
function serveFileJSON(response, request) {
	var pathname = url.parse(request.url).pathname;
	fs.readFile(pathname.substring(1, pathname.length), function(error, data) {
		response.writeHead(200, {
			'content-type' : 'text/json'
		});
		response.end(data);
	});
}

// Open css file if requested
function serveFileCSS(response, request) {
	var pathname = url.parse(request.url).pathname;
	fs.readFile(pathname.substring(1, pathname.length), function(error, data) {
		response.writeHead(200, {
			'content-type' : 'text/css'
		});
		response.end(data);
	});
}

// Open image file if requested
function serveFileImage(response, request) {
	// list of supported image types
	var imageTypeMimeMap = new Object();
	imageTypeMimeMap.png = "image/png";
	imageTypeMimeMap.jpg = "image/jpeg";
	imageTypeMimeMap.jpeg = "image/jpeg";
	imageTypeMimeMap.gif = "image/gif";

	var pathname = url.parse(request.url).pathname;
	var filetype = pathname.substring(pathname.lastIndexOf(".") + 1,
			pathname.length);
	console.log(filetype);
	console.log(imageTypeMimeMap[filetype]);
	console.log(pathname);
	fs.stat("." + pathname, function(err, stat) {
		console.log(stat);
		response.writeHead(200, {
			'content-type' : imageTypeMimeMap[filetype],
			'content-length' : stat.size
		});

		var rs = fs.createReadStream("." + pathname);
		console.log(rs);
		util.pump(rs, response, function(err) {
			if (err) {
				console.log(err);
				throw err;
			}
		});
	});
}

function getAllPosts(response, request){
	console.log("get posts");
	var results = [];
	db.posts.find({},{text:1, source:1, latitude:1, longitude:1, reposts_count:1, comments_count:1, created_at:1}, function(error, recordings){
		if(error || !recordings){
			console.log("No recordings are found.");
			var origin = (request.headers.origin || "*");
			response.writeHead("204", "No Content",
					{
						"access-control-allow-origin" : origin,
						"access-control-allow-methods" : "GET, POST, PUT, DELETE, OPTIONS",
						"access-control-allow-headers" : "content-type, accept",
						"access-control-max-age" : 10, // Seconds.
						"content-length" : 0
					});
			response.end();
		}else{
			console.log(recordings.length);
			recordings.forEach(function(item){
				var object = new Object();
				object.Text = item.text;
				object.Latitude = item.latitude;
				object.Longitude = item.longitude;
				object.Created_at = item.created_at;
				results.push(object);
			});
			response.writeHead(200, {'content-type': 'application/json'});
			response.end(JSON.stringify(results));
		}
	});
}

function getTerms(response, request){
	var origin = (request.headers.origin || "*");

    if (request.method == 'POST') {
        var body = '';
        request.on('data', function (data) {
            body += data;
        });
        request.on('end', function () {
			console.log("I got there weibos, to calculate term frequency: " + body);
/* In case we need to write files...
			var outputFile = "data/weibos.txt";
			fs.writeFile(outputFile, request, function(err){
				if(err){
					console.log(err);
				}else{
					console.log("weibos for one cluster is saved to " + outputFile);
				}
			});
*/
			var spawn = require('child_process').spawn;
			var prc = spawn('java',  ['-jar', '-Xmx512M', '-Dfile.encoding=utf8', 'data/terms.jar', body]);
			var output = "";

			//noinspection JSUnresolvedFunction
			prc.stdout.setEncoding('utf8');
			prc.stdout.on('data', function (data) {
				output += data + '\n';
			});
			prc.stdout.on('end', function (data) {
				var str = output.toString();
				console.log("After running jar, we get: " + str);
				response.writeHead(200, 
						{
							"access-control-allow-origin" : origin,
							"access-control-allow-methods" : "GET, POST, PUT, DELETE, OPTIONS",
							"access-control-allow-headers" : "content-type, accept",
							'content-type': 'application/text;charset=UTF-8'
						});
				response.end(str);
			});

			prc.on('close', function (code) {
				console.log('process exit code ' + code);
			});
        });
    } else {
		response.writeHead("204", "No Content",
				{
					"access-control-allow-origin" : origin,
					"access-control-allow-methods" : "GET, POST, PUT, DELETE, OPTIONS",
					"access-control-allow-headers" : "content-type, accept",
					"access-control-max-age" : 10, // Seconds.
					"content-length" : 0
				});
		response.end();
	}
}

function getAllPostsForMap(response, request){
	var results = new Object();
	db.posts.find({},{latitude:1, longitude:1, text:1}, function(error, recordings){
		if(error || !recordings){
			console.log("No recordings are found.");
			var origin = (request.headers.origin || "*");
			response.writeHead("204", "No Content",
					{
						"access-control-allow-origin" : origin,
						"access-control-allow-methods" : "GET, POST, PUT, DELETE, OPTIONS",
						"access-control-allow-headers" : "content-type, accept",
						"access-control-max-age" : 10, // Seconds.
						"content-length" : 0
					});
			response.end();
		}else{
			var url = require('url');
			var url_parts = url.parse(request.url, true);
			var query = url_parts.query;
			var max = query.data;
			if(max == null || max < 0) {
				max = 5000;
			}
			var count = 0;

			console.log(recordings.length);
			results.type = "FeatureCollection";
			var features = [];
			var StringDecoder = require('string_decoder').StringDecoder;
			var decoder = new StringDecoder('utf8');

			recordings.forEach(function(item){
				var object = new Object();
				object.type = "Feature";
				var point = new Object();
				point.type = "Point";
				var coordinates = [];
				coordinates.push(item.longitude);
				coordinates.push(item.latitude);
				point.coordinates = coordinates;
				object.geometry = point;
				var text = new Object();
				text.text = decoder.write(item.text);
				object.properties = text;

				if(count < max) {
					features.push(object);
					count++;
				}
			});

			results.features = features;
			response.writeHead(200, 
					{
						"access-control-allow-origin" : origin,
						"access-control-allow-methods" : "GET, POST, PUT, DELETE, OPTIONS",
						"access-control-allow-headers" : "content-type, accept",
						'content-type': 'application/json;charset=UTF-8'
					});
			response.end(JSON.stringify(results));
		}
	});
}

// Define module names
exports.portal = portal;
exports.getAllPosts = getAllPosts;
exports.serveFileJS = serveFileJS;
exports.serveFileCSS = serveFileCSS;
exports.serveFileImage = serveFileImage;
exports.serveFileJSON = serveFileJSON;

exports.map = map;
exports.map1 = map1;
exports.getAllPostsForMap = getAllPostsForMap;
exports.getTerms = getTerms;
