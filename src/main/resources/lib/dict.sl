IMPORT "std/list" AS List
IMPORT "std/option" AS Opt

DATA EXTERN Dict a 

PUBLIC FUN empty : Dict a
DEF EXTERN empty = {| {} |}

PUBLIC FUN put : String -> a -> Dict a -> Dict a
DEF EXTERN put = {| function(k){return function(v){return function(dict){
	var newDict = Object();
	newDict.__proto__ = dict;
	newDict[k] = v;
	return newDict;
}}} |}

PUBLIC FUN get : Dict a -> String -> a
DEF EXTERN get = {| function(dict){return function(k){
	if (k in dict) {
		return dict[k];
	} else {
		throw "Could not select <"+k+"> from dictionary.";
	}
}} |}

PUBLIC FUN has : Dict a -> String -> Bool
DEF EXTERN has = {| function(dict){return function(k){
	return (k in dict);
}} |}

PUBLIC FUN getOpt : Dict a -> String -> Opt.Option a
DEF getOpt dict k =
	IF (has dict k) THEN Opt.Some (get dict k) ELSE Opt.None 

PUBLIC FUN merge : Dict a -> Dict a -> Dict a
DEF EXTERN merge = {| function(dict1){return function(dict2){
	var newDict = {};
	for(var key in dict1) {
     newDict[key] = dict1[key];
    }
    for(var key in dict2) {
     newDict[key] = dict2[key];
    }
    return newDict;
}} |}

PUBLIC FUN filter : (a -> Bool) -> Dict a -> Dict a
DEF EXTERN filter = {| function(predicate){return function(dict){
	var newDict = {};
	for(var key in dict) {
      if (predicate(dict[key])) {
      	newDict[key] = dict[key];
      }
    }
    return newDict;
}} |}

PUBLIC FUN reduce : (String -> a -> b) -> b -> Dict a -> b
DEF EXTERN reduce = {| function(f){return function(neut){return function(dict){
	for(var key in dict) {
      neut = f(key)(dict[key]);
    }
    return neut;
}}} |}

PUBLIC FUN toString : (a -> String) -> Dict a -> String
DEF toString tS dict = "{" ++ toStringI tS dict ++ "}"
	FUN toStringI : (a -> String) -> Dict a -> String
	DEF EXTERN toStringI = {| function(tS){return function(dict){
	    var str = "";
		for(var key in dict) {
		  if (str != "") str += ",";
	      str += key + ":" + tS(dict[key]);
    	}
    	return str;
	}} |}

PUBLIC FUN fromList : (String -> a) -> List.List String -> Dict a
DEF fromList generator list =
	List.reduce (\k.\d.put k (generator k) d) empty list
