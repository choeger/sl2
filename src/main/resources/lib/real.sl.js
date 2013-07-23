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
exports.$cos = Math.cos;
var $cos = exports.$cos;
exports.$pow = function(a){return function(b){return Math.pow(a,b)};
var $pow = exports.$pow;
exports.$m = _sub;
var $m = exports.$m;
exports.$sqrt = Math.sqrt;
var $sqrt = exports.$sqrt;
exports.$fromInt = function(i){return i;};
var $fromInt = exports.$fromInt;
exports.$isNaN = isNaN;
var $isNaN = exports.$isNaN;
exports.$round = Math.round;
var $round = exports.$round;
exports.$p = _add;
var $p = exports.$p;
exports.$tan = Math.tan;
var $tan = exports.$tan;
exports.$abs = Math.abs;
var $abs = exports.$abs;
exports.$sin = Math.sin;
var $sin = exports.$sin;
exports.$log = Math.log;
var $log = exports.$log;
exports.$iNaN = NaN;
var $iNaN = exports.$iNaN;
exports.$d = _div;
var $d = exports.$d;
exports.$g = _greater;
var $g = exports.$g;
exports.$eps = function ()
{
  var $eps = 1.0E-16;
  return $eps
}();
var $eps = exports.$eps;
function $e$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $a = _arg0;
      var $b = _arg1;
      var $37 = $l;
      var $36 = $abs;
      var $35 = $m;
      var $34 = $a;
      var $33 = $35($34);
      var $32 = $b;
      var $31 = $33($32);
      var $30 = $36($31);
      var $29 = $37($30);
      var $28 = $eps;
      var _return = $29($28);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$e$e = $e$e;
function $d$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $43 = $$prelude.$not;
      var $42 = $e$e;
      var $41 = $x;
      var $40 = $42($41);
      var $39 = $y;
      var $38 = $40($39);
      var _return = $43($38);
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