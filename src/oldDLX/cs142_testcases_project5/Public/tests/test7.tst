main

array[2][3] a;
array[3][2] b;
array[2][2] c;
var i, j, k;

{
  i <- 0;
  while i < 2 do
    j <- 0;
    while j < 3 do
      a[i][j] <- call inputnum();
      j <- j + 1
    od;
    i <- i + 1
  od;

  i <- 0;
  while i < 3 do
    j <- 0;
    while j < 2 do
      b[i][j] <- call inputnum();
      j <- j + 1
    od;
    i <- i + 1
  od;

  i <- 0;
  while i < 2 do
    j <- 0;
    while j < 2 do
      c[i][j] <- 0;
      k <- 0;
      while k < 3 do
        c[i][j] <- c[i][j] + a[i][k] * b[k][j];
        k <- k + 1
      od;
      call outputnum(c[i][j]);
      j <- j + 1
    od;
    i <- i + 1
  od

}.
