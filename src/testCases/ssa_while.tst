main 

function test();
var x;
var y;
var z;

{
	let x <- 0;
	let y <- 1;
	let z <- 2;

	while x <= 10 do
		let x <- x + 1;
		let y <- 2 * x + z
	od;
  
	return y

};

{
  call test()
}.













