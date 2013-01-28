main
var inp, outp;
{
  let outp <- call inputnum();
  let inp <- outp * outp / outp - 22;

  if outp == inp then
    call outputnum(inp);
    let outp <- call inputnum();
    call outputnewline()
  fi;

  call outputnum(inp);
  call outputnewline();
  call outputnum(outp);
  call outputnewline()
}.
