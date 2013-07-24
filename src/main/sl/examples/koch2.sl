IMPORT "real" AS R
IMPORT "basicweb" AS Web

DEF createCanvas = 
	Web.document &= \ doc .
	Web.getBody doc &= \ body .
	Web.createElement doc "canvas" &= \ canvas .
	{| $canvas.width = 600 |} &
	{| $canvas.height = 600 |} &
	Web.appendChild body canvas &
	yield canvas 

DEF context = createCanvas &= \canvas.{| $canvas.getContext("2d") |} 

DEF save c = {| $c.save() |}

DEF scale ctxt a b = {| $ctxt.scale($a, $b) |}

DEF rotate c deg =
	LET rad = deg2rad deg
	IN {| $c.rotate($rad) |}

DEF deg2rad deg = deg R.* pi R./ 180.

DEF pi = 3.1415926535897932384626433832795

DEF translate c x y = {| $c.translate($x, $y) |}

DEF moveTo c x y = {| $c.moveTo($x, $y) |}

DEF lineTo c x y = {| $c.lineTo($x, $y) |}

DEF restore c = {| $c.restore() |}

DEF closePath c = {| $c.closePath() |}

DEF stroke c = {| $c.stroke() |}

DEF snowflake c n x y len = 
    LET leg = \n . (save c) & 
      (IF n == 0 THEN 
	    	lineTo c len 0 
	  ELSE
    	    (scale c (1. R./ 3.) (1. R./ 3.)) &
            (leg (n - 1)) &         -- Recurse for the first sub-leg
            (rotate c 60.) &       -- Turn 60 degrees clockwise
            (leg (n - 1)) &         -- Second sub-leg
            (rotate c (-120.)) &   -- Rotate 120 degrees back
            (leg (n - 1)) &         -- Third sub-leg
            (rotate c 60.) &       -- Rotate back to our original heading
            (leg (n - 1))           -- Final sub-leg
	  ) &           
         (restore c) &            -- Restore the transformation
         (translate c len 0)     -- But translate to make end of leg (0,0) 
    IN 
    (save c) &		-- Save current transformation
    (translate c x y) & -- Translate origin to starting point
    (moveTo c 0 0) &  	-- Begin a new subpath at the new origin
    (leg n) & 	   	-- Draw the first leg of the snowflake
    (rotate c (-120.)) &   -- Now rotate 120 degrees counterclockwise
    (leg n) &           -- Draw the second leg
    (rotate c (-120.)) &    -- Rotate again
    (leg n) &           -- Draw the final leg
    (closePath c) &     -- Close the subpath
    (restore c)         -- And restore original transformation

DEF main = context &= ( \c.    	       	   -- bind the context element
    	   (snowflake c 0 5 115 125) &     -- A level-0 snowflake is an equilateral triangle
           (snowflake c 1 145 115 125) &   -- A level-1 snowflake is a 6-sided star
           (snowflake c 2 285 115 125) &   -- etc.
           (snowflake c 3 425 115 125) &
           (snowflake c 4 565 115 125) &   -- A level-4 snowflake looks like a snowflake!
           (stroke c) &	      	       	   -- Stroke this very complicated path
	   {| console.log("done.") |} )	                  