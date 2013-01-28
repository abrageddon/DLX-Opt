main
var i;

function f(x);
{
  return x + 1
};

function g(x);
{
  return 7 * x
};

{
  let i <- 2;
  while i < 100 do
    if 2 * (i / 2) == i then
      call outputnum(call f(i))
    else
      call outputnum(call g(i))
    fi;
    let i <- 3 * i + 1
  od
}.
