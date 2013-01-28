main
var a22a, b33b;
{
  let a22a <- 0;
  let b33b <- 4;

  if a22a < b33b then
    let a22a <- a22a - b33b - b33b;
    if a22a < b33b then
      let a22a <- a22a - b33b - b33b
    fi 
  fi;

  call outputnum(a22a);
  call outputnewline();
  call outputnum(b33b);
  call outputnewline()
}.
