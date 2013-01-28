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
  let nn <- call readnum;
  let nn <- call incnum(nn);
  call writenum(nn)
}.
