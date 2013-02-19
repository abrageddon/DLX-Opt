main 

function test();
var x;
var y;
var z;

{
	let x <- 0;
	let y <- 1;
	let z <- 2;

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













