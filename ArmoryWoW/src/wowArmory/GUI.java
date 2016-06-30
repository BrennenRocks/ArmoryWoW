/**
 * @author Brennen Davis
 */

package wowArmory;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI implements PropertyChangeListener{
	
	private JFrame frame;
	
	private WebDriver driver;
	private Execute execute;
		
	private String playerOneServer = null;
	private String playerOneName = null;
	private String playerTwoServer = null;
	private String playerTwoName = null;
	private String playerThreeServer = null;
	private String playerThreeName = null;
	private String playerFourServer = null;
	private String playerFourName = null;
	private String playerFiveServer = null;
	private String playerFiveName = null;
	private String playerSixServer = null;
	private String playerSixName = null;
	private String playerSevenServer = null;
	private String playerSevenName = null;
	private String playerEightServer = null;
	private String playerEightName = null;
	private String playerNineServer = null;
	private String playerNineName = null;
	private String playerTenServer = null;
	private String playerTenName = null;
	private String playerElevenServer = null;
	private String playerElevenName = null;
	private String playerTwelveServer = null;
	private String playerTwelveName = null;
	
	private String[] playerInfo;
	
	private JFormattedTextField txtPlayerOneServer;
	private JFormattedTextField txtPlayerOneName;
	private JFormattedTextField txtPlayerTwoServer;
	private JFormattedTextField txtPlayerTwoName;
	private JFormattedTextField txtPlayerThreeServer;
	private JFormattedTextField txtPlayerThreeName;
	private JFormattedTextField txtPlayerFourServer;
	private JFormattedTextField txtPlayerFourName;
	private JFormattedTextField txtPlayerFiveServer;
	private JFormattedTextField txtPlayerFiveName;
	private JFormattedTextField txtPlayerSixServer;
	private JFormattedTextField txtPlayerSixName;
	private JFormattedTextField txtPlayerSevenServer;
	private JFormattedTextField txtPlayerSevenName;
	private JFormattedTextField txtPlayerEightServer;
	private JFormattedTextField txtPlayerEightName;
	private JFormattedTextField txtPlayerNineServer;
	private JFormattedTextField txtPlayerNineName;
	private JFormattedTextField txtPlayerTenServer;
	private JFormattedTextField txtPlayerTenName;
	private JFormattedTextField txtPlayerElevenServer;
	private JFormattedTextField txtPlayerElevenName;
	private JFormattedTextField txtPlayerTwelveServer;
	private JFormattedTextField txtPlayerTwelveName;
	
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
		driver = new PhantomJSDriver();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("Closing the driver.");
				driver.quit();
			}
		});
		
		execute = new Execute();
				
		topHalf();
		bottomHalf();
		
		playerInfo = new String[] {
			playerOneServer, playerOneName, playerTwoServer, playerTwoName, playerThreeServer, playerThreeName, playerFourServer, playerFourName,
			playerFiveServer, playerFiveName, playerSixServer, playerSixName, playerSevenServer, playerSevenName, playerEightServer, playerEightName,
			playerNineServer, playerNineName, playerTenServer, playerTenName, playerElevenServer, playerElevenName, playerTwelveServer, playerTwelveName
		};
		
		playerTextFields = new JFormattedTextField[] {
				txtPlayerOneServer, txtPlayerOneName, txtPlayerTwoServer, txtPlayerTwoName, txtPlayerThreeServer,
				txtPlayerThreeName, txtPlayerFourServer, txtPlayerFourName, txtPlayerFiveServer, txtPlayerFiveName,
				txtPlayerSixServer, txtPlayerSixName, txtPlayerSevenServer, txtPlayerSevenName, txtPlayerEightServer, 
				txtPlayerEightName, txtPlayerNineServer, txtPlayerNineName, txtPlayerTenServer, txtPlayerTenName, 
				txtPlayerElevenServer, txtPlayerElevenName, txtPlayerTwelveServer, txtPlayerTwelveName
		};
		
		//System.out.println(playerInfo.length);
		//System.out.println(playerTextFields.length);
		
	}
	
	private void getStats(){
		//clear the error message before getting stats
		errorMessage = "";
		lblFileNameError.setText("");
		
		if(fileName == null){
			lblFileNameError.setText("NO FILE NAME GIVEN!");
			return;
		}
		
		if(playerOneServer != null && playerOneName != null){
			String playerOneError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerOneServer + "/" + playerOneName + "/advanced", playerOneName, playerOneServer);
			setErrorMessage(playerOneError);
		}
		if(playerTwoServer != null && playerTwoName != null && playerTwoServer != "" && playerTwoName != ""){
			String playerTwoError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerTwoServer + "/" + playerTwoName + "/advanced", playerTwoName, playerTwoServer);
			setErrorMessage(playerTwoError);
		}
		if(playerThreeServer != null && playerThreeName != null && playerThreeServer != "" && playerThreeName != ""){
			String playerThreeError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerThreeServer + "/" + playerThreeName + "/advanced", playerThreeName, playerThreeServer);
			setErrorMessage(playerThreeError);
		}
		if(playerFourServer != null && playerFourName != null && playerFourServer != "" && playerFourName != ""){
			String playerFourError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerFourServer + "/" + playerFourName + "/advanced", playerFourName, playerFourServer);
			setErrorMessage(playerFourError);
		}
		if(playerFiveServer != null && playerFiveName != null && playerFiveServer != "" && playerFiveName != ""){
			String playerFiveError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerFiveServer + "/" + playerFiveName + "/advanced", playerFiveName, playerFiveServer);
			setErrorMessage(playerFiveError);
		}
		if(playerSixServer != null && playerSixName != null){
			String playerSixError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerSixServer + "/" + playerSixName + "/advanced", playerSixName, playerSixServer);
			setErrorMessage(playerSixError);
		}
		if(playerSevenServer != null && playerSevenName != null && playerSevenServer != "" && playerSevenName != ""){
			String playerSevenError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerSevenServer + "/" + playerSevenName + "/advanced", playerSevenName, playerSevenServer);
			setErrorMessage(playerSevenError);
		}
		if(playerEightServer != null && playerEightName != null && playerEightServer != "" && playerEightName != ""){
			String playerEightError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerEightServer + "/" + playerEightName + "/advanced", playerEightName, playerEightServer);
			setErrorMessage(playerEightError);
		}
		if(playerNineServer != null && playerNineName != null && playerNineServer != "" && playerNineName != ""){
			String playerNineError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerNineServer + "/" + playerNineName + "/advanced", playerNineName, playerNineServer);
			setErrorMessage(playerNineError);
		}
		if(playerTenServer != null && playerTenName != null && playerTenServer != "" && playerTenName != ""){
			String playerTenError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerTenServer + "/" + playerTenName + "/advanced", playerTenName, playerTenServer);
			setErrorMessage(playerTenError);
		}
		if(playerElevenServer != null && playerElevenName != null && playerElevenServer != "" && playerElevenName != ""){
			String playerElevenError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerElevenServer + "/" + playerElevenName + "/advanced", playerElevenName, playerElevenServer);
			setErrorMessage(playerElevenError);
		}
		if(playerTwelveServer != null && playerTwelveName != null && playerTwelveServer != "" && playerTwelveName != ""){
			String playerTwelveError = execute.pullElements(driver, "http://us.battle.net/wow/en/character/" 
					+ playerTwelveServer + "/" + playerTwelveName + "/advanced", playerTwelveName, playerTwelveServer);
			setErrorMessage(playerTwelveError);
		}
		
		
		if(fileName != null){
			execute.writeToFile(fileName);
		}
		
		
	}
	
	private void setErrorMessage(String error){
		errorMessage += error + " ";
		lblErrorMessage.setText("<html>" + errorMessage + "</html>");
	}

	private void topHalf() {
		lblInstructions();		
		txtFields();
	}
	
	private void bottomHalf() {
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
				getStats();
			}
		});

		JButton btnClearInfo = new JButton("Clear Info");
		btnClearInfo.setBounds(308, 379, 96, 23);
		frame.getContentPane().add(btnClearInfo);
		btnClearInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				txtPlayerOneServer.setText("");
				txtPlayerOneName.setText(""); 
				txtPlayerTwoServer.setText(""); 
				txtPlayerTwoName.setText(""); 
				txtPlayerThreeServer.setText("");
				txtPlayerThreeName.setText(""); 
				txtPlayerFourServer.setText(""); 
				txtPlayerFourName.setText(""); 
				txtPlayerFiveServer.setText(""); 
				txtPlayerFiveName.setText("");
				txtPlayerSixServer.setText(""); 
				txtPlayerSixName.setText(""); 
				txtPlayerSevenServer.setText("");
				txtPlayerSevenName.setText(""); 
				txtPlayerEightServer.setText(""); 
				txtPlayerEightName.setText(""); 
				txtPlayerNineServer.setText(""); 
				txtPlayerNineName.setText(""); 
				txtPlayerTenServer.setText(""); 
				txtPlayerTenName.setText("");				
				txtPlayerElevenServer.setText(""); 
				txtPlayerElevenName.setText(""); 
				txtPlayerTwelveServer.setText(""); 
				txtPlayerTwelveName.setText("");
				
				playerOneServer = null; 
				playerOneName = null; 
				playerTwoServer = null; 
				playerTwoName = null; 
				playerThreeServer = null; 
				playerThreeName = null; 
				playerFourServer = null; 
				playerFourName = null;
				playerFiveServer = null; 
				playerFiveName = null; 
				playerSixServer = null;
				playerSixName = null; 
				playerSevenServer = null; 
				playerSevenName = null; 
				playerEightServer = null; 
				playerEightName = null;
				playerNineServer = null; 
				playerNineName = null; 
				playerTenServer = null; 
				playerTenName = null; 
				playerElevenServer = null; 
				playerElevenName = null; 
				playerTwelveServer = null; 
				playerTwelveName = null;
	
				errorMessage = "";
				lblFileNameError.setText("");
				
				//Can't get the looping to work properly to clear the text and the string.
