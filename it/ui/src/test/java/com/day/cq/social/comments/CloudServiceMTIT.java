package com.day.cq.social.comments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adobe.cq.testing.client.CQ5Client;
import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.client.SecurityClient;
import com.adobe.granite.testing.util.QuickstartOptions;
import com.day.cq.collab.RandomString;
import com.day.cq.collab.SocialTest;
import com.day.cq.social.comments.MachineTranslationConsoleControl;
import com.day.cq.social.comments.page.CloudServiceMTPage;
import com.day.cq.social.comments.page.CommentsMTPage;

public class CloudServiceMTIT extends SocialTest {

    protected SecurityClient sAdminPublish;
    protected CloudServiceMTPage cloudServiceMTPage;

    protected CQ5Client testUser;

    @Before
    public void setup() {
        // Setting up the test. We will be getting a random string and attaching it to the string 'qecomments'
        super.setup();
        cloudServiceMTPage = new CloudServiceMTPage(driver);
    }

    @Test
    public void testConfigureTIFOnPublish() throws InterruptedException {
        browseTo(getURL(cloudServiceMTPage), true);
        WebElement tranIntegration = driver.findElement(By.cssSelector("div.product.machine-translation a"));
        tranIntegration.click();
        assertTrue("Unable to find configuration button",
            isElementPresent(By.cssSelector("#machine-translation-more")));
        WebElement defaultConfig =
            driver.findElement(By.linkText("Default configuration (Translation Integration configuration)"));
        defaultConfig.click();
        assertTrue("Unable to find go to translation integration button",
            isElementPresent(By.linkText("Go to Translation Integration Framework")));
        assertTrue("Unable to find edit button", isElementPresent(By.id("cq-gen13")));

        WebElement edit = driver.findElement(By.id("cq-gen14"));
        assertEquals("Edit", edit.getText());
        edit.click();
        assertTrue("Unable to find edit component", isElementPresent(By.id("cq-gen40")));
        assertEquals("Edit Component", driver.findElement(By.id("cq-gen40")).getText());
        assertEquals("Machine Translation Configuration", driver.findElement(By.id("ext-comp-1010")).getText());
        assertEquals("Go to Translation Integration Framework", driver
            .findElement(By.cssSelector("#ext-comp-1014 a")).getText());
        assertTrue("Unable to find category", isElementPresent(By.id("ext-comp-1024")));
    }

    @Test
    public void testConfigureMSConnectorOnPublish() throws InterruptedException {
        browseTo(getURL(cloudServiceMTPage), true);
        WebElement msConnector = driver.findElement(By.cssSelector("div.product.msft-translation a"));
        msConnector.click();
        assertTrue("Unable to find configuration button", isElementPresent(By.cssSelector("#msft-translation-more")));
        assertTrue("Unable to find default configuration",
            isElementPresent(By
                .linkText("Microsoft Translator Default Configuration (Microsoft Translation Configuration)")));
        assertTrue("Unable to find trail configuration",
            isElementPresent(By.linkText("Microsoft Translator Trial License (Microsoft Translation Configuration)")));

        WebElement defaultConfig =
            driver.findElement(By
                .linkText("Microsoft Translator Default Configuration (Microsoft Translation Configuration)"));
        defaultConfig.click();
        WebElement edit = driver.findElement(By.id("cq-gen14"));
        edit.click();

        WebElement label = driver.findElement(By.cssSelector("#x-form-el-ext-comp-1021 input"));
        WebElement attribution = driver.findElement(By.cssSelector("#x-form-el-ext-comp-1022 input"));
        WebElement workspaceID = driver.findElement(By.cssSelector("#x-form-el-ext-comp-1023 input"));
        WebElement clientID = driver.findElement(By.cssSelector("#x-form-el-ext-comp-1024 input"));
        WebElement clientSecret = driver.findElement(By.cssSelector("#x-form-el-ext-comp-1025 input"));
        label.clear();
        label.sendKeys("test");
        attribution.clear();
        attribution.sendKeys("test");
        workspaceID.clear();
        workspaceID.sendKeys("test");
        clientID.clear();
        clientID.sendKeys("AdobeAEMTrial");
        clientSecret.clear();
        clientSecret.sendKeys("AEMTrialAccount9468uTp");
        WebElement verifyButton = driver.findElement(By.id("cq-gen65"));
        verifyButton.click();
        WebElement okButton = driver.findElement(By.id("cq-gen90"));
        okButton.click();
        okButton = driver.findElement(By.id("cq-gen44"));
        okButton.click();

        Thread.sleep(2000);
        List<WebElement> property = driver.findElements(By.cssSelector("#cq-gen8 li"));
        Iterator it = property.iterator();
        it.next();

        assertEquals("Translation Label: test", ((WebElement) it.next()).getText());

    }

}
