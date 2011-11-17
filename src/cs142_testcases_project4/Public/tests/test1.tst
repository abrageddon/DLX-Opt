main
var n1, n2;

function readnum;
{
  return call inputnum()
};

function incnum(v);
{
  return v + 1
};

procedure writenum(v);
{
  call outputnum(v);
  call outputnewline()
};

{
  n1 <- call readnum;
  n2 <- call readnum;
  n1 <- call incnum(n1 * 2) + call incnum(n2 - 3);
  call writenum(n1)
}.
