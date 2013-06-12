DEF canvas = {| document.getElementById("canvas") |}

DEF context = canvas &= \canvas.{| $canvas.getContext("2d") |} 

DEF save c = {| $c.save() |}

DEF scale ctxt a b c d = {| $ctxt.scale($a/$b, $c/$d) |}

DEF rotate c deg = {| $c.rotate($deg * Math.PI/180) |}

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
    	    (scale c 1 3 1 3) &
	    (leg (n-1)) &         -- Recurse for the first sub-leg
            (rotate c 60) &       -- Turn 60 degrees clockwise
            (leg (n-1)) &         -- Second sub-leg
            (rotate c (0-120)) &   -- Rotate 120 degrees back
            (leg (n-1)) &         -- Third sub-leg
            (rotate c 60) &       -- Rotate back to our original heading
            (leg (n-1))           -- Final sub-leg
	 ) &           
         (restore c) &            -- Restore the transformation
         (translate c len 0)     -- But translate to make end of leg (0,0)
	    
    IN 
    (save c) &		-- Save current transformation
    (translate c x y) & -- Translate origin to starting point
    (moveTo c 0 0) &  	-- Begin a new subpath at the new origin
    (leg n) & 	   	-- Draw the first leg of the snowflake
    (rotate c ((0-1) * 120)) &   -- Now rotate 120 degrees counterclockwise
    (leg n) &           -- Draw the second leg
    (rotate c ((0-1) * 120)) &    -- Rotate again
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