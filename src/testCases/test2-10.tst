main
var i, r;

function pow(b, e);
var t;
{
  if e == 0 then return 1 fi;
  if e == 1 then return b fi;

  let t <- call pow(b, e / 2);
  let t <- t * t;
  if 2 * (e / 2) != e then
    let t <- t * b
  fi;
  return t
};

function sumpow(b, n);
var i, s;
{
  let s <- 0;
  let i <- 0;
  while i <= n do
    let s <- s + call pow(b, i);
    let i <- i + 1
  od;
  return s
};

{
  call outputnum(call sumpow(2, 10));
  call outputnum(call sumpow(3, 7));
  call outputnum(call sumpow(5, 3))
}.
