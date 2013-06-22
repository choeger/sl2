define(function(require, exports, module) {
    
    var a = require("modules/A");
    
    exports.a = function() {
        return "B.a("+a.a()+")";
    };
    exports.b = function() {
        return "B.b";
    };
});