package com.day.cq.social.comments.page;

import org.openqa.selenium.WebDriver;
import com.adobe.cq.testing.ui.selenium.page.CqPage;

public class CloudServiceMTPage extends CqPage {

    WebDriver driver;

    public CloudServiceMTPage(WebDriver browser) {
        super(browser);
        driver = browser;
    }

    public String getResourceURI() {
        return "/etc/cloudservices.html";
    }

}
