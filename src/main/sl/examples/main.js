/***********************************/
// generated from: HelloWorld-Error.sl
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
    
    requirejs(["HelloWorld-Error.sl"], function($$$HelloWorld-Error) {
        $$$HelloWorld-Error.$main()
    });
} else {
    /* in browsers*/ 
    require(["HelloWorld-Error.sl"], function($$$HelloWorld-Error) {
        $$$HelloWorld-Error.$main()
    });
}