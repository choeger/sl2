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
        var $372 = $Cons;
        var $371 = $ft;
        var $370 = $372($371);
        var $369 = $conc;
        var $368 = $rt;
        var $367 = $369($368);
        var $366 = $list;
        var $365 = $367($366);
        var _return = $370($365);
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
        var $382 = $Cons;
        var $381 = $f;
        var $380 = $ft;
        var $379 = $381($380);
        var $378 = $382($379);
        var $377 = $map;
        var $376 = $f;
        var $375 = $377($376);
        var $374 = $rt;
        var $373 = $375($374);
        var _return = $378($373);
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
      var $384 = $$std$prelude.$error;
      var $383 = "Cannot select head from empty list.";
      var _return = $384($383);
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
    var $388 = $Cons;
    var $387 = $a;
    var $386 = $388($387);
    var $385 = $Nil;
    var _return = $386($385);
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
        var $391 = $p;
        var $390 = $ft;
        var $389 = $391($390);
        if($389)
        {
          var $399 = $Cons;
          var $398 = $ft;
          var $397 = $399($398);
          var $396 = $filter;
          var $395 = $p;
          var $394 = $396($395);
          var $393 = $rt;
          var $392 = $394($393);
          var _return = $397($392)
        }
        else 
        {
          var $403 = $filter;
          var $402 = $p;
          var $401 = $403($402);
          var $400 = $rt;
          var _return = $401($400)
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
        var $406 = $p;
        var $405 = $ft;
        var $404 = $406($405);
        if($404)
        {
          var _return = $rt
        }
        else 
        {
          var $414 = $Cons;
          var $413 = $ft;
          var $412 = $414($413);
          var $411 = $removeFirst;
          var $410 = $p;
          var $409 = $411($410);
          var $408 = $rt;
          var $407 = $409($408);
          var _return = $412($407)
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
          var $429 = $$std$prelude.$a$e;
          var $428 = $reduceDom;
          var $427 = $f;
          var $426 = $428($427);
          var $425 = $n;
          var $424 = $426($425);
          var $423 = $rt;
          var $422 = $424($423);
          var $421 = $429($422);
          var $415 = function (_arg0)
          {
            if(true)
            {
              var $res = _arg0;
              var $420 = $f;
              var $419 = $ft;
              var $418 = $420($419);
              var $417 = $res;
              var $416 = $418($417)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $416
          };
          var _return = $421($415);
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
          var $439 = $f;
          var $438 = $ft;
          var $437 = $439($438);
          var $436 = $reduce;
          var $435 = $f;
          var $434 = $436($435);
          var $433 = $n;
          var $432 = $434($433);
          var $431 = $rt;
          var $430 = $432($431);
          var _return = $437($430);
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
      var $463 = $$std$prelude.$yield;
      var $462 = $Nil;
      var _return = $463($462);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $461 = $$std$prelude.$a$e;
        var $460 = $f;
        var $459 = $ft;
        var $458 = $460($459);
        var $457 = $461($458);
        var $440 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $456 = $$std$prelude.$a$e;
            var $455 = $mapDom;
            var $454 = $f;
            var $453 = $455($454);
            var $452 = $rt;
            var $451 = $453($452);
            var $450 = $456($451);
            var $442 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $449 = $$std$prelude.$yield;
                var $448 = $Cons;
                var $447 = $newFt;
                var $446 = $448($447);
                var $445 = $newRt;
                var $444 = $446($445);
                var $443 = $449($444)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $443
            };
            var $441 = $450($442)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $441
        };
        var _return = $457($440);
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
      var $469 = $$std$prelude.$p;
      var $468 = 1;
      var $467 = $469($468);
      var $466 = $length;
      var $465 = $rt;
      var $464 = $466($465);
      var _return = $467($464);
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
    var $471 = Option.$Some;
    var $470 = $ft;
    var _return = $471($470);
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
        var $487 = $tS;
        var $486 = $ft;
        var _return = $487($486);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $485 = $$std$prelude.$p$p;
          var $484 = $$std$prelude.$p$p;
          var $483 = $tS;
          var $482 = $ft;
          var $481 = $483($482);
          var $480 = $484($481);
          var $479 = ",";
          var $478 = $480($479);
          var $477 = $485($478);
          var $476 = $toStringI;
          var $475 = $rt;
          var $474 = $476($475);
          var $473 = $tS;
          var $472 = $474($473);
          var _return = $477($472);
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
    var $493 = $reduce;
    var $492 = $conc;
    var $491 = $493($492);
    var $490 = $Nil;
    var $489 = $491($490);
    var $488 = $list;
    var _return = $489($488);
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
      var $505 = $$std$prelude.$p$p;
      var $504 = $$std$prelude.$p$p;
      var $503 = "<";
      var $502 = $504($503);
      var $501 = $toStringI;
      var $500 = $list;
      var $499 = $501($500);
      var $498 = $tS;
      var $497 = $499($498);
      var $496 = $502($497);
      var $495 = $505($496);
      var $494 = ">";
      var _return = $495($494);
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