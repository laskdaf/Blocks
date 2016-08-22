/**
 * Class Name: Coordinates
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
 * A Coordinate object that represents a two dimensional coordinate space
 */
public class Coordinates 
{
	private int row; // row position
	private int col; // column position
	
	/**
	 * Coordinate's constructor that sets its row and column values
	 * 
	 * @param row: row of the coordinate
	 * @param col: column of the coordinate
	 */
	public Coordinates( int row, int col )
	{
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Returns the row value of the coordinate
	 * 
	 * @return row: row of the coordinate
	 */
	public int getRow( )
	{
		return row;
	}
	
	/**
	 * Sets the row value of the coordinate
	 * 
	 * @param row: row of the coordinate
	 */
	public void setRow( int row )
	{
		this.row = row;
	}
	
	/**
	 * Gets the column value of the coordinate
	 * 
	 * @return col: column of the coordinate
	 */
	public int getCol( )
	{
		return col;
	}
	
	/**
	 * Sets the column value of the coordinate
	 * 
	 * @param col: column of the coordinate
	 */
	public void setCol( int col )
	{
		this.col = col;
	}
	
	/**
	 * Checks whether two coordinates are equivalent
	 * 
	 * @return true if the coordinates are the same, false otherwise
	 */
	@Override
	public boolean equals( Object o )
	{
		// Checks if object o is a coordinate
		if( !( o instanceof Coordinates ) )
		{
			return false;
		}
		// Casts object o to a coordinate
		Coordinates c = ( Coordinates ) o;
			
		// Returns true if the two coordinate have the same values
		if( row == c.getRow() && col == c.getCol() )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the hash code of the coordinate
	 * 
	 * @return the hash code of the coordinate
	 */
	@Override
	public int hashCode( )
	{
		String s = "";
		// Concats row and col to s
		s += row;
		s += col;
		// Change s to an integer and return
		return Integer.parseInt( s );
	}
	
	/**
	 * The toString method of Coordinate
	 * 
	 * @return "row, col"
	 */
	@Override
	public String toString( )
	{
		String s = "";
		s += row + ", ";
		s += col;
		return s;
	}
	
	/**
	 * A main method that tests the functionality of the class' methods
	 * 
	 * @param args
	 */
	public static void main( String [] args )
	{
		// A new coordinate
		Coordinates c1 = new Coordinates( 1, 2 );
		// Test toString()
		System.out.println( "Coordinate"
				+ ": " + c1 );
		// Test getRow()
		System.out.println( "Row: " + c1.getRow() );
		// Test getCol()
		System.out.println( "Col: " + c1.getCol() );
		// Test hashCode()
		System.out.println( "Hash code: " + c1.hashCode() );
		System.out.println( );
		
		// Sets c1 to a new coordinate
		c1.setRow( 3 );
		c1.setCol( 4 );
		
		// A second coordinate c2 at the same location as c1
		Coordinates c2 = new Coordinates( 3, 4 );
		
		// Print the two coordinate
		System.out.println( "Coordinate 1: " + c1 );
		System.out.println( "Coordinate 2: " + c2 );
		
		// Test if c1 and c2 are equivalent
		System.out.println( "Coordinates 1 and 2 are equal: " 
				+ c1.equals( c2 ) );
	}
}
