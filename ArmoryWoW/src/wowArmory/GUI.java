/**
 /**
 * @author Brennen Davis
 */

package wowArmory;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class GUI implements PropertyChangeListener{
	private static final int WIDTH = 640;
	
	private JFrame frame;
			
	private Execute execute;
	
	private String[] playerInfo;
	ArrayList<JFormattedTextField> txtFields;
	
	private JFormattedTextField[] playerTextFields;
	
	private String fileName = null;
	private String errorMessage = "";
	private JFormattedTextField txtFileName;
	
	private JLabel lblFileNameError;
	private JLabel lblErrorMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//set up the WebDriver before the frame starts
		System.setProperty("phantomjs.binary.path", "src/wowArmory/phantomjs.exe");
		//System.setProperty("webdriver.chrome.driver", "src/wowArmory/chromedriver.exe");
		
		ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
		ArrayList<Thread> playerThreads = new ArrayList<Thread>();

		frame = new JFrame();
		frame.setBounds(100, 100, WIDTH, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("Closing any used drivers.");
				for(WebDriver driver : drivers){
					driver.quit();
				}
				
			}
		});
		
		GUISetup setup = new GUISetup();
		setup.setLocation(frame.getX() + 150, frame.getY() + 100);
		setup.setVisible(true);
		setup.setAlwaysOnTop(true);
		
		

		execute = new Execute();
		
		//int numberOfLblAndTxt = setup.getNumberOfPlayers();
		int numberOfLblAndTxt = 12;
		
		playerInfo = new String[numberOfLblAndTxt * 2];
		txtFields = new ArrayList<JFormattedTextField>();
		
		topHalf(numberOfLblAndTxt * 2);
		bottomHalf(drivers, playerThreads);
		
	}
	
	private void getStats(ArrayList<WebDriver> drivers, ArrayList<Thread> playerThreads){
		//clear the error message before getting stats
		errorMessage = "";
		lblFileNameError.setText(null);
		
		if(fileName == null){
			lblFileNameError.setText("NO FILE NAME GIVEN!");
			return;
		}
		
		for(int i = 0; i < playerInfo.length; i += 2){
			//these ints are acceptably in scope of the Thread
			int f = i;
			int j = i + 1;
			int k;
			if(i > 0){
				k = i / 2;
			}else{
				k = i;
			}
			
			if(playerInfo[i] != null && playerInfo[j] != null){
				playerThreads.add(new Thread(new Runnable(){
					@Override
					public void run() {
						//drivers.add(new ChromeDriver());
						drivers.add(new PhantomJSDriver());
						try{
							Thread.sleep(2000);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						
						String playerOneError = execute.pullElements(drivers.get(k), "http://us.battle.net/wow/en/character/" 
								+ playerInfo[f] + "/" + playerInfo[j] + "/advanced", playerInfo[j], playerInfo[f]);
						setErrorMessage(playerOneError);
					}
				}));
				
			}
			
		}

		for(Thread thread : playerThreads){
			thread.start();
		}
		
		//Forces the program to wait here until all threads are complete
		for(Thread thread : playerThreads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		
		if(fileName != null){
			execute.writeToFile(fileName);
		}
		
		//Clear the threads and drivers to be able to run the program again
		playerThreads.clear();
		for(WebDriver driver : drivers){
			driver.quit();
		}
		drivers.clear();
	}
	
	private void setErrorMessage(String error){
		errorMessage += error + " ";
		lblErrorMessage.setText("<html>" + errorMessage + "</html>");
	}

	void topHalf(int numberOfLblAndText) {
		lblInstructions(numberOfLblAndText);		
	}
	
	private void bottomHalf(ArrayList<WebDriver> drivers, ArrayList<Thread> playerThreads) {
		JLabel lblFilePath = new JLabel("Name Your CSV File without the Extension");
		lblFilePath.setHorizontalAlignment(SwingConstants.CENTER);
		lblFilePath.setBounds(181, 315, 240, 22);
		frame.getContentPane().add(lblFilePath);
		
		JLabel lblWhereSave = new JLabel("This will save directly to your desktop");
		lblWhereSave.setHorizontalAlignment(SwingConstants.CENTER);
		lblWhereSave.setBounds(190, 333, 228, 14);
		frame.getContentPane().add(lblWhereSave);
		
		txtFileName = new JFormattedTextField();
		txtFileName.setBounds(213, 348, 182, 20);
		frame.getContentPane().add(txtFileName);
		txtFileName.addPropertyChangeListener("value", this);
		
		JButton btnGetIlvls = new JButton("Get iLvls");
		btnGetIlvls.setBounds(190, 379, 102, 23);
		frame.getContentPane().add(btnGetIlvls);
		btnGetIlvls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getStats(drivers, playerThreads);
			}
		});

		JButton btnClearInfo = new JButton("Clear Info");
		btnClearInfo.setBounds(308, 379, 96, 23);
		frame.getContentPane().add(btnClearInfo);
		btnClearInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				errorMessage = "";
				lblFileNameError.setText("");
				//fileName = "";
				
				for(int i = 0; i < playerTextFields.length; i++){
					playerTextFields[i].setText(null);
				}
				
				for(int i = 0; i < playerInfo.length; i++){		
					playerInfo[i] = null;
				}
				
			}
		});
		
		lblErrorMessage = new JLabel("");
		lblErrorMessage.setBackground(new Color(106, 90, 205));
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		lblErrorMessage.setBounds(10, 466, 604, 64);
		frame.getContentPane().add(lblErrorMessage);
		
		lblFileNameError = new JLabel("");
		lblFileNameError.setHorizontalAlignment(SwingConstants.CENTER);
		lblFileNameError.setForeground(Color.RED);
		lblFileNameError.setBounds(178, 413, 243, 23);
		frame.getContentPane().add(lblFileNameError);

	}
		/**
		 * Potentially could be used for strange char letters in names to make them URL-ready.
		 * 
		 * @param inString - string to be changed. Server and Name strings.
		 * @return String - Provides the string with the URL-read strange chars
		 */
