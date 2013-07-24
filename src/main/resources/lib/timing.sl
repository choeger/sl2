
-- Interval timer
-- (Should work with node.js and current browsers)

DATA EXTERN IntervalTimer
DATA EXTERN TimeoutTimer

PUBLIC FUN interval : DOM Void -> Int -> DOM IntervalTimer
DEF interval cb delay = {| setInterval($cb, $delay); |} : DOM IntervalTimer

PUBLIC FUN clearInterval : IntervalTimer -> DOM Void
DEF EXTERN clearInterval = {| clearInterval |}

PUBLIC FUN timeout : LAZY (DOM Void) -> Int -> DOM TimeoutTimer
DEF timeout (SUSPEND cb) delay = 
	{| setTimeout(function(){$cb(0)()}, $delay); |} : DOM TimeoutTimer

PUBLIC FUN clearTimeout : TimeoutTimer -> DOM Void
DEF EXTERN clearTimeout = {| clearTimeout |} 