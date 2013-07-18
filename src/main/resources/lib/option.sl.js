/***********************************/
// generated from: option.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl")

;
;
exports._Some = 0;
var _Some = exports._Some;
function $Some(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
exports.$Some = $Some;
exports.$None = 1;
var $None = exports.$None;
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
exports.$getOrElse = $getOrElse;
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
      var $43 = $$prelude.$error;
      var $42 = "Cannot get value from None-Option.";
      var _return = $43($42);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$get = $get;
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
        var $47 = $rel;
        var $46 = $a;
        var $45 = $47($46);
        var $44 = $b;
        var _return = $45($44);
        return _return
      }
      else 
      {
        if(((true && (_arg1 === $None)) && (_arg2 === $None)))
        {
          var $rel = _arg0;
          var _return = $True;
          return _return
        }
        else 
        {
          if(((true && true) && true))
          {
            var $rel = _arg0;
            var $a = _arg1;
            var $b = _arg2;
            var _return = $False;
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
exports.$cmp = $cmp;
function $map(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Some) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $51 = $Some;
      var $50 = $f;
      var $49 = $a;
      var $48 = $50($49);
      var _return = $51($48);
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
exports.$map = $map
});