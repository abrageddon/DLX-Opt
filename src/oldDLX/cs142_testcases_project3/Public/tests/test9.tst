main
var a111, b222, c333, d444, e555, f666;
{
  a111 <- 666;
  b222 <- 555;
  c333 <- 444;
  d444 <- 333;
  e555 <- 222;
  f666 <- 111;

  if a111 > b222 then
    a111 <- call inputnum()
  fi;

  if b222 > a111 then
    b222 <- call inputnum()
  fi;
 
  if a111 + b222 > 0 then
    call outputnum(a111);
    call outputnewline()
  fi;

  if a111 * b222 >= c333 / 22 then
    call outputnum(f666);
    call outputnewline()
  fi
}.