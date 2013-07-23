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
        var $311 = $Cons;
        var $310 = $ft;
        var $309 = $311($310);
        var $308 = $conc;
        var $307 = $rt;
        var $306 = $308($307);
        var $305 = $list;
        var $304 = $306($305);
        var _return = $309($304);
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
        var $321 = $Cons;
        var $320 = $f;
        var $319 = $ft;
        var $318 = $320($319);
        var $317 = $321($318);
        var $316 = $map;
        var $315 = $f;
        var $314 = $316($315);
        var $313 = $rt;
        var $312 = $314($313);
        var _return = $317($312);
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
      var $323 = $$prelude.$error;
      var $322 = "Cannot select head from empty list.";
      var _return = $323($322);
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
    var $327 = $Cons;
    var $326 = $a;
    var $325 = $327($326);
    var $324 = $Nil;
    var _return = $325($324);
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
        var $330 = $p;
        var $329 = $ft;
        var $328 = $330($329);
        if($328)
        {
          var $338 = $Cons;
          var $337 = $ft;
          var $336 = $338($337);
          var $335 = $filter;
          var $334 = $p;
          var $333 = $335($334);
          var $332 = $rt;
          var $331 = $333($332);
          var _return = $336($331)
        }
        else 
        {
          var $342 = $filter;
          var $341 = $p;
          var $340 = $342($341);
          var $339 = $rt;
          var _return = $340($339)
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
        var $345 = $p;
        var $344 = $ft;
        var $343 = $345($344);
        if($343)
        {
          var _return = $rt
        }
        else 
        {
          var $349 = $removeFirst;
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
exports.$removeFirst = $removeFirst;
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
          var $364 = $$prelude.$a$e;
          var $363 = $reduceDom;
          var $362 = $f;
          var $361 = $363($362);
          var $360 = $n;
          var $359 = $361($360);
          var $358 = $rt;
          var $357 = $359($358);
          var $356 = $364($357);
          var $350 = function (_arg0)
          {
            if(true)
            {
              var $res = _arg0;
              var $355 = $f;
              var $354 = $ft;
              var $353 = $355($354);
              var $352 = $res;
              var $351 = $353($352)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $351
          };
          var _return = $356($350);
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
exports.$reduceDom = $reduceDom;
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
          var $374 = $f;
          var $373 = $ft;
          var $372 = $374($373);
          var $371 = $reduce;
          var $370 = $f;
          var $369 = $371($370);
          var $368 = $n;
          var $367 = $369($368);
          var $366 = $rt;
          var $365 = $367($366);
          var _return = $372($365);
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
      var $398 = $$prelude.$yield;
      var $397 = $Nil;
      var _return = $398($397);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $396 = $$prelude.$a$e;
        var $395 = $f;
        var $394 = $ft;
        var $393 = $395($394);
        var $392 = $396($393);
        var $375 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $391 = $$prelude.$a$e;
            var $390 = $mapDom;
            var $389 = $f;
            var $388 = $390($389);
            var $387 = $rt;
            var $386 = $388($387);
            var $385 = $391($386);
            var $377 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $384 = $$prelude.$yield;
                var $383 = $Cons;
                var $382 = $newFt;
                var $381 = $383($382);
                var $380 = $newRt;
                var $379 = $381($380);
                var $378 = $384($379)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $378
            };
            var $376 = $385($377)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $376
        };
        var _return = $392($375);
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
      var $404 = $$prelude.$p;
      var $403 = 1;
      var $402 = $404($403);
      var $401 = $length;
      var $400 = $rt;
      var $399 = $401($400);
      var _return = $402($399);
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
        var $420 = $tS;
        var $419 = $ft;
        var _return = $420($419);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $418 = $$prelude.$p$p;
          var $417 = $$prelude.$p$p;
          var $416 = $tS;
          var $415 = $ft;
          var $414 = $416($415);
          var $413 = $417($414);
          var $412 = ",";
          var $411 = $413($412);
          var $410 = $418($411);
          var $409 = $toStringI;
          var $408 = $rt;
          var $407 = $409($408);
          var $406 = $tS;
          var $405 = $407($406);
          var _return = $410($405);
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
    var $426 = $reduce;
    var $425 = $conc;
    var $424 = $426($425);
    var $423 = $Nil;
    var $422 = $424($423);
    var $421 = $list;
    var _return = $422($421);
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
      var $438 = $$prelude.$p$p;
      var $437 = $$prelude.$p$p;
      var $436 = "<";
      var $435 = $437($436);
      var $434 = $toStringI;
      var $433 = $list;
      var $432 = $434($433);
      var $431 = $tS;
      var $430 = $432($431);
      var $429 = $435($430);
      var $428 = $438($429);
      var $427 = ">";
      var _return = $428($427);
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