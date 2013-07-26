IMPORT "option" AS Opt
IMPORT "std/option" AS OptOrig
IMPORT "std/debuglog" AS Dbg

-- this is just meant do demonstrate that similarly named
-- definitions can coexist locally and in std.

FUN get : Opt.Option a -> a
DEF get (Opt.Avail a) = a

PUBLIC FUN main : DOM Void
DEF main = 
  Dbg.print (get (Opt.Avail "hello world from here")) &
  Dbg.print (OptOrig.get (OptOrig.Some "hello world from std"))

  