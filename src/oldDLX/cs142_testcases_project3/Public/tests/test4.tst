main
var x, y, z;
{
  x <- 128;
  y <- 256;
  z <- call inputnum();
  z <- z + y + x;
  x <- x + y * 24;
  if z == x then
    call outputnum(z);
    call outputnewline();
    call outputnum(x);
    call outputnewline()
  else 
    call outputnum(x);
    call outputnewline()
  fi
}.