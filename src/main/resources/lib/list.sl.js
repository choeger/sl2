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
        var $261 = $Cons;
        var $260 = $ft;
        var $259 = $261($260);
        var $258 = $conc;
        var $257 = $rt;
        var $256 = $258($257);
        var $255 = $list;
        var $254 = $256($255);
        var _return = $259($254);
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
        var $271 = $Cons;
        var $270 = $f;
        var $269 = $ft;
        var $268 = $270($269);
        var $267 = $271($268);
        var $266 = $map;
        var $265 = $f;
        var $264 = $266($265);
        var $263 = $rt;
        var $262 = $264($263);
        var _return = $267($262);
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
      var $273 = $$prelude.$error;
      var $272 = "Cannot select head from empty list.";
      var _return = $273($272);
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
    var $277 = $Cons;
    var $276 = $a;
    var $275 = $277($276);
    var $274 = $Nil;
    var _return = $275($274);
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
        var $280 = $p;
        var $279 = $ft;
        var $278 = $280($279);
        if($278)
        {
          var $288 = $Cons;
          var $287 = $ft;
          var $286 = $288($287);
          var $285 = $filter;
          var $284 = $p;
          var $283 = $285($284);
          var $282 = $rt;
          var $281 = $283($282);
          var _return = $286($281)
        }
        else 
        {
          var $292 = $filter;
          var $291 = $p;
          var $290 = $292($291);
          var $289 = $rt;
          var _return = $290($289)
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
          var $302 = $f;
          var $301 = $ft;
          var $300 = $302($301);
          var $299 = $reduce;
          var $298 = $f;
          var $297 = $299($298);
          var $296 = $n;
          var $295 = $297($296);
          var $294 = $rt;
          var $293 = $295($294);
          var _return = $300($293);
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
function $mapDom(_arg0)
{
  return function (_arg1)
  {
    if((true && (_arg1 === $Nil)))
    {
      var $f = _arg0;
      var $326 = $$prelude.$yield;
      var $325 = $Nil;
      var _return = $326($325);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $324 = $$prelude.$a$e;
        var $323 = $f;
        var $322 = $ft;
        var $321 = $323($322);
        var $320 = $324($321);
        var $303 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $319 = $$prelude.$a$e;
            var $318 = $mapDom;
            var $317 = $f;
            var $316 = $318($317);
            var $315 = $rt;
            var $314 = $316($315);
            var $313 = $319($314);
            var $305 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $312 = $$prelude.$yield;
                var $311 = $Cons;
                var $310 = $newFt;
                var $309 = $311($310);
                var $308 = $newRt;
                var $307 = $309($308);
                var $306 = $312($307)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $306
            };
            var $304 = $313($305)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $304
        };
        var _return = $320($303);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$mapDom = $mapDom;
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
      var $332 = $$prelude.$p;
      var $331 = 1;
      var $330 = $332($331);
      var $329 = $length;
      var $328 = $rt;
      var $327 = $329($328);
      var _return = $330($327);
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
        var $348 = $tS;
        var $347 = $ft;
        var _return = $348($347);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $346 = $$prelude.$p$p;
          var $345 = $$prelude.$p$p;
          var $344 = $tS;
          var $343 = $ft;
          var $342 = $344($343);
          var $341 = $345($342);
          var $340 = ",";
          var $339 = $341($340);
          var $338 = $346($339);
          var $337 = $toStringI;
          var $336 = $rt;
          var $335 = $337($336);
          var $334 = $tS;
          var $333 = $335($334);
          var _return = $338($333);
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
    var $354 = $reduce;
    var $353 = $conc;
    var $352 = $354($353);
    var $351 = $Nil;
    var $350 = $352($351);
    var $349 = $list;
    var _return = $350($349);
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
      var $366 = $$prelude.$p$p;
      var $365 = $$prelude.$p$p;
      var $364 = "<";
      var $363 = $365($364);
      var $362 = $toStringI;
      var $361 = $list;
      var $360 = $362($361);
      var $359 = $tS;
      var $358 = $360($359);
      var $357 = $363($358);
      var $356 = $366($357);
      var $355 = ">";
      var _return = $356($355);
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