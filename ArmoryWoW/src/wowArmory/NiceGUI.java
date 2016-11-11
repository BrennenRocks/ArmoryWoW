/**
 * @author Brennen Davis
 * @version 1.4
 */
package wowArmory;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class NiceGUI extends JFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int numOfChars = 27;
	
	private JPanel contentPane;
	
	private JLabel lblErrorMessage;
		
	private ArrayList<JFormattedTextField> txtFields = new ArrayList<>();
	private String[] charName = new String[numOfChars];
	
	private ArrayList<JComboBox<String>> servers = new ArrayList<>();
	private ArrayList<WebDriver> drivers = new ArrayList<>();

	private Execute execute;
	private JFileChooser fc;
	
	//Limit the amount of Threads to be run at one time to the number of Available Processors in the machine times two
	private int processorsx2 = Runtime.getRuntime().availableProcessors() * 2;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NiceGUI frame = new NiceGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * This will place the phantomjs.exe file outside of the .jar file inside of the present working directory
	 * @return String - The file path of the working directory where the .exe will be extracted to.
	 */
	private static String loadPhantomJS() {
	    String phantomJs = "phantomjs.exe";
	    try {
	        InputStream in = NiceGUI.class.getResourceAsStream("/resources/" + phantomJs);
	        File fileOut = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath().toString() + phantomJs);
	        OutputStream out = FileUtils.openOutputStream(fileOut);
	        IOUtils.copy(in, out);
	        in.close();
	        out.close();
	        return fileOut.getAbsolutePath();
	    } catch (Exception e) {
	    	System.out.println("Could not find correct file path.");
	        return "";
	    }
	} 

	/**
	 * Create the frame and sets up the PhantomJS Webdriver 
	 */
	public NiceGUI() {
		System.out.println("Number of Processors on machine " + processorsx2 / 2);
		//set up the WebDriver before the frame starts
		//System.setProperty("phantomjs.binary.path", loadPhantomJS());
		System.setProperty("phantomjs.binary.path", "src/resources/phantomjs.exe");
		//System.setProperty("webdriver.chrome.driver", "src/resources/chromedriver.exe");
		
		execute = new Execute();
		fc = new JFileChooser();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("Closing any running Drivers.");
				if(drivers.size() > 0){
					for(WebDriver driver : drivers){
						driver.quit();
					}
				}
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		Toolkit kit = Toolkit.getDefaultToolkit();
	    Dimension screenSize = kit.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
		
		setBounds(100, 100, screenWidth / 2, screenHeight / 2);
		
		this.setTitle("WoW Character iLvl Getter");
		
		URL urlImage = NiceGUI.class.getResource("/resources/treasureChest.png");
		this.setIconImage(new ImageIcon(urlImage).getImage());
		
		addComponentsToPane(getContentPane());
	}

	/**
	 * Sets everything up onto the frame!
	 * @param pane
	 */
	private void addComponentsToPane(Container pane) {
		//Keep in this order!
		addTitlePanel(pane);
		addCharactersPanel(pane);
		addActionPanel(pane);
		addUpdateLabels(pane);
		
	}
	
	/***********************************************************
	 **********************   PANELS   *************************
	 ***********************************************************/
	
	private void addTitlePanel(Container pane){
		JPanel title = new JPanel();
		title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
		JLabel lblTitle = new JLabel("Character iLvl Getter");
		lblTitle.setFont(new Font("Century Gothic", Font.BOLD | Font.ITALIC, 24));
		lblTitle.setForeground(new Color(139, 0, 0));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		title.add(lblTitle);
		
		pane.add(title);
	}
	
	private void addCharactersPanel(Container pane){
		JPanel characters = new JPanel();
		characters.setLayout(new GridLayout(0, 6, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane(characters);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	
		for(int i = 0; i < numOfChars; i++){
			addServerDropDownMenu(characters);
			addJFormattedTextField(characters);
		}
		
		pane.add(scrollPane);
	}
	
	private void addActionPanel(Container pane){
		JPanel buttons = new JPanel();
		addButton("Get iLvls", buttons, event -> {
			fc.showOpenDialog(pane);
			
			try {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				buttons.setEnabled(false);
				getStats();
				buttons.setEnabled(true);
				this.setCursor(Cursor.getDefaultCursor());
			} catch (InterruptedException e) {
				System.out.println("Interrupted the awaitTermination method.");
				e.printStackTrace();
			}
		});
		
		addButton("Clear", buttons, event -> {
			for(int i = 0; i < charName.length; i++){
				charName[i] = null;
			}
			
			for(JComboBox<String> box : servers){
				box.setSelectedItem(box.getItemAt(0));
			}
			for(JFormattedTextField txtField : txtFields){
				txtField.setText(null);
			}

			lblErrorMessage.setText("");
		});
		
		pane.add(buttons);
	}
	
	private void addUpdateLabels(Container pane){
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		lblErrorMessage = new JLabel("");
		lblErrorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblErrorMessage.setBackground(new Color(106, 90, 205));
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		bottomPanel.add(lblErrorMessage);

		pane.add(bottomPanel);
	}
	
	/***********************************************************
	 *******************   COMPONENTS    ***********************
	 ***********************************************************/
	
	private void addButton(String name, Container pane, ActionListener listener){
		JButton button = new JButton(name);
		button.addActionListener(listener);
		pane.add(button);
		
	}
	
	private void addJFormattedTextField(Container pane){
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.addPropertyChangeListener("value", this);
		txtFields.add(formattedTextField);
		pane.add(formattedTextField);
	}
	
	private void addServerDropDownMenu(Container pane){
		//246 servers
		String[] serverNames = {"Aegwynn", "Aerie Peak", "Agamaggan", "Aggramar", "Akama", "Alexstrasza", "Alleria", "Altar of Storms", "Alterac Mountains", 
				"Aman'Thul", "Andorhal", "Anetheron", "Antonidas", "Anub'arak", "Anvilmar", "Arathor", "Archimonde", "Area 52", "Argent Dawn", "Arthas", 
				"Arygos", "Auchindoun", "Azgalor", "Azjol-Nerub", "Azralon", "Azshara", "Azuremyst", "Baelgun", "Balnazzar", "Barthilas", 
				"Black Dragonflight", "Blackhand", "Blackrock", "Blackwater Raiders", "Blackwing Lair", "Blade's Edge", "Bladefist", "Bleeding Hollow", 
				"Blood Furnace", "Bloodhoof", "Bloodscalp", "Bonechewer", "Borean Tundra", "Boulderfist", "Bronzebeard", "Burning Blade", "Burning Legion",
				"Caelestrasz", "Cairne", "Cenarion Circle", "Cenarius", "Cho'gall", "Chromaggus", "Coilfang", "Crushridge", "Daggerspine", "Dalaran", 
				"Dalvengyr", "Dark Iron", "Darkspear", "Darrowmere", "Dath'Remar", "Dawnbringer", "Deathbringer", "Deathwing", "Demon Soul", "Dentarg",
				"Destromath", "Dethecus", "Detheroc", "Doomhammer", "Draenor", "Dragonblight", "Dragonmaw", "Drak'Tharon", "Drak'thul", "Draka", "Drakkari",
				"Dreadmaul", "Drenden", "Durotan", "Duskwood", "Earthen Ring", "Echo Isles", "Eitrigg", "Eldre'Thalas", "Elune", "Emerald Dream", "Eonar",
				"Eredar", "Executus", "Exodar", "Farstriders", "Feathermoon", "Fenris", "Firetree", "Fizzcrank", "Frostmane", "Frostmourne", "Frostwolf",
				"Galakrond", "Gallywix", "Garithos", "Garona", "Garrosh", "Ghostlands", "Gilneas", "Gnomeregan", "Goldrinn", "Gorefiend", "Gorgonnash",
				"Greymane", "Grizzly Hills", "Gul'dan", "Gundrak", "Gurubashi", "Hakkar", "Haomarush", "Hellscream", "Hydraxis", "Hyjal", "Icecrown",
				"Illidan", "Jaedenar", "Jubei'Thos", "Kael'thas", "Kalecgos", "Kargath", "Kel'Thuzad", "Khadgar", "Khaz Modan", "Khaz'goroth", "Kil'Jaeden",
				"Kilrogg", "Kirin Tor", "Korgath", "Korialstrasz", "Kul Tiras", "Laughing Skull", "Lethon", "Lightbringer", "Lightning's Blade",
				"Lightninghoof", "Llane", "Lothar", "Madoran", "Maelstrom", "Magtheridon", "Maiev", "Mal'Ganis", "Malfurion", "Malorne", "Malygos", 
				"Mannoroth", "Medivh", "Misha", "Mok'Nathal", "Moon Guard", "Moonrunner", "Mug'thol", "Muradin", "Nagrand", "Nathrezim", "Nazgrel",
				"Nazjatar", "Nemesis", "Ner'zhul", "Nesingwary", "Nordrassil", "Norgannon", "Onyxia", "Perenolde", "Proudmoore", "Quel'Thalas", "Quel'dorei",
				"Rangnaros", "Ravencrest", "Ravenholdt", "Rexxar", "Rivendare", "Runetotem", "Sargeras", "Saurfang", "Scarlet Crusade", "Scilla", "Sen'jin",
				"Sentinels", "Shadow Council", "Shadowmoon", "Shadowsong", "Shandris", "Shattered Halls", "Shattered Hand", "Shu'halo", "Silver Hand",
				"Silvermoon", "Sisters of Elune", "Skullcrusher", "Skywall", "Smoderthorn", "Spinebreaker", "Spirestone", "Staghelm", "Steamwheedle Cartel",
				"Stonemaul", "Stormrage", "Stormreaver", "Stormscale", "Suramar", "Tanaris", "Terenas", "Terokkar", "Thaurissan", "The Forgotten Coast",
				"The Scryers", "The Underbog", "The Venture Co", "Thorium Brotherhood", "Thrall", "Thunderhorn", "Thunderlord", "Tichondrius", "Tol Barad",
				"Tortheldrin", "Trollbane", "Turalyon", "Twisting Nether", "Uldaman", "Uldum", "Undermine", "Ursin", "Uther", "Vashj", "Vek'nilash", "Velen",
				"Warsong", "Whisperwind", "Wildhammer", "Windrunner", "Winterhoof", "Wyrmrest Accord", "Ysera", "Ysondre", "Zul'jin", "Zuluhed"};
		
		JComboBox<String> menu = new JComboBox<String>(serverNames);
		servers.add(menu);
		pane.add(menu);
	}

	/***********************************************************
	 ********************   FUNCTIONALITY    *******************
	 ***********************************************************/
	
	/**
	 * Sets up the threads and calls the execute method from Execute.java to
	 * pull the iLvl for each character specified and calls writeToFile method
	 * from Execute.java to write the .csv file.
	 */
	private void getStats() throws InterruptedException{
		ExecutorService execGetStats = Executors.newFixedThreadPool(processorsx2);
		ExecutorService execDrivers = Executors.newFixedThreadPool(processorsx2);
		
		//clear the error message before getting stats
		lblErrorMessage.setText(null);
		
		//It takes time for the drivers to start up. We start all the necessary drivers
		//here before the threads execute to ensure each thread will actually have a driver when
		//the thread tries to use it.
		for(int i = 0; i < charName.length; i++){
			int f = i;
			if(charName[i] != null){
				Runnable r = () -> {
					drivers.add(new PhantomJSDriver());
					System.out.println("Driver for slot " + f + " created.");
				};
				execDrivers.execute(r);
			}
		}
		
		execDrivers.shutdown();
		//Forces the program to wait here until all threads are complete
		execDrivers.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

		int count = 0;
		for(int i = 0; i < charName.length; i++){	
			int f = i;
			
			if(charName[i] != null){
				int threadCount = count;
				Runnable r = () -> {
					System.out.println("Thread : " + threadCount + " start.");

					boolean characterFound = execute.pullElements(drivers.get(threadCount), "http://us.battle.net/wow/en/character/"
							+ servers.get(f).getSelectedItem().toString() + "/" + charName[f] + "/advanced",
							charName[f], servers.get(f).getSelectedItem().toString());
									
					if(!characterFound){
						if(lblErrorMessage.getText() == null){
							lblErrorMessage.setText("Couldn't find " + charName[f] + " on " + servers.get(f).getSelectedItem().toString() + ". ");
						}else{
							lblErrorMessage.setText(lblErrorMessage.getText() + " Couldn't find " + charName[f] + " on " + servers.get(f).getSelectedItem().toString() + ". ");
						}

					}	
				};
				execGetStats.execute(r);
				count++;
			}		
		}
		
		
		execGetStats.shutdown();
		//Forces the program to wait here until all threads are complete
		execGetStats.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

		
		
		//TODO: Ensure the file the user types ends in .csv. (May still need a few more checks)
		String fileName = fc.getSelectedFile().toString();
		if(fileName != null && fileName.length() < 3){
			execute.writeToFile(fileName + ".csv");
		}else if(fileName != null && fileName.length() >= 3){
			String csvChecker = fileName.substring(fileName.length() - 3);
			if(csvChecker.equals("csv")){
				execute.writeToFile(fileName);
			}else{
				execute.writeToFile(fileName + ".csv");
			}
		}

		
		//Clear the drivers to be able to run the program again
		System.out.println("Program complete, shutting down WebDrivers");
		for(WebDriver driver : drivers){
			driver.quit();
		}
		drivers.clear();

	}
	
	/**
	 * This automatically will read what is put into the JFormattedTextField and assign it to charName[]
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		
		for(int i = 0; i < txtFields.size(); i++){
			if(source == txtFields.get(i)){
				if(txtFields.get(i).getText().length() > 1){
					charName[i] = txtFields.get(i).getText();
				}else{
					charName[i] = null;
				}
				
			}
		}
	}
}
