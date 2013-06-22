if (typeof window === 'undefined') {
    /* in node.js */
    var requirejs = require('requirejs');
    
    requirejs.config({
        //Pass the top-level main.js/index.js require
        //function to requirejs so that node modules
        //are loaded relative to the top-level JS file.
        nodeRequire: require
    });
    
    requirejs(["modules/A",  "modules/B"], function(a, b) {
        console.log(a.a());
        console.log(b.b());
    });
} else {
    /* in browsers*/ 
    require(["modules/A",  "modules/B"], function(a, b) {
        document.write(a.a());
        document.write(b.b());
    });
}
