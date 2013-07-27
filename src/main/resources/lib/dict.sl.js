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
      var $902 = $has;
      var $901 = $dict;
      var $900 = $902($901);
      var $899 = $k;
      var $898 = $900($899);
      if($898)
      {
        var $908 = Opt.$Some;
        var $907 = $get;
        var $906 = $dict;
        var $905 = $907($906);
        var $904 = $k;
        var $903 = $905($904);
        var _return = $908($903)
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
      var $924 = List.$reduce;
      var $913 = function (_arg0)
      {
        if(true)
        {
          var $k = _arg0;
          var $914 = function (_arg0)
          {
            if(true)
            {
              var $d = _arg0;
              var $923 = $put;
              var $922 = $k;
              var $921 = $923($922);
              var $920 = $generator;
              var $919 = $k;
              var $918 = $920($919);
              var $917 = $921($918);
              var $916 = $d;
              var $915 = $917($916)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $915
          }
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $914
      };
      var $912 = $924($913);
      var $911 = $empty;
      var $910 = $912($911);
      var $909 = $list;
      var _return = $910($909);
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
      var $936 = $$std$prelude.$p$p;
      var $935 = $$std$prelude.$p$p;
      var $934 = "{";
      var $933 = $935($934);
      var $932 = $toStringI;
      var $931 = $tS;
      var $930 = $932($931);
      var $929 = $dict;
      var $928 = $930($929);
      var $927 = $933($928);
      var $926 = $936($927);
      var $925 = "}";
      var _return = $926($925);
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