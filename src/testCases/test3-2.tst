main

array[10] a;

{
  let a[0] <- 0;
  let a[1] <- 1;
  let a[2] <- a[0] + a[1];
  let a[3] <- a[1] + a[2];
  let a[4] <- a[2] + a[3];
  let a[5] <- a[3] + a[4];
  let a[6] <- a[4] + a[5];
  let a[7] <- a[5] + a[6];
  let a[8] <- a[6] + a[7];
  let a[9] <- a[7] + a[8];

  call outputnum(a[9])
}.
