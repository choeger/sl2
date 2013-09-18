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
// generated from: real
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl")

;
var $pi = Math.PI;
var $t = _mul;
var $l$e = _leq;
var $fromString = parseFloat;
var $l = _lesser;
var $g$e = _geq;
var $cos = Math.cos;
var $pow = function(a){return function(b){return Math.pow(a,b);}};
var $m = _sub;
var $sqrt = Math.sqrt;
var $fromInt = function(i){return i;};
var $isNaN = isNaN;
var $toString = function(i){return i.toString();};
var $round = Math.round;
var $p = _add;
var $tan = Math.tan;
var $abs = Math.abs;
var $sin = Math.sin;
var $log = Math.log;
var $iNaN = NaN;
var $d = _divr;
var $g = _greater;
var $eps = function ()
{
  var $eps = 1.0E-16;
  return $eps
}();
function $e$e(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $a = _arg0;
      var $b = _arg1;
      var $21 = $l;
      var $20 = $abs;
      var $19 = $m;
      var $18 = $a;
      var $17 = $19($18);
      var $16 = $b;
      var $15 = $17($16);
      var $14 = $20($15);
      var $13 = $21($14);
      var $12 = $eps;
      var _return = $13($12);
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
      var $27 = $$std$prelude.$not;
      var $26 = $e$e;
      var $25 = $x;
      var $24 = $26($25);
      var $23 = $y;
      var $22 = $24($23);
      var _return = $27($22);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$pi = $pi;
exports.$t = $t;
exports.$l$e = $l$e;
exports.$d$e = $d$e;
exports.$fromString = $fromString;
exports.$l = $l;
exports.$g$e = $g$e;
exports.$cos = $cos;
exports.$pow = $pow;
exports.$m = $m;
exports.$sqrt = $sqrt;
exports.$e$e = $e$e;
exports.$fromInt = $fromInt;
exports.$eps = $eps;
exports.$isNaN = $isNaN;
exports.$toString = $toString;
exports.$round = $round;
exports.$p = $p;
exports.$tan = $tan;
exports.$abs = $abs;
exports.$sin = $sin;
exports.$log = $log;
exports.$iNaN = $iNaN;
exports.$d = $d;
exports.$g = $g
});