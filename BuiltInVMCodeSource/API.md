Jack OS API
===========

File
----

function __int open(String path, bool writable)__: Opens the file on the host system at *path*. The file can only be opened for reading OR writing. Returns a file descriptor.
function __void close(int fd)__: Close the file associated with the descriptor *fd*.
function __void write(int fd, String str)__: Write *string* to the file associated with the descriptor *fd*.
function __void read(int fd, String buffer, int maxBytes)__: Read at most *maxBytes* into *buffer* from the file associated with the descriptor *fd*. *buffer* must be allocated for at least *maxBytes* max size.
function __String readln(int fd)__: Read from the file associated with the descriptor *fd* until a line terminator is reached and return the bytes as a String. Will return an empy string if no bytes are available.
function __bool ready(int fd)__: Returns *true* if the file associated with the descriptor *fd* has bytes available to read.


Sys
---

function __void halt()__: halts the program execution.
function __void error(int errorCode)__: prints the error code on the screen and halts.
function __void wait(int duration)__: waits approximately *duration* milliseconds and returns.
function __int argumentCount()__: returns the number of command-line arguments.
function __String argument(int n)__: returns the *nth* command-line argument.

