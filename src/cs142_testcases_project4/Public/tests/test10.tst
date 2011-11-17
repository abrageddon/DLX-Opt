main
var i, r;

function pow(b, e);
var t;
{
  if e == 0 then return 1 fi;
  if e == 1 then return b fi;

  t <- call pow(b, e / 2);
  t <- t * t;
  if 2 * (e / 2) != e then
    t <- t * b
  fi;
  return t
};

function sumpow(b, n);
var i, s;
{
  s <- 0;
  i <- 0;
  while i <= n do
    s <- s + call pow(b, i);
    i <- i + 1
  od;
  return s
};

{
  call outputnum(call sumpow(2, 10));
  call outputnum(call sumpow(3, 7));
  call outputnum(call sumpow(5, 3))
}.
