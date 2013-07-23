-- simple product data type
PUBLIC DATA Pair a b = Pair a b

PUBLIC FUN fst : Pair a b -> a
DEF fst (Pair a b) = a
	
PUBLIC FUN snd : Pair a b -> b
DEF snd (Pair a b) = b