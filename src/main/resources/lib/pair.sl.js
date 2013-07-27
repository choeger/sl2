/***********************************/
// generated from: pair
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl")

;
;
var _Pair = 0;
function $Pair(_arg0)
{
  function f(_arg1)
  {
    return {_cid : 0, _var0 : _arg0, _var1 : _arg1
    }
  };
  return f
};
exports._Pair = _Pair;
exports.$Pair = $Pair;
function $snd(_arg0)
{
  if((((_arg0["_cid"] === _Pair) && true) && true))
  {
    var $a = _arg0["_var0"];
    var $b = _arg0["_var1"];
    var _return = $b;
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $fst(_arg0)
{
  if((((_arg0["_cid"] === _Pair) && true) && true))
  {
    var $a = _arg0["_var0"];
    var $b = _arg0["_var1"];
    var _return = $a;
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
;
exports.$fst = $fst;
exports.$snd = $snd
});