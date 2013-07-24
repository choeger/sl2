/***********************************/
// generated from: dict.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl"); var Opt = require("option.sl"); var List = require("list.sl")

;
;
var _Dict = 0;
function $Dict(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
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
      var $73 = $has;
      var $72 = $dict;
      var $71 = $73($72);
      var $70 = $k;
      var $69 = $71($70);
      if($69)
      {
        var $79 = Opt.$Some;
        var $78 = $get;
        var $77 = $dict;
        var $76 = $78($77);
        var $75 = $k;
        var $74 = $76($75);
        var _return = $79($74)
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
      var $95 = List.$reduce;
      var $84 = function (_arg0)
      {
        if(true)
        {
          var $k = _arg0;
          var $85 = function (_arg0)
          {
            if(true)
            {
              var $d = _arg0;
              var $94 = $put;
              var $93 = $k;
              var $92 = $94($93);
              var $91 = $generator;
              var $90 = $k;
              var $89 = $91($90);
              var $88 = $92($89);
              var $87 = $d;
              var $86 = $88($87)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $86
          }
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $85
      };
      var $83 = $95($84);
      var $82 = $empty;
      var $81 = $83($82);
      var $80 = $list;
      var _return = $81($80);
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
      var $107 = $$prelude.$p$p;
      var $106 = $$prelude.$p$p;
      var $105 = "{";
      var $104 = $106($105);
      var $103 = $toStringI;
      var $102 = $tS;
      var $101 = $103($102);
      var $100 = $dict;
      var $99 = $101($100);
      var $98 = $104($99);
      var $97 = $107($98);
      var $96 = "}";
      var _return = $97($96);
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