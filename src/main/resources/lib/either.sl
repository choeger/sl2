
IMPORT "std/option" AS Option

-- simple union data type
PUBLIC DATA Either a b =
	  Left a
	| Right b
	
-- (the function that can actually encode every reasonable thing one could do with an Either)
PUBLIC FUN fold : (a -> c) -> (b -> c) -> Either a b -> c
DEF fold fa fb (Left a) = fa a
DEF fold fa fb (Right b) = fb b

PUBLIC FUN mapL : (a -> aa) -> Either a b -> Either aa b
DEF mapL f (Left a) = Left (f a)
DEF mapL f (Right b) = Right b

PUBLIC FUN mapR : (b -> bb) -> Either a b -> Either a bb
DEF mapR f (Left a) = Left a
DEF mapR f (Right b) = Right (f b)

PUBLIC FUN left : Either a b -> Option.Option a
DEF left e = fold Option.Some (\b.Option.None) e

-- why does this code pass the type check? (actually it would have to be Option.Option b, i think) 
PUBLIC FUN right : Either a b -> Option.Option a
DEF right e = fold (\a.Option.None) Option.Some e
