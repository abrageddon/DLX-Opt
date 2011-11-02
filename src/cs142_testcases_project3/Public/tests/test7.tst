main
var a, b, c, d, e, f;
{
  a <- 22;
  b <- 33;
  c <- 44;
  d <- 55;
  e <- 66;
  f <- 77;

  a <- a + call inputnum();
  b <- b + a;
  c <- d * b;
  d <- c / a;
  e <- a + b + c + d * 22 - 18;
  f <- 86 / 22 + a;

  if a != b then
    if b != c then
      if c != d then
        if d != e then
          call outputnum(e);
          call outputnewline()
        fi
      fi
    fi
  fi;

  call outputnum(a);
  call outputnewline();
  call outputnum(b);
  call outputnewline()
}.