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
;
;
var $Nil = 0;
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
var $True = true;
var $False = false;
;
var _ExternalDOM = 0;
function $ExternalDOM(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
;
var $ExternalString = 0;
;
var $ExternalVoid = 0;
;
var $ExternalChar = 0;
;
var $ExternalReal = 0;
;
var $ExternalInt = 0;
var $t =  _mul ;
var $l$e =  _leq ;
var $l =  _lesser ;
var $g$e =  _geq ;
var $yield =  _yield ;
var $m =  _sub ;
var $a$e =  _bind ;
var $p$p =  _adds ;
var $e$e =  _eq ;
var $p =  _add ;
var $d =  _div ;
var $g =  _greater ;
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
function $a(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $x = _arg0;
      var $y = _arg1;
      var $14 = $a$e;
      var $13 = $x;
      var $12 = $14($13);
      var $10 = function (_arg0)
      {
        if(true)
        {
          var $r = _arg0;
          var $11 = $y
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $11
      };
      var _return = $12($10);
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
      var $20 = $not;
      var $19 = $e$e;
      var $18 = $x;
      var $17 = $19($18);
      var $16 = $y;
      var $15 = $17($16);
      var _return = $20($15);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
}