main
var i, s;

procedure pp(v);
{
  s <- s + v
};

{
  i <- 1;
  s <- 0;
  while i <= 50 do
    call pp(i);
    i <- i + 1
  od;
  call outputnum(s)
}.
