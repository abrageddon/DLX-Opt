main
var a, b, c, d;
{
  let a <- 2;
  let b <- 4;
  let c <- 6;
  let d <- 8;

  let a <- a + b;
  let b <- b + c;
  let c <- c + d;
  let d <- d - a * b;

  let d <- d + call inputnum();

  call outputnum(a);
  call outputnewline();
  call outputnum(b);
  call outputnewline();
  call outputnum(c);
  call outputnewline();
  call outputnum(d);
  call outputnewline()
}.