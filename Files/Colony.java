import java.awt.*;
import java.io.*;

class Colony
{
  //create 2d array
  private boolean grid[] []; 
  
  //constructor to create colony
  public Colony (double density)
  {
    grid = new boolean [100] [100];
    for (int row = 0 ; row < grid.length ; row++)
      for (int col = 0 ; col < grid [0].length ; col++)
      grid [row] [col] = Math.random () < density;
  }
  
  public void show (Graphics g)
  {
    for (int row = 0 ; row < grid.length ; row++)
      for (int col = 0 ; col < grid [0].length ; col++)
    {
      if (grid [row] [col]) // life
        g.setColor (Color.black);
      else
        g.setColor (Color.white);
      g.fillRect (col * 5 + 2, row * 5 + 2, 5, 5); // draw life form
    }
  }
  
  //checks whether a cell is alive or dead. if out of bounds, returns dead.
  private boolean checkStatus (int row, int col)
  {
    try //try catch to avoid out of bounds
    {
      if (grid[row][col] == true) //if cell live
        return true; //return live
      else
        return false; //else cell dead
    }
    catch (Exception e) 
    {
      return false; //if out of bounds it is equivalent to dead
    }
  }
  
  //returns true if alive, false if dead after next advance
  private boolean live (int row, int col)
  {
    int counter = 0; //counts number of live cells around selected cell
    boolean current; //tracks current cell status
    
    //check 8 cells around it
    if (checkStatus(row - 1, col - 1))
      counter++;
    if (checkStatus(row - 1, col))
      counter++;
    if (checkStatus(row - 1, col + 1))
      counter++;
    if (checkStatus(row + 1, col - 1))
      counter++;
    if (checkStatus(row + 1, col))
      counter++;
    if (checkStatus(row + 1, col + 1))
      counter++;
    if (checkStatus(row, col + 1))
      counter++;
    if (checkStatus(row, col - 1))
      counter++;
    
    current = checkStatus(row,col); //find if current alive or dead
    
    //if current cell is dead, it lives with 3 living neighbours
    if (current == false)
    {
      if(counter == 3) //new cell born
        return true;
      else
        return false; //remains dead
    }
    
    else //current cell alive
    {
      //if <2, it dies
      if (counter < 2)
        return false;
      //if 2-3, it lives
      else if (counter == 2 || counter == 3)
        return true;
      //if 3+, it dies
      else 
        return false;
    }                    
  }
  
  public void advance () //uses live() to determine next iteration of grid
  {
    //create temporary array
    boolean temp [] [] = new boolean [100] [100];
    //scan through array and determine new statuses, put in temporary array
    for (int row = 0 ; row < 100 ; row++)
    {
      for (int col = 0 ; col < 100 ; col++)
        temp[row][col] = live(row,col);
    }
    //replace array with temporary
    grid = temp;
  }
  
  //kills 80% of cells in area
  public void eradicate(int x, int y, int xEnd, int yEnd)
  { 
    for (int i = x - 1; i < xEnd; i++ ) //loops through x specified
    {
      for (int j = y - 1; j < yEnd; j++) //loops through y specified
      {
        if (Math.random() < 0.8) //80% chance
        {
          if (i < 100 && j < 100)
            grid[i][j] = false; //kill cell
        }
      }
    }
  }
  
  //fills 80% of area with cells
  public void populate(int x, int y, int xEnd, int yEnd)
  {
    for (int i = x - 1; i < xEnd; i++ ) //loops through x specified
    {
      for (int j = y - 1; j < yEnd; j++) //loops through y specified
      {
        if (Math.random() < 0.8) //80% chance
        {
          grid[i][j] = true; //add cell
        }
      }
    }
  }
  
  //saves current position into text file
  public void save (String fname)
  {
    try {
      
      //add extension if not there
      if(fname.substring(fname.length() - 4,fname.length()) != ".txt")
        fname = fname + ".txt";
      
      FileWriter fw = new FileWriter (fname);
      PrintWriter fileout = new PrintWriter (fw);
       
      //loop through whole grid row by row
      for (int row = 0 ; row < 100; row++)
      {
        for (int col = 0; col < 100; col++)
        {
          if (checkStatus(row,col)) //if alive
            fileout.print("1"); //print 1
          else
            fileout.print("0"); //print 0
        }
        fileout.println(); //next row
      }
      fileout.close (); // close file
         
    }
    catch (IOException e) {
      System.out.println("Save Error");
    }
  }
  
  //loads scenario from a text file
  public void load(String fname)
  {
    try {
      
      //wipe current array, fill with dead;
      grid = new boolean [100] [100];
      for (int r = 0 ; r < grid.length ; r++)
        for (int c = 0 ; c < grid [0].length ; c++)
        grid [r] [c] = false;
      
      int row = -1; //tracks row
      
      //create reader
      FileReader fr = new FileReader (fname);
      BufferedReader filein = new BufferedReader (fr);
      
      String line;
      while ((line = filein.readLine ()) != null) // file has not ended
      {
        row ++;
        if (line.length() <= 100) //watch for input that is too long
        {
          for (int col = 0; col < line.length(); col++) //check through all of line
          {
            if (line.charAt(col) == '1') //if there is a 1, make cell live
              grid[row][col] = true;
          }
        }
      }
      filein.close (); // close file
    }
    catch (IOException e2) {
      System.out.println("Load Error");
    }
  }
}



