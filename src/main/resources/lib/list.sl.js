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
        var $565 = $Cons;
        var $564 = $ft;
        var $563 = $565($564);
        var $562 = $conc;
        var $561 = $rt;
        var $560 = $562($561);
        var $559 = $list;
        var $558 = $560($559);
        var _return = $563($558);
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
        var $575 = $Cons;
        var $574 = $f;
        var $573 = $ft;
        var $572 = $574($573);
        var $571 = $575($572);
        var $570 = $map;
        var $569 = $f;
        var $568 = $570($569);
        var $567 = $rt;
        var $566 = $568($567);
        var _return = $571($566);
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
      var $577 = $$std$prelude.$error;
      var $576 = "Cannot select head from empty list.";
      var _return = $577($576);
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
    var $581 = $Cons;
    var $580 = $a;
    var $579 = $581($580);
    var $578 = $Nil;
    var _return = $579($578);
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
        var $584 = $p;
        var $583 = $ft;
        var $582 = $584($583);
        if($582)
        {
          var $592 = $Cons;
          var $591 = $ft;
          var $590 = $592($591);
          var $589 = $filter;
          var $588 = $p;
          var $587 = $589($588);
          var $586 = $rt;
          var $585 = $587($586);
          var _return = $590($585)
        }
        else 
        {
          var $596 = $filter;
          var $595 = $p;
          var $594 = $596($595);
          var $593 = $rt;
          var _return = $594($593)
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
        var $599 = $p;
        var $598 = $ft;
        var $597 = $599($598);
        if($597)
        {
          var _return = $rt
        }
        else 
        {
          var $607 = $Cons;
          var $606 = $ft;
          var $605 = $607($606);
          var $604 = $removeFirst;
          var $603 = $p;
          var $602 = $604($603);
          var $601 = $rt;
          var $600 = $602($601);
          var _return = $605($600)
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
          var $622 = $$std$prelude.$a$e;
          var $621 = $reduceDom;
          var $620 = $f;
          var $619 = $621($620);
          var $618 = $n;
          var $617 = $619($618);
          var $616 = $rt;
          var $615 = $617($616);
          var $614 = $622($615);
          var $608 = function (_arg0)
          {
            if(true)
            {
              var $res = _arg0;
              var $613 = $f;
              var $612 = $ft;
              var $611 = $613($612);
              var $610 = $res;
              var $609 = $611($610)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $609
          };
          var _return = $614($608);
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
          var $632 = $f;
          var $631 = $ft;
          var $630 = $632($631);
          var $629 = $reduce;
          var $628 = $f;
          var $627 = $629($628);
          var $626 = $n;
          var $625 = $627($626);
          var $624 = $rt;
          var $623 = $625($624);
          var _return = $630($623);
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
      var $656 = $$std$prelude.$yield;
      var $655 = $Nil;
      var _return = $656($655);
      return _return
    }
    else 
    {
      if((true && (((_arg1["_cid"] === _Cons) && true) && true)))
      {
        var $f = _arg0;
        var $ft = _arg1["_var0"];
        var $rt = _arg1["_var1"];
        var $654 = $$std$prelude.$a$e;
        var $653 = $f;
        var $652 = $ft;
        var $651 = $653($652);
        var $650 = $654($651);
        var $633 = function (_arg0)
        {
          if(true)
          {
            var $newFt = _arg0;
            var $649 = $$std$prelude.$a$e;
            var $648 = $mapDom;
            var $647 = $f;
            var $646 = $648($647);
            var $645 = $rt;
            var $644 = $646($645);
            var $643 = $649($644);
            var $635 = function (_arg0)
            {
              if(true)
              {
                var $newRt = _arg0;
                var $642 = $$std$prelude.$yield;
                var $641 = $Cons;
                var $640 = $newFt;
                var $639 = $641($640);
                var $638 = $newRt;
                var $637 = $639($638);
                var $636 = $642($637)
              }
              else 
              {
                throw "Pattern for lambda expression did not match arguments"
              };
              return $636
            };
            var $634 = $643($635)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $634
        };
        var _return = $650($633);
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
      var $662 = $$std$prelude.$p;
      var $661 = 1;
      var $660 = $662($661);
      var $659 = $length;
      var $658 = $rt;
      var $657 = $659($658);
      var _return = $660($657);
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
        var $678 = $tS;
        var $677 = $ft;
        var _return = $678($677);
        return _return
      }
      else 
      {
        if(((((_arg0["_cid"] === _Cons) && true) && true) && true))
        {
          var $ft = _arg0["_var0"];
          var $rt = _arg0["_var1"];
          var $tS = _arg1;
          var $676 = $$std$prelude.$p$p;
          var $675 = $$std$prelude.$p$p;
          var $674 = $tS;
          var $673 = $ft;
          var $672 = $674($673);
          var $671 = $675($672);
          var $670 = ",";
          var $669 = $671($670);
          var $668 = $676($669);
          var $667 = $toStringI;
          var $666 = $rt;
          var $665 = $667($666);
          var $664 = $tS;
          var $663 = $665($664);
          var _return = $668($663);
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
    var $684 = $reduce;
    var $683 = $conc;
    var $682 = $684($683);
    var $681 = $Nil;
    var $680 = $682($681);
    var $679 = $list;
    var _return = $680($679);
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
      var $696 = $$std$prelude.$p$p;
      var $695 = $$std$prelude.$p$p;
      var $694 = "<";
      var $693 = $695($694);
      var $692 = $toStringI;
      var $691 = $list;
      var $690 = $692($691);
      var $689 = $tS;
      var $688 = $690($689);
      var $687 = $693($688);
      var $686 = $696($687);
      var $685 = ">";
      var _return = $686($685);
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