IMPORT "dict" AS Dict
IMPORT "list" AS List
IMPORT "debuglog" AS Dbg

DEF main =
	LET list = listTest IN
	LET dict = dictTest list IN
	Dbg.print ("Expected output: \n"
		++ "<helloooo,world> \n"
		++ "{helloooo:8,world:5}")
	
-- covers: List, List.toString
DEF listTest =
	Dbg.andPrint (List.Cons "helloooo" (List.Cons "world" List.Nil)) (List.toString id)

-- create and output a map that associates strings with their length. 
-- covers:
-- 	Dict.fromList, Dict.toString,
--	List.length, List.fromString,
--  Prelude.#  (function composition), Prelude.intToString
DEF dictTest li =
	Dbg.andPrint
		(Dict.fromList
			(List.length # List.fromString) li)
		(Dict.toString intToString)