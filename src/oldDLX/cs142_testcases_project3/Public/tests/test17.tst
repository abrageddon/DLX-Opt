main
var a, b;
{
  a <- 20;
  b <- 0;

  while a > 0 do
    b <- a / 2;
    if b == 0 then
      call outputnum(a);
      call outputnewline()
    fi;
    a <- a - 1
  od
}.
