//Basic if statement test
main
var a, b;
{
	let a <- 1;
	let b <- 2;
	if a > b then
		let a <- a + 1
	else
		let b <- b + 1
	fi;
	
	if a < b then
		let a <- a - 1
	fi;
	
	call OutputNum(a);
	call OutputNum(b)
}.