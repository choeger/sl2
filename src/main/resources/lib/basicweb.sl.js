/***********************************/
// generated from: basicweb.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl")

;
;
exports.$Document = 0;
var $Document = exports.$Document;
;
exports.$Node = 0;
var $Node = exports.$Node;
exports.$document = function ()
{
  var $document = function ()
  {
    return document
  };
  return $document
}();
var $document = exports.$document;
function $appendChild(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $doc = _arg0;
      var $child = _arg1;
      var _return = function ()
      {
        return $doc.body.appendChild($child)
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
exports.$appendChild = $appendChild;
function $create(_arg0)
{
  return function (_arg1)
  {
    return function (_arg2)
    {
      if(((true && true) && true))
      {
        var $doc = _arg0;
        var $text = _arg1;
        var $callback = _arg2;
        var $43 = $$prelude.$a$e;
        var $42 = function ()
        {
          return $doc.createElement('button')
        };
        var $41 = $43($42);
        var $29 = function (_arg0)
        {
          if(true)
          {
            var $button = _arg0;
            var $40 = $$prelude.$a;
            var $39 = $$prelude.$a;
            var $38 = function ()
            {
              return $button.innerText = $text
            };
            var $37 = $39($38);
            var $36 = function ()
            {
              return $button.onclick = $callback
            };
            var $35 = $37($36);
            var $34 = $40($35);
            var $33 = $$prelude.$yield;
            var $32 = $button;
            var $31 = $33($32);
            var $30 = $34($31)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $30
        };
        var _return = $41($29);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$create = $create
});