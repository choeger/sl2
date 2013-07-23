/***********************************/
// generated from: HelloWorld.sl
/***********************************/
if (typeof window === 'undefined') {
    /* in node.js */
    var requirejs = require('requirejs');
    
    requirejs.config({
        //Pass the top-level main.js/index.js require
        //function to requirejs so that node modules
        //are loaded relative to the top-level JS file.
        nodeRequire: require
    });
    
    requirejs(["HelloWorld.sl"], function($$$HelloWorld) {
        $$$HelloWorld.$main()
    });
} else {
    /* in browsers*/ 
    require(["HelloWorld.sl"], function($$$HelloWorld) {
        $$$HelloWorld.$main()
    });
}