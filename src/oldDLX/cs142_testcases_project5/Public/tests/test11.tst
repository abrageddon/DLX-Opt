main

array[5] a;
var nn;

function ni;
var r;
{
  r <- nn;
  nn <- nn + 1;
  return r
};

{
  nn <- 0;
  while nn < 5 do
    a[call ni] <- 13
  od;

  while nn < 10 do
    call outputnum(a[call ni - 5])
  od
}.
