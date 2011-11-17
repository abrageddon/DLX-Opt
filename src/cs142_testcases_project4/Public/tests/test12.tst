main

function f(x, y);
{
  return x + y
};

function g(a, b, c);
{
  return call f(a * b, b * c)
};

{
  call outputnum(call f(3, 5));
  call outputnum(call g(2, 3, 4))
}.
