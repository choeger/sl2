
IMPORT "std/basicweb" AS Web
IMPORT "std/option" AS Option

IMPORT "book" AS Book
IMPORT "database" AS DB

PUBLIC FUN main : DOM Void
DEF main = 
  Web.document &= \ doc .
  Web.getBody doc &= \ body .
  Web.createInput doc "" {| /*nothing*/ |} &= \ input .
  Web.createButton doc "Find book by title" (cbFindBook doc input) &= \ btnFindByTitle .
  Web.appendChild body input &
  Web.appendChild body btnFindByTitle

DEF cbFindBook doc input =
  Web.getValue input &= \ val .
  Web.alert
    (CASE (DB.findByTitle val)
      OF Option.Some b THEN "We found your book <" ++ Book.toString b ++ ">"
      OF Option.None THEN "Sorry, there is no book with title <" ++ val ++ "> in the database")