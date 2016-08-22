import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class Name: Board
 * 
 * @author Chang, Kevin
 * SSID: 26075225
 * 
 * Session: Spring 2016
 * Due Date: April 29, 2016
 * Class Num: CS 47B
 * Project: Block Project
 * 
 * OS: OSX Yosemite
 * Compiler: Eclipse Luna 4.4.0
 * 
 * A Board object that represents a Board of non-overlapping Blocks that can 
 * slide around as long as they do not collide with other blocks
 */
public class Board implements Comparable< Board >
{
	private Block [][] board; // 2D array that keeps track of the position of 
							  // the blocks.
	private Board parent; // The Board's parent.
	private String stepFromParent; // A string representation of the step taken
								   // to go from the parent to this block.
	
	// An array list of blocks that keeps track of the goal
	private ArrayList<Block> goal = new ArrayList<Block>( );
	
	// A hash set of blocks that keeps track of the blocks in the board
	private HashSet<Block> bContent = new HashSet<Block>( );
	
	// A hash set of coordinates that keeps track of the empty spaces in the 
	// board that are adjacent to the blocks in bContent
	private HashSet<Coordinates> adjEmptySpaces = new HashSet<Coordinates>();
	
	/**
	 * Board Constructor that takes in the input file name and constructs a 2D
	 * array from the file.
	 * 
	 * @param file: intput file
	 * @param g: ArrayList of goal blocks
	 */
	public Board( String file, ArrayList<Block> g )
	{
		// Reads file and creates a corresponding board
		readFile( file );
		// Sets the list of goal blocks
		goal = g;
		// Sets the list of blocks in the board
		setContent( );
		// Sets the adjacent empty spaces
		setAdjEmptySpaces( );
	}

	/**
	 * Board Constructor that takes in the a 2D array of blocks and makes a 
	 * deep clone of it.
	 * 
	 * @param board: source board
	 */
	private Board( Block [][] board, ArrayList<Block> g )
	{
		// Instantiates the board
		this.board = new Block[ board.length ][ board[ 0 ].length ];
		// Creates a deep clone of the board
		for( int i = 0; i < board.length; i ++ )
		{
			for( int j = 0; j < board[ 0 ].length; j++ )
			{
				if( board[ i ][ j ] != null )
				{
					// Deep Clone of the block at i, j
					Block b = new Block( board[ i ][ j ].getHeight( ), 
							board[ i ][ j ].getWidth( ), 
							board[ i ][ j ].getRow( ), 
							board[ i ][ j ].getCol( ) );
					this.board[ i ][ j ] = b;
				}
			}
		}
		// Sets the list of goal blocks
		goal = g;
		// Sets the list of blocks in the board
		setContent( );
	}

	/**
	 * Reads the file and constructs an initial board according to the 
	 * instructions in the file
	 */
	private void readFile( String file )
	{
		// A buffered reader that will read the input file
		BufferedReader br = null;
		// Attempt to create a buffered reader for the file
		try 
		{
			br = new BufferedReader( new FileReader( file ) );
		} 
		catch( FileNotFoundException e ) 
		{
			System.out.println ( "File Invalid" );
			e.printStackTrace( );
			return;
		}

		// String that will store the string of a line in the file
		String line;

		// Tries to iterate through file and create the board with blocks
		try
		{
			int counter = 1;
			// Reads the file until it reaches an empty line
			while( ( line = br.readLine( ) ) != null )
			{
				// The dimensions of the board is the first line
				if( counter == 1 )
				{
					// Create board with the given dimensions
					int space = line.indexOf( " " );
					int bHeight = Integer.parseInt( line.substring( 0 , space ) );
					int bWidth = Integer.parseInt( line.substring( space + 1 ) );
					board = new Block[ bHeight ][ bWidth ];
				}
				else
				{
					// Sets the height, width, and coordinates of a block
					int start;
					int end = line.indexOf( " " );
					int height = Integer.parseInt( line.substring( 0 , end ) );
					start = end + 1;
					end = line.indexOf( " ", start );
					int width = Integer.parseInt( line.substring( start , end ) );
					start = end + 1;
					end = line.indexOf( " ", start );
					int row = Integer.parseInt( line.substring( start , end ) );
					int col = Integer.parseInt( line.substring( end + 1 ) );
					addBlock(new Block( height, width, row , col ) );
				}
				counter++;
			}
		} 
		catch( IOException e ) 
		{
			e.printStackTrace( );
		}
	}

	/**
	 * Add a block to the board 
	 * 
	 * @param b: block to be added to the board
	 */
	private void addBlock(Block b)
	{
		// All the "spaces" on the board covered by the block will point to the
		// block.
		for( int row = 0; row < b.getHeight( ); row++ )
		{
			for( int col = 0; col < b.getWidth( ); col++ )
			{
				board[ row + b.getRow( ) ][ col+b.getCol( ) ] = b;
			}
		}
	}

