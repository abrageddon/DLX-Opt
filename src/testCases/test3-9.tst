main

array[3][3][3] a, b;
var i, j, k;

{
  let i <- 0;
  while i < 3 do
    let j <- 0;
    while j < 3 do
      let k <- 0;
      while k < 3 do
        let a[i][j][k] <- i + 2 * j + i * k;
        let k <- k + 1
      od;
      let j <- j + 1
    od;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 3 do
    let j <- 0;
    while j < 3 do
      let k <- 0;
      while k < 3 do
        let b[i][j][k] <- a[i][j][k] + 1;
        let k <- k + 1
      od;
      let j <- j + 1
    od;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 3 do
    let j <- 0;
    while j < 3 do
      let k <- 0;
      while k < 3 do
        call outputnum(b[i][j][k]);
        let k <- k + 1
      od;
      let j <- j + 1
    od;
    let i <- i + 1
  od
}.
