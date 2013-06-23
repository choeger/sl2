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
        console.log(a.b()+"<br/>");
        console.log(b.a()+"<br/>");
    });
} else {
    /* in browsers*/ 
    require(["modules/A",  "modules/B"], function(a, b) {
        document.writeln(a.b()+"<br/>");
        document.writeln(b.a()+"<br/>");
    });
}