main
var a, b, c, d;
{
  let a <- call inputnum();
  let b <- call inputnum();
  let c <- call inputnum();
  let d <- call inputnum();

  while a < b do
    while b < c do
      call outputnum(b);
      call outputnewline();
      let b <- b + 2
    od;
    let a <- a + 1;
    let b <- d
  od
}.
