/***********************************/
// generated from: timing.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl")

;
;
;
exports.$clearInterval = clearInterval;
var $clearInterval = exports.$clearInterval;
exports.$clearTimeout = clearTimeout;
var $clearTimeout = exports.$clearTimeout;
function $timeout(_arg0)
{
  return function (_arg1)
  {
    if((((_arg0["_cid"] === $$prelude._SUSPEND) && true) && true))
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
exports.$timeout = $timeout;
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
exports.$interval = $interval
});