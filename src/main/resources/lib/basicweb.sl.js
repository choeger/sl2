/***********************************/
// generated from: basicweb.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl"); var List = require("list.sl")

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
          var $115 = $$prelude.$a$e;
          var $114 = $createElement;
          var $113 = $doc;
          var $112 = $114($113);
          var $111 = "input";
          var $110 = $112($111);
          var $109 = $115($110);
          var $93 = function (_arg0)
          {
            if(true)
            {
              var $node = _arg0;
              var $108 = $$prelude.$a;
              var $107 = $$prelude.$a;
              var $106 = $$prelude.$a;
              var $105 = function ()
              {
                return $node.type = $type
              };
              var $104 = $106($105);
              var $103 = function ()
              {
                return $node.value = $text
              };
              var $102 = $104($103);
              var $101 = $107($102);
              var $100 = function ()
              {
                return $node.onclick = $callback
              };
              var $99 = $101($100);
              var $98 = $108($99);
              var $97 = $$prelude.$yield;
              var $96 = $node;
              var $95 = $97($96);
              var $94 = $98($95)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $94
          };
          var _return = $109($93);
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
        var $123 = $createInputElement;
        var $122 = $doc;
        var $121 = $123($122);
        var $120 = "text";
        var $119 = $121($120);
        var $118 = $text;
        var $117 = $119($118);
        var $116 = $callback;
        var _return = $117($116);
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
        var $131 = $createInputElement;
        var $130 = $doc;
        var $129 = $131($130);
        var $128 = "button";
        var $127 = $129($128);
        var $126 = $text;
        var $125 = $127($126);
        var $124 = $callback;
        var _return = $125($124);
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