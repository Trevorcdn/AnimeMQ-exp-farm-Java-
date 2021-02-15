import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Logging in
        BufferedReader reader;
        String sCurrentLine;
        ArrayList<String> userInfo = new ArrayList<>(); //[0] = username while [1] = password

        try{
            URL path = Main.class.getResource("amqTxt.txt");
            File f = new File(path.getFile());
            reader = new BufferedReader(new FileReader(f));
            Closeable resource = reader;
            while ((sCurrentLine = reader.readLine()) != null) {
                if(sCurrentLine.trim().startsWith("//") || sCurrentLine.isEmpty()) {
                    resource.close();
                    break;
                }
                String[] lineArray  = sCurrentLine.split("\\s+"); //Parsing Username and password from text file
                userInfo.add(lineArray[1]);
            }
        }
        catch(Exception e){
            System.exit(0);
        }



        String userEmail = userInfo.get(0); //userInfo[0]
        String password = userInfo.get(1); //userInfo[1]
        int numOfSong = 40;

        String exePath = "C:\\geckodriver-v0.26.0-win64\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", exePath);
        WebDriver driver= new FirefoxDriver();
        String loginURL = "https://animemusicquiz.com/";
        driver.get(loginURL);

        amqScript.login(driver, userEmail, password);
        amqScript.hostSolo(driver, Integer.toString(numOfSong));
        amqScript.startGame(driver);
        amqScript.answer(driver);


    }
}
