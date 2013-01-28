main

array[2] a;
var i;

{
  let i <- 0;
  while i < 2 do
    let a[i] <- call inputnum();
    let i <- i + 1
  od;

  call outputnum(a[0]);
  call outputnum(a[1])
}.
