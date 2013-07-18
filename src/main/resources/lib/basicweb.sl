DATA Node = Node
DATA Document = Document

PUBLIC FUN document : DOM Document
DEF document = {| document |} : DOM Document

PUBLIC FUN appendChild : Document -> Node -> DOM Void
DEF appendChild doc child = {| $doc.body.appendChild($child) |}

-- for a document creates a new button with name and callback
PUBLIC FUN createButton : Document -> String -> (DOM Void) -> (DOM Node)
DEF createButton doc text callback =
   {| $doc.createElement('input') |} : DOM Node &=
   (\button .
     {| $button.type = "button" |} &
     {| $button.value = $text |} &
     {| $button.onclick = $callback |} &
     (yield button))
     
-- for a document creates a new input field with text and callback
PUBLIC FUN createInput : Document -> String -> (DOM Void) -> (DOM Node)
DEF createInput doc text callback =
   {| $doc.createElement('input') |} : DOM Node &=
   (\node .
     {| $node.type = "text" |} &
     {| $node.value = $text |} &
     {| $node.onclick = $callback |} &
     (yield node))
     
PUBLIC FUN alert : String -> DOM Void
DEF alert msg = {| alert($msg) |}