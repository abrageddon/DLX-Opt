main
var nn;

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
  nn <- call readnum;
  nn <- call incnum(nn);
  call writenum(nn)
}.
