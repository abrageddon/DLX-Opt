main
var i, s;

procedure pp(v);
{
  let s <- s + v
};

{
  let i <- 1;
  let s <- 0;
  while i <= 50 do
    call pp(i);
    let i <- i + 1
  od;
  call outputnum(s)
}.
