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
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.sql.SQLException;

public class RaptorPOC extends Common{

    @Test
    public void importBatch() throws InterruptedException, IOException {
        driver.get("http://" + Temp.xxx + "@cpsapp:8084/cps/");
        driver.manage().window().maximize();
        String newFileName = editBatch("test.xef"/*, newFileName*/);

        findElement("importDavkyLnk").click();
        findElement("importDavkyInput").sendKeys(pathToBatches + newFileName);
        //Naimportovat
        findElement("importDavkyBtn").click();
        waitUntilPleaseWaitDisappears();

        actionsDoubleClick("cisloSmlovyLnk");

        //Uložit a odeslat
        Thread.sleep(1000);
        findElement("ulozitAodeslatBtn").click();
        waitUntilPleaseWaitDisappears();

        //Korespondence
        findElement("hlavniMenuLnk").click();
        findElement("korespondenceLnk").click();
        findElement("mojeDnesVygenerovanaKorespondenceLnk").click();
        Assert.assertTrue(isContractGenerated(contractNumber));

        //Kontrola smlouvy
        actionsDoubleClick("prvniSmlouvaVseznamuDoubleClick");

        //Stahnout PDF
        findElement("lokalniTiskBtn").click();
        findElement("stahnoutPDFLnk").click();

        Thread.sleep(3000);
        Assert.assertTrue(isFileDownloaded("C:\\Users\\AKU\\Downloads\\"));
        print("PASSED");


    }


    @Test
    public void tabSequence() throws InterruptedException {
        print("Pocet datovych variant: " + dataset.size() + 1);
        for(datasetNumber = 0; datasetNumber < dataset.size(); datasetNumber++) {
            print(datasetNumber + " : " + dataset);
            driver.get("http://" + Temp.xxx + "@cpsapp:8084/cps/");
            maximizeWindow();
            findElement("vytvoritSmlouvuDPSLnk").click();
            switchToDefaultContent();
            findElement("novaSmlouvaOdpsLnk").click();

            //TAB sekvence
            findElementAndFill("cisloDokumentuEdit");
            findElement("cisloDokumentuEdit").sendKeys(Keys.TAB);
            switchToSendKeysAndTAB("01", 1);
            switchToSendKeysAndTAB("05", 1);
            switchToSendKeysAndTAB("1985", 1);
            switchToSendKeysAndTAB("TISK01", 5);
            switchToSendKeysAndTAB("Nowak", 2);
            switchToSendKeysAndTAB("John", 0);
            driver.findElement(By.name("panel:clientPanel:isMale")).click();
        }
    }

