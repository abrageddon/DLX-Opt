main

function f();
{
  return call inputnum
};

function g(x);
{
  return 1 + 3 * x
};

procedure h(x);
{
  call outputnum(x)
};

{
  call h(call g(call f))
}.
