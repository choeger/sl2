;
;
var $Nil = 0;
var _Cons = 1;
function $Cons(_arg0)
{
  function f(_arg1)
  {
    return {_cid : 1, _var0 : _arg0, _var1 : _arg1
    }
  };
  return f
};
var $True = true;
var $False = false;
;
var _ExternalDOM = 0;
function $ExternalDOM(_arg0)
{
  return {_cid : 0, _var0 : _arg0
  };
  return f
};
;
var $ExternalString = 0;
;
var $ExternalVoid = 0;
;
var $ExternalChar = 0;
;
var $ExternalReal = 0;
;
var $ExternalInt = 0;
function $not(_arg0)
{
  if((_arg0 === $True))
  {
    var _return = $False;
    return _return
  }
  else 
  {
    if((_arg0 === $False))
    {
      var _return = $True;
      return _return
    }
    else 
    {
      throw "Pattern not exhaustive!"
    }
  }
}