    @Test
    public void createDPSContract() throws InterruptedException, SQLException {
        print("Pocet datovych variant: " + dataset.size() + 1);
        for(datasetNumber = 0; datasetNumber < dataset.size(); datasetNumber++) {
            try {
                driver.get("http://" + Temp.xxx + "@cpsapp:8084/cps/");
                maximizeWindow();
                print(dataset.get(datasetNumber).get("vytvoritSmlouvuDPSLnk"));
                findElement("vytvoritSmlouvuDPSLnk").click();
                switchToDefaultContent();
                findElement("novaSmlouvaOdpsLnk").click();

                findElementAndFill("denSepsaniSmlovyEdit");
                findElementAndFill("mesicSepsaniSmlouvyEdit");
                findElementAndFill("rokSepsaniSmlouvyEdit");
                findElementAndFill("cisloProdejceEdit");
                Assert.assertTrue(findElement("typSmlouvyVP").getAttribute("value").equals("301"));

                /**
                 * Typovani smlovy
                 */
                findElementAndFill("jmenoKlientaEdit");
                findElement("pohlaviMuzChck").click();
                findElementAndFill("prijmeniKlientaEdit");

                //Obcanstvi, danove rezidentstvi selecty
                selectByValue("obcanstviSel");
                selectByValue("danovaRezidenceSel");

                //Generovat RC, zkontrolovat duplicitu
                String birthNumber = calcRC("M", "35");
                if (DBUtils.rcAlreadyExists(birthNumber)) {
                    birthNumber = calcRC("M", "35");
                }
                print("Vygenerovano rodne cislo: " + birthNumber);
                jsClick("rodneCisloEdit");
                Thread.sleep(500);
                findElement("rodneCisloEdit").sendKeys(birthNumber);
                findElement("rodneCisloEdit").clear();
                findElement("rodneCisloEdit").sendKeys(birthNumber);
                print("Vyplnene RC: " + findElement("rodneCisloEdit").getAttribute("value"));
                selectByValue("zemeNarozeniSel");

                //Adresa
                findElementAndFill("trvaleBytemUliceEdit");
                findElementAndFill("trvaleBytemCisloOrientacniEdit");
                findElementAndFill("trvaleBytemPSCEdit");
                //Assert.assertTrue(driver.findElement(By.name("panel:addressPanel:city")).getAttribute("value").equals("Praha 98"));

                //Podilovy fond, investicni dotaznik
                findElementAndFill("procentoKonzervativniFondEdit");
                findElementAndFill("investicniDotaznikEdit");

                //Mesicni prispevek
                findElementAndFill("mesicniPrispevekEdit");
                findElement("ucastnikemLbl").click();
                //findElement("zpusobPlaceniJinyRdbtn").click();
                //Thread.sleep(1500);
                waitUntilPleaseWaitDisappears();
                jsClick("zpusobPlaceniJinyRdbtn");
                //actionsClick("zpusobPlaceniJinyRdbtn");
                //tabAndSendKeys(2,Keys.ENTER);
                //findElement("frekvencePlaceniMesicniRdbtn").click();

                //Odeslat formular 1. faze
                findElement("vytvoritSmlouvuDPSBtn").click();

                //**
            /* Kontrola 4 oci
            */
                waitUntilPleaseWaitDisappears();
                findElementAndFill("cisloProdejceEdit");

                //Rodne cislo
                print("Podruhe zadane RC: " + birthNumber);
                findElement("rodneCisloEdit").sendKeys(birthNumber);
                print("Vyplnene RC: " + findElement("rodneCisloEdit").getAttribute("value"));

                //Podilovy fond, investicni dotaznik
                findElementAndFill("procentoKonzervativniFondEdit");
                //FIXMI Zmena? Driv bylo editovatelne
                //findElementAndFill("investicniDotaznikEdit");

                //Mesicni prispevek
                findElementAndFill("mesicniPrispevekEdit");
                findElement("ucastnikemLbl").click();
                //findElement("zpusobPlaceniJinyRdbtn").click();
                Thread.sleep(500);
                jsClick("zpusobPlaceniJinyRdbtn");

                //Ověřit a odeslat
                findElement("overitAodeslatFormularBtn").click();
                waitUntilPleaseWaitDisappears();
                //findElementAndFill("mesicniPrispevekEdit");
                //findElement("zpusobPlaceniJinyRdbtn").click();
                //findElement("frekvencePlaceniMesicniRdbtn").click();
                //findElement("overitAodeslatFormularBtn").click();

                //Cislo smlouvy
                String contractNumber = findElement("cisloSmlouvyText").getAttribute("value");
                print("Cislo smlouvy:" + contractNumber);

                //Kontrola smlouvy
                waitUntilPleaseWaitDisappears();
                findElement("hlavniMenuLnk").click();
                findElement("produktovaPodporaLnk").click();
                WebElement contractInput = findContract();
                print(contractInput.getTagName());

                contractInput.sendKeys(contractNumber);
                contractInput.sendKeys(Keys.ENTER);

                doubleClickOnContractNumber(contractNumber);
                print(findElement("procentaKonzervativniFondVP").getAttribute("textContent"));
                Assert.assertTrue(findElement("procentaKonzervativniFondVP").getAttribute("textContent").equals("100"));

                //Korespondence
                findElement("hlavniMenuLnk").click();
                findElement("korespondenceLnk").click();
                findElement("mojeDnesVygenerovanaKorespondenceLnk").click();
                Assert.assertTrue(isContractGenerated(contractNumber));

                //Kontrola smlouvy
                actionsDoubleClick("prvniSmlouvaVseznamuDoubleClick");

                //Stahnout PDF
                findElement("lokalniTiskBtn").click();
                findElement("stahnoutPDFLnk").click();
                Thread.sleep(3000);

                Assert.assertTrue(isFileDownloaded("C:\\Users\\AKU\\Downloads\\"));
                print("Nazev prave uspesne ukonceneho testu: " + testName.getMethodName() +
                        "    Cislo datasetu: " + String.valueOf(datasetNumber + 1));
            }catch (Exception e){
                e.printStackTrace();
                print("Test " + testName.getMethodName() + " dopadl neuspesne. Cislo datasetu je: " + datasetNumber);
            }
        }
    }

    @After
    public void tearDown(){
        datasetNumber = 0;
        //driver.quit();
    }

}
