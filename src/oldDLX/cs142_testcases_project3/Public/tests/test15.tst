main
var x, y, z;
{
  x <- 0;
  y <- 1;
  z <- 0;
  z <- z + y + x;
  x <- y * 20 - 17;
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