//		private String textCleanUp(String inString) {
//			
//			//currently not available
//			String outString;
//			outString = inString.replace("\t", "");
//			
//			return outString;
//						
//		}

	private void lblInstructions(int numberOfLblAndTxt) {
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 310, 624, 2);
		frame.getContentPane().add(separator);
		
		JLabel lblTitle = new JLabel("Character iLvl Finder");
		lblTitle.setFont(new Font("OCR A Extended", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(128, 0, 0));
		lblTitle.setBounds(120, 11, 357, 36);
		frame.getContentPane().add(lblTitle);
		
		
		int lblColumn1A = 48;
		int lblColumn1B = 178;
		int lblColumn2A = 395;
		int lblColumn2B = 525;
		
		int txtColumn1A = 10;
		int txtColumn1B = 140;
		int txtColumn2A = 357;
		int txtColumn2B = 487;
		
		int lblRows = 44;
		int txtRows = 60;
		int j = 0;
		int k = 0;
		System.out.println(numberOfLblAndTxt);
		
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		
		for(int i = 0; i < numberOfLblAndTxt; i += 2){		
			
			if(k >= numberOfLblAndTxt / 4){
				k = 0;
			}
			
			int currentLblRows = lblRows + (k * 42);
			int currentTxtRows = txtRows + (k * 42);
			
			labels.add(new JLabel("P" + (j + 1) + " Server"));
			labels.add(new JLabel("P" + (j + 1) + " Name"));
			
			txtFields.add(new JFormattedTextField());
			txtFields.add(new JFormattedTextField());
						
			if (i < numberOfLblAndTxt / 2){				
				labels.get(i).setBounds(lblColumn1A, currentLblRows, 65, 14);
				frame.getContentPane().add(labels.get(i));
				
				labels.get(i + 1).setBounds(lblColumn1B, currentLblRows, 62, 14);
				frame.getContentPane().add(labels.get(i + 1));
				
				txtFields.get(i).setBounds(txtColumn1A, currentTxtRows, 120, 20);
				frame.getContentPane().add(txtFields.get(i));
				txtFields.get(i).addPropertyChangeListener("value", this);
				
				txtFields.get(i + 1).setBounds(txtColumn1B, currentTxtRows, 120, 20);
				frame.getContentPane().add(txtFields.get(i + 1));
				txtFields.get(i + 1).addPropertyChangeListener("value", this);
				
			
			}else{
				
				labels.get(i).setBounds(lblColumn2A, currentLblRows, 65, 14);
				frame.getContentPane().add(labels.get(i));
				
				labels.get(i + 1).setBounds(lblColumn2B, currentLblRows, 62, 14);
				frame.getContentPane().add(labels.get(i + 1));
				
				txtFields.get(i).setBounds(txtColumn2A, currentTxtRows, 120, 20);
				frame.getContentPane().add(txtFields.get(i));
				txtFields.get(i).addPropertyChangeListener("value", this);
				
				txtFields.get(i + 1).setBounds(txtColumn2B, currentTxtRows, 120, 20);
				frame.getContentPane().add(txtFields.get(i + 1));
				txtFields.get(i + 1).addPropertyChangeListener("value", this);
								
			}
			
			j++;
			k++;
			
		}
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		for(int i = 0; i < txtFields.size(); i++){
			if(source == txtFields.get(i) && txtFields.get(i).getText().length() > 1){
				playerInfo[i] = txtFields.get(i).getText();
				
			}
		}
		
		if(source == txtFileName){
			if(txtFileName.getText().length() > 1){
				fileName = txtFileName.getText();
			}
		}
	}
}