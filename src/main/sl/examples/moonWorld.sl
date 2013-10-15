IMPORT "hello" AS W
IMPORT "sub/hello" AS M

PUBLIC FUN main: DOM Void
DEF main = W.main & M.main