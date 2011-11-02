main
var x, y;
{
  x <- 1;
  y <- call inputnum();
  while x <= y do
    if x != 5 then
      call outputnum(x);
      call outputnewline()
    fi;
    x <- x + 1
  od;
  call outputnum(x);
  call outputnewline();
  call outputnum(y);
  call outputnewline()
}.