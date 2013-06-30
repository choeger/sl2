IMPORT "prelude_stub2" AS Ps

DEF hello = "Hello World" Ps.++ "23"

DEF main = (Ps.yield hello) Ps.&= (\h.{| console.log($h); |})