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
	Web.createButton doc "Sort" (cbSort doc numbers) &= \ btnSort .
	Web.appendChild body input &
	Web.appendChild body btnAddNumber &
	Web.appendChild body btnSum &
	Web.appendChild body btnSort &
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
	
DATA NumNode = NodeWithNumber Web.Node Int
DEF minNode (NodeWithNumber n1 i1) (NodeWithNumber n2 i2) =
	IF i1 <= i2
	THEN (NodeWithNumber n1 i1)
	ELSE (NodeWithNumber n2 i2)
DEF eqNode (NodeWithNumber n1 i1) (NodeWithNumber n2 i2) = i1 == i2
DEF getNode (NodeWithNumber n1 i1) = n1

DEF cbSort doc numberNode =
	Web.getChildNodes numberNode &= \ numberNodes .
	List.mapDom (\node. Web.getValue node &=
					\v. yield (NodeWithNumber node (strToInt v))) numberNodes &= \ nodesWithNumbers .
	LET sorted = Dbg.andPrint (selectionSort nodesWithNumbers) (List.toString Dbg.anyToString) IN
	List.mapDom (Web.removeChild numberNode) numberNodes &
	List.mapDom (Web.appendChild numberNode # getNode) sorted &
	{||}
	
DEF selectionSort List.Nil = List.Nil
DEF selectionSort list =
	LET min = List.reduce minNode (List.head list) list
		newList = List.removeFirst (eqNode min) list IN
	List.Cons min (selectionSort newList)