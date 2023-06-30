import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
   static Simulation[] sim;
	static JLabel timeLabel;
	static volatile boolean checkStart = false;
	static volatile boolean timeStart = false;
	static volatile boolean restart = false;
	static volatile boolean step = true;
	static volatile int count = 0;

	// start timer
	/*private*/ static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Terrain landdata) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      JPanel g = new JPanel();
      g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
      fp.addMouseListener(new WaterclickListener(fp));
		g.add(fp);
	  
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		JPanel b = new JPanel();
	   b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton endB = new JButton("End");;
      JButton startButton = new JButton("Start");;
      JButton resetButton = new JButton("Reset");;
      JButton pauseButton = new JButton("Pause");;
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
			}
		});
      startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

            //int x = fp.xpoint;
				//int y = fp.ypoint;
				if (!(checkStart)) {
					sim = new Simulation[4];
					

					int size = landdata.dim();
					int quarter = size / 4;
					int lo;
					int hi;
					// Creating the four threads
					for (int i = 0; i < 4; i++) {
						lo = i * quarter;
						hi = i * quarter + quarter;
						sim[i] = new Simulation(landdata, lo, hi);
						sim[i].start();
					}
					tock();
					checkStart = true;
					timeStart = true;
				} else {
					timeStart = true;
					for (int i = 0; i < 4; i++) {
						sim[i].unpause();
					}
					 
					fp.unpause();
				}
            //fp.start();

			}

		});
      
      resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// to do ask threads to reset
				for (int i = 0; i < 4; i++) {
					sim[i].pause();
				}
				for (int i = 0; i < 4; i++) {
					sim[i].restart();
				}

				fp.pause();
				fp.restart();

			}
		});
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// to do ask threads to pause
            startButton.setText("Play");
				timeStart = false;
				for (int i = 0; i < 4; i++) {
					sim[i].pause();
					;
				}
				fp.pause();
			}
		});
      b.add(endB);
      b.add(startButton);
      b.add(pauseButton);
      b.add(resetButton);
      //timeLabel = new JLabel("0");
      //b.add(timeLabel);
		g.add(b);
      
		
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on simreen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
	}
	public static void setFalse (){
		step = false;
	}

	public static void setTrue(){
		step = true;
	}
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			//System.exit(0);
		}
				
		// landsimape information from file supplied as argument
		// 
		//landdata.readData(args[0]);
		landdata.readData(args[0]);
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));
		int counter = 0;
		setTrue();
		while (step) {
			
			if (checkStart) {
				if (sim[0].getflag() == true || sim[1].getflag() == true || sim[2].getflag() == true//start simulation
						|| sim[3].getflag() == true) {
							counter++;

					try {
						sim[0].join();//join simulation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						sim[1].join();//join simulation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						sim[2].join();//join simulation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						sim[3].join();//join simulation
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
							
				}
			setFalse();
			}}
		
	}

