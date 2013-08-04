/***********************************/
// generated from: list
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl"); var Option = require("std/option.sl")

;
;
var $Nil = 0;
exports.$Nil = $Nil;
var _Cons = 1;
function $Cons(_arg0)
{
  function f(_arg1)
  {
    return {_cid : 1, _var0 : _arg0, _var1 : _arg1
    }
  };
  return f
};
exports._Cons = _Cons;
exports.$Cons = $Cons;
var $fromString = function(str) {
	var list = $Nil;
	for (var i = str.length-1; i >= 0 ; i--) {
		list = $Cons(str.charAt(i))(list);
	}
	return list;
};
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
        var $318 = $Cons;
        var $317 = $ft;
        var $316 = $318($317);
        var $315 = $conc;
        var $314 = $rt;
        var $313 = $315($314);
        var $312 = $list;
        var $311 = $313($312);
        var _return = $316($311);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
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
        var $328 = $Cons;
        var $327 = $f;
        var $326 = $ft;
        var $325 = $327($326);
        var $324 = $328($325);
        var $323 = $map;
        var $322 = $f;
        var $321 = $323($322);
        var $320 = $rt;
        var $319 = $321($320);
        var _return = $324($319);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
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
      var $330 = $$std$prelude.$error;
      var $329 = "Cannot select head from empty list.";
      var _return = $330($329);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $fromOption(_arg0)
{
  if(((_arg0["_cid"] === Option._Some) && true))
  {
    var $a = _arg0["_var0"];
    var $334 = $Cons;
    var $333 = $a;
    var $332 = $334($333);
    var $331 = $Nil;
    var _return = $332($331);
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
        var $337 = $p;
        var $336 = $ft;
        var $335 = $337($336);
        if($335)
        {
          var $345 = $Cons;
          var $344 = $ft;
          var $343 = $345($344);
          var $342 = $filter;
          var $341 = $p;
          var $340 = $342($341);
          var $339 = $rt;
          var $338 = $340($339);
          var _return = $343($338)
        }
        else 
        {
          var $349 = $filter;
          var $348 = $p;
          var $347 = $349($348);
          var $346 = $rt;
          var _return = $347($346)
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
function $removeFirst(_arg0)
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
        var $352 = $p;
        var $351 = $ft;
        var $350 = $352($351);
        if($350)
        {
          var _return = $rt
        }
        else 
        {
          var $360 = $Cons;
          var $359 = $ft;
          var $358 = $360($359);
          var $357 = $removeFirst;
          var $356 = $p;
          var $355 = $357($356);
          var $354 = $rt;
          var $353 = $355($354);
          var _return = $358($353)
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
function $reduceDom(_arg0)
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
          var $375 = $$std$prelude.$a$e;
          var $374 = $reduceDom;
          var $373 = $f;
          var $372 = $374($373);
          var $371 = $n;
          var $370 = $372($371);
          var $369 = $rt;
          var $368 = $370($369);
          var $367 = $375($368);
          var $361 = function (_arg0)
          {
            if(true)
            {
              var $res = _arg0;
              var $366 = $f;
              var $365 = $ft;
              var $364 = $366($365);
              var $363 = $res;
              var $362 = $364($363)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $362
          };
          var _return = $367($361);
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
          var $385 = $f;
          var $384 = $ft;
          var $383 = $385($384);
          var $382 = $reduce;
          var $381 = $f;
          var $380 = $382($381);
          var $379 = $n;
          var $378 = $380($379);
          var $377 = $rt;
          var $376 = $378($377);
          var _return = $383($376);
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
function $mapDom(_arg0)
{
  return function (_arg1)
  {
    if((true && (_arg1 === $Nil)))
    {
      var $f = _arg0;
      var $409 = $$std$prelude.$yield;
      var $408 = $Nil;
      var _return = $409($408);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $407 = $$std$prelude.$a$e;
        var $406 = $f;
        var $405 = $ft;
        var $404 = $406($405);
        var $403 = $407($404);
        var $386 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $402 = $$std$prelude.$a$e;
            var $401 = $mapDom;
            var $400 = $f;
            var $399 = $401($400);
            var $398 = $rt;
            var $397 = $399($398);
            var $396 = $402($397);
            var $388 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $395 = $$std$prelude.$yield;
                var $394 = $Cons;
                var $393 = $newFt;
                var $392 = $394($393);
                var $391 = $newRt;
                var $390 = $392($391);
                var $389 = $395($390)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $389
            };
            var $387 = $396($388)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $387
        };
        var _return = $403($386);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
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
      var $415 = $$std$prelude.$p;
      var $414 = 1;
      var $413 = $415($414);
      var $412 = $length;
      var $411 = $rt;
      var $410 = $412($411);
      var _return = $413($410);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
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
        var $431 = $tS;
        var $430 = $ft;
        var _return = $431($430);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $429 = $$std$prelude.$p$p;
          var $428 = $$std$prelude.$p$p;
          var $427 = $tS;
          var $426 = $ft;
          var $425 = $427($426);
          var $424 = $428($425);
          var $423 = ",";
          var $422 = $424($423);
          var $421 = $429($422);
          var $420 = $toStringI;
          var $419 = $rt;
          var $418 = $420($419);
          var $417 = $tS;
          var $416 = $418($417);
          var _return = $421($416);
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
function $flatten(_arg0)
{
  if(true)
  {
    var $list = _arg0;
    var $437 = $reduce;
    var $436 = $conc;
    var $435 = $437($436);
    var $434 = $Nil;
    var $433 = $435($434);
    var $432 = $list;
    var _return = $433($432);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $toString(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $tS = _arg0;
      var $list = _arg1;
      var $449 = $$std$prelude.$p$p;
      var $448 = $$std$prelude.$p$p;
      var $447 = "<";
      var $446 = $448($447);
      var $445 = $toStringI;
      var $444 = $list;
      var $443 = $445($444);
      var $442 = $tS;
      var $441 = $443($442);
      var $440 = $446($441);
      var $439 = $449($440);
      var $438 = ">";
      var _return = $439($438);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$flatten = $flatten;
exports.$conc = $conc;
exports.$fromString = $fromString;
exports.$map = $map;
exports.$head = $head;
exports.$fromOption = $fromOption;
exports.$tail = $tail;
exports.$filter = $filter;
exports.$removeFirst = $removeFirst;
exports.$reduceDom = $reduceDom;
exports.$reduce = $reduce;
exports.$mapDom = $mapDom;
exports.$toString = $toString;
exports.$length = $length
});