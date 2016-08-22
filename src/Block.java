import java.util.ArrayList;
import java.util.HashSet;


/**
 * Class Name: Block
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
 * A Block object that represents a block with a static width and height at a
 * row and column coordinate. * 
 */
public class Block 
{
	private int height; // height of the block
	private int width; // width of the block
	private int row; // row position of the block
	private int col; // column position of the block
	
	/**
	 * Block constructor that sets the parameters to their corresponding
	 * instance variables 
	 * 
	 * @param height: height of the block
	 * @param width: width of the block
	 * @param row: row position of the block
	 * @param col: column position of the block
	 */
	public Block( int height, int width, int row, int col )
	{
		this.height = height;
		this.width = width;
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Returns height of the block
	 * 
	 * @return height: height of the block
	 */
	public int getHeight( )
	{
		return height;
	}
	
	/**
	 * Returns width of the block
	 * 
	 * @return width: width of the block
	 */
	public int getWidth( ) 
	{
		return width;
	}
	
	/**
	 * Returns x-coordinate of the block
	 * 
	 * @return x: x-coordinate
	 */
	public int getRow( )
	{
		return row;
	}
	
	/**
	 * Returns the y-coordinate of the block
	 * 
	 * @return y: y-coordinate
	 */
	public int getCol( )
	{
		return col;
	}
	
	/**
	 * Returns the area of the block
	 * 
	 * @return area of the block
	 */
	public int getArea( )
	{
		return row * col; 
	}
	
	/**
	 * Gives new x and y coordinates to the block. The x and y coordinates
	 * define the upper left corner of the block.
	 * 
	 * @param x: x-coordinate
	 * @param y: y-coordinate
	 */
	public void setCoordinates( int row , int col )
	{
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Returns an array list of coordinate spaces adjacent to this block
	 * 
	 * @return: adjacentSpaces
	 */
	public ArrayList<Coordinates> getAdjacentSpaces( )
	{
		// HashSet to keep track of the adjacent spaces
		HashSet<Coordinates> adjacentSpaces = new HashSet<Coordinates>( );
		
		// Iterate through the spaces to the left and right of the block
		for( int i = 0; i < height; i++ )
		{
			int r = i + row; // The current row of the coordinate
			int c1 = col - 1; // The column left of the block
			int c2 = col + width; // The column right of the block
			
			// If the values of coordinate space to the left of the block 
			// are positive...
			if( r >= 0 && c1 >= 0 )
			{
				// Add the coordinate space to the hash set
				adjacentSpaces.add( new Coordinates( r, c1 ) );
			}
			// If the values of coordinate space to the right of the block 
			// are positive...
			if( r >= 0 && c2 >= 0 )
			{
				// Add the coordinate space to the hash set
				adjacentSpaces.add( new Coordinates( r, c2 ) );				
			}
		}		
		// Iterate through the spaces above and below the block
		for( int i = 0; i < width; i++ )
		{
			int r1 = row - 1; // The row above the block
			int r2 = row + height; // The row below the block
			int c = i + col; // The current column of the coordinate
			
			// If the values of coordinate space above the block are positive
			if( r1 >= 0 && c >= 0 )
			{
				// Add the coordinate space to the hash set
				adjacentSpaces.add( new Coordinates( r1, c ) );
			}
			// If the values of coordinate space below the block are positive
			if( r2 >= 0 && c >= 0 )
			{
				// Add the coordinate space to the hash set
				adjacentSpaces.add( new Coordinates( r2, c ) );				
			}
		}		
		// Return adjacentSpaces in the form of an array list of coordinates
		return new ArrayList<Coordinates>( adjacentSpaces );
	}
	
	/**
	 * Checks if another block has the same coordinate and size as this block
	 * 
	 * @param b: another block
	 * @return true if the blocks are the same, else otherwise
	 */
	@Override
	public boolean equals( Object o )
	{
		// Check if object o is a block
		if( !( o instanceof Block ) )
		{
			return false;
		}
		// Cast object o to block
		Block b = ( Block ) o;
		
		// Check if this block and block b have the same dimensions and the
		// same coordinates
		if ( height == b.getHeight( ) && width == b.getWidth( ) 
				&& row == b.getRow( ) && col == b.getCol( ) )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a hashCode for the block
	 * 
	 * @return hashCode for the block
	 */
	@Override
	public int hashCode( )
	{
		return getArea( ) * ( row * 3 + col * 5 );
	}
	
	/**
	 * ToString method of the block
	 */
	@Override
	public String toString( ) 
	{
		return height + " " + width + " " + row + " " + col;
	}
	
	/**
	 * Main that tests the functionality of Block
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		// A new block with arbitrary values
		Block b1 = new Block( 1, 2, 3, 4 );
		
		// Returns height
		System.out.println( "Height: " + b1.getHeight( ) );
				
		// Returns width
		System.out.println( "Width: " + b1.getWidth( ) );
		
		// Gets block area
		System.out.println( "Area: " + b1.getArea( ) );
		
		// Returns coordinates
		System.out.println( "Coodinate: (" + b1.getRow( ) + ", " 
				+ b1.getCol( ) + ")" );
		
		// Sets new coordinates
		b1.setCoordinates( 5, 6 );
		
		// Returns new coordinates
		System.out.println( "New coodinate: (" + b1.getRow( ) + ", " 
				+ b1.getCol( ) + ")" );
		
		// Print's block's hash value
		System.out.println( b1.hashCode() + "\n" );
		
		Block b2 = new Block( 1, 2, 5, 6 );
		
		// Print Block 1
		System.out.println( "Block 1: " + b1 );
		// Print Block 2
		System.out.println( "Block 2: " + b2 );
		
		// Compares whether two blocks are equivalent
		System.out.println( "Block's equivalent: " + b1.equals( b2 ) + "\n" );
		
		// Print the adjacent coordinate spaces to b1
		System.out.println( "Spaces adjacent to b1: " );
		for( Coordinates c: b1.getAdjacentSpaces( ) )
		{
			System.out.println( c );
		}
	}
}
