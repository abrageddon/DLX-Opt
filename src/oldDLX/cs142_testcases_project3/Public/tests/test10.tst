main
var a, b, c, d, e, f;
{
  a <- 0;
  b <- 0;
  c <- 0;
  d <- 0;
  e <- 0;
  f <- 0;

  if a == b then
    a <- 24;
    call outputnum(a);
    call outputnewline()
  fi;

  b <- b + 1 * 15;

  if a <= b then
    c <- 55 / 11 + 16 * a / 2;
    call outputnum(c);
    call outputnewline()
  fi;
 
  a <- 1;
  b <- 1;
  c <- 1;
  d <- 1;
  e <- 1;

  e <- a + b + c + d + e - a - b - c - d;
  call outputnum(e);
  call outputnewline()
}.
