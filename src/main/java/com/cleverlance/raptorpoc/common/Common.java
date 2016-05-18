package com.cleverlance.raptorpoc.common;


import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.junit.Assert.fail;

public class Common extends Temp{

    protected static WebDriver driver;
    final static int implicitWaitInSeconds = 10;
    final protected static String pathToBatches = "c:\\Tools\\Java_workspace\\RaptorAutomat\\";
    protected static HashMap<String, String> method;
    protected static HashMap<String, String> elements;
    protected static ArrayList<HashMap<String, String>> dataset;
    private static int loopCounter = 0;
    protected static int datasetNumber;
    protected static String contractNumber;

    protected static Actions actions;

    DBUtils sql = new DBUtils();

    public static void print(Object o){
        System.out.println(o);
    }

    public static void maximizeWindow(){

        driver.manage().window().maximize();
    }

    public static void switchToDefaultContent(){
        driver.switchTo().defaultContent();
    }
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("no-sandbox");
        System.setProperty("webdriver.chrome.driver", "c:\\SeleniumNode\\chromedriver.exe");
        driver = new ChromeDriver(options);

        //driver = new FirefoxDriver();
        //System.setProperty("webdriver.firefox.bin", "c:\\Program Files (x86)\\Mozilla Firefox\\Firefox.exe");
        driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS);
        ObjectMapBuilder omb = new ObjectMapBuilder();
        method = omb.getObjectMap(ObjectMapBuilder.METHOD, "RaptorObjectMap.xls");
        elements = omb.getObjectMap(ObjectMapBuilder.ELEMENT, "RaptorObjectMap.xls");
        print("Nazev testu: " + testName.getMethodName());
        dataset = DataSetBuilder.getDataset("RaptorDataset.xls", testName.getMethodName());

         actions = new Actions(driver);
    }

    public static WebElement findElement(String name) {
        WebElement element = null;
        try {
        element = actionsMoveTo(name);
            /*if (method.get(name).equals("By.id")) {
                element = driver.findElement(By.id(elements.get(name)));
            } else if (method.get(name).equals("By.name")) {
                element = driver.findElement(By.name(elements.get(name)));
            } else if (method.get(name).equals("By.xpath")) {
                element = driver.findElement(By.xpath(elements.get(name)));
            } else if (method.get(name).equals("By.linkText")) {
                element = driver.findElement(By.linkText(elements.get(name)));
            } else if (method.get(name).equals("By.className")) {
                element = driver.findElement(By.className(elements.get(name)));
            } else if (method.get(name).equals("By.partialLinkText")) {
                element = driver.findElement(By.partialLinkText(elements.get(name)));
            }*/
            print("Element " + elements.get(name) + " je nalezen");
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            print("Element zmizel z DOM, zkousim dohledat jeste jednou");
            print(name + "Stale element found!!");
            loopCounter++;
            if (loopCounter == 3) {
                loopCounter = 0;
                fail("Stale element infinite loop!");
            }
            findElement(name);
        } catch (ElementNotVisibleException e) {
            print("Element " + elements.get(name) + " neni viditelny, nelze s nim pracovat");
            fail("Element " + elements.get(name) + " neni viditelny, nelze s nim pracovat");
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            print("Element " + elements.get(name) + " neexistuje!");
            fail("Element " + elements.get(name) + " neexistuje!");
            e.printStackTrace();
        } catch (NullPointerException e) {
            print("Hledani  " + name + "  " + elements.get(name) + " skoncilo NullPointerException! Pravdepodobne chybi v objektove mape");
            fail("Hledani  " + name + "  " + elements.get(name) + " skoncilo NullPointerException! Pravdepodobne chybi v objektove mape");
            e.printStackTrace();
        }
        return element;
    }

    protected static String findElementAndFill(String elementName) throws InterruptedException {
        int i = 0;
        try {
            findElement(elementName).clear();
            if(datasetNumber != dataset.size()) {
                findElement(elementName).sendKeys(dataset.get(datasetNumber).get(elementName));
            }
        }catch (StaleElementReferenceException e) {
            i++;
            Thread.sleep(100);
            if (i > 3) fail("Chyba StaleElementException po dvou sekundach!!!");
            findElementAndFill(elementName);
        }
        return findElement(elementName).getAttribute("value");
    }

    protected static void actionsClick(String elementName) {
        //actionsMoveTo(elementName);
        WebElement element = findElement(elementName);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    protected static void actionsSendKeys(String elementName, String keys) {
        WebElement element = findElement(elementName);
        Actions actions = new Actions(driver);
        //actions.moveToElement(element).click().perform();
        actions.moveToElement(element).sendKeys(keys).perform();
    }

    protected static void actionsSendKeys(String elementName) {
        actionsMoveTo(elementName);
        actions.sendKeys(dataset.get(datasetNumber).get(elementName)).perform();
    }

    protected static WebElement actionsMoveTo(String elementName) {
        try {
            if (method.get(elementName).equals("By.id")) {
                actions.moveToElement(driver.findElement(By.id(elements.get(elementName)))).perform();
                return driver.findElement(By.id(elements.get(elementName)));
            } else if (method.get(elementName).equals("By.name")) {
                actions.moveToElement(driver.findElement(By.name(elements.get(elementName)))).perform();
                return driver.findElement(By.name(elements.get(elementName)));
            } else if (method.get(elementName).equals("By.xpath")) {
                actions.moveToElement(driver.findElement(By.xpath(elements.get(elementName)))).perform();
                return driver.findElement(By.xpath(elements.get(elementName)));
            } else if (method.get(elementName).equals("By.linkText")) {
                actions.moveToElement(driver.findElement(By.linkText(elements.get(elementName)))).perform();
                return driver.findElement(By.linkText(elements.get(elementName)));
            } else if (method.get(elementName).equals("By.className")) {
                actions.moveToElement(driver.findElement(By.className(elements.get(elementName)))).perform();
                return driver.findElement(By.className(elements.get(elementName)));
            } else if (method.get(elementName).equals("By.partialLinkText")) {
                actions.moveToElement(driver.findElement(By.partialLinkText(elements.get(elementName)))).perform();
                return driver.findElement(By.partialLinkText(elements.get(elementName)));
            }
            print("Element " + elements.get(elementName) + " je nalezen");
        }catch (StaleElementReferenceException e){
            print("Element zmizel z DOM, zkousim dohledat jeste jednou");
            print(elementName + " Stale element found!!");
            loopCounter++;
            if (loopCounter == 3) {
                loopCounter = 0;
                fail("Stale element infinite loop!");
            }
        }
        catch (Exception e){
            print("Chyba pri navigaci v ramci formulare!");
            e.printStackTrace();
            fail();
        }
        return null;
    }

    protected static void actionsMoveTo(WebElement element) {
        actions.moveToElement(element).perform();
    }

    protected static void actionsDoubleClick(String elementName){
        WebElement element = findElement(elementName);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).doubleClick().perform();
    }

    protected static void waitUntilPleaseWaitDisappears(){
        int i = 0;
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            if (driver.findElement(By.id("please-wait-panel")) != null) {
                while (!driver.findElements(By.id("please-wait-panel")).isEmpty()) {
                    //print("Waiting 300ms");
                    Thread.sleep(3000);
                    i++;
                    if (i == 6) break;
                }
            }
        } catch (Exception e) {
            System.out.println("Splashscreen please-wait-panel nenalezen");
        } finally {
            driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS);
        }
    }

    public static String randomString(int length) {
        String result = "";
        Random r = new Random();
        String alfabeth = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        for (int i = 0; i <= length; i++) {
            result = result + alfabeth.charAt(r.nextInt(alfabeth.length()));
        }
        return result;
    }

    public static String randomNumber(int length) {
        String result = "";
        Random r = new Random();
        String alfabeth = "123456789";
        for (int i = 1; i <= length; i++) {
            result = result + alfabeth.charAt(r.nextInt(alfabeth.length()));
        }
        return result;
    }

    public static void switchToSendKeysAndTAB(String keys, int numberOfTabs){
        driver.switchTo().activeElement().sendKeys(keys);
        for (int i = 1; i <= numberOfTabs; i++){
            driver.switchTo().activeElement().sendKeys(Keys.TAB);
        }
    }

    public static void tabAndSendKeys(int numberOfTabs, Keys keys){
        try {
            for (int i = 1; i <= numberOfTabs; i++) {
                driver.switchTo().activeElement().sendKeys(Keys.TAB);
            }
            try {
                driver.switchTo().activeElement().sendKeys(keys);
            } catch (StaleElementReferenceException e) {
                print("Stale element exception in tab sequence catched!");
                driver.switchTo().activeElement().sendKeys(Keys.TAB);
            }
        }catch(StaleElementReferenceException e){
            print("Magie");
        }
        print(keys + "  :  " + driver.switchTo().activeElement().getAttribute("id"));
    }

    public static String editBatch(String fileName) throws IOException {
        String content = new String(readAllBytes(get(fileName)));
        String newFileName = String.valueOf(Integer.parseInt(new String(readAllBytes(get("c:\\Tools\\Java_workspace\\RaptorAutomat\\newFileName.txt")))) + 1);
        contractNumber = randomNumber(9);
        File f = new File(newFileName + "_01.xef");
        FileWriter fw = new FileWriter(f);
        String rc = calcRC("M", "30");
        //Cislo smlouvy
        content = content.replaceAll("X", contractNumber);
        //Rodne cislo
        content = content.replaceAll("ASDF", rc);
        //Nazev davky
        content = content.replaceAll("QWE", newFileName.substring(0,3));
        //content = content.replaceAll("MNB", contractNumber);
        //ZCC datum narozeni
        String datumNarozeni = "19" + rc.substring(0,2) + rc.substring(2,4) + rc.substring(4,6);
        content = content.replaceAll("ZCC", datumNarozeni);
        //CLUID
        content = content.replaceAll("MNB", randomString(20));
        fw.write(content);
        fw.flush();
        File ff = new File("newFileName.txt");
        FileWriter ffw = new FileWriter(ff);
        ffw.write(newFileName);
        ffw.flush();
        System.out.println(contractNumber);
        System.out.println(rc + "    " + datumNarozeni);
        return newFileName + "_01.xef";
    }

    public static String calcRC(String pohlavi, String vek) {

        Calendar cal = Calendar.getInstance();
        long c34 = 0;
        int vekInt = Integer.parseInt(vek);
        String result = new String();

        SimpleDateFormat f = new SimpleDateFormat("YYYY");

        Random generator = new Random();

        long c12 = Integer.parseInt(f.format(cal.getTime())) - vekInt;
        if (c12 < 2000) {
            c12 = c12 - 1900;
        } else {
            c12 = c12 - 2000;
        }

        // long c1 = generator.nextInt(3)+6;
        // long c2 = generator.nextInt(9);
        if (pohlavi.equals("M")) {
            c34 = generator.nextInt(11) + 1;
        } else if (pohlavi.equals("Z")) {
            c34 = generator.nextInt(11) + 51;
        } else {
            System.out.println("Spatne pohlavi na vstupu, musi byt M nebo Z");
        }
        long c56 = generator.nextInt(27) + 1;
        long c789 = generator.nextInt(999);
        long c10 = (((c12 * 100 + c34) * 100 + c56) * 1000 + c789) % 11;
        if (c10 == 10) {
            c10 = 0;
        }

        long rc = ((((c12 * 100 + c34) * 100 + c56) * 1000 + c789) * 10 + c10);

        result = String.valueOf(rc);

        // Osetreni nul na zacatku RC v pripade deti
        if (result.length() == 9) {
            result = "0" + result;
        } else if (result.length() == 8) {
            result = "00" + result;
        } else if (result.length() == 7) {
            result = "000" + result;
        }

        // kdyz je vic nez sto let
        if (vekInt > 100) {
            result = result.substring(0, 8);
        }

        //return result.replaceAll(result.substring(0,6), result.substring(0,6) + "/");
        return result;
    }


    protected static void selectByValue(String element, String value) {
        Select s = new Select(findElement(element));
        s.selectByValue(value);
    }

    protected static void selectByValue(String element) {
        Select s = new Select(findElement(element));
        s.selectByValue(dataset.get(datasetNumber).get(element));
    }

    protected static WebElement findContract(){
        findElement("vyhledatSmlouvuEdit1").click();
        return findElement("vyhledatSmlouvuEdit2");
    }

    protected static void doubleClickOnContractNumber(String contractNumber){
        actions.doubleClick(driver.findElement(By.xpath("//strong[text()='" + contractNumber + "']"))).perform();
    }

    protected static boolean isContractGenerated(String contractNumber){
        return driver.findElement(By.xpath("//tbody/*[contains(.,'"+
                contractNumber +"') and contains(.,'Vygenerován')]")).isDisplayed() == true;
    }

    //Doubleclick na smlouvu, kontrola vygenerovani PDF, je nutne cekat, proto for cyklus
    protected static boolean isContractPDFGenerated(String contractNumber) throws InterruptedException {
        waitUntilPleaseWaitDisappears();
        for (int i = 0; i < 50; i++) {
            try {
                driver.findElement(By.xpath("//tbody/*[contains(.,'" + contractNumber + "')]"));
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
        actions.doubleClick(driver.findElement(By.xpath("//tbody/*[contains(.,'" + contractNumber + "')]"))).perform();
        return driver.findElement(By.xpath("//div[@id='cor-pdf-prev']")).isDisplayed();
    }

    protected boolean isFileDownloaded(String downloadPath){
        File dir = new File(downloadPath);
        FileFilter fileFilter = new WildcardFileFilter("lokalni_tisk*.pdf");
        File[] files = dir.listFiles(fileFilter);
        if(files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            return true;
        } else {
            return false;
        }

    }

    protected static void jsClick(String elementName) {
        WebElement element = findElement(elementName);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void main(String args[]) throws IOException {
        print(calcRC("M", "40"));
        //editBatch("test.xef", "test1.txt");
    }

}
