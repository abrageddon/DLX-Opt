main
var a, b;
{
  let a <- call inputnum();
  let b <- call inputnum();

  if a > b then
    while b < a do
      call outputnum(b);
      call outputnewline();
      let b <- b + 1
    od
  else
    while a < b do
     call outputnum(a);
     call outputnewline();
     let a <- a + 1 
    od
  fi
}.
