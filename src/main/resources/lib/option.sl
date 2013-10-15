
PUBLIC DATA Option a =
	  Some a
	| None

PUBLIC FUN map : (a -> b) -> Option a -> Option b
DEF map f (Some a) = Some (f a) 
DEF map f None = None

PUBLIC FUN cmp : (a -> b -> Bool) -> Option a -> Option b -> Bool
DEF cmp rel (Some a) (Some b) = (rel a b)
DEF cmp rel None None = True 
DEF cmp rel a b = False

PUBLIC FUN getOrElse : Option a -> a -> a
DEF getOrElse (Some a) b = a
DEF getOrElse None b  = b

PUBLIC FUN get : Option a -> a
DEF get (Some a) = a
DEF get (None) = error "Cannot get value from None-Option."