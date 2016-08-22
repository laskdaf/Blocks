Blocks Project.

Code that simulates and solves a block slider puzzle. Attempts to find a set of moves (each move is sliding a block N, S, E, or W) that can manipulate the puzzle from a given starting config to a target config.

Takes a directed breadth-first approach, calculating the total manhattan distance from the current position of each block to a possible target position, to find a possible path.

TEST CASES:

initial config file	goal file		comments
--------------------------------------------------------------------------------
1x1			1x1.goal		the name says it all
140x140			140x140.goal		140x140 puzzle; solution
doesn't require any search
init.from.handout	goal.2.from.handout	puzzle solved in project
description
easy			easy.goal		2x2 puzzle with three blocks;
goal is to move a block
full.1			easy.goal		2x2 puzzle that's full of 1x1
blocks, with a goal that's
instantly satisfied
full.2			easy.goal		2x2 puzzle with two 1x1 blocks
and one 1x2 block, with
a goal that can't be
satisfied
big.block.1		big.block.1.goal	four 1x2 blocks, one of which
has to be moved up and over
big.block.2		big.block.2.goal	same as big.block.1 only
in the other direction
big.block.3		big.block.3.goal	same as big.block.1 except
rotated 90 degrees
big.block.4		big.block.4.goal	same as big.block.1 except
with two more 1x1 blocks
to clutter things up
big.search.1		big.search.1.goal	rearrange 8 blocks in 3x3 tray
big.search.2		big.search.2.goal	rearrange 8 blocks in 3x3 tray
big.tray.2		big.tray.2.goal		one 1x100 block in a 100x100
tray
check.diff.blocks	check.diff.blocks.goal	switch positions of two blocks
enormous.full.1		enormous.full.goal.1	large full tray, 1-block goal
enormous.full.1		enormous.full.goal.2	large full tray, 1 enormous
block goal
enormous.3		enormous.deadend.1	large full tray, unachievable
goal
enormous.full.1		enormous.full.deadend.2	ditto
forced.match.1		forced.match.1.goal	two 1x1 blocks and a 1x3
block; goal requires move
of two blocks, no search
necessary
forced.match.1+90	forced.match.1+90.goal	same as forced.match.1
except rotated 90 degrees
forced.match.2		forced.match.2.goal	same as forced.match.1
except goal is shuffled
(though blocks are the
same)
immed.match.0		immed.match.0.goal	five 1x1 blocks; goal requires
move of one block
immed.match.1		immed.match.1.goal	two 1x1 blocks and a 1x3 block;
goal requires move of one
block
immed.match.2		immed.match.2.goal	same as immed.match.1 except
goal is shuffled (though
blocks are the same)
immed.match.2+90	immed.match.2+90.goal	same as immed.match.2
immed.match.2+180	immed.match.2+180.goal	  except rotated
immed.match.2+270	immed.match.2+270.goal	  
impossible.1		impossible.1.goal	five 1x1 blocks on one side
of a 1x3 block; goal has
two of the 1x1 blocks on
the other side of the 1x3
block
impossible.1+90		impossible.1+90.goal	impossible.1 rotated 90 degrees
impossible.3		impossible.3.goal	impossible tray with more
blocks
impossible.3x4		impossible.3x4.goal.1	impossible with horizontally
sliding blocks
impossible.4x3		impossible.4x3.goal.1	impossible with vertically
sliding blocks
impossible.4		impossible.4.goal	impossible with more blocks
impossible.4+90		impossible.4+90.goal	impossible.4 rotated
impossible.4+180	impossible.4+180.goal	impossible.4 rotated
impossible.4+270	impossible.4+270.goal	impossible.4 rotated
instant.match.0		instant.match.0.goal	five 1x1 blocks; goal
exactly matches init
instant.match.1		instant.match.1.goal	two 1x1 blocks and a 1x3 block;
goal exactly matches init
instant.match.1+90	instant.match.1+90.goal	same as instant.match.1
except rotated 90 degrees
instant.match.2		instant.match.2.goal	same as instant.match.1
except goal is shuffled
(though blocks are the same)
instant.match.3		instant.match.3.goal	same as instant.match.2
except goal only specifies
the 1x3 block
instant.match.3+90	instant.match.3+90.goal	same as instant.match.3
except rotated 90 degrees
no.move.1		no.move.1.goal.6	full tray that satisfies 
two-block goal
no.move.2		no.move.2.goal.6	ditto, with three-block goal
no.move.3		no.move.3.goal.6	ditto, with one-block goal
no.move.1		no.move.1.deadend.goal.6 full board, unsatisfiable goal
no.move.2		no.move.2.deadend.goal.6 ditto
no.move.3		no.move.3.deadend.goal.6 ditto, 1-block goal
one.move.1		one.move.1.deadend.goal.6 one move possible, but
goal inaccessible
small.search		small.search.goal	move several blocks to win
small.search+90		small.search+90.goal	rotated
tree			tree.goal		multiple block movements
tree+90			tree+90.goal		rotated 90 degrees
tree+180		tree+180.goal		rotated 180 degrees
tree+270		tree+270.goal		rotated 270 degrees