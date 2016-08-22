import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Class Name: Solver
 * 
 * kevinhuynh760@gmail.com
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
 * The Solver class attempts to manipulate a the blocks in a board with a 
 * starting configuration determined by an input file until the board's
 * configuration is the same as the blocks given by a goal file.
 */
public class Solver 
{
	private Board currBoard; // The current board being analyzed

	// A list of Blocks describing the configuration of blocks that the goal 
	// should have
	private ArrayList<Block> goal = new ArrayList<Block>( );

	// A hashset that keeps track of the board configurations that have been
	// attained
	private HashSet<Board> hash = new HashSet<Board>();

	// A queue of boards that keeps track of the board configurations that
	// should be analyzed
	private PriorityQueue<Board> bQueue = new PriorityQueue<Board>( );
	
	// Debugging variables
	private static String options = "-ooption";
	static int DEBUGOP = 0;

	/**
	 * Solver constructor that takes in a debugger boolean, input file, and 
	 * target file. This sets up the starting board and ending goal so a
	 * solution can be attempted to be found.
	 * 
	 * @param debug: boolean to determine whether to print debug messages
	 * @param inputFile: file describing the starting state of the board
	 * @param targetFile: file describing the goal state of the board
	 */
	public Solver( String inputFile, String targetFile )
	{
		setGoal( targetFile );

		currBoard = new Board( inputFile, goal );

		hash.add( currBoard );
		//		hash.put( currBoard.hashCode(), currBoard );
		bQueue.add( currBoard );
	}

