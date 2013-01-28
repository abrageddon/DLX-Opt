main
var a, b, c, d, e, f;
{
  let a <- 0;
  let b <- 0;
  let c <- 0;
  let d <- 0;
  let e <- 0;
  let f <- 0;

  if a == b then
    let a <- 24;
    call outputnum(a);
    call outputnewline()
  fi;

  let b <- b + 1 * 15;

  if a <= b then
    let c <- 55 / 11 + 16 * a / 2;
    call outputnum(c);
    call outputnewline()
  fi;
 
  let a <- 1;
  let b <- 1;
  let c <- 1;
  let d <- 1;
  let e <- 1;

  let e <- a + b + c + d + e - a - b - c - d;
  call outputnum(e);
  call outputnewline()
}.
