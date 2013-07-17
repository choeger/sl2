DATA Node = Node
DATA Document = Document

PUBLIC FUN document : DOM Document
DEF document = {| document |} : DOM Document

PUBLIC FUN appendChild : Document -> Node -> DOM Void
DEF appendChild doc child = {| $doc.body.appendChild($child) |}

-- for a document creates a new button with name and callback
PUBLIC FUN createButton : Document -> String -> (DOM Void) -> DOM Void
DEF createButton doc text callback =
   {| $doc.createElement('button') |} : DOM Node &= (\button .
   ({| $button.innerText = $text |} : DOM Void &
   ({| $button.onclick = $callback |} : DOM Void &
   yield button)))