//				for(int i = 0; i < playerTextFields.length; i++){
//					playerTextFields[i] = new JFormattedTextField();
//					playerTextFields[i].setText("poop");
//				}
//				
//				for(int i = 0; i < playerInfo.length; i++){		
//					playerInfo[i] = null;
//				}
//				System.out.println(playerOneServer);
//				System.out.println(txtPlayerOneServer.getText());
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

	private void txtFields() {
		txtPlayerOneServer = new JFormattedTextField();
		txtPlayerOneServer.setBounds(10, 60, 120, 20);
		frame.getContentPane().add(txtPlayerOneServer);
		txtPlayerOneServer.addPropertyChangeListener("value", this);
		
		txtPlayerOneName = new JFormattedTextField();
		txtPlayerOneName.setBounds(140, 60, 120, 20);
		frame.getContentPane().add(txtPlayerOneName);
		txtPlayerOneName.addPropertyChangeListener("value", this);
		
		txtPlayerTwoServer = new JFormattedTextField();
		txtPlayerTwoServer.setBounds(10, 102, 120, 20);
		frame.getContentPane().add(txtPlayerTwoServer);
		txtPlayerTwoServer.addPropertyChangeListener("value", this);
		
		txtPlayerTwoName = new JFormattedTextField();
		txtPlayerTwoName.setBounds(140, 102, 120, 20);
		frame.getContentPane().add(txtPlayerTwoName);
		txtPlayerTwoName.addPropertyChangeListener("value", this);
		
		txtPlayerThreeServer = new JFormattedTextField();
		txtPlayerThreeServer.setBounds(10, 144, 120, 20);
		frame.getContentPane().add(txtPlayerThreeServer);
		txtPlayerThreeServer.addPropertyChangeListener("value", this);
		
		txtPlayerThreeName = new JFormattedTextField();
		txtPlayerThreeName.setBounds(140, 144, 120, 20);
		frame.getContentPane().add(txtPlayerThreeName);
		txtPlayerThreeName.addPropertyChangeListener("value", this);
		
		txtPlayerFourServer = new JFormattedTextField();
		txtPlayerFourServer.setBounds(10, 186, 120, 20);
		frame.getContentPane().add(txtPlayerFourServer);
		txtPlayerFourServer.addPropertyChangeListener("value", this);
		
		txtPlayerFourName = new JFormattedTextField();
		txtPlayerFourName.setBounds(140, 186, 120, 20);
		frame.getContentPane().add(txtPlayerFourName);
		txtPlayerFourName.addPropertyChangeListener("value", this);
	
		txtPlayerFiveServer = new JFormattedTextField();
		txtPlayerFiveServer.setBounds(10, 228, 120, 20);
		frame.getContentPane().add(txtPlayerFiveServer);
		txtPlayerFiveServer.addPropertyChangeListener("value", this);
		
		txtPlayerFiveName = new JFormattedTextField();
		txtPlayerFiveName.setBounds(140, 228, 120, 20);
		frame.getContentPane().add(txtPlayerFiveName);
		txtPlayerFiveName.addPropertyChangeListener("value", this);
		
		txtPlayerSixServer = new JFormattedTextField();
		txtPlayerSixServer.setBounds(357, 60, 120, 20);
		frame.getContentPane().add(txtPlayerSixServer);
		txtPlayerSixServer.addPropertyChangeListener("value", this);
		
		txtPlayerSixName = new JFormattedTextField();
		txtPlayerSixName.setBounds(487, 60, 120, 20);
		frame.getContentPane().add(txtPlayerSixName);
		txtPlayerSixName.addPropertyChangeListener("value", this);
		
		txtPlayerSevenServer = new JFormattedTextField();
		txtPlayerSevenServer.setBounds(357, 102, 120, 20);
		frame.getContentPane().add(txtPlayerSevenServer);
		txtPlayerSevenServer.addPropertyChangeListener("value", this);
		
		txtPlayerSevenName = new JFormattedTextField();
		txtPlayerSevenName.setBounds(487, 102, 120, 20);
		frame.getContentPane().add(txtPlayerSevenName);
		txtPlayerSevenName.addPropertyChangeListener("value", this);
		
		txtPlayerEightServer = new JFormattedTextField();
		txtPlayerEightServer.setBounds(357, 144, 120, 20);
		frame.getContentPane().add(txtPlayerEightServer);
		txtPlayerEightServer.addPropertyChangeListener("value", this);
		
		txtPlayerEightName = new JFormattedTextField();
		txtPlayerEightName.setBounds(487, 144, 120, 20);
		frame.getContentPane().add(txtPlayerEightName);
		txtPlayerEightName.addPropertyChangeListener("value", this);

		txtPlayerNineServer = new JFormattedTextField();
		txtPlayerNineServer.setBounds(357, 186, 120, 20);
		frame.getContentPane().add(txtPlayerNineServer);
		txtPlayerNineServer.addPropertyChangeListener("value", this);
		
		txtPlayerNineName = new JFormattedTextField();
		txtPlayerNineName.setBounds(487, 186, 120, 20);
		frame.getContentPane().add(txtPlayerNineName);
		txtPlayerNineName.addPropertyChangeListener("value", this);
		
		txtPlayerTenServer = new JFormattedTextField();
		txtPlayerTenServer.setBounds(357, 228, 120, 20);
		frame.getContentPane().add(txtPlayerTenServer);
		txtPlayerTenServer.addPropertyChangeListener("value", this);
		
		txtPlayerTenName = new JFormattedTextField();
		txtPlayerTenName.setBounds(487, 228, 120, 20);
		frame.getContentPane().add(txtPlayerTenName);
		txtPlayerTenName.addPropertyChangeListener("value", this);

		txtPlayerElevenServer = new JFormattedTextField();
		txtPlayerElevenServer.setBounds(48, 275, 120, 20);
		frame.getContentPane().add(txtPlayerElevenServer);
		txtPlayerElevenServer.addPropertyChangeListener("value", this);
		
		txtPlayerElevenName = new JFormattedTextField();
		txtPlayerElevenName.setBounds(178, 275, 120, 20);
		frame.getContentPane().add(txtPlayerElevenName);
		txtPlayerElevenName.addPropertyChangeListener("value", this);

		txtPlayerTwelveServer = new JFormattedTextField();
		txtPlayerTwelveServer.setBounds(323, 275, 120, 20);
		frame.getContentPane().add(txtPlayerTwelveServer);
		txtPlayerTwelveServer.addPropertyChangeListener("value", this);

		txtPlayerTwelveName = new JFormattedTextField();
		txtPlayerTwelveName.setBounds(453, 275, 120, 20);
		frame.getContentPane().add(txtPlayerTwelveName);
		txtPlayerTwelveName.addPropertyChangeListener("value", this);	
	}
	
		public void propertyChange(PropertyChangeEvent e) {
			Object source = e.getSource();
			if(source == txtPlayerOneServer){
				if(txtPlayerOneServer.getText().length() > 1){
					playerOneServer = txtPlayerOneServer.getText();
//					playerOneServer = textCleanUp(playerOneServer);
				}
			}else if(source == txtPlayerOneName){
				if(txtPlayerOneName.getText().length() > 1){
					playerOneName = txtPlayerOneName.getText();
//					playerOneName = textCleanUp(playerOneName);
				}
			}else if(source == txtPlayerTwoServer){
				if(txtPlayerTwoServer.getText().length() > 1){
					playerTwoServer = txtPlayerTwoServer.getText();
//					textCleanUp(playerTwoServer);
				}
			}else if(source == txtPlayerTwoName){
				if(txtPlayerTwoName.getText().length() > 1){
					playerTwoName = txtPlayerTwoName.getText();
//					textCleanUp(playerTwoName);
				}
			}else if(source == txtPlayerThreeServer){
				if(txtPlayerThreeServer.getText().length() > 1){
					playerThreeServer = txtPlayerThreeServer.getText();
//					textCleanUp(playerThreeServer);
				}
			}else if(source == txtPlayerThreeName){
				if(txtPlayerThreeName.getText().length() > 1){
					playerThreeName = txtPlayerThreeName.getText();
//					textCleanUp(playerThreeName);
				}
			}else if(source == txtPlayerFourServer){
				if(txtPlayerFourServer.getText().length() > 1){
					playerFourServer = txtPlayerFourServer.getText();
//					textCleanUp(playerFourServer);
				}
			}else if(source == txtPlayerFourName){
				if(txtPlayerFourName.getText().length() > 1){
					playerFourName = txtPlayerFourName.getText();
//					textCleanUp(playerFourName);
				}
			}else if(source == txtPlayerFiveServer){
				if(txtPlayerFiveServer.getText().length() > 1){
					playerFiveServer = txtPlayerFiveServer.getText();
//					textCleanUp(playerFiveServer);
				}
			}else if(source == txtPlayerFiveName){
				if(txtPlayerFiveName.getText().length() > 1){
					playerFiveName = txtPlayerFiveName.getText();
//					textCleanUp(playerFiveName);
				}
			}else if(source == txtPlayerSixServer){
				if(txtPlayerSixServer.getText().length() > 1){
					playerSixServer = txtPlayerSixServer.getText();
//					textCleanUp(playerSixServer);
				}
			}else if(source == txtPlayerSixName){
				if(txtPlayerSixName.getText().length() > 1){
					playerSixName = txtPlayerSixName.getText();
//					textCleanUp(playerSixName);
				}
			}else if(source == txtPlayerSevenServer){
				if(txtPlayerSevenServer.getText().length() > 1){
					playerSevenServer = txtPlayerSevenServer.getText();
//					textCleanUp(playerSevenServer);
				}
			}else if(source == txtPlayerSevenName){
				if(txtPlayerSevenName.getText().length() > 1){
					playerSevenName = txtPlayerSevenName.getText();
//					textCleanUp(playerSevenName);
				}
			}else if(source == txtPlayerEightServer){
				if(txtPlayerEightServer.getText().length() > 1){
					playerEightServer = txtPlayerEightServer.getText();
//					textCleanUp(playerEightServer);
				}
			}else if(source == txtPlayerEightName){
				if(txtPlayerEightName.getText().length() > 1){
					playerEightName = txtPlayerEightName.getText();
//					textCleanUp(playerEightName);
				}
			}else if(source == txtPlayerNineServer){
				if(txtPlayerNineServer.getText().length() > 1){
					playerNineServer = txtPlayerNineServer.getText();
//					textCleanUp(playerNineServer);
				}
			}else if(source == txtPlayerNineName){
				if(txtPlayerNineName.getText().length() > 1){
					playerNineName = txtPlayerNineName.getText();
//					textCleanUp(playerNineName);
				}
			}else if(source == txtPlayerTenServer){
				if(txtPlayerTenServer.getText().length() > 1){
					playerTenServer = txtPlayerTenServer.getText();
//					textCleanUp(playerTenServer);
				}
			}else if(source == txtPlayerTenName){
				if(txtPlayerTenName.getText().length() > 1){
					playerTenName = txtPlayerTenName.getText();
//					textCleanUp(playerTenName);
				}
			}else if(source == txtPlayerElevenServer){
				if(txtPlayerElevenServer.getText().length() > 1){
					playerElevenServer = txtPlayerElevenServer.getText();
//					textCleanUp(playerElevenServer);
				}
			}else if(source == txtPlayerElevenName){
				if(txtPlayerElevenName.getText().length() > 1){
					playerElevenName = txtPlayerElevenName.getText();
//					textCleanUp(playerElevenName);
				}
			}else if(source == txtPlayerTwelveServer){
				if(txtPlayerTwelveServer.getText().length() > 1){
					playerTwelveServer = txtPlayerTwelveServer.getText();
//					textCleanUp(playerTwelveServer);
				}
			}else if(source == txtPlayerTwelveName){
				if(txtPlayerTwelveName.getText().length() > 1){
					playerTwelveName = txtPlayerTwelveName.getText();
//					textCleanUp(playerTwelveName);
				}
			}else if(source == txtFileName){
				if(txtFileName.getText().length() > 1){
					fileName = txtFileName.getText();
				}
			}
		}

		/**
		 * Potentially could be used for strange char letters in names to make them URL-ready.
		 * 
		 * @param inString - string to be changed. Server and Name strings.
		 * @return String - Provides the string with the URL-read strange chars
		 */
		private String textCleanUp(String inString) {
			
			//currently not available
			String outString;
			outString = inString.replace("\t", "");
			
			return outString;
						
		}

	private void lblInstructions() {
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 310, 624, 2);
		frame.getContentPane().add(separator);
		
		JLabel lblTitle = new JLabel("Character iLvl Finder");
		lblTitle.setFont(new Font("OCR A Extended", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(128, 0, 0));
		lblTitle.setBounds(120, 11, 357, 36);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblPlayerOneServer = new JLabel("P1 Server");
		lblPlayerOneServer.setBounds(48, 44, 62, 14);
		frame.getContentPane().add(lblPlayerOneServer);
		
		JLabel lblPlayerOneName = new JLabel("P1 Name");
		lblPlayerOneName.setBounds(178, 44, 62, 14);
		frame.getContentPane().add(lblPlayerOneName);
		
		JLabel lblPlayerTwoServer = new JLabel("P2 Server");
		lblPlayerTwoServer.setBounds(48, 86, 62, 14);
		frame.getContentPane().add(lblPlayerTwoServer);
		
		JLabel lblPlayerTwoName = new JLabel("P2 Name");
		lblPlayerTwoName.setBounds(178, 86, 62, 14);
		frame.getContentPane().add(lblPlayerTwoName);
		
		JLabel lblPlayerThreeServer = new JLabel("P3 Server");
		lblPlayerThreeServer.setBounds(48, 128, 62, 14);
		frame.getContentPane().add(lblPlayerThreeServer);
		
		JLabel lblPlayerThreeName = new JLabel("P3 Name");
		lblPlayerThreeName.setBounds(178, 128, 62, 14);
		frame.getContentPane().add(lblPlayerThreeName);
		
		JLabel lblPlayerFourServer = new JLabel("P4 Server");
		lblPlayerFourServer.setBounds(48, 170, 62, 14);
		frame.getContentPane().add(lblPlayerFourServer);
		
		JLabel lblPlayerFourName = new JLabel("P4 Name");
		lblPlayerFourName.setBounds(178, 170, 62, 14);
		frame.getContentPane().add(lblPlayerFourName);
		
		JLabel lblPlayerFiveServer = new JLabel("P5 Server");
		lblPlayerFiveServer.setBounds(48, 212, 62, 14);
		frame.getContentPane().add(lblPlayerFiveServer);
		
		JLabel lblPlayerFiveName = new JLabel("P5 Name");
		lblPlayerFiveName.setBounds(178, 212, 62, 14);
		frame.getContentPane().add(lblPlayerFiveName);
		
		JLabel lblPlayerSixServer = new JLabel("P6 Server");
		lblPlayerSixServer.setBounds(395, 44, 62, 14);
		frame.getContentPane().add(lblPlayerSixServer);
		
		JLabel lblPlayerSixName = new JLabel("P6 Name");
		lblPlayerSixName.setBounds(525, 44, 62, 14);
		frame.getContentPane().add(lblPlayerSixName);
		
		JLabel lblPlayerSevenServer = new JLabel("P7 Server");
		lblPlayerSevenServer.setBounds(395, 86, 62, 14);
		frame.getContentPane().add(lblPlayerSevenServer);
		
		JLabel lblPlayerSevenName = new JLabel("P7 Name");
		lblPlayerSevenName.setBounds(525, 86, 62, 14);
		frame.getContentPane().add(lblPlayerSevenName);
		
		JLabel lblPlayerEightServer = new JLabel("P8 Server");
		lblPlayerEightServer.setBounds(395, 128, 62, 14);
		frame.getContentPane().add(lblPlayerEightServer);
		
		JLabel lblPlayerEightName = new JLabel("P8 Name");
		lblPlayerEightName.setBounds(525, 128, 62, 14);
		frame.getContentPane().add(lblPlayerEightName);
		
		JLabel lblPlayerNineServer = new JLabel("P9 Server");
		lblPlayerNineServer.setBounds(395, 170, 62, 14);
		frame.getContentPane().add(lblPlayerNineServer);
		
		JLabel lblPlayerNineName = new JLabel("P9 Name");
		lblPlayerNineName.setBounds(525, 170, 62, 14);
		frame.getContentPane().add(lblPlayerNineName);
		
		JLabel lblPlayerTenServer = new JLabel("P10 Server");
		lblPlayerTenServer.setBounds(395, 212, 62, 14);
		frame.getContentPane().add(lblPlayerTenServer);
		
		JLabel lblPlayerTenName = new JLabel("P10 Name");
		lblPlayerTenName.setBounds(525, 212, 62, 14);
		frame.getContentPane().add(lblPlayerTenName);
		
		JLabel lblPlayerElevenServer = new JLabel("P11 Server");
		lblPlayerElevenServer.setBounds(84, 259, 62, 14);
		frame.getContentPane().add(lblPlayerElevenServer);
		
		JLabel lblPlayerElevenName = new JLabel("P11 Name");
		lblPlayerElevenName.setBounds(214, 259, 62, 14);
		frame.getContentPane().add(lblPlayerElevenName);
		
		JLabel lblPlayerTwelveServer = new JLabel("P12 Server");
		lblPlayerTwelveServer.setBounds(359, 259, 62, 14);
		frame.getContentPane().add(lblPlayerTwelveServer);
		
		JLabel lblPlayerTwelveName = new JLabel("P12 Name");
		lblPlayerTwelveName.setBounds(489, 259, 62, 14);
		frame.getContentPane().add(lblPlayerTwelveName);
	}
}