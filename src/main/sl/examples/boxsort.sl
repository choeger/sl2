IMPORT "basicweb" AS Web

DEF main = 
	Web.document &= \ doc .
	(addButton doc)
	
DEF addButton doc =
	(Web.createButton doc "Hello" (addButton doc)) &= (\ button .
	(Web.appendChild doc button))