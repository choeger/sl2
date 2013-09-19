

PUBLIC DATA Book =
  Book String Int String
--     author year title

PUBLIC FUN toString : Book -> String
DEF toString (Book a y t) =
  a ++ "(" ++ intToString y ++ "): " ++  t
