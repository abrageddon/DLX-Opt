main
var x, y, z;
{
  let x <- 128;
  let y <- 256;
  let z <- call inputnum();
  let z <- z + y + x;
  let x <- x + y * 24;
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