define(function(require, exports, module) {
    
    var b = require("modules/B");
    
    exports.a=function() { return "A.a("+b.a()+")"; };
    exports.b=function() { return "A.b("+b.b()+")"; };
});