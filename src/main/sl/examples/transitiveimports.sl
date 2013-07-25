IMPORT "std/dict" AS Dict
IMPORT "std/list" AS List
IMPORT "std/debuglog" AS IO

PUBLIC FUN main : DOM Void
DEF main =
	LET dict = IO.andPrint
		(Dict.put "fourtytwo" 42 (Dict.put "twentythree" 23 Dict.empty))
		(Dict.toString intToString) IN
	-- sel is of type Option (Option.Option in list.sl and Opt.option in dict.sl)
	-- Option is not imported directly. still it should work!
	LET sel = Dict.getOpt dict "twentythree" IN
	LET list = IO.andPrint (List.fromOption sel) (List.toString intToString) IN
	IO.print "Did it output <23>?"
