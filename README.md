# Magnet Backtracker
A backtracker that will return a configuration of magnets based on the specified positive and negative charges of each row and column.
A file will be read in the format of\
5 4\
-1 2 2 -1 -1\
2 -1 -1 1\
2 -1 1 -1 -1\
1 -1 1 -1\
T T T T\
B B B B\
L R T T\
T T B B\
B B L R
* the first line represents the number of rows and columns, respectively
* the second line represents the amount of positive charges a row has (a -1 means no specific amount)
* the third line represents the amount of positive charges a column has (a -1 means no specific amount)
* the fourth line represents the amount of negative charges a row has (a -1 means no specific amount)
* the fifth row represents the amount of negative charges a column has (a -1 means no specific amount)
* the lines under it are the letters T, B, L, R which represents which way a magnet can be placed
## Prerequisites
* Java 8=>11 (Make sure to have correct JAVA_HOME setup in your environment)
* Javac (Java and Javac should be same version. Execute `javac -version` and `java -version` to check)
## How to run it
Clone the repository and go to the root directory
* On IntelliJ, choose a configuration that is set up or create a configuration using Add Configuration.
* On other platforms, Execute `java -cp ./out/ src/magnets/Magnets.java data/filename true|false`
