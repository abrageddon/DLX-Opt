main
var i;

procedure many(a, b, c, d, e, f, g, h);
{
  call outputnum(a + b - 2 * c + d * e + f * g / h)
};

{
  i <- 10;
  while i < 16 do
    call many(i - 1, i, i + 1, i - 1, i, i + 1, i, i + 2);
    i <- i + 1
  od
}.
