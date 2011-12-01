main

array[10] a;
var i, j;

{
  i <- 0;
  while i < 10 do
    a[i] <- 2 + i;
    i <- i + 1
  od;

  i <- 2;
  while i < 8 do
    j <- 0 - 2;
    while j <= 2 do
      a[i + j] <- a[i + j] + a[i];
      j <- j + 1
    od;
    i <- i + 1
  od;

  i <- 0;
  while i < 10 do
    call outputnum(a[i]);
    i <- i + 1
  od
}.
