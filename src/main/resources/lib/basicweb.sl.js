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
          var $812 = $$std$prelude.$a$e;
          var $811 = $createElement;
          var $810 = $doc;
          var $809 = $811($810);
          var $808 = "input";
          var $807 = $809($808);
          var $806 = $812($807);
          var $790 = function (_arg0)
          {
            if(true)
            {
              var $node = _arg0;
              var $805 = $$std$prelude.$a;
              var $804 = $$std$prelude.$a;
              var $803 = $$std$prelude.$a;
              var $802 = function ()
              {
                return $node.type = $type
              };
              var $801 = $803($802);
              var $800 = function ()
              {
                return $node.value = $text
              };
              var $799 = $801($800);
              var $798 = $804($799);
              var $797 = function ()
              {
                return $node.onclick = $callback
              };
              var $796 = $798($797);
              var $795 = $805($796);
              var $794 = $$std$prelude.$yield;
              var $793 = $node;
              var $792 = $794($793);
              var $791 = $795($792)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $791
          };
          var _return = $806($790);
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
        var $820 = $createInputElement;
        var $819 = $doc;
        var $818 = $820($819);
        var $817 = "text";
        var $816 = $818($817);
        var $815 = $text;
        var $814 = $816($815);
        var $813 = $callback;
        var _return = $814($813);
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
        var $828 = $createInputElement;
        var $827 = $doc;
        var $826 = $828($827);
        var $825 = "button";
        var $824 = $826($825);
        var $823 = $text;
        var $822 = $824($823);
        var $821 = $callback;
        var _return = $822($821);
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