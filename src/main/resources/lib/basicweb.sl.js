/***********************************/
// generated from: basicweb.sl
/***********************************/
define(function(require, exports, module) {
    var $$prelude = require("prelude.sl"); var List = require("list.sl")

;
;
exports.$Document = 0;
var $Document = exports.$Document;
;
exports.$Node = 0;
var $Node = exports.$Node;
exports.$getChildNodes = function(node){return function(){
	var list = List.$Nil;
	for (var i=0; i<node.childNodes.length; i++) {
		list = List.$Cons(node.childNodes[i])(list);
	}
	return list;
}};
var $getChildNodes = exports.$getChildNodes;
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
exports.$createElement = $createElement;
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
exports.$setOnClick = $setOnClick;
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
exports.$prompt = $prompt;
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
exports.$removeChild = $removeChild;
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
exports.$getValue = $getValue;
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
exports.$getBody = $getBody;
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
          var $113 = $$prelude.$a$e;
          var $112 = $createElement;
          var $111 = $doc;
          var $110 = $112($111);
          var $109 = "input";
          var $108 = $110($109);
          var $107 = $113($108);
          var $91 = function (_arg0)
          {
            if(true)
            {
              var $node = _arg0;
              var $106 = $$prelude.$a;
              var $105 = $$prelude.$a;
              var $104 = $$prelude.$a;
              var $103 = function ()
              {
                return $node.type = $type
              };
              var $102 = $104($103);
              var $101 = function ()
              {
                return $node.value = $text
              };
              var $100 = $102($101);
              var $99 = $105($100);
              var $98 = function ()
              {
                return $node.onclick = $callback
              };
              var $97 = $99($98);
              var $96 = $106($97);
              var $95 = $$prelude.$yield;
              var $94 = $node;
              var $93 = $95($94);
              var $92 = $96($93)
            }
            else 
            {
              throw "Pattern for lambda expression did not match arguments"
            };
            return $92
          };
          var _return = $107($91);
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
exports.$createInputElement = $createInputElement;
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
        var $121 = $createInputElement;
        var $120 = $doc;
        var $119 = $121($120);
        var $118 = "text";
        var $117 = $119($118);
        var $116 = $text;
        var $115 = $117($116);
        var $114 = $callback;
        var _return = $115($114);
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
        var $129 = $createInputElement;
        var $128 = $doc;
        var $127 = $129($128);
        var $126 = "button";
        var $125 = $127($126);
        var $124 = $text;
        var $123 = $125($124);
        var $122 = $callback;
        var _return = $123($122);
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