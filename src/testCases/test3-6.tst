main

array[16] f;
var i;

{
  let f[0] <- 0;
  let f[1] <- 1;
  let i <- 2;
  while i < 16 do
    let f[i] <- f[i - 1] + f[i - 2];
    call outputnum(f[i]);
    let i <- i + 1
  od
}.

