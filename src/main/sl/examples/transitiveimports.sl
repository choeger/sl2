IMPORT "dict" AS Dict
IMPORT "list" AS List
IMPORT "basicio" AS IO

DEF main =
	LET dict = IO.andPrint
		(Dict.put (Dict.put Dict.empty "twentythree" 23) "fourtytwo" 42)
		(Dict.toString intToStr) IN
	-- sel is of type Option (Option.Option in list.sl and Opt.option in dict.sl)
	-- still it should work!
	LET sel = Dict.getOpt dict "twentythree" IN
	LET list = IO.andPrint (List.fromOption sel) (List.toString intToStr) IN
	IO.print "did it work?"