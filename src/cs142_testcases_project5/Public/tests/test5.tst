main

array[10] a;
var i, j, t;

{
  i <- 0;
  while i < 10 do
    a[i] <- call inputnum();
    i <- i + 1
  od;

  i <- 0;
  while i < 10 do
    j <- 0;
    while j < 9 do
      if a[j] > a[j + 1] then
        t <- a[j];
        a[j] <- a[j + 1];
        a[j + 1] <- t
      fi;
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

