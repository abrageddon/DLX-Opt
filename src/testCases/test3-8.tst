main

array[50] pr;
var i, j;

{
  let pr[0] <- 0;
  let pr[1] <- 0;
  let i <- 2;
  while i < 50 do
    let pr[i] <- 1;
    let i <- i + 1
  od;

  let i <- 2;
  while i < 50 do
    if pr[i] == 1 then
      let j <- 2 * i;
      while j < 50 do
        let pr[j] <- 0;
        let j <- j + i
      od;
      call outputnum(i)
    fi;
    let i <- i + 1
  od
}.
