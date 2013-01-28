main

array[2] a;
var i, t;

{
  let i <- 0;
  while i < 2 do
    let a[i] <- call inputnum();
    let i <- i + 1
  od;

  let t <- a[0];
  let a[0] <- a[1];
  let a[1] <- t;

  call outputnum(a[0]);
  call outputnum(a[1])
}.
