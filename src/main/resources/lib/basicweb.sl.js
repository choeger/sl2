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
exports.$document = function ()
{
  var $document = function ()
  {
    return document
  };
  return $document
}();
var $document = exports.$document;
function $alert(_arg0)
{
  if(true)
  {
    var $msg = _arg0;
    var _return = function ()
    {
      return alert($msg)
    };
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
exports.$alert = $alert;
function $createInput(_arg0)
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
        var $103 = $$prelude.$a$e;
        var $102 = function ()
        {
          return $doc.createElement('input')
        };
        var $101 = $103($102);
        var $85 = function (_arg0)
        {
          if(true)
          {
            var $node = _arg0;
            var $100 = $$prelude.$a;
            var $99 = $$prelude.$a;
            var $98 = $$prelude.$a;
            var $97 = function ()
            {
              return $node.type = "text"
            };
            var $96 = $98($97);
            var $95 = function ()
            {
              return $node.value = $text
            };
            var $94 = $96($95);
            var $93 = $99($94);
            var $92 = function ()
            {
              return $node.onclick = $callback
            };
            var $91 = $93($92);
            var $90 = $100($91);
            var $89 = $$prelude.$yield;
            var $88 = $node;
            var $87 = $89($88);
            var $86 = $90($87)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $86
        };
        var _return = $101($85);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$createInput = $createInput;
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
        var $122 = $$prelude.$a$e;
        var $121 = function ()
        {
          return $doc.createElement('input')
        };
        var $120 = $122($121);
        var $104 = function (_arg0)
        {
          if(true)
          {
            var $button = _arg0;
            var $119 = $$prelude.$a;
            var $118 = $$prelude.$a;
            var $117 = $$prelude.$a;
            var $116 = function ()
            {
              return $button.type = "button"
            };
            var $115 = $117($116);
            var $114 = function ()
            {
              return $button.value = $text
            };
            var $113 = $115($114);
            var $112 = $118($113);
            var $111 = function ()
            {
              return $button.onclick = $callback
            };
            var $110 = $112($111);
            var $109 = $119($110);
            var $108 = $$prelude.$yield;
            var $107 = $button;
            var $106 = $108($107);
            var $105 = $109($106)
          }
          else 
          {
            throw "Pattern for lambda expression did not match arguments"
          };
          return $105
        };
        var _return = $120($104);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
exports.$createButton = $createButton
});