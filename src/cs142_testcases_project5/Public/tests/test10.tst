main

array[10] a;
var i, p;

{
  p <- call inputnum();
  a[0] <- 1;
  a[1] <- p;
  i <- 2;
  while i < 10 do
    if i == 2 * (i / 2) then
       call outputnum(i);
       a[i] <- a[i / 2] * a[i / 2]
    else
       a[i] <- p * a[i - 1]
    fi;
    i <- i + 1
  od;

  i <- 0;
  while i < 10 do
    call outputnum(a[i]);
    i <- i + 1
  od
}.
