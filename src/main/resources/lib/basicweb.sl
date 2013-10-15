IMPORT "std/list" AS List

DATA EXTERN Node
DATA EXTERN Document

PUBLIC FUN document : DOM Document
DEF document = {| document |} : DOM Document

PUBLIC FUN getBody : Document -> DOM Node
DEF getBody doc = {| $doc.body |} : DOM Node

PUBLIC FUN appendChild : Node -> Node -> DOM Void
DEF appendChild node child = {| $node.appendChild($child) |}

PUBLIC FUN removeChild : Node -> Node -> DOM Void
DEF removeChild node child = {| $node.removeChild($child) |}

PUBLIC FUN getChildNodes : Node -> DOM (List.List Node)
DEF EXTERN getChildNodes = {| function(node){return function(){
	var list = List.$Nil;
	for (var i=0; i<node.childNodes.length; i++) {
		list = List.$Cons(node.childNodes[i])(list);
	}
	return list;
}}	|}

PUBLIC FUN setOnClick : Node -> DOM Void -> DOM Void
DEF setOnClick node cb = {| $node.onclick = $cb |}

PUBLIC FUN getValue : Node -> DOM String
DEF getValue node = {|$node.value|} : DOM String
PUBLIC FUN setValue : Node -> String -> DOM Void
DEF setValue node value = {| $node.value = $value |}

PUBLIC FUN createElement : Document -> String -> DOM Node
DEF createElement doc elemType =
   {| $doc.createElement($elemType) |} : DOM Node 

PUBLIC FUN createInputElement : Document -> String -> String -> DOM Void -> DOM Node
DEF createInputElement doc type text callback =
   createElement doc "input" &=
   (\node .
     {| $node.type = $type |} &
     {| $node.value = $text |} &
     {| $node.onclick = $callback |} &
     (yield node))

-- for a document creates a new button with name and callback
PUBLIC FUN createButton : Document -> String -> (DOM Void) -> (DOM Node)
DEF createButton doc text callback =
	createInputElement doc "button" text callback
     
-- for a document creates a new input field with text and callback
PUBLIC FUN createInput : Document -> String -> (DOM Void) -> (DOM Node)
DEF createInput doc text callback =
	createInputElement doc "text" text callback

PUBLIC FUN alert : String -> DOM Void
DEF alert msg = {| alert($msg) |}

PUBLIC FUN prompt : String -> String -> DOM String
DEF prompt msg pre = {| prompt($msg, $pre) |} : DOM String
