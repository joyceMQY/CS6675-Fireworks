/**
 * Entry point
 */

var server = require("./server");
var router = require("./router");
var requestHandlers = require("./requestHandlers");

var handle = {}
handle["/"] = requestHandlers.portal;
handle["/getAllPosts"] = requestHandlers.getAllPosts;
handle["/*.js"] = requestHandlers.serveFileJS;
handle["/*.css"] = requestHandlers.serveFileCSS;
handle["/*.json"] = requestHandlers.serveFileJSON;
handle["/image"] = requestHandlers.serveFileImage;

handle["/map"] = requestHandlers.map;
handle["/map1"] = requestHandlers.map1;
handle["/getAllPostsForMap"] = requestHandlers.getAllPostsForMap;
handle["/getTerms"] = requestHandlers.getTerms;

server.start(router.route, handle);
