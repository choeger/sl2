define(function(require, exports, module) {
    
    var c = require("modules/C");
    
    exports.a = function() { return "A.a("+c.a()+")"; };
    exports.b = function() { return "A.b("+c.b()+")"; };
});