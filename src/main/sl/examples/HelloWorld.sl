IMPORT "list" AS List

DEF hello = "Hello World" ++ "23"

PUBLIC FUN main : DOM Void

DEF main = (yield hello) &= (\h.{|
  console.log($h);
|})
