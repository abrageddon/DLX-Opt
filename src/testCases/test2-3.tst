main
var i, s;

function pp(v);
var j, t;
{
  let j <- 1;
  let t <- 0;
  while j <= v do
    let t <- t + j;
    let j <- j + 1
  od;
  return t
};

{
  let i <- 1;
  let s <- 0;
  while i < 20 do
    let s <- s + call pp(i);
    let i <- i + 1
  od;
  call outputnum(s)
}.
