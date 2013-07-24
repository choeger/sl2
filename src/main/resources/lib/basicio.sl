-- The module supplys basic functions to output information
-- to a log (if the js environment is capable of that.)

-- Attention: This module can be misused to break the functional
-- 		paradigm... Still it is extremely useful for debugging.

-- Should work with Firefox, IE8 (Developer Tools), and node.js.

-- Test whether there is a console.
PUBLIC FUN logAvailable : Bool
DEF EXTERN logAvailable = {| typeof console != "undefined" |}

-- Prints a string if there is a console object.
PUBLIC FUN print : String -> DOM Void
DEF print str = {| console && console.log($str); |}

-- Prints a debug message generated after evaluating the lhs statement 
-- and then passes on the result.
PUBLIC FUN andPrint : a -> (a -> String) -> a
DEF EXTERN andPrint = {| function(res){return function(printer){
	console && console.log(printer(res)); 
	return res;
}}|}

PUBLIC FUN andPrintMessage : a -> String -> a
DEF andPrintMessage a s = andPrint a (\r.s)

-- Uses JavaScripts toString-method to convert anything to a string.
-- (This often will not be very helpful for SL-values.)
PUBLIC FUN anyToString : a -> String
DEF EXTERN anyToString = {| function(some){
	return some.toString();
}|}
