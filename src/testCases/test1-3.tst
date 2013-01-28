main
var x, y;
{
  let x <- 1;
  let y <- call inputnum();
  while x <= y do
    if x != 5 then
      call outputnum(x);
      call outputnewline()
    fi;
    let x <- x + 1
  od;
  call outputnum(x);
  call outputnewline();
  call outputnum(y);
  call outputnewline()
}.