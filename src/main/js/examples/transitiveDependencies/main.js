if (typeof window === 'undefined') {
    /* in node.js */
    var requirejs = require('requirejs');
    
    requirejs.config({
        //Pass the top-level main.js/index.js require
        //function to requirejs so that node modules
        //are loaded relative to the top-level JS file.
        nodeRequire: require
    });
    
    requirejs(["modules/A"], function(a) {
        console.log(a.a());
    });
} else {
    /* in browsers*/ 
    require(["modules/A"], function(a) {
        document.write(a.a());
    });
}