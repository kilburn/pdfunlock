# PDF File unlocker utility #

pdfunlock is a small java command line utility to unlock (remove protections) PDF files without requiring any password.

## Compilation ##

To compile the program into a single (executable) jar file, you just have to run:

	ant package-for-store

Once finished, you will have an executable jar file in the 'store' folder.

## Running ##

The most basic usage pattern is to simply call the uitlity with the input file (the one you want to unlock)

	java -jar "pdfunlock" path/to/pdffile.pdf

This will generate an unlocked version of the file at the same location than the original one, but with '_unlocked' appended to the name.

You can use the '-h' switch to get further help on usage parameters.
