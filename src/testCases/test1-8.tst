main
var a, b, c, d, e, f;
{
  let a <- 22;
  let b <- 33;
  let c <- 44;
  let d <- 55;
  let e <- 66;
  let f <- 77;

  let a <- a + call inputnum();
  let b <- b + a;
  let c <- d * b;
  let d <- c / a;
  let e <- a + b + c + d * 22 / 4;
  let f <- 86 / 22 + a;

  if a == b then
    if b != c then
      if c != d then
        if d != e then
          call outputnum(e);
          call outputnewline()
        fi
      fi
    fi
  else
    call outputnum(a);
    call outputnewline()
  fi;

  call outputnum(c);
  call outputnewline()
}.