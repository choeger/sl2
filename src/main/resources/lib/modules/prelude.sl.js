/***********************************/
// included from: /home/ben/Dokumente/compilerbaupraxis/sl2/src/main/sl/lib/_prelude.js
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
var $True = true;
var $False = false;
;
exports._ExternalDOM = 0;
var _ExternalDOM = exports._ExternalDOM;
function $ExternalDOM(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
exports.$ExternalDOM = $ExternalDOM;
;
exports.$ExternalString = 0;
var $ExternalString = exports.$ExternalString;
;
exports.$ExternalVoid = 0;
var $ExternalVoid = exports.$ExternalVoid;
;
exports.$ExternalChar = 0;
var $ExternalChar = exports.$ExternalChar;
;
exports.$ExternalReal = 0;
var $ExternalReal = exports.$ExternalReal;
;
exports.$ExternalInt = 0;
var $ExternalInt = exports.$ExternalInt;
exports.$t = _mul;
var $t = exports.$t;
exports.$l$e = _leq;
var $l$e = exports.$l$e;
exports.$l = _lesser;
var $l = exports.$l;
exports.$g$e = _geq;
var $g$e = exports.$g$e;
exports.$yield = _yield;
var $yield = exports.$yield;
exports.$m = _sub;
var $m = exports.$m;
exports.$a$e = _bind;
var $a$e = exports.$a$e;
exports.$p$p = _adds;
var $p$p = exports.$p$p;
exports.$e$e = _eq;
var $e$e = exports.$e$e;
exports.$p = _add;
var $p = exports.$p;
exports.$d = _div;
var $d = exports.$d;
exports.$g = _greater;
var $g = exports.$g;
exports.$not = function (_arg0)
{
  if((_arg0 === True))
  {
    var _return = $False;
    return _return
  }
  else 
  {
    if((_arg0 === False))
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
exports.$a = function (_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $25 = $a$e;
      var $24 = $x;
      var $23 = $25($24);
      var $21 = function (_arg0)
      {
        if(true)
        {
          var $r = _arg0;
          var $22 = $y
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $22
      };
      var _return = $23($21);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$d$e = function (_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $31 = $not;
      var $30 = $e$e;
      var $29 = $x;
      var $28 = $30($29);
      var $27 = $y;
      var $26 = $28($27);
      var _return = $31($26);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
}
});