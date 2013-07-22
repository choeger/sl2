/***********************************/
// generated from: either.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl"); var Option = require("option.sl")

;
;
exports._Left = 0;
var _Left = exports._Left;
function $Left(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
exports.$Left = $Left;
exports._Right = 1;
var _Right = exports._Right;
function $Right(_arg0)
{
  return {_cid : 1, _var0 : _arg0
  };
  return f
};
exports.$Right = $Right;
function $mapL(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Left) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $97 = $Left;
      var $96 = $f;
      var $95 = $a;
      var $94 = $96($95);
      var _return = $97($94);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $93 = $Right;
        var $92 = $b;
        var _return = $93($92);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$mapL = $mapL;
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
        var $101 = $fa;
        var $100 = $a;
        var _return = $101($100);
        return _return
      }
      else 
      {
        if(((true && true) && ((_arg2["_cid"] === _Right) && true)))
        {
          var $fa = _arg0;
          var $fb = _arg1;
          var $b = _arg2["_var0"];
          var $99 = $fb;
          var $98 = $b;
          var _return = $99($98);
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
exports.$fold = $fold;
function $mapR(_arg0)
{
  return function (_arg1)
  {
    if((true && ((_arg1["_cid"] === _Left) && true)))
    {
      var $f = _arg0;
      var $a = _arg1["_var0"];
      var $107 = $Left;
      var $106 = $a;
      var _return = $107($106);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $105 = $Right;
        var $104 = $f;
        var $103 = $b;
        var $102 = $104($103);
        var _return = $105($102);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$mapR = $mapR;
function $left(_arg0)
{
  if(true)
  {
    var $e = _arg0;
    var $114 = $fold;
    var $113 = Option.$Some;
    var $112 = $114($113);
    var $110 = function (_arg0)
    {
      if(true)
      {
        var $b = _arg0;
        var $111 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $111
    };
    var $109 = $112($110);
    var $108 = $e;
    var _return = $109($108);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
exports.$left = $left;
function $right(_arg0)
{
  if(true)
  {
    var $e = _arg0;
    var $121 = $fold;
    var $119 = function (_arg0)
    {
      if(true)
      {
        var $a = _arg0;
        var $120 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $120
    };
    var $118 = $121($119);
    var $117 = Option.$Some;
    var $116 = $118($117);
    var $115 = $e;
    var _return = $116($115);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
exports.$right = $right
});