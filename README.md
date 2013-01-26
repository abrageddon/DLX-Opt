DLX-Opt
=======


VCG
===
Ubuntu install:
http://launchpadlibrarian.net/7479854/vcg_1.30debian-6_amd64.deb

Run:
xvcg -font "-*-courier 10 pitch-*-*-*-*-*-*-*-*-*-*-*-*" fileName.vcg
examples are in vcg.1.30/expl

Compiler
========
Implemented the Scanner/Parser part of the front end. For testing use the JUnit framework in package 'tests'.
The 'old' test cases need minor modifications to be compatible with current grammar specification; the only difference that I noticed is the lack of 'let' keyword in the assignment statement of the 'old' grammar.
Currently the Parser has a Symbol table but lacks type checking which will be implemented together with the SSA generation.
Next step: CFG + SSA.