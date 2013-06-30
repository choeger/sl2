if (typeof window === 'undefined') {
    /* in node.js */
    var requirejs = require('requirejs');
    
    requirejs.config({
        //Pass the top-level main.js/index.js require
        //function to requirejs so that node modules
        //are loaded relative to the top-level JS file.
        nodeRequire: require
    });
    
    requirejs([%%MODULE_PATHS_LIST%%], function(%%MODULE_NAMES_LIST%%) {
        %%MAIN%%
    });
} else {
    /* in browsers*/ 
    require([%%MODULE_PATHS_LIST%%], function(%%MODULE_NAMES_LIST%%) {
        %%MAIN%%
    });
}
