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
        var $327 = $Cons;
        var $326 = $ft;
        var $325 = $327($326);
        var $324 = $conc;
        var $323 = $rt;
        var $322 = $324($323);
        var $321 = $list;
        var $320 = $322($321);
        var _return = $325($320);
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
        var $337 = $Cons;
        var $336 = $f;
        var $335 = $ft;
        var $334 = $336($335);
        var $333 = $337($334);
        var $332 = $map;
        var $331 = $f;
        var $330 = $332($331);
        var $329 = $rt;
        var $328 = $330($329);
        var _return = $333($328);
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
      var $339 = $$std$prelude.$error;
      var $338 = "Cannot select head from empty list.";
      var _return = $339($338);
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
    var $343 = $Cons;
    var $342 = $a;
    var $341 = $343($342);
    var $340 = $Nil;
    var _return = $341($340);
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
        var $346 = $p;
        var $345 = $ft;
        var $344 = $346($345);
        if($344)
        {
          var $354 = $Cons;
          var $353 = $ft;
          var $352 = $354($353);
          var $351 = $filter;
          var $350 = $p;
          var $349 = $351($350);
          var $348 = $rt;
          var $347 = $349($348);
          var _return = $352($347)
        }
        else 
        {
          var $358 = $filter;
          var $357 = $p;
          var $356 = $358($357);
          var $355 = $rt;
          var _return = $356($355)
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
        var $361 = $p;
        var $360 = $ft;
        var $359 = $361($360);
        if($359)
        {
          var _return = $rt
        }
        else 
        {
          var $369 = $Cons;
          var $368 = $ft;
          var $367 = $369($368);
          var $366 = $removeFirst;
          var $365 = $p;
          var $364 = $366($365);
          var $363 = $rt;
          var $362 = $364($363);
          var _return = $367($362)
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
          var $384 = $$std$prelude.$a$e;
          var $383 = $reduceDom;
          var $382 = $f;
          var $381 = $383($382);
          var $380 = $n;
          var $379 = $381($380);
          var $378 = $rt;
          var $377 = $379($378);
          var $376 = $384($377);
          var $370 = function (_arg0)
          {
            if(true)
            {
              var $res = _arg0;
              var $375 = $f;
              var $374 = $ft;
              var $373 = $375($374);
              var $372 = $res;
              var $371 = $373($372)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $371
          };
          var _return = $376($370);
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
          var $394 = $f;
          var $393 = $ft;
          var $392 = $394($393);
          var $391 = $reduce;
          var $390 = $f;
          var $389 = $391($390);
          var $388 = $n;
          var $387 = $389($388);
          var $386 = $rt;
          var $385 = $387($386);
          var _return = $392($385);
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
      var $418 = $$std$prelude.$yield;
      var $417 = $Nil;
      var _return = $418($417);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $416 = $$std$prelude.$a$e;
        var $415 = $f;
        var $414 = $ft;
        var $413 = $415($414);
        var $412 = $416($413);
        var $395 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $411 = $$std$prelude.$a$e;
            var $410 = $mapDom;
            var $409 = $f;
            var $408 = $410($409);
            var $407 = $rt;
            var $406 = $408($407);
            var $405 = $411($406);
            var $397 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $404 = $$std$prelude.$yield;
                var $403 = $Cons;
                var $402 = $newFt;
                var $401 = $403($402);
                var $400 = $newRt;
                var $399 = $401($400);
                var $398 = $404($399)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $398
            };
            var $396 = $405($397)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $396
        };
        var _return = $412($395);
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
      var $424 = $$std$prelude.$p;
      var $423 = 1;
      var $422 = $424($423);
      var $421 = $length;
      var $420 = $rt;
      var $419 = $421($420);
      var _return = $422($419);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $headOption(_arg0)
{
  if((((_arg0["_cid"] === _Cons) && true) && true))
  {
    var $ft = _arg0["_var0"];
    var $rt = _arg0["_var1"];
    var $426 = Option.$Some;
    var $425 = $ft;
    var _return = $426($425);
    return _return
  }
  else 
  {
    if((_arg0 === $Nil))
    {
      var _return = Option.$None;
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
        var $442 = $tS;
        var $441 = $ft;
        var _return = $442($441);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $440 = $$std$prelude.$p$p;
          var $439 = $$std$prelude.$p$p;
          var $438 = $tS;
          var $437 = $ft;
          var $436 = $438($437);
          var $435 = $439($436);
          var $434 = ",";
          var $433 = $435($434);
          var $432 = $440($433);
          var $431 = $toStringI;
          var $430 = $rt;
          var $429 = $431($430);
          var $428 = $tS;
          var $427 = $429($428);
          var _return = $432($427);
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
    var $448 = $reduce;
    var $447 = $conc;
    var $446 = $448($447);
    var $445 = $Nil;
    var $444 = $446($445);
    var $443 = $list;
    var _return = $444($443);
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
      var $460 = $$std$prelude.$p$p;
      var $459 = $$std$prelude.$p$p;
      var $458 = "<";
      var $457 = $459($458);
      var $456 = $toStringI;
      var $455 = $list;
      var $454 = $456($455);
      var $453 = $tS;
      var $452 = $454($453);
      var $451 = $457($452);
      var $450 = $460($451);
      var $449 = ">";
      var _return = $450($449);
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
exports.$length = $length;
exports.$headOption = $headOption
});