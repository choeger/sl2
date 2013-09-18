
IMPORT "std/list" AS List
IMPORT "std/option" AS Option

IMPORT "book" AS Book

DEF db =
  List.Cons (Book.Book "Douglas R. Hofstadter" 1979 "Gödel, Escher, Bach")
  List.Nil

PUBLIC FUN findByTitle : String -> (Option.Option Book.Book)
DEF findByTitle title =
  List.headOption (List.filter (\ (Book.Book a y t) . strEq t title) db)
