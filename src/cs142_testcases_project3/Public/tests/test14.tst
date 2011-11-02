main
var inp, outp;
{
  outp <- call inputnum();
  inp <- outp * outp / outp - 22;

  if outp == inp then
    call outputnum(inp);
    outp <- call inputnum();
    call outputnewline()
  fi;

  call outputnum(inp);
  call outputnewline();
  call outputnum(outp);
  call outputnewline()
}.
