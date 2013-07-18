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
function $createButton(_arg0)
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
        var $65 = $$prelude.$a$e;
        var $64 = function ()
        {
          return $doc.createElement('button')
        };
        var $63 = $65($64);
        var $51 = function (_arg0)
        {
          if(true)
          {
            var $button = _arg0;
            var $62 = $$prelude.$a;
            var $61 = $$prelude.$a;
            var $60 = function ()
            {
              return $button.innerText = $text
            };
            var $59 = $61($60);
            var $58 = function ()
            {
              return $button.onclick = $callback
            };
            var $57 = $59($58);
            var $56 = $62($57);
            var $55 = $$prelude.$yield;
            var $54 = $button;
            var $53 = $55($54);
            var $52 = $56($53)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $52
        };
        var _return = $63($51);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$createButton = $createButton;
exports.$hello = function ()
{
  var $73 = $$prelude.$a;
  var $72 = $$prelude.$yield;
  var $71 = 23;
  var $70 = $72($71);
  var $69 = $73($70);
  var $68 = $$prelude.$yield;
  var $67 = "S";
  var $66 = $68($67);
  var $hello = $69($66);
  return $hello
}();
var $hello = exports.$hello
});