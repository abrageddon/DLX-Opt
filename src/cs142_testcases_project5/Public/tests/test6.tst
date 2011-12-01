main

array[16] f;
var i;

{
  f[0] <- 0;
  f[1] <- 1;
  i <- 2;
  while i < 16 do
    f[i] <- f[i - 1] + f[i - 2];
    call outputnum(f[i]);
    i <- i + 1
  od
}.

