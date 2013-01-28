main

function gcd(a, b);
var m;
{
  if a < b then return call gcd(b, a) fi;
  if a == b then return a fi;
  if b == 0 then return a fi;
  let m <- a - (a / b) * b;
  return call gcd(b, m)
};

{
  call outputnum(call gcd(252, 105));
  call outputnum(call gcd(384, 1024));
  call outputnum(call gcd(1989, 867))
}.
