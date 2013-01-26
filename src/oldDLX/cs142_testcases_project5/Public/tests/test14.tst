main

array[10] a;
var i;

function max(nn);
var vv;
{
  if nn == 0 then return a[nn] fi;

  vv <- call max(nn - 1);
  if a[nn] > vv then return a[nn] fi;
  return vv
};

{
  i <- 0;
  while i < 10 do
    a[i] <- call inputnum();
    i <- i + 1
  od;
  
  call outputnum(call max(9))
}.
