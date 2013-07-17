/***********************************/
// generated from: HelloWorldPrelude.sl
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
    
    requirejs(["modules/HelloWorldPrelude.sl"], function($$$HelloWorldPrelude) {
        $$$HelloWorldPrelude.$main()
    });
} else {
    /* in browsers*/ 
    require(["modules/HelloWorldPrelude.sl"], function($$$HelloWorldPrelude) {
        $$$HelloWorldPrelude.$main()
    });
}