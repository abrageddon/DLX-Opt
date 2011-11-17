main
var a, b, c;

function f;
{
  a <- a + 1;
  return a
};

function g;
{
  b <- b + 1;
  return b
};

{
  a <- 1;
  b <- 2;
  c <- 3;
  while c < 10 do
    if call f < call g then
      call outputnum(c)
    fi;
    c <- c + 1
  od
}.
