/***********************************/
// generated from: basicweb
/***********************************/
define(function(require, exports, module) {
    var $$std$prelude = require("std/prelude.sl"); var List = require("std/list.sl")

;
;
;
var $getChildNodes = function(node){return function(){

	var list = List.$Nil;

	for (var i=0; i<node.childNodes.length; i++) {

		list = List.$Cons(node.childNodes[i])(list);

	}

	return list;

}};
function $createElement(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $doc = _arg0;
      var $elemType = _arg1;
      var _return = function ()
      {
        return $doc.createElement($elemType)
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $appendChild(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $node = _arg0;
      var $child = _arg1;
      var _return = function ()
      {
        return $node.appendChild($child)
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
var $document = function ()
{
  var $document = function ()
  {
    return document
  };
  return $document
}();
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
function $setOnClick(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $node = _arg0;
      var $cb = _arg1;
      var _return = function ()
      {
        return $node.onclick = $cb
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $prompt(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $msg = _arg0;
      var $pre = _arg1;
      var _return = function ()
      {
        return prompt($msg, $pre)
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $setValue(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $node = _arg0;
      var $value = _arg1;
      var _return = function ()
      {
        return $node.value = $value
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $removeChild(_arg0)
{
  return function (_arg1)
  {
    if((true && true))
    {
      var $node = _arg0;
      var $child = _arg1;
      var _return = function ()
      {
        return $node.removeChild($child)
      };
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
};
function $getValue(_arg0)
{
  if(true)
  {
    var $node = _arg0;
    var _return = function ()
    {
      return $node.value
    };
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $getBody(_arg0)
{
  if(true)
  {
    var $doc = _arg0;
    var _return = function ()
    {
      return $doc.body
    };
    return _return
  }
  else 
  {
    throw "Pattern not exhaustive!"
  }
};
function $createInputElement(_arg0)
{
  return function (_arg1)
  {
    return function (_arg2)
    {
      return function (_arg3)
      {
        if((((true && true) && true) && true))
        {
          var $doc = _arg0;
          var $type = _arg1;
          var $text = _arg2;
          var $callback = _arg3;
          var $582 = $$std$prelude.$a$e;
          var $581 = $createElement;
          var $580 = $doc;
          var $579 = $581($580);
          var $578 = "input";
          var $577 = $579($578);
          var $576 = $582($577);
          var $560 = function (_arg0)
          {
            if(true)
            {
              var $node = _arg0;
              var $575 = $$std$prelude.$a;
              var $574 = $$std$prelude.$a;
              var $573 = $$std$prelude.$a;
              var $572 = function ()
              {
                return $node.type = $type
              };
              var $571 = $573($572);
              var $570 = function ()
              {
                return $node.value = $text
              };
              var $569 = $571($570);
              var $568 = $574($569);
              var $567 = function ()
              {
                return $node.onclick = $callback
              };
              var $566 = $568($567);
              var $565 = $575($566);
              var $564 = $$std$prelude.$yield;
              var $563 = $node;
              var $562 = $564($563);
              var $561 = $565($562)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $561
          };
          var _return = $576($560);
          return _return
        }
        else 
        {
          throw "Pattern not exhaustive!"
        }
      }
    }
  }
};
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
        var $590 = $createInputElement;
        var $589 = $doc;
        var $588 = $590($589);
        var $587 = "text";
        var $586 = $588($587);
        var $585 = $text;
        var $584 = $586($585);
        var $583 = $callback;
        var _return = $584($583);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
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
        var $598 = $createInputElement;
        var $597 = $doc;
        var $596 = $598($597);
        var $595 = "button";
        var $594 = $596($595);
        var $593 = $text;
        var $592 = $594($593);
        var $591 = $callback;
        var _return = $592($591);
        return _return
      }
      else 
      {
        throw "Pattern not exhaustive!"
      }
    }
  }
};
;
exports.$createElement = $createElement;
exports.$appendChild = $appendChild;
exports.$document = $document;
exports.$createInputElement = $createInputElement;
exports.$alert = $alert;
exports.$setOnClick = $setOnClick;
exports.$prompt = $prompt;
exports.$createInput = $createInput;
exports.$setValue = $setValue;
exports.$getChildNodes = $getChildNodes;
exports.$removeChild = $removeChild;
exports.$getValue = $getValue;
exports.$getBody = $getBody;
exports.$createButton = $createButton
});