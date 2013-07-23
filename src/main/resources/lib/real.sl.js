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
// generated from: real.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl")

;
exports.$t = _mul;
var $t = exports.$t;
exports.$l$e = _leq;
var $l$e = exports.$l$e;
exports.$l = _lesser;
var $l = exports.$l;
exports.$g$e = _geq;
var $g$e = exports.$g$e;
exports.$m = _sub;
var $m = exports.$m;
exports.$e$e = _eq;
var $e$e = exports.$e$e;
exports.$fromInt = function(i){return i;};
var $fromInt = exports.$fromInt;
exports.$isNaN = isNaN;
var $isNaN = exports.$isNaN;
exports.$round = Math.round;
var $round = exports.$round;
exports.$p = _add;
var $p = exports.$p;
exports.$abs = Math.abs;
var $abs = exports.$abs;
exports.$iNaN = NaN;
var $iNaN = exports.$iNaN;
exports.$d = _div;
var $d = exports.$d;
exports.$g = _greater;
var $g = exports.$g;
function $d$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $16 = $$prelude.$not;
      var $15 = $e$e;
      var $14 = $x;
      var $13 = $15($14);
      var $12 = $y;
      var $11 = $13($12);
      var _return = $16($11);
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