	/**
	 * Attempts to move the block at (row, dir) in the direction specified in 
	 * the parameter dir. It then returns the state of the board after the 
	 * movement. The board state will not change if the movement is impossible.
	 * 
	 * @param row: row position of the block
	 * @param col: col position of the block
	 * @param dir: the direction to move the block
	 * @return the state of the board after moving the block
	 */
	public Board moveBlock ( int row, int col, int dir )
	{
		// Check if the input coordinates are within the board's boundary
		// If not, return the current board
		if( ( 0 > row || row >= board.length 
				|| 0 > col || col >= board[ 0 ].length ) 
				|| board[ row ][ col ] == null )
		{
			return new Board( board, goal );
		}

		// The board to be returned as not to affect the original.
		Board rBoard = new Board( board, goal );
		// A Block b to keep track of the block to be moved
		Block b = rBoard.board[ row ][ col ];
		// Block b's position, which will be manipulated to move the block
		int bRow = b.getRow( );
		int bCol = b.getCol( );

		// Change b's coordinates according to the direction input
		switch( dir % 360 )
		{
		case 0: bRow -= 1;
		break;
		case 90: bCol += 1;
		break;
		case 180: bRow += 1;
		break;
		case 270: bCol -= 1;
		break;
		}

		// Check if the block is inside the Board with the new coordinates
		if( 0 <= bRow && bRow < rBoard.board.length 
				&& 0 <= bCol && bCol < rBoard.board[ 0 ].length 

				&& 0 <= ( bRow + b.getHeight( ) - 1 ) 
				&& ( bRow + b.getHeight( ) - 1 ) < rBoard.board.length 
				&& 0 <= bCol && bCol < rBoard.board[ 0 ].length

				&& 0 <= bRow && bRow < rBoard.board.length 
				&& 0 <= ( bCol + b.getWidth( ) - 1 ) 
				&& ( bCol + b.getWidth( ) - 1 ) < rBoard.board[ 0 ].length

				&& 0 <= ( bRow + b.getHeight( ) - 1 ) 
				&& ( bRow + b.getHeight( ) - 1 ) < rBoard.board.length 
				&& 0 <= ( bCol + b.getWidth( ) - 1 ) 
				&& ( bCol + b.getWidth( ) - 1 ) < rBoard.board[ 0 ].length )
		{
			// Remove block to make sure there's no interference when 
			// considering overlap
			rBoard.removeBlock( b );
			
			// Boolean to test whether the block's new position will overlap 
			// with other blocks
			boolean overlap = false;
			for( int r = 0; r < b.getHeight( ); r++ )
			{
				for( int c = 0; c < b.getWidth( ); c++ )
				{
					// If the block overlaps, set overlap to true
					if( rBoard.board[ r + bRow ][ c + bCol ] != null )
					{
						overlap = true;
					}
				}
			}
			// If there is no overlap move the block by changing it's 
			// coordinates and placing it down in its new place
			if( overlap == false )
			{			
				b.setCoordinates( bRow, bCol );
				rBoard.addBlock( b );
			}
			else
			{
				// If there's overlap, put the block back that was initially removed.
				rBoard.addBlock( b );
			}
		}
		// Update rBoard's empty spaces
		rBoard.setAdjEmptySpaces();
		// Return the Board configuration after the movement. If the move was 
		// impossible there will be no change to the board.
		return rBoard;
	}

	/**
	 * Remove the block b from the board
	 * 
	 * @param b: block to be removed
	 */
	private void removeBlock( Block b )
	{
		// All the "spaces" on the board covered by the block will be set to 
		// null.
		for( int row = 0; row < b.getHeight( ); row++ )
		{
			for( int col = 0; col < b.getWidth( ); col++ )
			{
				board[ row + b.getRow( ) ][ col+b.getCol( ) ] = null;
			}
		}
	}

	/**
	 * Returns the array that stores the blocks
	 * 
	 * @return board: the 2D array representation of the board
	 */
	public Block[ ][ ] getArray( )
	{
		return board;
	}
	
	/**
	 * Adds the blocks in the board to bContent
	 */
	private void setContent( )
	{
		// Iterate through the spaces in the board
		for( int i = 0; i < board.length; i ++ )
		{
			for( int j = 0; j < board[ 0 ].length; j++ )
			{ 
				Block b = board[ i ][ j ];
				// If there is block at (i, j), add it to bContent
				if ( b != null)
				{					
					bContent.add( b );
				}
			}
		}
	}
	
