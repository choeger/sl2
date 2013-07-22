/***********************************/
// generated from: basicio.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl")

;
exports.$andPrint = function(res){return function(printer){
	console && console.log(printer(res)); 
	return res;
}};
var $andPrint = exports.$andPrint;
exports.$anyToString = function(some){
	return some.toString();
};
var $anyToString = exports.$anyToString;
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
exports.$print = $print;
function $andPrintDbg(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $a = _arg0;
      var $s = _arg1;
      var $15 = $andPrint;
      var $14 = $a;
      var $13 = $15($14);
      var $11 = function (_arg0)
      {
        if(true)
        {
          var $r = _arg0;
          var $12 = $s
        }
        else 
        {
          throw "Pattern for lambda expression did not match arguments"
        };
        return $12
      };
      var _return = $13($11);
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$andPrintDbg = $andPrintDbg
});