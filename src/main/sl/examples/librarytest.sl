IMPORT "dict" AS Dict
IMPORT "list" AS List
IMPORT "basicio" AS IO

DEF main =
	LET list = listTest IN
	LET dict = dictTest list IN
	IO.print "did it work?"
	
-- covers: List, List.toString
DEF listTest =
	IO.andPrint (List.Cons "helloooo" (List.Cons "world" List.Nil)) (List.toString id)

-- create and output a map that associates strings with their length. 
-- covers:
-- 	Dict.fromList, Dict.toString,
--	List.length, List.fromString,
--  Prelude.#  (function composition), Prelude.intToStr
DEF dictTest li =
	IO.andPrint
		(Dict.fromList
			(List.length # List.fromString) li)
		(Dict.toString intToStr)