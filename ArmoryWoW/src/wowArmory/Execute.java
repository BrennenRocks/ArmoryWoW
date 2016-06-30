/**
 * @author Brennen Davis
 * @version 1.0
 */

package wowArmory;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.openqa.selenium.*;

public class Execute {
	
	private ArrayList<String> slotsString = new ArrayList<String>();

	/**
	 * Pull Elements will go to the player's armory page and get the player's iLvl and Armory iLvl. 
	 * If the URL or an Item is not available or being worn it will let you know.
	 * 
	 * @param driver - the WebDriver
	 * @param url - URL to which the driver will go to
	 * @param player - which Player is currently calling pullElements
	 * @param server - which Server is currently calling pullElements
	 * @return String - an error message if needs be
	 */
	public String pullElements(WebDriver driver, String url, String player, String server){	
		
		ArrayList<String> slotNames = new ArrayList<String>();
		slotNames.add("");
		slotNames.add("Head: ");
		slotNames.add("Neck: ");
		slotNames.add("Shoulders: ");
		slotNames.add("");
		slotNames.add("Chest: ");
		slotNames.add("Waist: ");
		slotNames.add("Legs: ");
		slotNames.add("Feet: ");
		slotNames.add("Wrist: ");
		slotNames.add("Gloves: ");
		slotNames.add("");
		slotNames.add("");
		slotNames.add("Ring One:");
		slotNames.add("Ring Two: ");
		slotNames.add("Trinket One: ");
		slotNames.add("Back: ");
		slotNames.add("");
		slotNames.add("");
		slotNames.add("");
		slotNames.add("");
		slotNames.add("Main Hand: ");
		slotNames.add("Offhand: ");
		
		try{		
			driver.get(url);
			Thread.sleep(2000);
	
			//Fill up the ArrayList!
			//name
			slotsString.add(driver.findElement(By.cssSelector("#profile-wrapper > div.profile-sidebar-anchor > div > div > div > div.profile-info-anchor > div > div.name > a")).getText());			
			slotsString.add(driver.findElement(By.cssSelector("#profile-wrapper > div.profile-contents > div.summary-top > div.summary-top-right > div > div.rest > span")).getText() + " ,");
					
			//Armor
			for(int i = 1; i < 23; i++){
				try{
					//strange numbers for the slots.... ask blizzard....
					if(i == 4){
						i = 5;
					}else if(i == 11){
						i = 16;
					}else if(i == 17){
						i = 21;
					}
				slotsString.add(slotNames.get(i));
				slotsString.add(driver.findElement(By.cssSelector("#summary-inventory > div.slot.slot-" + i + "> div > div > div > span.name-shadow")).getText() + ",");

				}catch (Exception e){
					slotsString.add("Item not equipped" + ",");
					System.out.println("Item not equipped.");
					//System.out.println(e.getMessage());
				}
			}
			
			//Rings and Trinkets
			for(int i = 13; i < 17; i++){
				//once again... ask blizzard about the strange numbers in their xpaths / cssSelectors
				try{
					if(i == 16){
						slotsString.add("Trinket Two: ");
					}
					if(i != 16){
						slotsString.add(slotNames.get(i));
					}
					slotsString.add(driver.findElement(By.cssSelector("#summary-inventory > div:nth-child(" + i + ") > div > div > div > span.name-shadow")).getText() + ",");
				}catch(Exception e){
					slotsString.add("Item not equipped" + ",");
					System.out.println("Item not equipped.");
					//System.out.println(e.getMessage());
				}
			}
			
			//armor ilvl get
			for(int i = 1; i < 23; i++){
				try{
					//strange numbers for the slots.... ask blizzard....
					if(i == 4){
						i = 5;
					}else if(i == 11){
						i = 16;
					}else if(i == 17){
						i = 21;
					}
				slotsString.add(driver.findElement(By.cssSelector("#summary-inventory > div.slot.slot-" + i + "> div > div > div > span.level")).getText() + ",");
				}catch (Exception e){
					slotsString.add("Item not equipped" + ",");
					System.out.println("Item not equipped.");
					//System.out.println(e.getMessage());
				}
			}
			
			//rings and trinkets ilvl get
			for(int i = 13; i < 17; i++){
				//once again... ask blizzard about the strange numbers in their xpaths / cssSelectors
				try{
					slotsString.add(driver.findElement(By.cssSelector("#summary-inventory > div:nth-child(" + i + ") > div > div > div > span.level")).getText() + ",");
				}catch(Exception e){
					slotsString.add("Item not equipped" + ",");
					System.out.println("Item not equipped.");
					//System.out.println(e.getMessage());
				}
			}
		}catch(Exception e){
			//System.out.println(e.getMessage());
			System.out.println("Couldn't find " + player + " on " + server + ".");
			return "Couldn't find " + player + " on " + server + ". ";
		}
		return "";
	}
	
	/**
	 * Write the CSV file. Must be called after Pull Elements.
	 * @param filePath - give the file name
	 */
	public void writeToFile(String filePath){

		try(PrintWriter writer = new PrintWriter(System.getProperty("user.home") + "/Desktop/"+filePath+".csv")){				
			int afterNameCounter = 0;
			int ilvlLineCounter = 0;
			//WRITE THE CSV FILE
			for(int i = 0; i < slotsString.size(); i++){
				if(i == 2 + (50 * afterNameCounter) && i != 0){
					writer.println();
					writer.print(",");
					afterNameCounter++;
				}
				if(i == 34 + (50 * ilvlLineCounter) && i != 0){
					writer.println();
					writer.print(",");
					ilvlLineCounter++;
				}
				if(i % 50 == 0 && i != 0){
					writer.println();
					writer.println();
				}
				writer.print(slotsString.get(i));				
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		slotsString.clear();
		System.out.println("Done");
	}
	

}