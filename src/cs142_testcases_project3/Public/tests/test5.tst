main
var a, b, c, d;
{
  a <- 2;
  b <- 4;
  c <- 6;
  d <- 8;

  a <- a + b;
  b <- b + c;
  c <- c + d;
  d <- d - a * b;

  d <- d + call inputnum();

  call outputnum(a);
  call outputnewline();
  call outputnum(b);
  call outputnewline();
  call outputnum(c);
  call outputnewline();
  call outputnum(d);
  call outputnewline()
}.