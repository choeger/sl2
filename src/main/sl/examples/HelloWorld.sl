DEF hello = "Hello World" ++ "23"

DEF main = (yield hello) &= (\h.{|
  console.log($h);
|})