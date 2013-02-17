main 

function test();
var x;
var y;
var z;

{
	let x <- 1;
	let y <- 2;
	let z <- 3;
	if x <= 2 then
		let x <- y + z
	else
  		let x <- y - z
  	fi;
  
  return x

};

{
  call test()
}.













