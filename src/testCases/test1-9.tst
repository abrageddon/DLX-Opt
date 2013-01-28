main
var a111, b222, c333, d444, e555, f666;
{
  let a111 <- 666;
  let b222 <- 555;
  let c333 <- 444;
  let d444 <- 333;
  let e555 <- 222;
  let f666 <- 111;

  if a111 > b222 then
    let a111 <- call inputnum()
  fi;

  if b222 > a111 then
    let b222 <- call inputnum()
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