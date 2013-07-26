IMPORT "hello" AS H

-- Prints Hello World!, not Hello Moon!, to show that
-- require.js modules names are absolute, not relative
PUBLIC FUN main: DOM Void
DEF main = H.main