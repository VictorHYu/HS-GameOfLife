// Jan 5 2016
// Victor Yu
// Life Simulation: simulator for Conway's game of life
// Mr. Jay
// 12 Computer Science

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import java.io.*;

class LifeSimulation extends JFrame
{
  //declare variables
  private static Colony colony = new Colony (0.5);
  public static Timer t;
  private static boolean running = false;
  private static JComboBox <Integer> delayBox, xStart, xEnd, yStart, yEnd; 
  private static JFileChooser file;
  private static LifeSimulation window
    ;
  
  //======================================================== constructor
  public LifeSimulation ()
  {
    // 1... Create/initialize components
    BtnListener btnListener = new BtnListener (); // listener for all buttons
    file = new JFileChooser();
    
    //create buttons and add listeners
    JButton simulateBtn = new JButton ("Simulate");
    JButton stopBtn = new JButton ("Stop");
    JButton popBtn = new JButton ("Populate");
    JButton eradBtn = new JButton ("Eradicate");
    JButton saveBtn = new JButton ("Save");
    JButton loadBtn = new JButton ("Load");
    simulateBtn.addActionListener (btnListener);
    stopBtn.addActionListener (btnListener);
    popBtn.addActionListener (btnListener);
    eradBtn.addActionListener (btnListener);
    loadBtn.addActionListener (btnListener);
    saveBtn.addActionListener (btnListener);
    
    //create comboboxes and use arrays to fill options
    Integer[] val = {50,100,200,1000};
    delayBox = new JComboBox <> (val);
    
    Integer[] val2 = new Integer [100]; //create array with 1-100 in it
    for (int i = 0; i < 100; i ++)
      val2[i] = i + 1;
    
    //initialize comboboxes with options 1-100
    xStart = new JComboBox <> (val2);
    xEnd = new JComboBox <> (val2);
    yStart = new JComboBox <> (val2);
    yEnd = new JComboBox <> (val2);
    
    // 2... Create content pane, set layout
    JPanel content = new JPanel ();        // Create a content pane
    content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
    JPanel north = new JPanel ();
    north.setLayout (new FlowLayout ()); // Use FlowLayout for input area
    JPanel south = new JPanel ();
    south.setLayout (new FlowLayout ()); // Use FlowLayout for input area
    JPanel center = new JPanel ();
    center.setLayout (new FlowLayout());
    
    DrawArea board = new DrawArea (500, 500);
    board.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // 3... Add the components to the input area.
    north.add (simulateBtn);
    north.add (new JLabel ("Delay (ms):"));
    north.add (delayBox);
    north.add (stopBtn);
    north.add (saveBtn);
    north.add (loadBtn);
    
    south.add (new JLabel ("X Start:"));
    south.add (xStart);
    south.add (new JLabel ("X End:"));
    south.add (xEnd);
    south.add (new JLabel ("Y Start:"));
    south.add (yStart);
    south.add (new JLabel ("Y End:"));
    south.add (yEnd);
    south.add (popBtn);
    south.add (eradBtn);
      
    center.add (board);
    
    content.add (north, "North"); // Input area
    content.add (center, "Center"); // Output area
    content.add (south, "South");
    
    // 4... Set this window's attributes.
    setContentPane (content);
    pack ();
    setTitle ("Life Simulation");
    setSize (600 + 200, 570);
    setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo (null);           // Center window.
    setResizable(false);
  }
  
  class BtnListener implements ActionListener 
  {
    public void actionPerformed (ActionEvent e)
    {
      if (e.getActionCommand ().equals ("Simulate"))
      {
        if (t != null)
          t.stop(); //stop old timer if applicable
        Movement moveColony = new Movement (); // ActionListener for timer
        t = new Timer ((int) delayBox.getSelectedItem() , moveColony); // set up Movement to run on specified delay
        t.start (); // start simulation
        running = true; //update status
      }
      else if (e.getActionCommand ().equals ("Stop"))
      {
        if (running == true) //if running
        {
          t.stop(); //stop timer
          running = false; //update status
        }
      }
      else if (e.getActionCommand ().equals ("Populate"))
      {
        //check validity of combobox options
        if ((int) xEnd.getSelectedItem() > (int) xStart.getSelectedItem() && 
            (int) yEnd.getSelectedItem() > (int) yStart.getSelectedItem() )
          //use colony's populate method
          colony.populate((int) xStart.getSelectedItem(), (int) yStart.getSelectedItem(), 
                          (int) xEnd.getSelectedItem(), (int) yEnd.getSelectedItem());
      }
      else if (e.getActionCommand ().equals ("Eradicate"))
      {
        //check validity of combobox options
        if ((int) xEnd.getSelectedItem() > (int) xStart.getSelectedItem() && 
            (int) yEnd.getSelectedItem() > (int) yStart.getSelectedItem() )
          //use colony's eradicate method
          colony.eradicate((int) xStart.getSelectedItem(), (int) yStart.getSelectedItem(),
                           (int) xEnd.getSelectedItem(), (int) yEnd.getSelectedItem());
      }
      else if (e.getActionCommand ().equals ("Save"))
      {
        int result; //saves result from chooser
        result = file.showSaveDialog(window);
        
        if (result == JFileChooser.APPROVE_OPTION) //choice approved by filechooser
        {
          colony.save(file.getSelectedFile().getAbsolutePath()); //calls method with filepath chosen
        }
      }
      else if (e.getActionCommand ().equals ("Load"))
      {
        int result; //saves result from chooser
        result = file.showOpenDialog(window);
        
        if (result == JFileChooser.APPROVE_OPTION) //choice approved by filechooser
        {
          colony.load(file.getSelectedFile().getAbsolutePath()); //calls method with filepath chosen
        }
      }
      repaint ();            // refresh display of colony
    }
  }
  
  //handles GUI graphics area that displays all the cells
  class DrawArea extends JPanel
  {
    public DrawArea (int width, int height)
    {
      this.setPreferredSize (new Dimension (width, height)); // size
    }
    
    public void paintComponent (Graphics g)
    {
      colony.show (g); // display current state of colony
    }
  }
  
  //class for timer 
  class Movement implements ActionListener
  {
    public void actionPerformed (ActionEvent event)
    {
      colony.advance (); // advance to the next time step
      repaint (); // refresh 
    }
  }
  
  //======================================================== method main
  public static void main (String[] args)
  {
    window = new LifeSimulation ();
    window.setVisible (true);
  }
}

