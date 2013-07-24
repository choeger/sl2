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
        nodeRequire: require,
	paths: {std : "/home/fpz/code/sl2/src/main/resources/lib" }
    });
    
    requirejs(["HelloWorld.sl"], function($$$HelloWorld) {
        $$$HelloWorld.$main()
    });
} else {
    require.config({
	paths: {std : "/home/fpz/code/sl2/src/main/resources/lib" }
    });

    /* in browsers*/ 
    require(["HelloWorld.sl"], function($$$HelloWorld) {
        $$$HelloWorld.$main()
    });
}