/***********************************/
// generated from: either
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl"); var Option = require("std/option.sl")

;
;
var _Left = 0;
function $Left(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
exports._Left = _Left;
exports.$Left = $Left;
var _Right = 1;
function $Right(_arg0)
{
  return {_cid : 1, _var0 : _arg0
  };
  return f
};
exports._Right = _Right;
exports.$Right = $Right;
function $mapL(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Left) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $222 = $Left;
      var $221 = $f;
      var $220 = $a;
      var $219 = $221($220);
      var _return = $222($219);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $218 = $Right;
        var $217 = $b;
        var _return = $218($217);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
function $fold(_arg0)
{
  return function (_arg1)
  {
    return function (_arg2)
    {
      if(((true && true) && ((_arg2["_cid"] === _Left) && true)))
      {
        var $fa = _arg0;
        var $fb = _arg1;
        var $a = _arg2["_var0"];
        var $226 = $fa;
        var $225 = $a;
        var _return = $226($225);
        return _return
      }
      else 
      {
        if(((true && true) && ((_arg2["_cid"] === _Right) && true)))
        {
          var $fa = _arg0;
          var $fb = _arg1;
          var $b = _arg2["_var0"];
          var $224 = $fb;
          var $223 = $b;
          var _return = $224($223);
          return _return
        }
        else 
        {
          throw "Pattern not exhaustive!"
        }
      }
    }
  }
};
function $mapR(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Left) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $232 = $Left;
      var $231 = $a;
      var _return = $232($231);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $230 = $Right;
        var $229 = $f;
        var $228 = $b;
        var $227 = $229($228);
        var _return = $230($227);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
function $left(_arg0)
{
  if(true)
  {
    var $e = _arg0;
    var $239 = $fold;
    var $238 = Option.$Some;
    var $237 = $239($238);
    var $235 = function (_arg0)
    {
      if(true)
      {
        var $b = _arg0;
        var $236 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $236
    };
    var $234 = $237($235);
    var $233 = $e;
    var _return = $234($233);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $right(_arg0)
{
  if(true)
  {
    var $e = _arg0;
    var $246 = $fold;
    var $244 = function (_arg0)
    {
      if(true)
      {
        var $a = _arg0;
        var $245 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $245
    };
    var $243 = $246($244);
    var $242 = Option.$Some;
    var $241 = $243($242);
    var $240 = $e;
    var _return = $241($240);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
;
exports.$mapL = $mapL;
exports.$left = $left;
exports.$fold = $fold;
exports.$mapR = $mapR;
exports.$right = $right
});