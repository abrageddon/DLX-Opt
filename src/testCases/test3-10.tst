main

array[10] a;
var i, p;

{
  let p <- call inputnum();
  let a[0] <- 1;
  let a[1] <- p;
  let i <- 2;
  while i < 10 do
    if i == 2 * (i / 2) then
       call outputnum(i);
       let a[i] <- a[i / 2] * a[i / 2]
    else
       let a[i] <- p * a[i - 1]
    fi;
    let i <- i + 1
  od;

  let i <- 0;
  while i < 10 do
    call outputnum(a[i]);
    let i <- i + 1
  od
}.
