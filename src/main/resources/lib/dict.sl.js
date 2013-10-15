/***********************************/
// generated from: dict
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl"); var Opt = require("std/option.sl"); var List = require("std/list.sl")

;
;
var $empty = {};
var $has = function(dict){return function(k){

	return (k in dict);

}};
var $filter = function(predicate){return function(dict){

	var newDict = {};

	for(var key in dict) {

      if (predicate(dict[key])) {

      	newDict[key] = dict[key];

      }

    }

    return newDict;

}};
var $merge = function(dict1){return function(dict2){

	var newDict = {};

	for(var key in dict1) {

     newDict[key] = dict1[key];

    }

    for(var key in dict2) {

     newDict[key] = dict2[key];

    }

    return newDict;

}};
var $reduce = function(f){return function(neut){return function(dict){

	for(var key in dict) {

      neut = f(key)(dict[key]);

    }

    return neut;

}}};
var $put = function(k){return function(v){return function(dict){

	var newDict = Object();

	newDict.__proto__ = dict;

	newDict[k] = v;

	return newDict;

}}};
var $get = function(dict){return function(k){

	if (k in dict) {

		return dict[k];

	} else {

		throw "Could not select <"+k+"> from dictionary.";

	}

}};
var $toStringI = function(tS){return function(dict){

	    var str = "";

		for(var key in dict) {

		  if (str != "") str += ",";

	      str += key + ":" + tS(dict[key]);

    	}

    	return str;

	}};
function $getOpt(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $dict = _arg0;
      var $k = _arg1;
      var $633 = $has;
      var $632 = $dict;
      var $631 = $633($632);
      var $630 = $k;
      var $629 = $631($630);
      if($629)
      {
        var $639 = Opt.$Some;
        var $638 = $get;
        var $637 = $dict;
        var $636 = $638($637);
        var $635 = $k;
        var $634 = $636($635);
        var _return = $639($634)
      }
      else 
      {
        var _return = Opt.$None
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $fromList(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $generator = _arg0;
      var $list = _arg1;
      var $655 = List.$reduce;
      var $644 = function (_arg0)
      {
        if(true)
        {
          var $k = _arg0;
          var $645 = function (_arg0)
          {
            if(true)
            {
              var $d = _arg0;
              var $654 = $put;
              var $653 = $k;
              var $652 = $654($653);
              var $651 = $generator;
              var $650 = $k;
              var $649 = $651($650);
              var $648 = $652($649);
              var $647 = $d;
              var $646 = $648($647)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $646
          }
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $645
      };
      var $643 = $655($644);
      var $642 = $empty;
      var $641 = $643($642);
      var $640 = $list;
      var _return = $641($640);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $toString(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $tS = _arg0;
      var $dict = _arg1;
      var $667 = $$std$prelude.$p$p;
      var $666 = $$std$prelude.$p$p;
      var $665 = "{";
      var $664 = $666($665);
      var $663 = $toStringI;
      var $662 = $tS;
      var $661 = $663($662);
      var $660 = $dict;
      var $659 = $661($660);
      var $658 = $664($659);
      var $657 = $667($658);
      var $656 = "}";
      var _return = $657($656);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$empty = $empty;
exports.$fromList = $fromList;
exports.$has = $has;
exports.$filter = $filter;
exports.$merge = $merge;
exports.$reduce = $reduce;
exports.$put = $put;
exports.$toString = $toString;
exports.$get = $get;
;
exports.$getOpt = $getOpt
});