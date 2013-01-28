main

array[2][3] a;
array[3][2] b;
array[2][2] c;
var i, j, k;

{
  let i <- 0;
  while i < 2 do
    let j <- 0;
    while j < 3 do
      let a[i][j] <- call inputnum();
      let j <- j + 1
    od;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 3 do
    let j <- 0;
    while j < 2 do
      let b[i][j] <- call inputnum();
      let j <- j + 1
    od;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 2 do
    let j <- 0;
    while j < 2 do
      let c[i][j] <- 0;
      let k <- 0;
      while k < 3 do
        let c[i][j] <- c[i][j] + a[i][k] * b[k][j];
        let k <- k + 1
      od;
      call outputnum(c[i][j]);
      let j <- j + 1
    od;
    let i <- i + 1
  od

}.
