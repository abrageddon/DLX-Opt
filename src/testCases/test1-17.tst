main
var a, b;
{
  let a <- 20;
  let b <- 0;

  while a > 0 do
    let b <- a / 2;
    if b == 0 then
      call outputnum(a);
      call outputnewline()
    fi;
    let a <- a - 1
  od
}.
