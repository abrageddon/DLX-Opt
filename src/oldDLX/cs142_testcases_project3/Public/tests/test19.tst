main
var a, b, c, d;
{
  a <- 424;
  b <- 4920;
  c <- 9302;
  d <- 2391;

  if a != b then
    if b != c then
      if c != d then
        call outputnum(a)
      else
        call outputnum(a)
      fi
    else
     call outputnum(a)
    fi
  else
    call outputnum(a)
  fi;
  call outputnewline()
}.
