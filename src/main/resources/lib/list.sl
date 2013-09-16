IMPORT "std/option" AS Option

PUBLIC DATA List a =
	  Nil
	| Cons a (List a)

PUBLIC FUN head : List a -> a
DEF head (Cons ft rt) = ft
DEF head Nil = error "Cannot select head from empty list."

PUBLIC FUN headOption : List a -> Option.Option a
DEF headOption (Cons ft rt) = Option.Some ft
DEF headOption Nil = Option.None

PUBLIC FUN tail : List a -> List a
DEF tail (Cons ft rt) = rt
DEF tail Nil = Nil

PUBLIC FUN conc : (List a) -> (List a) -> (List a)
DEF conc Nil list = list
DEF conc (Cons ft rt) list = Cons ft (conc rt list)

PUBLIC FUN flatten : (List (List a)) -> List a
DEF flatten list = reduce conc Nil list

PUBLIC FUN length : (List a) -> Int
DEF length Nil = 0
DEF length (Cons ft rt) = 1 + (length rt)

PUBLIC FUN reduce : (a -> b -> b) -> b -> List a -> b
DEF reduce f n Nil = n
DEF reduce f n (Cons ft rt) = f ft (reduce f n rt)

-- special function to reduce a list using a monadic function.
-- (last element will be treated first!)
PUBLIC FUN reduceDom : (a -> b -> DOM b) -> DOM b -> List a -> DOM b
DEF reduceDom f n Nil = n
DEF reduceDom f n (Cons ft rt) =
	reduceDom f n rt &= \ res .
	f ft res

PUBLIC FUN filter : (a -> Bool) -> List a -> List a
DEF filter p Nil = Nil
DEF filter p (Cons ft rt) =
	IF p ft THEN
		Cons ft (filter p rt) 
	ELSE
		filter p rt

PUBLIC FUN removeFirst : (a -> Bool) -> List a -> List a
DEF removeFirst p Nil = Nil
DEF removeFirst p (Cons ft rt) =
	IF p ft THEN rt ELSE Cons ft (removeFirst p rt)
		
PUBLIC FUN map : (a -> b) -> List a -> List b
DEF map f Nil = Nil
DEF map f (Cons ft rt) =
	Cons (f ft) (map f rt)

-- special function to map a monadic function on a list.
-- (first element will be evaluated first!)
PUBLIC FUN mapDom : (a -> DOM b) -> List a -> DOM List b
DEF mapDom f Nil = yield Nil
DEF mapDom f (Cons ft rt) =
	f ft &= \newFt.
	mapDom f rt &= \newRt.
	yield (Cons newFt newRt)
	
PUBLIC FUN toString : (a -> String) -> List a -> String
DEF toString tS list = "<" ++ toStringI list tS ++ ">"
	DEF toStringI Nil tS = ""
	DEF toStringI (Cons ft Nil) tS = tS ft
	DEF toStringI (Cons ft rt) tS = tS ft ++ "," ++ toStringI rt tS
	
PUBLIC FUN fromString : String -> List Char
DEF EXTERN fromString = {| function(str) {
	var list = $Nil;
	for (var i = str.length-1; i >= 0 ; i--) {
		list = $Cons(str.charAt(i))(list);
	}
	return list;
}|} 

PUBLIC FUN fromOption : Option.Option a -> List a
DEF fromOption (Option.Some a) = Cons a Nil
DEF fromOption Option.None = Nil
