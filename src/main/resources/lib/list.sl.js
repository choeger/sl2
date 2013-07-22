/***********************************/
// generated from: list.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl"); var Option = require("option.sl")

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
exports.$fromString = function(str) {
	var list = $Nil;
	for (var i = str.length-1; i >= 0 ; i--) {
		list = $Cons(str.charAt(i))(list);
	}
	return list;
};
var $fromString = exports.$fromString;
function $conc(_arg0)
{
  return function (_arg1)
  {
    if(((_arg0 === $Nil) && true))
    {
      var $list = _arg1;
      var _return = $list;
      return _return
    }
    else 
    {
      if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
      {
        var $ft = _arg0["_var0"];
        var $rt = _arg0["_var1"];
        var $list = _arg1;
        var $210 = $Cons;
        var $209 = $ft;
        var $208 = $210($209);
        var $207 = $conc;
        var $206 = $rt;
        var $205 = $207($206);
        var $204 = $list;
        var $203 = $205($204);
        var _return = $208($203);
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
function $map(_arg0)
{
  return function (_arg1)
  {
    if((true && (_arg1 === $Nil)))
    {
      var $f = _arg0;
      var _return = $Nil;
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $220 = $Cons;
        var $219 = $f;
        var $218 = $ft;
        var $217 = $219($218);
        var $216 = $220($217);
        var $215 = $map;
        var $214 = $f;
        var $213 = $215($214);
        var $212 = $rt;
        var $211 = $213($212);
        var _return = $216($211);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$map = $map;
function $head(_arg0)
{
  if((((_arg0["_cid"] === _Cons) && true) && true))
  {
    var $ft = _arg0["_var0"];
    var $rt = _arg0["_var1"];
    var _return = $ft;
    return _return
  }
  else 
  {
    if((_arg0 === $Nil))
    {
      var $222 = $$prelude.$error;
      var $221 = "Cannot select head from empty list.";
      var _return = $222($221);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$head = $head;
function $fromOption(_arg0)
{
  if(((_arg0["_cid"] === Option._Some) && true))
  {
    var $a = _arg0["_var0"];
    var $226 = $Cons;
    var $225 = $a;
    var $224 = $226($225);
    var $223 = $Nil;
    var _return = $224($223);
    return _return
  }
  else 
  {
    if((_arg0 === Option.$None))
    {
      var _return = $Nil;
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$fromOption = $fromOption;
function $tail(_arg0)
{
  if((((_arg0["_cid"] === _Cons) && true) && true))
  {
    var $ft = _arg0["_var0"];
    var $rt = _arg0["_var1"];
    var _return = $rt;
    return _return
  }
  else 
  {
    if((_arg0 === $Nil))
    {
      var _return = $Nil;
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$tail = $tail;
function $filter(_arg0)
{
  return function (_arg1)
  {
    if((true && (_arg1 === $Nil)))
    {
      var $p = _arg0;
      var _return = $Nil;
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $p = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $229 = $p;
        var $228 = $ft;
        var $227 = $229($228);
        if($227)
        {
          var $237 = $Cons;
          var $236 = $ft;
          var $235 = $237($236);
          var $234 = $filter;
          var $233 = $p;
          var $232 = $234($233);
          var $231 = $rt;
          var $230 = $232($231);
          var _return = $235($230)
        }
        else 
        {
          var $241 = $filter;
          var $240 = $p;
          var $239 = $241($240);
          var $238 = $rt;
          var _return = $239($238)
        };
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$filter = $filter;
function $reduce(_arg0)
{
  return function (_arg1)
  {
    return function (_arg2)
    {
      if(((true && true) && (_arg2 === $Nil)))
      {
        var $f = _arg0;
        var $n = _arg1;
        var _return = $n;
        return _return
      }
      else 
      {
        if(((true && true) && (((_arg2["_cid"] === _Cons) && true) && true)))
        {
          var $f = _arg0;
          var $n = _arg1;
          var $ft = _arg2["_var0"];
          var $rt = _arg2["_var1"];
          var $251 = $f;
          var $250 = $ft;
          var $249 = $251($250);
          var $248 = $reduce;
          var $247 = $f;
          var $246 = $248($247);
          var $245 = $n;
          var $244 = $246($245);
          var $243 = $rt;
          var $242 = $244($243);
          var _return = $249($242);
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
exports.$reduce = $reduce;
function $length(_arg0)
{
  if((_arg0 === $Nil))
  {
    var _return = 0;
    return _return
  }
  else 
  {
    if((((_arg0["_cid"] === _Cons) && true) && true))
    {
      var $ft = _arg0["_var0"];
      var $rt = _arg0["_var1"];
      var $257 = $$prelude.$p;
      var $256 = 1;
      var $255 = $257($256);
      var $254 = $length;
      var $253 = $rt;
      var $252 = $254($253);
      var _return = $255($252);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$length = $length;
function $toStringI(_arg0)
{
  return function (_arg1)
  {
    if(((_arg0 === $Nil) && true))
    {
      var $tS = _arg1;
      var _return = "";
      return _return
    }
    else 
    {
      if(((((_arg0["_cid"] === _Cons) && true) && (_arg0["_var1"] === $Nil)) && true))
      {
        var $ft = _arg0["_var0"];
        var $tS = _arg1;
        var $273 = $tS;
        var $272 = $ft;
        var _return = $273($272);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $271 = $$prelude.$p$p;
          var $270 = $$prelude.$p$p;
          var $269 = $tS;
          var $268 = $ft;
          var $267 = $269($268);
          var $266 = $270($267);
          var $265 = ",";
          var $264 = $266($265);
          var $263 = $271($264);
          var $262 = $toStringI;
          var $261 = $rt;
          var $260 = $262($261);
          var $259 = $tS;
          var $258 = $260($259);
          var _return = $263($258);
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
exports.$toStringI = $toStringI;
function $flatten(_arg0)
{
  if(true)
  {
    var $list = _arg0;
    var $279 = $reduce;
    var $278 = $conc;
    var $277 = $279($278);
    var $276 = $Nil;
    var $275 = $277($276);
    var $274 = $list;
    var _return = $275($274);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
exports.$flatten = $flatten;
function $toString(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $tS = _arg0;
      var $list = _arg1;
      var $291 = $$prelude.$p$p;
      var $290 = $$prelude.$p$p;
      var $289 = "<";
      var $288 = $290($289);
      var $287 = $toStringI;
      var $286 = $list;
      var $285 = $287($286);
      var $284 = $tS;
      var $283 = $285($284);
      var $282 = $288($283);
      var $281 = $291($282);
      var $280 = ">";
      var _return = $281($280);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$toString = $toString
});