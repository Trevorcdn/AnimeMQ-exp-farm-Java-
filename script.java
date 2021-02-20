import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
//working for patch 0.17.12

public class amqScript extends Thread {
    public static int dirtyBit;
    public static void login(WebDriver driver, String userEmail, String password) throws InterruptedException {

        WebElement txtEmail = driver.findElement(By.xpath("//input[@id='loginUsername']"));
        WebElement txtPassword = driver.findElement(By.xpath("//input[@id='loginPassword']"));
        txtEmail.sendKeys(userEmail);
        txtPassword.sendKeys(password);
        driver.findElement(By.partialLinkText("Got it!")).click(); //clicks accept cookies

        //login button is an onclick function so we have to so an action class
        WebElement input = driver.findElement(By.id("loginButton"));
        new Actions(driver).moveToElement(input).click().perform();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        dirtyBit = 1;
        Thread.sleep(2000);

    }

    public static void hostSolo(WebDriver driver, String numOfSong) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //Cannot click on mpPlaySolo because loadingScreen obscure it so wait for invisibility of it
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadingScreen")));
        driver.findElement(By.id("mpPlaySolo")).click();

        WebElement numSongTxt = driver.findElement(By.xpath("//input[@id='mhNumberOfSongsText']"));
        numSongTxt.click();
        numSongTxt.clear();
        numSongTxt.clear();
        numSongTxt.sendKeys(numOfSong);

        WebElement host = driver.findElement(By.id("mhHostButton"));
        new Actions(driver).moveToElement(host).click().perform();
        Thread.sleep(2000);
    }

    public static void startGame(WebDriver driver){
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("lbStartButton"))).click();
        }
        catch(NoSuchWindowException we){
            System.out.println("Warning: No Window Detected! Exiting...");
            System.exit(0);
        }
        catch (Exception e){
            System.out.println("Warning: Exception in startGame");
        }
    }

    public static void block_recording(WebDriver driver){
        while(true){
            try{
                WebElement txt = driver.findElement(By.id("qpHiderText"));
                String timerTxt = txt.getText();
                if(timerTxt.equals("Loading.")) continue;
                //To make sure we are inputting the answer only when timer is between 15&21
                if(Integer.parseInt(timerTxt)  >= 15 && Integer.parseInt(timerTxt)  < 21)
                    break;

            }
            catch(NoSuchWindowException we){
                System.out.println("Warning: No Window Detected! Exiting...");
                System.exit(0);
            }
            catch (Exception e){
                //System.out.println("Warning: Some Other exception in Block_recording");
                break;
            }
        }
    }
    public static void answer(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 30);

        if(dirtyBit == 1){
            wait.until(ExpectedConditions.elementToBeClickable(By.id("qpVolumeIcon"))).click();
            dirtyBit = 0;
        }

        while(true){
            try{
                if (Thread.interrupted())  // Clears interrupted status!
                    throw new InterruptedException();
                block_recording(driver);
                wait.until(ExpectedConditions.elementToBeClickable(By.id("qpAnswerInput")));
                WebElement box = driver.findElement(By.id("qpAnswerInput"));
                box.sendKeys("One Piece");
                box.sendKeys(Keys.RETURN);
                Thread.sleep(5000);

            }
            catch(InterruptedException ie){
                driver.quit();
                break;
            }
            catch(NoSuchWindowException we){
                System.out.println("Warning: No Window Detected! Exiting...");
                System.exit(0);
            } catch (Exception e){
                System.out.println("Warning: Some Other exception"); // do nothing
                //if startGame element exist then call startGame();
                if(driver.findElement(By.id("lbStartButton")).isDisplayed())
                    startGame(driver);
            }


        }
    }
}
