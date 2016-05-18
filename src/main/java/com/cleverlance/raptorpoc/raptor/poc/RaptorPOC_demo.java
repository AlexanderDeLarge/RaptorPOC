package com.cleverlance.raptorpoc.raptor.poc;


import com.cleverlance.raptorpoc.common.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class RaptorPOC_demo extends Common{

    @Test
    public void newContract() throws InterruptedException {
        Actions action = new Actions(driver);
        driver.get("http://" + Temp.xxx + "@cpsapp:8082/cps/");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//a[@href='./inc/createcontractmenu']")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.partialLinkText("smlouva o DS")).click();
        driver.findElement(By.className("datepicker-day")).sendKeys("13");
        driver.findElement(By.className("datepicker-month")).sendKeys("04");
        driver.findElement(By.name("panel:documentInfoPanel:contractSaved:year")).sendKeys("2014");
        driver.findElement(By.name("panel:sellerIdPanel:sellerNumber")).sendKeys("TISK01");


        driver.findElement(By.name("panel:clientPanel:isMale")).click();
        driver.findElement(By.name("panel:clientPanel:surname")).sendKeys("Nowak");

        actionsSendKeys("panel:clientPanel:name", "John");
        actionsClick("panel:clientPanel:taxCountry");
        actionsClick("panel:clientPanel:taxCountry");
        Select s = new Select(driver.findElement(By.name("panel:clientPanel:taxCountry")));
        s.selectByValue("CZ");
        /****************************************************************************************
         * jak to NEMA vypadat
         */

        actionsMoveTo("panel:clientPanel:pin");

        driver.findElement(By.name("panel:clientPanel:unknownPin")).click();
        /*
        try{
            driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:day")).sendKeys("01");
        }catch (StaleElementReferenceException e){
            driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:day")).sendKeys("01");
        }
        driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:month")).sendKeys("05");
        driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:year")).sendKeys("1985");
*/
        /******************************************************************************************
         * jak to MA vypadat
         */
        findElementAndFill("denNarozeni");
        findElementAndFill("mesicNarozeni");
        findElementAndFill("rokNarozeni");
        /********************************************************************************************/

        actionsMoveTo("panel:clientPanel:birthCountry");
        s = new Select(driver.findElement(By.name("panel:clientPanel:birthCountry")));
        s.selectByValue("CZ");
        actionsMoveTo("panel:contractTermsPanel:conservativeFund");
        driver.findElement(By.name("panel:contractTermsPanel:conservativeFund")).sendKeys("100");

        driver.findElement(By.xpath("//a//strong[text()='Vytvo�it']")).click();


        //Duplicity
        try{
            driver.findElement(By.name("panel:sellerIdPanel:sellerNumber")).sendKeys("TISK01");
        }catch (StaleElementReferenceException e){
            driver.findElement(By.name("panel:sellerIdPanel:sellerNumber")).sendKeys("TISK01");
        }
        actionsMoveTo("panel:clientPanel:pin");
        actionsMoveTo("panel:clientPanel:birthDateWrapper:birthDate:day");
        try{
            driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:day")).sendKeys("01");
        }catch (StaleElementReferenceException e){
            driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:day")).sendKeys("01");
        }
        driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:month")).sendKeys("05");
        driver.findElement(By.name("panel:clientPanel:birthDateWrapper:birthDate:year")).sendKeys("1985");
        actionsMoveTo("panel:contractTermsPanel:conservativeFund");
        driver.findElement(By.name("panel:contractTermsPanel:conservativeFund")).sendKeys("100");
        //Ov��it a odeslat
        driver.findElement(By.xpath("//a//strong[text()='Ov��it a odeslat']")).click();

        actionsMoveTo("panel:clientPanel:name");
        driver.findElement(By.name("panel:clientPanel:name")).sendKeys("John");
        Thread.sleep(4000);
        actionsMoveTo("panel:addressPanel:zipCode");
        driver.findElement(By.name("panel:addressPanel:street")).sendKeys("Sokolovsk�");
        driver.findElement(By.name("panel:addressPanel:houseNumber")).sendKeys("12");
        driver.findElement(By.name("panel:addressPanel:zipCode")).sendKeys("19800");

        actionsMoveTo("panel:addressPanel:city");
        Thread.sleep(500);
        //Assert.assertTrue(driver.findElement(By.name("panel:addressPanel:city")).getText().equals("Praha 98"));
        actionsMoveTo("panel:contractTermsPanel:questionareResult");
        driver.findElement(By.name("panel:contractTermsPanel:questionareResult")).sendKeys("4");
        actionsMoveTo("panel:documentInfoPanel:crsPath");
        driver.findElement(By.xpath("//a/strong[text()='Naj�t']")).click();

        action.doubleClick(driver.findElement(By.xpath("(//strong[contains(text(),'8758243175')])[1]"))).perform();
        Thread.sleep(500);
        //Ulozt cislo smlouvy
        String contractNumber = driver.findElement(By.name("panel:dsContractNumber:dsContractNumber")).getAttribute("value");
        System.out.println("Cislo smlouvy:" + contractNumber);

        //Ulo�it a odeslat
        driver.findElement(By.xpath("//a/strong[text()='Ulo�it a odeslat']")).click();

        //Kontrola smlouvy
        waitUntilPleaseWaitDisappears();
        driver.findElement(By.id("HeaderMenuIcon")).click();
        driver.findElement(By.xpath("//a/strong[text()='Produktov� podpora']")).click();
        driver.findElement(By.xpath("(//input[@title='��slo smlouvy'])[1]")).click();
        WebElement contractInput = driver.findElement(By.xpath("(//input[@title='��slo smlouvy'])[2]"));
        System.out.println(contractInput.getTagName());

        contractInput.sendKeys(contractNumber);
        contractInput.sendKeys(Keys.ENTER);
        //driver.findElement(By.xpath("//a/strong[text()='OK']")).click();
        action.doubleClick(driver.findElement(By.xpath("//strong[text()='" + contractNumber + "']"))).perform();
        System.out.println(driver.findElement(By.xpath("(//small[@id='percent'])[1]")).getAttribute("textContent"));
        Assert.assertTrue(driver.findElement(By.xpath("(//small[@id='percent'])[1]")).getAttribute("textContent").equals("100"));

        //Korespondence
        driver.findElement(By.id("HeaderMenuIcon")).click();
        driver.findElement(By.xpath("//a/strong[text()='Korespondence']")).click();
        driver.findElement(By.partialLinkText("Moje dnes vygenerovan�")).click();
    }
    //@Test
    public void importBatch(){
        //Import souboru s datov�mi v�tami
        Actions action = new Actions(driver);
        driver.get("http://" + Temp.xxx + "@cpsapp:8082/cps/");
        driver.manage().window().maximize();

        driver.findElement(By.linkText("Import souboru s datov�mi v�tami")).click();
        driver.findElement(By.name("fileUpload:fileUploadForm:fileUpload")).sendKeys(pathToBatches + "400_01.xef");
        //Naimportovat
        driver.findElement(By.xpath("//a/strong[text()='Naimportovat']")).click();
        waitUntilPleaseWaitDisappears();
    }

    @After
    public void tearDown(){
        //driver.quit();
    }



}
