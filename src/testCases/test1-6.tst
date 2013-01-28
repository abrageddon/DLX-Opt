main
var a;
{
  let a <- 22;
  while a > 0 do
    call outputnum(a);
    call outputnewline();
    let a <- a - 1
  od
}.