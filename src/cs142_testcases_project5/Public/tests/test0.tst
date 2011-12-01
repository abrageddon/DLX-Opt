main

array[2] a;
var i;

{
  i <- 0;
  while i < 2 do
    a[i] <- call inputnum();
    i <- i + 1
  od;

  call outputnum(a[0]);
  call outputnum(a[1])
}.
