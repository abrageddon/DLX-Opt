main
var a, b;
{
  let a <- 0;
  let b <- 10;

  while a < b do
    if a < 10 then
      call outputnum(a)
    else
      call outputnum(a);
      call outputnewline()
    fi;
    let a <- a + 1
  od
}.
