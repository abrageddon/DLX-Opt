main
var i, s;

function pp(v);
var j, t;
{
  j <- 1;
  t <- 0;
  while j <= v do
    t <- t + j;
    j <- j + 1
  od;
  return t
};

{
  i <- 1;
  s <- 0;
  while i < 20 do
    s <- s + call pp(i);
    i <- i + 1
  od;
  call outputnum(s)
}.
