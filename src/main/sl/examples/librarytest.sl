IMPORT "std/dict" AS Dict
IMPORT "std/list" AS List
IMPORT "std/debuglog" AS Dbg
IMPORT "std/real" AS Real

PUBLIC FUN main : DOM Void
DEF main =
	LET list = listTest IN
	LET dict = dictTest list IN
	LET real = realTest dict IN
	Dbg.print ("Expected output: \n"
		++ "<helloooo,world> \n"
		++ "{helloooo:8,world:5} \n"
		++ "23")
	
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



-- redefinition of *  (originally Int -> Int -> Int in prelude...)
FUN * : Real -> Real -> Real
DEF a * b = a Real.* b

-- covers:
--   Real.*, Real./, Real.sin, Real.pi, Real.isNaN, Real.toString
DEF realTest dict =
	-- local redefinition of isNaN (originally in prelude)
	LET isNaN = Real.isNaN IN 
	-- this isNaN on Ints from prelude has always been strange anyways!
	Dbg.andPrint
		(IF isNaN (0.0 Real./ 0.0) THEN (23.0 * Real.sin (0.5 * Real.pi)) ELSE 42.0)
		Real.toString