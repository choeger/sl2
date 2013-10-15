/***********************************/
// generated from: webinterface
/***********************************/
if (typeof window === 'undefined') {
    /* in node.js */
    var requirejs = require('requirejs');
    
    requirejs.config({
        //Pass the top-level main.js/index.js require
        //function to requirejs so that node modules
        //are loaded relative to the top-level JS file.
        nodeRequire: require,
	paths: {std : "/home/ben/Dokumente/compilerbaupraxis/sl2/target/scala-2.10/classes/lib" }
    });
    
    requirejs(["webinterface.sl"], function($$$webinterface) {
        $$$webinterface.$main()
    });
} else {
    require.config({
	paths: {std : "file:/home/ben/Dokumente/compilerbaupraxis/sl2/target/scala-2.10/classes/lib/" }
    });

    /* in browsers*/ 
    require(["webinterface.sl"], function($$$webinterface) {
        $$$webinterface.$main()
    });
}