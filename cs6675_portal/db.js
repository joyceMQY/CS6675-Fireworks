/**
 * Mongo DB module connecting to remotedebugging database
 */
var host = "localhost";
var databaseURI = host + ":27017/cs6675";
var collections = ["posts"];
var db = require("mongojs").connect(databaseURI, collections);

module.exports = db;