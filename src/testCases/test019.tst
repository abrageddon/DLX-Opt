# Copy propagation transitive test
main
var a, b, c, d;
{
	let b <- a;
	let c <- b;
	let d <- c;
	
	let d <- d + 1
}
.