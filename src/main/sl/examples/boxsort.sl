IMPORT "basicweb" AS Web

DEF main = 
	Web.document &= \ doc .
	Web.createInput doc "" {||} &= \ input .
	Web.createButton doc "Add Number" (buttonCb doc input) &= \ button .
	Web.appendChild doc input &
	Web.appendChild doc button
		
DEF buttonCb doc inp =
	{|$inp.value|} : DOM String &= \ val .
	LET value = strToInt val IN
	IF isNaN value THEN
	  Web.alert(val ++ " is not a number. Expected a number!")
	ELSE
	  (Web.createButton doc (intToStr value) {||} &= \ button .
	  LET removeButton = {|$doc.body.removeChild($button)|} IN
	  {| $button.onclick = $removeButton |} &
	  Web.appendChild doc button)