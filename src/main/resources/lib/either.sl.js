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
  }
};
exports._Left = _Left;
exports.$Left = $Left;
var _Right = 1;
function $Right(_arg0)
{
  return {_cid : 1, _var0 : _arg0
  }
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
      var $161 = $Left;
      var $160 = $f;
      var $159 = $a;
      var $158 = $160($159);
      var _return = $161($158);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $157 = $Right;
        var $156 = $b;
        var _return = $157($156);
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
        var $165 = $fa;
        var $164 = $a;
        var _return = $165($164);
        return _return
      }
      else 
      {
        if(((true && true) && ((_arg2["_cid"] === _Right) && true)))
        {
          var $fa = _arg0;
          var $fb = _arg1;
          var $b = _arg2["_var0"];
          var $163 = $fb;
          var $162 = $b;
          var _return = $163($162);
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
      var $171 = $Left;
      var $170 = $a;
      var _return = $171($170);
      return _return
    }
    else 
    {
      if((true && ((_arg1["_cid"] === _Right) && true)))
      {
        var $f = _arg0;
        var $b = _arg1["_var0"];
        var $169 = $Right;
        var $168 = $f;
        var $167 = $b;
        var $166 = $168($167);
        var _return = $169($166);
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
    var $178 = $fold;
    var $177 = Option.$Some;
    var $176 = $178($177);
    var $174 = function (_arg0)
    {
      if(true)
      {
        var $b = _arg0;
        var $175 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $175
    };
    var $173 = $176($174);
    var $172 = $e;
    var _return = $173($172);
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
    var $185 = $fold;
    var $183 = function (_arg0)
    {
      if(true)
      {
        var $a = _arg0;
        var $184 = Option.$None
      }
      else 
      {
        throw "Pattern for lambda expression did not match arguments"
      };
      return $184
    };
    var $182 = $185($183);
    var $181 = Option.$Some;
    var $180 = $182($181);
    var $179 = $e;
    var _return = $180($179);
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