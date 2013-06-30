IMPORT "src/main/resources/lib/prelude" AS P

DEF hello = "Hello World" P.++ "23"

DEF main = (P.yield hello) P.&= (\h.{|
  console.log($h);
|})