	/**
	 * Return all the blocks in the board in the form of a hash set
	 * 
	 * @return bContent
	 */
	public HashSet<Block> getContent( )
	{
		return bContent;
	}
	
	/**
	 * Find all the empty spaces in the board that are adjacent to a block
	 */
	private void setAdjEmptySpaces( )
	{
		// Look at all the blocks in the board
		for( Block b: getContent() )
		{
			// Look at all the coordinates adjacent to block b
			for( Coordinates c: b.getAdjacentSpaces() )
			{
				int row = c.getRow( ); // The row of coordinate c
				int col = c.getCol( ); // The column of coordinate c
				
				// If the coordinate space is in the board and it is empty...
				if( row >= 0 && row < board.length 
						&& col >= 0 && col < board[ 0 ].length 
						&& board[ row ][ col ] == null )
				{
					// Add the coordinate to adjEmptySpaces
					adjEmptySpaces.add( c );					
				}
			}
		}
	}
	
	/**
	 * Return an ArrayList of all the empty spaces adjacent to a block
	 * 
	 * @return the empty spaces adjacent to a block
	 */
	public ArrayList<Coordinates> getAdjEmptySpaces( )
	{
		return new ArrayList<Coordinates>( adjEmptySpaces );
	}
	
	/**
	 * Returns a hash value of the board
	 * 
	 * @return hash: the hash value of the board
	 */
	@Override
	public int hashCode( )
	{
		return this.toString().hashCode();
	}
	
	/**
	 * Overrides the generic toString() method. Returns a string description 
	 * of all the blocks in the board
	 * 
	 * @return all the blocks in the board in the form of a string
	 */
	@Override
	public String toString()
	{
		String s = "";
		// Iterate through all the blocks and add their toString() to s
		for ( Block b: bContent )
		{
			s += b + ", ";
		}
		return s;
	}

	/**
	 * Set the Board's parent
	 * 
	 * @param p: parent
	 */
	public void setParent( Board p )
	{
		parent = p;
	}
	
	/**
	 * Return the Board's parent
	 * 
	 * @return parent;
	 */
	public Board getParent( )
	{
		return parent;
	}
	
	/**
	 * Set an instance variable to a string that tells which blocks were moved
	 * to get from the parent 
	 * 
	 * @param s: step
	 */
	public void setStep( String s )
	{
		stepFromParent = s;
	}
	
	/**
	 * Return a string telling what step it took to get from the board's parent
	 * to this board
	 * 
	 * @return: stepFromParent
	 */
	public String getStepFromParent( )
	{
		return stepFromParent;
	}
	
