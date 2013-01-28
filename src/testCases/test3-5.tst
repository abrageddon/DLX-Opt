main

array[10] a;
var i, j, t;

{
  let i <- 0;
  while i < 10 do
    let a[i] <- call inputnum();
    let i <- i + 1
  od;

  let i <- 0;
  while i < 10 do
    let j <- 0;
    while j < 9 do
      if a[j] > a[j + 1] then
        let t <- a[j];
        let a[j] <- a[j + 1];
        let a[j + 1] <- t
      fi;
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

