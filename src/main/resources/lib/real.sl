IMPORT EXTERN "_prelude"

-------------------------------------
-- Arithmetics on Reals

PUBLIC FUN + : Real -> Real -> Real
DEF EXTERN + = {| _add |}

PUBLIC FUN - : Real -> Real -> Real
DEF EXTERN - = {| _sub |}

PUBLIC FUN * : Real -> Real -> Real
DEF EXTERN * = {| _mul |}

PUBLIC FUN / : Real -> Real -> Real
DEF EXTERN / = {| _div |}

PUBLIC FUN < : Real -> Real -> Bool
DEF EXTERN < = {| _lesser |}

PUBLIC FUN <= : Real -> Real -> Bool
DEF EXTERN <= = {| _leq |}

PUBLIC FUN == : Real -> Real -> Bool
DEF EXTERN == = {| _eq |}

PUBLIC FUN /= : Real -> Real -> Bool
DEF x /= y = not (x == y)

PUBLIC FUN >= : Real -> Real -> Bool
DEF EXTERN >= = {| _geq |}

PUBLIC FUN > : Real -> Real -> Bool
DEF EXTERN > = {| _greater |}

PUBLIC FUN iNaN : Real
DEF EXTERN iNaN = {| NaN |}
-- iNaN /= iNaN !!

PUBLIC FUN isNaN : Real -> Bool
DEF EXTERN isNaN = {| isNaN |}

PUBLIC FUN round : Real -> Int
DEF EXTERN round = {| Math.round |}

PUBLIC FUN abs : Real -> Real
DEF EXTERN abs = {| Math.abs |}

PUBLIC FUN fromInt : Int -> Real
DEF EXTERN fromInt = {| function(i){return i;} |}