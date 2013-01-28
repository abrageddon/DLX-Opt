main

array[10] a;
var i, j;

{
  let i <- 0;
  while i < 10 do
    let a[i] <- 2 + i;
    let i <- i + 1
  od;

  let i <- 2;
  while i < 8 do
    let j <- 0 - 2;
    while j <= 2 do
      let a[i + j] <- a[i + j] + a[i];
      let j <- j + 1
    od;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 10 do
    call outputnum(a[i]);
    let i <- i + 1
  od
}.
