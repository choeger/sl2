/***********************************/
// generated from: HelloWorldPrelude.sl
/***********************************/
define(function(require, exports, module) {
    var P = require("modules/prelude.sl")

;
;
function $conc(_arg0)
{
  if(true)
  {
    var $s = _arg0;
    var $51 = $s;
    if(($51 === P.$Nil))
    {
      var _return = ""
    }
    else 
    {
      if(((($51["_cid"] === P._Cons) && true) && true))
      {
        var $ft = $51["_var0"];
        var $rt = $51["_var1"];
        var $57 = P.$p$p;
        var $56 = $ft;
        var $55 = $57($56);
        var $54 = $conc;
        var $53 = $rt;
        var $52 = $54($53);
        var _return = $55($52)
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    };
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
exports.$conc = $conc;
exports.$words = function ()
{
  var $65 = P.$Cons;
  var $64 = "Hello";
  var $63 = $65($64);
  var $62 = P.$Cons;
  var $61 = "World";
  var $60 = $62($61);
  var $59 = P.$Nil;
  var $58 = $60($59);
  var $words = $63($58);
  return $words
}();
var $words = exports.$words;
exports.$hello = function ()
{
  var $71 = P.$p$p;
  var $70 = "Hey, ";
  var $69 = $71($70);
  var $68 = $conc;
  var $67 = $words;
  var $66 = $68($67);
  var $hello = $69($66);
  return $hello
}();
var $hello = exports.$hello;
exports.$main = function ()
{
  var $78 = P.$a$e;
  var $77 = P.$yield;
  var $76 = $hello;
  var $75 = $77($76);
  var $74 = $78($75);
  var $72 = function (_arg0)
  {
    if(true)
    {
      var $h = _arg0;
      var $73 = function ()
      {
        return console.log($h);
      }
    }
    else 
    {
      throw "Pattern for lambda expression did not match arguments"
    };
    return $73
  };
  var $main = $74($72);
  return $main
}();
var $main = exports.$main
});