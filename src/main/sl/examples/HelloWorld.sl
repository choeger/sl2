DEF hello = "Hello World"

DEF main = (yield hello) &= (\h.{|
  console.log($h);
|})