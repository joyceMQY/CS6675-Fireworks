/**
 * Router module to route different request URLs to different modules
 */

var patt_js = /\/public\/(.+)\.js$/;
var patt_css = /\/public\/(.+)\.css$/;
var patt_image = /\/public\/(.+)\.(png|jpg|jpeg|gif)$/;
var patt_json = /(.+)\.json$/;

function route(handle, pathname, response, request) {
  console.log("router: About to route a request for " + pathname);
  if (typeof handle[pathname] === 'function') {
    handle[pathname](response, request);
  } else if (pathname.match(patt_js)) {
    handle['/*.js'](response, request);
  } else if (pathname.match(patt_css)) {
    handle['/*.css'](response, request);
  } else if (pathname.match(patt_image)) {
    handle['/image'](response, request);
  } else if (pathname.match(patt_json)) {
    handle['/*.json'](response, request);
  } else {
    console.log("router: No request handler found for " + pathname);
    response.writeHead(404, {"Content-Type": "text/plain"});
    response.write("404 Not Found");
    response.end();
  }
}

exports.route = route;
