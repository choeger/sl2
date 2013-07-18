/***********************************/
// generated from: list.sl
/***********************************/
define(function(require, exports, module) {
    var $$lib$prelude = require("lib/prelude.sl")

;
;
exports.$Nil = 0;
var $Nil = exports.$Nil;
exports._Cons = 1;
var _Cons = exports._Cons;
function $Cons(_arg0)
{
  function f(_arg1)
  {
    return {_cid : 1, _var0 : _arg0, _var1 : _arg1
    }
  };
  return f
};
exports.$Cons = $Cons;
function $conc(_arg0)
{
  return function (_arg1)
  {
    if(((_arg0 === Nil) && true))
    {
      var $list = _arg1;
      var _return = $list;
      return _return
    }
    else 
    {
      if(((((_arg0["_cid"] === Cons) && true) && true) && true))
      {
        var $ft = _arg0["_var0"];
        var $rt = _arg0["_var1"];
        var $list = _arg1;
        var $39 = $Cons;
        var $38 = $ft;
        var $37 = $39($38);
        var $36 = $conc;
        var $35 = $rt;
        var $34 = $36($35);
        var $33 = $list;
        var $32 = $34($33);
        var _return = $37($32);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$conc = $conc;
function $length(_arg0)
{
  if((_arg0 === Nil))
  {
    var _return = 0;
    return _return
  }
  else 
  {
    if((((_arg0["_cid"] === Cons) && true) && true))
    {
      var $ft = _arg0["_var0"];
      var $rt = _arg0["_var1"];
      var $45 = $$lib$prelude.$p;
      var $44 = 1;
      var $43 = $45($44);
      var $42 = $length;
      var $41 = $rt;
      var $40 = $42($41);
      var _return = $43($40);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$length = $length
});