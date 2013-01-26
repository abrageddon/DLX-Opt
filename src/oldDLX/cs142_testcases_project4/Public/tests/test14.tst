main

procedure rec(n);
{
  if n == 0 then return fi;
  call outputnum(n);
  call rec(n - 1)
};

{
  call rec(8)
}.
