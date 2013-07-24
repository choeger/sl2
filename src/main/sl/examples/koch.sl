IMPORT "basicweb" AS Web
IMPORT "timing" AS Time
IMPORT "basicio" AS Dbg

DEF createCanvas = 
	Web.document &= \ doc .
	Web.getBody doc &= \ body .
	Web.createElement doc "canvas" &= \ canvas .
	{| $canvas.width = 800 |} &
	{| $canvas.height = 800 |} &
	Web.appendChild body canvas &
	yield canvas 

DEF context = createCanvas &= \canvas.{| $canvas.getContext("2d") |} 

DEF save c = {| $c.save() |}

DEF scale ctxt a b c d = {| $ctxt.scale($a/$b, $c/$d) |}

DEF rotate c deg = {| $c.rotate($deg * Math.PI/180) |}

DEF translate c x y = {| $c.translate($x, $y) |}

DEF moveTo c x y = {| $c.moveTo($x, $y) |}

DEF lineTo c x y = {| $c.lineTo($x, $y) |}

DEF restore c = {| $c.restore() |}

DEF beginPath c = {| $c.beginPath() |}
DEF closePath c = {| $c.closePath() |}

DEF stroke c = {| $c.stroke() |}

DEF clear c = {| $c.clearRect(0, 0, $c.canvas.width, $c.canvas.height) |}

DEF snowflake c n x y len = 
    LET leg = \n . (save c) & 
    	  (IF n == 0 THEN 
	    lineTo c len 0 
	  ELSE
    	    (scale c 1 3 1 3) &
	    (leg (n - 1)) &         -- Recurse for the first sub-leg
            (rotate c 60) &       -- Turn 60 degrees clockwise
            (leg (n - 1)) &         -- Second sub-leg
            (rotate c (0 - 120)) &   -- Rotate 120 degrees back
            (leg (n - 1)) &         -- Third sub-leg
            (rotate c 60) &       -- Rotate back to our original heading
            (leg (n - 1))           -- Final sub-leg
	 ) &           
         (restore c) &            -- Restore the transformation
         (translate c len 0)     -- But translate to make end of leg (0,0)
	    
    IN 
    (beginPath c) &
    (save c) &		-- Save current transformation
    (translate c x y) & -- Translate origin to starting point
    (moveTo c 0 0) &  	-- Begin a new subpath at the new origin
    (leg n) & 	   	-- Draw the first leg of the snowflake
    (rotate c ((0 - 1) * 120)) &   -- Now rotate 120 degrees counterclockwise
    (leg n) &           -- Draw the second leg
    (rotate c ((0 - 1) * 120)) &    -- Rotate again
    (leg n) &           -- Draw the final leg
    (closePath c) &     -- Close the subpath
    (restore c)         -- And restore original transformation

DEF mainLoop c i lazyness = 
	LET size = pingpong i 27 IN
	(IF (i % 19) == 0 THEN 
	LET col = (IF (i%3) == 0 THEN "c" ELSE "f") ++ intToString(i%10)
	    col3 = "#" ++ col ++ col ++ col
	IN {|$c.fillStyle = $col3|} & {| $c.fillRect(-50,-50,500,500) |} ELSE noop) &
	rotate c 10 &
	snowflake c (pingpong i 4) (size*6) (size*6) (125+size*6) &
	stroke c &
	Time.timeout (SUSPEND (mainLoop c (i+1))) 150 &
	noop

DEF pingpong a b =
	LET p = a%(2*b) IN
	IF p <= b THEN p ELSE (2*b) - p

DEF main = context &= ( \c.    	       	   -- bind the context element
		translate c 250 250 &
       (mainLoop c 0 Void) &
	   {| console.log("done.") |} )	                  