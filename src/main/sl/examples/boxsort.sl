IMPORT "basicweb" AS Web
IMPORT "list" AS List
IMPORT "basicio" AS Dbg

DEF main = 
	Web.document &= \ doc .
	Web.getBody doc &= \ body .
	Web.createElement doc "p" &= \ numbers .
	Web.createInput doc "" {||} &= \ input .
	Web.createButton doc "Add Number" (cbAddNumber doc numbers input) &= \ btnAddNumber .
	Web.createButton doc "Sum" (cbSum doc numbers) &= \ btnSum .
	Web.appendChild body input &
	Web.appendChild body btnAddNumber &
	Web.appendChild body btnSum &
	Web.appendChild body numbers
	
DEF cbAddNumber doc numberNode inp =
	Web.getValue inp &= \ val .
	LET value = strToInt val IN
	IF isNaN value THEN
	  Web.alert(val ++ " is not a number. Expected a number!")
	ELSE
	  Web.createButton doc (intToStr value) {||} &= \ button .
	  Web.setOnClick button (Web.removeChild numberNode button) &
	  Web.appendChild numberNode button
	  
DEF cbSum doc numberNode =
	Web.getChildNodes numberNode &= \ numberNodes .
	List.mapDom (\node. Web.getValue node &= (yield # strToInt)) numberNodes &= \ nums .
	LET sum = List.reduce (\x.\y.x+y) 0 nums IN
	Web.alert ("Sum: " ++ intToStr sum)