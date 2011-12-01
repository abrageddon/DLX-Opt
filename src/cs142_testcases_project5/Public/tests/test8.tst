main

array[50] pr;
var i, j;

{
  pr[0] <- 0;
  pr[1] <- 0;
  i <- 2;
  while i < 50 do
    pr[i] <- 1;
    i <- i + 1
  od;

  i <- 2;
  while i < 50 do
    if pr[i] == 1 then
      j <- 2 * i;
      while j < 50 do
        pr[j] <- 0;
        j <- j + i
      od;
      call outputnum(i)
    fi;
    i <- i + 1
  od
}.
