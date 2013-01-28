main

function fibo(n);
array[10] f;
var i;
{
  let f[0] <- 0;
  let f[1] <- 1;
  let i <- 2;
  while i < 10 do
    let f[i] <- f[i - 1] + f[i - 2];
    let i <- i + 1
  od;
  return f[n]
};

{
  call outputnum(call fibo(9))
}.

