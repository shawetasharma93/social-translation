package com.day.cq.social.comments.page;

import org.openqa.selenium.WebDriver;
import com.adobe.cq.testing.ui.selenium.page.CqPage;

public class CommentsMTPage extends CqPage {

    WebDriver driver;

    public CommentsMTPage(WebDriver browser) {
        super(browser);
        driver = browser;
    }

    public String getResourceURI() {
        return "/content/geometrixx-media/en/community/pad.html";
    }

}
