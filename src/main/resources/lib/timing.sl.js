/***********************************/
// generated from: timing
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl")

;
;
;
var $clearInterval = clearInterval;
var $clearTimeout = clearTimeout;
function $timeout(_arg0)
{
  return function (_arg1)
  {
    if((((_arg0["_cid"] === $$std$prelude._SUSPEND) && true) && true))
    {
      var $cb = _arg0["_var0"];
      var $delay = _arg1;
      var _return = function ()
      {
        return setTimeout(function(){$cb(0)()}, $delay);
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $interval(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $cb = _arg0;
      var $delay = _arg1;
      var _return = function ()
      {
        return setInterval($cb, $delay);
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
;
exports.$interval = $interval;
exports.$clearInterval = $clearInterval;
exports.$timeout = $timeout;
exports.$clearTimeout = $clearTimeout
});