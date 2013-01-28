main
var x, y, z;
{
  let x <- 0;
  let y <- 1;
  let z <- 0;
  let z <- z + y + x;
  let x <- y * 20 - 17;
  if z != x then
    call outputnum(z);
    call outputnewline();
    call outputnum(x);
    call outputnewline()
  else 
    call outputnum(x);
    call outputnewline()
  fi
}.
