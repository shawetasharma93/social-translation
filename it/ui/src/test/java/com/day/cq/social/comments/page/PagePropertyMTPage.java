package com.day.cq.social.comments.page;

import org.openqa.selenium.WebDriver;
import com.adobe.cq.testing.ui.selenium.page.CqPage;

public class PagePropertyMTPage extends CqPage {

    WebDriver driver;

    public PagePropertyMTPage(WebDriver browser) {
        super(browser);
        driver = browser;
    }

    public String getResourceURI() {
        return "/cf#/content/geometrixx-outdoors/en.html";
    }

}
