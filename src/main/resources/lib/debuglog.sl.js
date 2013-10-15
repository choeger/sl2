/***********************************/
// generated from: debuglog
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl")

;
var $logAvailable = typeof console != "undefined";
var $andPrint = function(res){return function(printer){

	console && console.log(printer(res)); 

	return res;

}};
var $anyToString = function(some){

	return some.toString();

};
function $print(_arg0)
{
  if(true)
  {
    var $str = _arg0;
    var _return = function ()
    {
      return console && console.log($str);
    };
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $andPrintMessage(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $a = _arg0;
      var $s = _arg1;
      var $38 = $andPrint;
      var $37 = $a;
      var $36 = $38($37);
      var $34 = function (_arg0)
      {
        if(true)
        {
          var $r = _arg0;
          var $35 = $s
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $35
      };
      var _return = $36($34);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$andPrint = $andPrint;
exports.$anyToString = $anyToString;
exports.$print = $print;
exports.$logAvailable = $logAvailable;
exports.$andPrintMessage = $andPrintMessage
});