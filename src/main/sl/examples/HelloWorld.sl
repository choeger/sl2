IMPORT "std/list" AS List

DEF hello = "Hello World" ++ "23"

DEF empty = List.Nil
DEF a !! b = List.Cons a b

DEF lines = "Hello" !! ("World" !! empty)

PUBLIC FUN main : DOM Void

DEF print str = (yield str) &= (\h.{|
  console.log($h);
|})

DEF printList List.Nil = print ""
DEF printList (List.Cons x xs) = print x & printList xs

DEF main = printList lines
