IMPORT "src/main/resources/lib/prelude" AS P

FUN nums : P.List Int
DEF nums = P.Cons 11 (P.Cons 12 P.Nil) 

-- apparently qualified names in patterns dont work...
FUN sums : (P.List Int) -> Int
DEF sums s =
	CASE s
		OF P.Nil THEN 0
		OF (P.Cons ft rt) THEN ft + sums rt

DEF hello = "Hello World" P.++ (sums nums)

DEF main = (P.yield hello) P.&= (\h.{|
  console.log($h);
|})

