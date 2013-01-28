main

array[5] a;
var nn;

function ni;
var r;
{
  let r <- nn;
  let nn <- nn + 1;
  return r
};

{
  let nn <- 0;
  while nn < 5 do
    let a[call ni] <- 13
  od;

  while nn < 10 do
    call outputnum(a[call ni - 5])
  od
}.
