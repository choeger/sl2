
-- prints a string if there is a console-object
PUBLIC FUN print : String -> DOM Void
DEF print str = {| console && console.log($str); |}

-- prints a debug message after evaluating the lhs statement and then passes on
-- the result. (thats not exactly "functional", but very useful for debugging...)
PUBLIC FUN andPrint : a -> (a -> String) -> a
DEF EXTERN andPrint = {| function(res){return function(printer){
	console && console.log(printer(res)); 
	return res;
}}|}
PUBLIC FUN andPrintDbg : a -> String -> a
DEF andPrintDbg a s = andPrint a (\r.s)

-- Uses JavaScripts toString-method to convert anything to a string.
-- (This often will not be very helpful for SL-values.)
PUBLIC FUN anyToString : a -> String
DEF EXTERN anyToString = {| function(some){
	return some.toString();
}|}