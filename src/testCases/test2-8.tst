main
var i;

function f(x);
{
  return x + 3
};

function g(x);
{
  return 2 * x
};

function h(x);
{
  return 50 - x * x
};

procedure abc(a, b, c);
{
  call outputnum(a + b * c + 2 * a * b);
  call outputnum(a * b + c);
  call outputnum(c * (b - a))
};

{
  let i <- 1;
  while i <= 10 do
    call abc(call f(call g(i)), call g(call h(i)), call h(call f(i)));
    let i <- i + 1
  od
}.
