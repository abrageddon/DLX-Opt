main
var a, b;
{
  a <- call inputnum();
  b <- call inputnum();

  if a > b then
    while b < a do
      call outputnum(b);
      call outputnewline();
      b <- b + 1
    od
  else
    while a < b do
     call outputnum(a);
     call outputnewline();
     a <- a + 1 
    od
  fi
}.
