main

array[3][3][3] a, b;
var i, j, k;

{
  i <- 0;
  while i < 3 do
    j <- 0;
    while j < 3 do
      k <- 0;
      while k < 3 do
        a[i][j][k] <- i + 2 * j + i * k;
        k <- k + 1
      od;
      j <- j + 1
    od;
    i <- i + 1
  od;

  i <- 0;
  while i < 3 do
    j <- 0;
    while j < 3 do
      k <- 0;
      while k < 3 do
        b[i][j][k] <- a[i][j][k] + 1;
        k <- k + 1
      od;
      j <- j + 1
    od;
    i <- i + 1
  od;

  i <- 0;
  while i < 3 do
    j <- 0;
    while j < 3 do
      k <- 0;
      while k < 3 do
        call outputnum(b[i][j][k]);
        k <- k + 1
      od;
      j <- j + 1
    od;
    i <- i + 1
  od
}.
