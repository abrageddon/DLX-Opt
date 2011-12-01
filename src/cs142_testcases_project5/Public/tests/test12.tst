main

function fibo(n);
array[10] f;
var i;
{
  f[0] <- 0;
  f[1] <- 1;
  i <- 2;
  while i < 10 do
    f[i] <- f[i - 1] + f[i - 2];
    i <- i + 1
  od;
  return f[n]
};

{
  call outputnum(call fibo(9))
}.

