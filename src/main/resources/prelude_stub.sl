-- Copyright (c) 2010, Andreas Buechele, Florian Lorenzen, Judith Rohloff
-- All rights reserved.

-- Redistribution and use in source and binary forms, with or without
-- modification, are permitted provided that the following conditions are
-- met:

--   * Redistributions of source code must retain the above copyright
--     notice, this list of conditions and the following disclaimer.
--   * Redistributions in binary form must reproduce the above
--     copyright notice, this list of conditions and the following
--     disclaimer in the documentation and/or other materials provided
--     with the distribution.
--   * Neither the name of the TU Berlin nor the names of its
--     contributors may be used to endorse or promote products derived
--     from this software without specific prior written permission.

-- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
-- "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
-- LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
-- A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
-- HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
-- SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
-- LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
-- DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
-- THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
-- (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
-- OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-- This is a stub for the predefined types and methods of SL, so that we
-- can create the prelude by a combination of compilation and hand coding.

DATA Int = ExternalInt
DATA Real = ExternalReal
DATA Char = ExternalChar
DATA Void = ExternalVoid
DATA String = ExternalString
DATA DOM a = ExternalDOM a

DATA Bool = True | False

DATA List a = Nil | Cons a (List a)

DEF main = ({|
  console.log($hello);
|})

DEF hello = "Hello World" ++ "23"

FUN &= : (DOM a) -> (a -> (DOM b)) -> (DOM b)
DEF (ExternalDOM x) &= f = ExternalDOM x


FUN ++ : (String -> String -> String)
DEF x ++ y = ExternalString


FUN & : (DOM a) -> (DOM b) -> (DOM b)
DEF x & y = (x &= (\ r . y))



FUN + : Int -> Int -> Int
DEF x + y = ExternalInt

FUN - : Int -> Int -> Int
DEF x - y = ExternalInt

FUN * : Int -> Int -> Int
DEF x * y = ExternalInt

FUN / : Int -> Int -> Int
DEF x / y = ExternalInt

FUN < : Int -> Int -> Bool
DEF x < y = True

FUN <= : Int -> Int -> Bool
DEF x <= y = True

FUN == : Int -> Int -> Bool
DEF x == y = True

FUN /= : Int -> Int -> Bool
DEF x /= y = True

FUN >= : Int -> Int -> Bool
DEF x >= y = True

FUN > : Int -> Int -> Bool
DEF x > y = True

FUN yield : a -> (DOM a)
DEF yield x = ExternalDOM x


-- val stolLex = "stol"
--  val ltosLex = "ltos"

FUN ord : Char -> Int
DEF ord x = ExternalInt

FUN chr : Int -> Char
DEF chr x = ExternalChar