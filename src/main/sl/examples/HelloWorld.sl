IMPORT "std/list" AS List
IMPORT "std/option" AS Opt
IMPORT "std/debuglog" AS Log
IMPORT "std/dict" AS Dict
IMPORT "wusel" AS Dusel
IMPORT "std/wusel" AS Wusel

DEF hello = "Hello World" ++ "23"

DEF empty = List.Nil
DEF a !! b = List.Cons a b

DEF lines = LET rest = "World" !! empty IN "Hello" !! rest

PUBLIC FUN main : DOM Void

FUN print : String -> DOM Void
DEF print str = (yield str) &= (\h.{|
  console.log($h);
|})

DEF printList List.Nil = print "---"
DEF printList (List.Cons x xs) = print x & printList xs

DEF printOpt (Opt.Some str) = print str
DEF printOpt Opt.None = print "Nope!"

DEF x = Opt.Some "Yes"
DEF y = Opt.None

DEF main = 
	printList lines & 
	printOpt x & 
	printOpt y & 
	printList (List.tail lines) &
	Log.print hello
