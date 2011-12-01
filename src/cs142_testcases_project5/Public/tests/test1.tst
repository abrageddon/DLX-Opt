main

array[2] a;
var i, t;

{
  i <- 0;
  while i < 2 do
    a[i] <- call inputnum();
    i <- i + 1
  od;

  t <- a[0];
  a[0] <- a[1];
  a[1] <- t;

  call outputnum(a[0]);
  call outputnum(a[1])
}.
