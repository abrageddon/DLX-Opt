main

function foo();
var a, b, c, d, e, f, g, h;
{
	let a <- 1;
	let b <- 2;
	let c <- 3;
	let d <- 4;
	let e <- 5;
	let f <- 6;
	let g <- 7;
	let h <- 8;
	
	if a < b then
		let a <- a + 1;
		let e <- e + 1;
		if b < c then
			let c <- c * 3;
			while a < c do
				let a <- a + 1
			od
		fi;
		let g <- 9 
	else
		let b <- b - 1;
		let f <- f + 1;
		if b < c then
			let c <- c * 3;
			while b < c do
				let b <- b + 1;
				let c <- c - 1
			od
		else
			let c <- c * 4;
			let d <- d + 1
		fi;
		let h <- 425
	fi;
	
	call OutputNum(a);
	call OutputNum(b);
	call OutputNum(c);
	call OutputNum(d);
	call OutputNum(e);
	call OutputNum(f);
	call OutputNum(h)
};

{
	call foo
}
.