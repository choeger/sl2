/***********************************/
// included from: /home/ben/Dokumente/compilerbaupraxis/sl2/src/main/resources/lib/_prelude.js
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
// generated from: prelude.sl
/***********************************/
define(function(require, exports, module) {
    

;
;
;
exports.$Void = 0;
var $Void = exports.$Void;
;
;
;
;
var $True = true;
var $False = false;
exports.$stringToInt = parseInt;
var $stringToInt = exports.$stringToInt;
exports.$charToInt = function(c){return c.charCodeAt(0);};
var $charToInt = exports.$charToInt;
exports.$stringGetChar = function(s){return function(i){
	if (s.length < i) {
		throw "stringGetChar failed: Char index out of bounds"
	} else {
		return s.charAt(i);
	}
}};
var $stringGetChar = exports.$stringGetChar;
exports.$charToString = function(c){return c;};
var $charToString = exports.$charToString;
exports.$t = _mul;
var $t = exports.$t;
exports.$l$e = _leq;
var $l$e = exports.$l$e;
exports.$l = _lesser;
var $l = exports.$l;
exports.$a = _bindnr;
var $a = exports.$a;
exports.$g$e = _geq;
var $g$e = exports.$g$e;
exports.$yield = _yield;
var $yield = exports.$yield;
exports.$intToChar = String.fromCharCode;
var $intToChar = exports.$intToChar;
exports.$m = _sub;
var $m = exports.$m;
exports.$a$e = _bind;
var $a$e = exports.$a$e;
exports.$p$p = _adds;
var $p$p = exports.$p$p;
exports.$error = function(msg){throw msg};
var $error = exports.$error;
exports.$e$e = _eq;
var $e$e = exports.$e$e;
exports.$isNaN = isNaN;
var $isNaN = exports.$isNaN;
exports.$intToString = function(i){return i.toString();};
var $intToString = exports.$intToString;
exports.$p = _add;
var $p = exports.$p;
exports.$iNaN = NaN;
var $iNaN = exports.$iNaN;
exports.$d = _div;
var $d = exports.$d;
exports.$g = _greater;
var $g = exports.$g;
exports.$noop = function ()
{
  var $26 = $yield;
  var $25 = $Void;
  var $noop = $26($25);
  return $noop
}();
var $noop = exports.$noop;
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
exports.$id = $id;
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
          var $31 = $f;
          var $30 = $g;
          var $29 = $x;
          var $28 = $30($29);
          var $27 = $31($28)
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $27
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$s = $s;
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
exports.$not = $not;
function $d$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $37 = $not;
      var $36 = $e$e;
      var $35 = $x;
      var $34 = $36($35);
      var $33 = $y;
      var $32 = $34($33);
      var _return = $37($32);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$d$e = $d$e
});