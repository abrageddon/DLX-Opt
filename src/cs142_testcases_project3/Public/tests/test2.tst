main
var x, y;
{
  x <- call inputnum();
  y <- call inputnum();
  while x < y do
    call outputnum(x);
    call outputnewline();
    x <- x + 1
  od
}.
