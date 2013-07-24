IMPORT "dict" AS Dict
IMPORT "list" AS List
IMPORT "basicio" AS IO

DEF main =
	LET list = listTest IN
	LET dict = dictTest list IN
	IO.print ("Expected output: \n"
		++ "<helloooo,world> \n"
		++ "{helloooo:8,world:5}")
	
-- covers: List, List.toString
DEF listTest =
	IO.andPrint (List.Cons "helloooo" (List.Cons "world" List.Nil)) (List.toString id)

-- create and output a map that associates strings with their length. 
-- covers:
-- 	Dict.fromList, Dict.toString,
--	List.length, List.fromString,
--  Prelude.#  (function composition), Prelude.intToString
DEF dictTest li =
	IO.andPrint
		(Dict.fromList
			(List.length # List.fromString) li)
		(Dict.toString intToString)