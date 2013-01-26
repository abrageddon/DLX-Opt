main
var a, b;
{
  a <- 0;
  b <- 10;

  while a < b do
    if a < 10 then
      call outputnum(a)
    else
      call outputnum(a);
      call outputnewline()
    fi;
    a <- a + 1
  od
}.
