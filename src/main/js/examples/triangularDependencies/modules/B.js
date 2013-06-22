define(function(require, exports, module) {
    
    var c = require("modules/C");
    
    exports.a=function() { return "B.a("+c.a()+")"; };
    exports.b=function() { return "B.b("+c.b()+")"; };
});