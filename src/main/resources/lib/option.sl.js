/***********************************/
// generated from: option
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl")

;
;
var _Some = 0;
function $Some(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  }
};
exports._Some = _Some;
exports.$Some = $Some;
var $None = 1;
exports.$None = $None;
function $getOrElse(_arg0)
{
  return function (_arg1)
  {
    if((((_arg0["_cid"] === _Some) && true) && true))
    {
      var $a = _arg0["_var0"];
      var $b = _arg1;
      var _return = $a;
      return _return
    }
    else 
    {
      if(((_arg0 === $None) && true))
      {
        var $b = _arg1;
        var _return = $b;
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
function $get(_arg0)
{
  if(((_arg0["_cid"] === _Some) && true))
  {
    var $a = _arg0["_var0"];
    var _return = $a;
    return _return
  }
  else 
  {
    if((_arg0 === $None))
    {
      var $72 = $$std$prelude.$error;
      var $71 = "Cannot get value from None-Option.";
      var _return = $72($71);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $cmp(_arg0)
{
  return function (_arg1)
  {
    return function (_arg2)
    {
      if(((true && ((_arg1["_cid"] === _Some) && true)) && ((_arg2["_cid"] === _Some) && true)))
      {
        var $rel = _arg0;
        var $a = _arg1["_var0"];
        var $b = _arg2["_var0"];
        var $76 = $rel;
        var $75 = $a;
        var $74 = $76($75);
        var $73 = $b;
        var _return = $74($73);
        return _return
      }
      else 
      {
        if(((true && (_arg1 === $None)) && (_arg2 === $None)))
        {
          var $rel = _arg0;
          var _return = $$std$prelude.$True;
          return _return
        }
        else 
        {
          if(((true && true) && true))
          {
            var $rel = _arg0;
            var $a = _arg1;
            var $b = _arg2;
            var _return = $$std$prelude.$False;
            return _return
          }
          else 
          {
            throw "Pattern not exhaustive!"
          }
        }
      }
    }
  }
};
function $map(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Some) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $80 = $Some;
      var $79 = $f;
      var $78 = $a;
      var $77 = $79($78);
      var _return = $80($77);
      return _return
    }
    else 
    {
      if((true && (_arg1 === $None)))
      {
        var $f = _arg0;
        var _return = $None;
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
;
exports.$map = $map;
exports.$cmp = $cmp;
exports.$getOrElse = $getOrElse;
exports.$get = $get
});