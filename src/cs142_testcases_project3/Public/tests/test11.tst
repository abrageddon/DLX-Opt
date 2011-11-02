main
var a, b, c, d;
{
  a <- call inputnum();
  b <- call inputnum();
  c <- call inputnum();
  d <- call inputnum();

  while a < b do
    while b < c do
      call outputnum(b);
      call outputnewline();
      b <- b + 2
    od;
    a <- a + 1;
    b <- d
  od
}.
