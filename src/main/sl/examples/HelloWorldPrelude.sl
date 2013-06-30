IMPORT "prelude" AS P

FUN words : P.List String
DEF words = P.Cons "Hello" (P.Cons "World" P.Nil) 

-- apparently qualified names in patterns dont work...
FUN conc : (P.List String) -> String
DEF conc s =
	CASE s
		OF P.Nil THEN ""
		OF P.Cons ft rt THEN ft P.++ (conc rt)

DEF hello = "Hey, " P.++ (conc words)

DEF main = (P.yield hello) P.&= (\h.{|
  console.log($h);
|})

