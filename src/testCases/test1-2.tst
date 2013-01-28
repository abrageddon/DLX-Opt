main
var x, y;
{
  let x <- call inputnum();
  let y <- call inputnum();
  while x < y do
    call outputnum(x);
    call outputnewline();
    let x <- x + 1
  od
}.