	/**
	 * Print the board
	 */
	public void print( ) 
	{
		// Iterates through the board and prints the blocks at each position
		// If there's no block, print an empty space.
		for( int i = 0; i < board.length; i ++ )
		{
			for( int j = 0; j < board[ 0 ].length; j++ )
			{
				if( board[ i ][ j ] == null )
				{
					System.out.print( "[       ] " );
				}
				else 
				{
					System.out.print( "[" + board[ i ][ j ] + "] " );
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Returns a score for the board. The score is based on how close the
	 * current board configuration is to the goal configuration. This is used
	 * in the compareTo() method and helps determine which board configurations
	 * in bQueue in the Solver should be considered first.
	 * 
	 * Generally, the lower the score, the closer the current configuration is
	 * to the goal configuration.
	 * 
	 * @return the score of the board
	 */
	public int score( )
	{	
		// An HashSet that keeps track of which blocks have been visited
		HashSet<Block> visited = new HashSet<Block>();

		// An array the keeps track of the score of each block
		int [] scores = new int[ goal.size() ];
		
		// Iterates through the goal array list
		for ( int i = 0; i < goal.size(); i++ ) 
		{
			// Sets the score of the current goal to a large integer
			scores[ i ] = 99999999;
			// Iterate through all the blocks in bContent
			for ( Block b: bContent ) 
			{
				// The coordinate values of Block b
				int bRow = b.getRow( );
				int bCol = b.getCol( );
				// The width and height of Block b
				int bWidth = b.getWidth( );
				int bHeight = b.getHeight( );
				
				// The coordinates value of the goal Block
				int gRow = goal.get( i ).getRow( );
				int gCol = goal.get( i ).getCol( );
				// The width and height of the goal Block
				int gWidth = goal.get( i ).getWidth( ) ;
				int gHeight = goal.get( i ).getHeight( );
				
				// If the two blocks have the same dimensions
				if ( bWidth == gWidth  && bHeight == gHeight )
				{
					// Set tempScore the the sum of the differences in each of
					// coordinate values
					int tempScore = ( int ) ( Math.abs( ( bCol - gCol ) ) 
							+ Math.abs( ( bRow - gRow ) ) );
					// If the block in bContent had not been considered before
					// and if the tempScore is less than the current score...
					if ( !visited.contains( b ) && tempScore < scores[ i ] ) 
					{
						// Set the current score to tempScore
						scores[i] = tempScore;
						// Add Block b to visited
						visited.add(b);
					}
				}
				// Multiply the current score by the goal block's width and 
				// height
				scores[i] = scores[i] * gWidth * gHeight;
			}
		}
		// Sum up the all the block scores
		int sum = 0;
		for (int i = 0; i < scores.length; i ++) 
		{
			sum = sum + scores[ i ] ;
		}
		// Return the sum
		return sum;
	}

	/**
	 * Compares whether two boards are equivalent
	 * 
	 * @param o: the object to be compared
	 */
	@Override
	public boolean equals( Object o )
	{
		// Checks if object o is a board
		if( !( o instanceof Board ) )
		{
			return false;
		}
		// Cast object o to board
		Board b = (Board) o;
		
		// Get the array of blocks of b
		Block[][] bBoard = b.getArray();
		// Checks if the two blocks have the same dimensions
		if( board.length != bBoard.length 
				|| board[0].length != bBoard[0].length )
		{
			System.out.println("dimensions");
			return false;
		}
		
		// Iterate through the coordinate spaces of both boards...
		for( int i = 0; i < board.length; i ++ )
		{
			for( int j = 0; j < board[ 0 ].length; j++ )
			{
				// If both spaces at (i ,j) are not empty...
				if( board[ i ][ j ] != null && bBoard[ i ][ j ] != null )
				{
					// If the blocks at both spaces are not equivalent, return
					// false
					if( !board[ i ][ j ].equals( bBoard[ i ][ j ] ) )
					{
						return false;
					}					
				}
				// If of of the spaces at (i ,j) is empty, return false
				else if( ( board[ i ][ j ] != null && bBoard[ i ][ j ] == null)
						||( board[ i ][ j ] == null && bBoard[ i ][ j ] != null ) )
				{
					return false;
				}
			}
		}
		// Return true if all the spaces in both boards are equivalent
		return true;
	}
	
	/**
	 * Compares two Boards and returns the difference in their scores
	 *
	 * @param b: the board to compare this board to
	 * 
	 * @return the difference in the two boards' scores
	 */
	@Override
	public int compareTo( Board b ) 
	{
		return Math.round( b.score() - this.score() );
	}
	
	/**
	 * Main that tests the functionality of Board
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		// A empty goal Array List to fulfill the constructor parameters
		ArrayList<Block> goal = new ArrayList<Block>( );
		
		// New board taking file input. This tests readFile(), addBlock() also.
		Board b1 = new Board( "big.block.1.txt", goal );
		// New board taking array input. This test getArray().
		Board b2 = new Board( b1.getArray(), goal );
		
		// Prints the information of B1
		System.out.println( "B1:" );
		b1.print( );
		System.out.println( "Blocks in B1: " );
		for( Block b: b1.getContent() )
		{
			System.out.println( b );
		}
		System.out.println( "\n" + "Empty Spaces in B1:" );
		for( Coordinates c: b1.getAdjEmptySpaces() )
		{
			System.out.println( c );
		}
		
		// Prints the information of B2
		System.out.println( "\nB2:" );
		b2.print( );
		System.out.println( "Blocks in B2: " );
		for( Block b: b2.getContent() )
		{
			System.out.println( b );
		}
		System.out.println( "\nEmpty Spaces in B2:" );
		for( Coordinates c: b2.getAdjEmptySpaces() )
		{
			System.out.println( c );
		}
		
		// Prints whether the two boards are equal
		System.out.println( "\nThe boards are equal: " + b1.equals( b2 ) );
		
		// Move block at (0, 0) down. This uses removeBlock()
		Board b3 = b1.moveBlock( 2, 0, 0 );
		// Prints the information of B3
		System.out.println( "\nB3: B1 after moving" );
		b3.print( );
		System.out.println( "Blocks in B3: " );
		for( Block b: b3.getContent() )
		{
			System.out.println( b );
		}
		System.out.println( "\nEmpty Spaces in B3:" );
		for( Coordinates c: b3.getAdjEmptySpaces( ) )
		{
			System.out.println( c );
		}
		
		// Print board's hashcode
		System.out.println( "B3 hash value: " + b3.hashCode( ) + "\n" );
		// Print board's hashcode
		System.out.println( "B2 hash value: " + b2.hashCode( ) + "\n" );
		
		System.out.println( "The boards are equal: " + b3.equals( b2 ) );
		
		// Set b2 as b1's parent.
		b3.setParent( b2 );
		
		// Get b1's parent and print
		System.out.println( "\nParent:" );
		b3.getParent().print();
	}
}
