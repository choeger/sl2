/***********************************/
// included from: /home/ben/Dokumente/compilerbaupraxis/sl2/target/scala-2.10/classes/lib/_prelude.js
/***********************************/
/*

 * This basic module is necessary as an import to every SL

 * source code. It defines the builtins.

 */



function toArray(x) {

    return Array.prototype.slice.call(x);

};



function liftCurry(f) {

    return function () {

	var args = toArray(arguments);

	if (args.length >= f.length) {

	    return f.apply(this, args);

	} else {

	    return curry(f, args, f.length - args.length);

	}

    } 

};



function curry(f, args, missing) {

    if (args.length<1) {

        return f; //nothing to curry with - return function

    }



    return function () {

	var nargs = args.concat(toArray(arguments));

	if (arguments.length >= missing) {

	    return f.apply(this, nargs);

	} else {

	    return curry(f, nargs, missing - arguments.length);

	}

    }



};



function _add(arg1) {

    return function(arg2) {

	return arg1 + arg2;

    };

};



var _adds = _add;



var _addr = _add;



function _sub(arg1) {

    return function(arg2) {

	return arg1 - arg2;

    };

};



var _subr = _sub;



function _mul(arg1) {

    return function(arg2) {

	return arg1 * arg2;

    };

};



var _mulr = _mul;



function _div(arg1) {

    return function(arg2) {

	return Math.floor(arg1 / arg2);

    };

};



function _divr(arg1) {

    return function(arg2) {

	return arg1 / arg2;

    };

};





function _eq(l){

    return function(r){

	return l == r;

    };

}



function _geq(l)

{

    return function(r)

    {

	return l >= r;

    };

}



function _leq(l)

{

    return function(r)

    {

	return l <= r;

    };

}



function _lesser(l)

{

    return function(r)

    {

	return l < r;

    };

}



function _greater(l)

{

    return function(r)

    {

	return l > r;

    };

}



function $not(arg) {

    return !arg;

}



function _yield(r) {

  return function() { return r; };

}



function _bind(l) {

  return function(r) {

      return function() {

	  /* l is a monad yielding a value, so evaluate l*/

	  var lv = l();

	  /* r is a function yielding a monad, evaluate r and then the result */

	  return r(lv)();

      }

  };

}



function _bindnr(l) {

  return function(r) {

      return function() {

	  var lv = l();

	  return r();

      };

  };

}

/***********************************/
/***********************************/
// generated from: prelude
/***********************************/
define(function(require, exports, module) {
    

;
;
var _SUSPEND = 0;
function $SUSPEND(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  }
};
exports._SUSPEND = _SUSPEND;
exports.$SUSPEND = $SUSPEND;
;
;
var $Void = 0;
exports.$Void = $Void;
;
;
;
;
var $True = true;
var $False = false;
exports.$True = true;
exports.$False = false;
var $stringToInt = parseInt;
var $charToInt = function(c){return c.charCodeAt(0);};
var $stringGetChar = function(s){return function(i){

	if (s.length < i) {

		throw "stringGetChar failed: Char index out of bounds"

	} else {

		return s.charAt(i);

	}

}};
var $strEq = _eq;
var $charToString = function(c){return c;};
var $t = _mul;
var $l$e = _leq;
var $r = function(a){return function(b){ return a%b; }};
var $l = _lesser;
var $a = _bindnr;
var $g$e = _geq;
var $yield = _yield;
var $intToChar = String.fromCharCode;
var $m = _sub;
var $a$e = _bind;
var $p$p = _adds;
var $error = function(msg){throw msg};
var $e$e = _eq;
var $isNaN = isNaN;
var $intToString = function(i){return i.toString();};
var $p = _add;
var $iNaN = NaN;
var $d = _div;
var $g = _greater;
function $force(_arg0)
{
  if(((_arg0["_cid"] === _SUSPEND) && true))
  {
    var $f = _arg0["_var0"];
    var $19 = $f;
    var $18 = $Void;
    var _return = $19($18);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $neg(_arg0)
{
  if(true)
  {
    var $i = _arg0;
    var $23 = $m;
    var $22 = 0;
    var $21 = $23($22);
    var $20 = $i;
    var _return = $21($20);
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
var $noop = function ()
{
  var $25 = $yield;
  var $24 = $Void;
  var $noop = $25($24);
  return $noop
}();
function $id(_arg0)
{
  if(true)
  {
    var $a = _arg0;
    var _return = $a;
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $s(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $f = _arg0;
      var $g = _arg1;
      var _return = function (_arg0)
      {
        if(true)
        {
          var $x = _arg0;
          var $30 = $f;
          var $29 = $g;
          var $28 = $x;
          var $27 = $29($28);
          var $26 = $30($27)
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $26
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $not(_arg0)
{
  if((_arg0 === $True))
  {
    var _return = $False;
    return _return
  }
  else 
  {
    if((_arg0 === $False))
    {
      var _return = $True;
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $boolToString(_arg0)
{
  if((_arg0 === $True))
  {
    var _return = "True";
    return _return
  }
  else 
  {
    if((_arg0 === $False))
    {
      var _return = "False";
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $d$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $36 = $not;
      var $35 = $e$e;
      var $34 = $x;
      var $33 = $35($34);
      var $32 = $y;
      var $31 = $33($32);
      var _return = $36($31);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$stringToInt = $stringToInt;
exports.$charToInt = $charToInt;
exports.$stringGetChar = $stringGetChar;
exports.$strEq = $strEq;
exports.$charToString = $charToString;
exports.$t = $t;
exports.$force = $force;
exports.$l$e = $l$e;
exports.$r = $r;
exports.$d$e = $d$e;
exports.$l = $l;
exports.$a = $a;
exports.$g$e = $g$e;
exports.$yield = $yield;
exports.$intToChar = $intToChar;
exports.$neg = $neg;
exports.$m = $m;
exports.$noop = $noop;
exports.$a$e = $a$e;
exports.$id = $id;
exports.$p$p = $p$p;
exports.$error = $error;
exports.$e$e = $e$e;
exports.$s = $s;
exports.$not = $not;
exports.$isNaN = $isNaN;
exports.$boolToString = $boolToString;
exports.$intToString = $intToString;
exports.$p = $p;
exports.$iNaN = $iNaN;
exports.$d = $d;
exports.$g = $g
});