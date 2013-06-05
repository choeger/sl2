define(function(require, exports, module) {
    var id = Math.random();
    
    exports.a=function () { return "C.a("+id+")"; };
    exports.b=function () { return "C.b("+id+")"; };
});