	/**
	 * Read the goal file, and set up the goal array to be verified.
	 * 
	 * @param file: goal file
	 */
	private void setGoal ( String file )
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
			// Reads the file until it reaches an empty line
			while( ( line = br.readLine( ) ) != null )
			{
				// Get the the height, width,and coordinates of a block
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
				goal.add( new Block( height, width, row , col ) );
			}
		} 
		catch( IOException e ) 
		{
			e.printStackTrace( );
		}
	}

	/**
	 * Generates the next possible boards from the current board and adds them
	 * to the queue if they are new configurations
	 */
	private void generateNextBoards( Board board ) 
	{
		// Consider all the empty spaces in the board adjacent to blocks
		for( Coordinates c: board.getAdjEmptySpaces( ) )
		{
			for( int dir = 0; dir <= 270; dir += 90 )
			{
				int rTemp = c.getRow();
				int cTemp = c.getCol();
				// Based on the direction, move the row and column 
				// position in the adjacent space in that direction
				switch( dir % 360 )
				{
				case 0: rTemp -= 1;
				break;
				case 90: cTemp += 1;
				break;
				case 180: rTemp += 1;
				break;
				case 270: cTemp -= 1;
				break;
				}
				// Create a new board by moving the block at 
				// (rTemp, cTemp) in the direction of the empty space
				// Board b will be equal to the current board if the 
				// move was impossible
				Board b = board.moveBlock( rTemp, cTemp, 
						( ( dir + 180 ) % 360 ) );

				// If Board b has not been "visited before" add it to 
				// hash and bQueue
				if( hash.add( b ) )
				{
					// Check if b is the goal
					isGoal ( b );
					// Add b to bQueue
					bQueue.add( b );
					// Set b's parent to board
					b.setParent( board );
					// Set the step it took to get from board to b
					b.setStep( ( rTemp + " " + cTemp + " " 
							+ c.getRow() + " " + c.getCol() ) );
				}
			}
		}
	}

	/**
	 * Checks if this board has reached the goal
	 * 
	 * @param board: board to be considered
	 * @return true if goal reached, false otherwise.
	 */
	private boolean isGoal( Board board )
	{
		// For all the blocks in the goal list
		for ( Block tBlock : goal )
		{
			// Get the block in the same position as tBlock
			Block bBlock 
			= board.getArray( )[ tBlock.getRow() ][ tBlock.getCol( )];
			// If there's no block in the same position or if the blocks are
			// not equal, return false
			if ( bBlock == null || !bBlock.equals(tBlock) )
			{
				return false;
			}
		}
		return true;

	}

	/**
	 * Attempt to solve the block and attain a path from the source board
	 * configuration to the goal configuration.
	 */
	public void solve( )
	{
		System.out.println( "Starting Configuration: " );
		bQueue.peek().print();
		System.out.println("==============================================\n");
		
		// While the queue of boards to be considered still has boards and the
		// target configuration has not been acquired
		while ( !bQueue.isEmpty( ) && !isGoal( currBoard ) )
		{
			// Take next board from the queue, generate the next possible board
			// configurations and add them to the end of the queue
			currBoard = bQueue.remove();
			// Print Debug options
			if ( DEBUGOP == 3 )
			{				
				System.out.println(currBoard);
			}
			if ( DEBUGOP == 4 )
			{				
				System.out.println( "Queue size: " + bQueue.size() );
			}
			if ( DEBUGOP == 5 )
			{				
				System.out.println( "Number visited: " + hash.size() );
			}
			if ( DEBUGOP == 6 )
			{				
				System.out.println( "Hash Code: " + currBoard.hashCode() );
			}
			generateNextBoards( currBoard );
		}

		// A stack to help display the path of the solution in the correct order
		Stack<Board> bStack = new Stack<Board>( );
		Board tempBoard = currBoard;
		// Iterate through the Board tree to the root, adding each board to the 
		// stack so the moves can be displayed in the correct order
		while ( tempBoard != null )
		{
			bStack.add( tempBoard );
			tempBoard = tempBoard.getParent();
		}

		int c = 0;
		// Print the moves taken to attain the goal board configuration.
		System.out.println( "Steps Taken: " );
		while ( !bStack.isEmpty() )
		{
			Board b = bStack.pop();
			if ( c++ != 0 )
			{				
				System.out.println( b.getStepFromParent( ) );
			}
			if ( DEBUGOP == 2 )
			{				
				b.print();
			}
		}

		// Print a message, showing whether the goal has been attained.
		if ( isGoal( currBoard ) )
		{
			System.out.println( "\nGoal Reached!\n" );
			System.out.println("==============================================");
			System.out.println( "Ending Configuration: " );
			currBoard.print();
		}
		else
		{
			System.out.println( "Puzzle is impossible. :(" );
			System.exit(1);
		}
	}
	
	/**
	 * Prints the debugging options of Solver
	 */
	public static void printDebugOptions( )
	{
		System.out.println ( "Available debugging options" );
		System.out.println ( "- o1: print runtime" );
		System.out.println ( "- o2: print configurations between after each "
				+ "move" );
		System.out.println ( "- o3: print Board being considered" );
		System.out.println ( "- o4: print the size of the queue" );
		System.out.println ( "- o5: print the number of configurations "
				+ "visited" );
		System.out.println ( "- o6: print Board bein considered's hash code" );
	}

	/**
	 * Main method that solves the puzzle and tests the functionality of the
	 * Solver class
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Strings to store the starting and goal files
		String start = "";
		String goal = "";

		// Check if the number of arguments is correct
		// Print an error if incorrect and exit
		if( args.length != 2 && args.length != 3 ) 
		{
			System.err.println ( "Incorrect number of arguments." );
			System.exit (1);
		}
		// If the number of arguments is 2 set start and goal to arguments 0
		// and 1 respectively
		else if (args.length == 2) 
		{
			start = args[ 0 ];
			goal = args[ 1 ];
		}
		// If the number of arguments is 3
		else if( args.length == 3 ) 
		{
			// Set start and goal to arguments 1 and 2 respectively
			start = args[ 1 ];
			goal = args[ 2 ];
			
			// If the options argument equals "-ooptions," print the
			// available debugging options and return
			if ( args[ 0 ].equals( options ) ) 
			{
				printDebugOptions();
				return;
			}
			// Set DEBUGGING to an integer corresponding to a debugging option
			else if( args[ 0 ].equals( "-o1" ) )
			{
				DEBUGOP = 1;
			}
			else if( args[ 0 ].equals( "-o2" ) ) 
			{
				DEBUGOP = 2;
			}
			else if( args[ 0 ].equals( "-o3" ) ) 
			{
				DEBUGOP = 3;
			}
			else if( args[ 0 ].equals( "-o4" ) ) 
			{
				DEBUGOP = 4;
			}
			else if( args[ 0 ].equals( "-o5" ) ) 
			{
				DEBUGOP = 5;
			}
			else if( args[ 0 ].equals( "-o6" ) ) 
			{
				DEBUGOP = 6;
			}
			// If the options arguments is not one of the above, return an 
			// error
			else 
			{
				System.err.println ( args[ 0 ].toString( ) 
						+ " is not an option" );
				System.exit( 1 );
			}
		}
		
		// Start the timer
		long startTime = System.currentTimeMillis();
		// Create a new solver with the start and goal configurations
		Solver solver = new Solver( start ,  goal );
		// Solve the puzzle!
		solver.solve( );
		
		// Print the time taken to sovle the puzzle
		if( DEBUGOP == 1 )
		{
			System.out.println( "\nTime taken: " 
					+ ( ( System.currentTimeMillis() - startTime ) / 1000.0 ) 
					+ " seconds");			
		}
	}
}
