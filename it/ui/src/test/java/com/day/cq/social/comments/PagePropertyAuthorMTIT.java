package com.day.cq.social.comments;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adobe.cq.testing.client.CQ5Client;
import com.adobe.granite.testing.client.SecurityClient;
import com.day.cq.collab.SocialTest;
import com.day.cq.social.comments.page.PagePropertyMTPage;

public class PagePropertyAuthorMTIT extends SocialTest {

    protected SecurityClient sAdminPublish;
    protected PagePropertyMTPage pagePropertyMTPage;

    protected CQ5Client testUser;

    @Before
    public void setup() {
        super.setup();
        pagePropertyMTPage = new PagePropertyMTPage(driver);
    }

    @Test
    public void testDefaultConfig() throws InterruptedException {
        browseTo(getURL(pagePropertyMTPage), true);
        waitForElement(By.id("cq-sk-tabpanel__ext-comp-1172"));
        WebElement page = driver.findElement(By.id("cq-sk-tabpanel__ext-comp-1172"));
        page.click();
        waitForElement(By.xpath("//button[contains(.,'Page Properties...')]"));
        WebElement pageProperty = driver.findElement(By.xpath("//button[contains(.,'Page Properties...')]"));
        Thread.sleep(5000);
        pageProperty.click();
        waitForElement(By.id("cq-cf-frame"));
        WebElement mainPage = driver.findElement(By.id("cq-cf-frame"));
        driver.switchTo().frame(mainPage);
        WebElement cloudService = driver.findElement(By.id("ext-comp-1414__ext-comp-1530"));
        cloudService.click();
        assertTrue("Unable to find trail configuration",
            isElementPresent(By.cssSelector("input[value='/etc/cloudservices/msft-translation/msft_trial']")));
        driver.switchTo().defaultContent();

    }

}
