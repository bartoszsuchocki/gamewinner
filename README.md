# gamewinner
University project - Program winning a logic game. 
App is used to play logic game against other app (or to help play with humans).
## Rules of the game
The board is is square shaped and consists of square fields.

In each turn, players are putting on the board rectangle blocks (one block takes the place of two fields on the board). Block can be put in vertical or horizontal orientation only on empty fields.
The target of the game is to prevent opponent from having the chance to put the block - player who puts last block on the board is the winner. 
The opposite side fields of the board are neighbours (e.g player can put one part of the block on the first-from-the-left field and second part on the first-from the-right field (of course in the same row). 

Before the start of the game 10% of the fields on the board are filled with random-put blocks.

## Algorithm overview
The main part of this program is the algorithm. It uses DFS of tree. Some of possible moves are put into the tree. The move that has the most "happy endings" is chosen.
Each move (in current turn) is processed in same amount of time. This is how algorithm chooses how many possible paths to generate. 

## Game flow
Two fighting programs are plaing together and the judge program decides which one wins. 
The judge is also responsible for communication between programs. It sends information about board size, list of filled fields and which program starts.
Fighting program has 0.5 seconds for its move (it can be changed easily) and the judge is responsible for checking it (it gives 0.5 seconds for move and then freezes the process until the second player performs his move).
The communication uses standard in.

Sample judge programs: https://github.com/kkonczak/WUT_ProgramSedziowski https://github.com/SiwyKrzysiek/Judge https://github.com/Reveso/project-armadillo 

This program can also be easily transformed to play against human.

## Used Technologies
* Java
