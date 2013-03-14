main

function fibo(n);
{
  if n <= 0 then
    return 0
  fi;
  if n == 1 then
    return 1
  fi;
  return call fibo(n - 1) + call fibo(n - 2)
};

{
  call outputnum(call fibo(8));
  call outputnum(call fibo(10));
  call outputnum(call fibo(12))
}.

