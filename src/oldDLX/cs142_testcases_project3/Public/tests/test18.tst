main
var a, b;
{
  a <- 1;
  b <- 22;

  if a < b then
    if a > b then
      call outputnum(22)
    else
      call outputnum(a);
      call outputnewline()
    fi
  fi
}.
