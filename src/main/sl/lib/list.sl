

PUBLIC DATA List a = Nil | Cons a (List a)

PUBLIC FUN conc : (List a) -> (List a) -> (List a)
DEF conc Nil list = list
DEF conc (Cons ft rt) list = Cons ft (conc rt list)

PUBLIC FUN length : (List a) -> Int
DEF length Nil = 0
DEF length (Cons ft rt) = 1 + (length rt)