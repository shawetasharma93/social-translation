package com.day.cq.social.comments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adobe.cq.testing.client.CQ5Client;
import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.client.SecurityClient;
import com.adobe.granite.testing.util.QuickstartOptions;
import com.day.cq.collab.RandomString;
import com.day.cq.collab.SocialTest;
import com.day.cq.social.comments.page.CommentsMTPage;

public class CommentsMTIT extends SocialTest {

    protected SecurityClient sAdminPublish;
    protected CommentsMTPage commentsMTPage;
    protected static RandomString random = new RandomString(5);
    protected static String pageName;
    protected static String commentsPath;
    protected static String commentsHtml;

    protected static final int FRENCH = 1;
    protected static final int SPANISH = 2;
    protected static final int GERMAN = 3;
    protected static final int TCHINESE = 4;
    protected static final int KOREAN = 5;
    protected static final int SWEDISH = 6;
    protected static final int TURKISH = 7;
    protected static final int DUTCH = 8;
    protected static final int RUSSIAN = 9;
    protected static final int POLISH = 10;
    protected static final int ITALIAN = 11;
    protected static final int PORT = 12;
    protected static final int SCHINESE = 13;
    protected static final int JAPANESE = 14;

    protected CQ5Client testUser;

    @Before
    public void setup() {
        // Setting up the test. We will be getting a random string and attaching it to the string 'qecomments'
        super.setup();
        commentsMTPage = new CommentsMTPage(driver);
        String randomString = random.nextString();
        pageName = "qecomments" + randomString;
        // String socialCommentsPath = getGeometirxxMediaPath() getGeometrixxOutdoorsPath() + "/" + pageName;

        org.junit.Assume.assumeTrue(ALLOW_PERFORMANCE || !name.getMethodName().contains("Perf"));

        ArrayList<String> languageArray = new ArrayList<String>();
        languageArray.add("en");
        languageArray.add("fr");
        languageArray.add("it");
        languageArray.add("de");
        languageArray.add("es");
        languageArray.add("zh_CN");
        languageArray.add("zh_TW");
        languageArray.add("sv");
        languageArray.add("pl");
        languageArray.add("ru");
        languageArray.add("tr");
        languageArray.add("nl");
        languageArray.add("ja");
        languageArray.add("ko");
        languageArray.add("pt");

        final String serverURL;
        final String rootContext;
        final String userName;
        final String password;

        serverURL = QuickstartOptions.getServerUrlPublish();
        rootContext = QuickstartOptions.getRootContextPublish();
        userName = "admin";
        password = "admin";

        // Does the work of setting up the MT console - calls FIGS array above
        // to set things up
        MachineTranslationConsoleControl mtConsoleControl =
            new MachineTranslationConsoleControl(serverURL, rootContext, userName, password);
        try {
            mtConsoleControl.dynamicEditConsole(languageArray);
        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateFrenchTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(FRENCH);
    }

    @Test
    public void testTranslateSpanishTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(SPANISH);
    }

    @Test
    public void testTranslateGermanTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(GERMAN);
    }

    @Test
    public void testTranslateTChineseTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(TCHINESE);
    }

    @Test
    public void testTranslateKoreanTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(KOREAN);
    }

    @Test
    public void testTranslateSwedishTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(SWEDISH);
    }

    @Test
    public void testTranslateTurkishTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(TURKISH);
    }

    @Test
    public void testTranslateDutchTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(DUTCH);
    }

    @Test
    public void testTranslateRussianTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(RUSSIAN);
    }

    @Test
    public void testTranslatePolishTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(POLISH);
    }

    @Test
    public void testTranslateItalianTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(ITALIAN);
    }

    @Test
    public void testTranslatePortTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(PORT);
    }

    @Test
    public void testTranslateSChineseTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(SCHINESE);
    }

    @Test
    public void testTranslateJapaneseTitleOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createDiscussion(JAPANESE);
    }

    @Test
    public void testTranslateFrenchCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(FRENCH);
    }

    @Test
    public void testTranslateSpanishCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(SPANISH);
    }

    @Test
    public void testTranslateGermanCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(GERMAN);
    }

    @Test
    public void testTranslateTChineseCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(TCHINESE);
    }

    @Test
    public void testTranslateKoreanCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(KOREAN);
    }

    @Test
    public void testTranslateSwedishCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(SWEDISH);
    }

    @Test
    public void testTranslateTurkishCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(TURKISH);
    }

    @Test
    public void testTranslateDutchCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(DUTCH);
    }

    @Test
    public void testTranslateRussianCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(RUSSIAN);
    }

    @Test
    public void testTranslatePolishCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(POLISH);
    }

    @Test
    public void testTranslateItalianCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(ITALIAN);
    }

    @Test
    public void testTranslatePortCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(PORT);
    }

    @Test
    public void testTranslateSChineseCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(SCHINESE);
    }

    @Test
    public void testTranslateJapaneseCommentOnPublish() throws InterruptedException {
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        signIn();
        browseTo(getURL(commentsMTPage), false);
        Thread.sleep(3000);
        createComment(JAPANESE);
    }

    private String createDiscussion(int languageID) throws InterruptedException {
        String title = "Test Title ";
        switch (languageID) {
            case FRENCH:
                title = title + RandomI18NString.randomFrenchString();
                break;
            case SPANISH:
                title = title + RandomI18NString.randomSpanishString();
                break;
            case GERMAN:
                title = title + RandomI18NString.randomGermanString();
                break;
            case TCHINESE:
                title = title + RandomI18NString.randomTChineseString();
                break;
            case KOREAN:
                title = title + RandomI18NString.randomKoreanString();
                break;
            case SWEDISH:
                title = title + RandomI18NString.randomSwedishString();
                break;
            case TURKISH:
                title = title + RandomI18NString.randomTurkishString();
                break;
            case DUTCH:
                title = title + RandomI18NString.randomDutchString();
                break;
            case RUSSIAN:
                title = title + RandomI18NString.randomRussianString();
                break;
            case POLISH:
                title = title + RandomI18NString.randomPolishString();
                break;
            case ITALIAN:
                title = title + RandomI18NString.randomItalianString();
                break;
            case PORT:
                title = title + RandomI18NString.randomPortString();
                break;
            case SCHINESE:
                title = title + RandomI18NString.randomSChineseString();
                break;
            case JAPANESE:
                title = title + RandomI18NString.randomJapaneseString();
                break;
        }

        createDiscussionwithTitle(title);
        waitForElement(By.linkText(title));
        WebElement readmore = driver.findElement(By.linkText(title));
        readmore.click();
        Thread.sleep(3000);
        waitForElement(By.cssSelector("a.generic-translation-button.comment-translation.comment-action"));
        assertTrue("Unable to find translate button",
            isElementPresent(By.cssSelector("a.generic-translation-button.comment-translation.comment-action"), 1000));
        WebElement translateButton =
            driver.findElement(By.cssSelector("a.generic-translation-button.comment-translation.comment-action"));
        assertTrue("Unable to click translate button", translateButton.isEnabled());
        assertEquals(translateButton.getText(), "Translate");
        System.out.println(translateButton.getText());
        translateButton.click();

        Thread.sleep(10000);
        assertEquals(translateButton.getText(), "Show Original");
        System.out.println(translateButton.getText());
        return title;
    }

    private String createComment(int languageID) throws InterruptedException {
        String comment = "";
        switch (languageID) {
            case FRENCH:
                comment = RandomI18NString.randomFrenchString();
                break;
            case SPANISH:
                comment = RandomI18NString.randomSpanishString();
                break;
            case GERMAN:
                comment = RandomI18NString.randomGermanString();
                break;
            case TCHINESE:
                comment = RandomI18NString.randomTChineseString();
                break;
            case KOREAN:
                comment = RandomI18NString.randomKoreanString();
                break;
            case SWEDISH:
                comment = RandomI18NString.randomSwedishString();
                break;
            case TURKISH:
                comment = RandomI18NString.randomTurkishString();
                break;
            case DUTCH:
                comment = RandomI18NString.randomDutchString();
                break;
            case RUSSIAN:
                comment = RandomI18NString.randomRussianString();
                break;
            case POLISH:
                comment = RandomI18NString.randomPolishString();
                break;
            case ITALIAN:
                comment = RandomI18NString.randomItalianString();
                break;
            case PORT:
                comment = RandomI18NString.randomPortString();
                break;
            case SCHINESE:
                comment = RandomI18NString.randomSChineseString();
                break;
            case JAPANESE:
                comment = RandomI18NString.randomJapaneseString();
                break;
        }
        createCommentwithComment(comment);
        waitForElement(By.cssSelector("a.generic-translation-button.comment-translation.comment-action"));
        assertTrue("Unable to find translate button",
            isElementPresent(By.cssSelector("a.generic-translation-button.comment-translation.comment-action"), 1000));
        WebElement translateButton =
            driver
                .findElement(By
                    .cssSelector("a.comment-translation.generic-translation-button.translation-button-children.comment-action"));
        assertTrue("Unable to click translate button", translateButton.isEnabled());
        assertTrue(translateButton.getText().equals("Translate"));
        System.out.println(translateButton.getText());
        translateButton.click();

        Thread.sleep(5000);
        assertTrue(translateButton.getText().equals("Show Original"));
        System.out.println(translateButton.getText());
        return comment;

    }

    private void signIn() throws InterruptedException {
        WebElement signInButton = driver.findElement(By.linkText("Sign In"));
        signInButton.click();
        WebElement userField = driver.findElement(By.id("login_component_username"));
        userField.sendKeys("admin");
        WebElement pwdField = driver.findElement(By.id("login_component_password"));
        pwdField.sendKeys("admin");
        WebElement submitButton = driver.findElement(By.className("form_button_submit"));
        submitButton.click();
        Thread.sleep(3000);
    }

    public void createDiscussionwithTitle(String title) throws InterruptedException {
        WebElement createDiscussion = driver.findElement(By.className("new-topic"));
        WebElement titleField = driver.findElement(By.name("subject"));
        WebElement submit = driver.findElement(By.cssSelector("div.scf-composer-actions input"));
        createDiscussion.click();

        titleField.clear();
        titleField.sendKeys(title);
        WebElement messageField = driver.findElement(By.cssSelector("#cke_1_contents iframe"));
        driver.switchTo().frame(messageField);
        WebElement description = driver.findElement(By.cssSelector("body"));
        description.sendKeys(title);
        driver.switchTo().defaultContent();

        submit.click();
        Thread.sleep(8000);
        driver.navigate().refresh();
    }

    public void createCommentwithComment(String comment) throws InterruptedException {
        WebElement createDiscussion = driver.findElement(By.className("new-topic"));
        WebElement titleField = driver.findElement(By.name("subject"));
        WebElement submit = driver.findElement(By.cssSelector("div.scf-composer-actions input"));
        createDiscussion.click();
        titleField.clear();
        String title = "Test Comment" + System.currentTimeMillis();
        titleField.sendKeys(title);
        WebElement messageField = driver.findElement(By.cssSelector("#cke_1_contents iframe"));
        driver.switchTo().frame(messageField);
        WebElement description = driver.findElement(By.cssSelector("body"));
        description.sendKeys("Test Comment");

        driver.switchTo().defaultContent();

        submit.click();
        Thread.sleep(8000);
        driver.navigate().refresh();
        waitForElement(By.linkText(title));
        WebElement readmore = driver.findElement(By.linkText(title));
        readmore.click();
        Thread.sleep(5000);
        WebElement createComment = driver.findElement(By.cssSelector("span.topic-reply-button.composer-button"));
        createComment.click();
        messageField = driver.findElement(By.cssSelector("#cke_2_contents iframe"));

        driver.switchTo().frame(messageField);
        description = driver.findElement(By.cssSelector("body"));
        description.sendKeys(comment);
        driver.switchTo().defaultContent();

        WebElement replyButton = driver.findElement(By.cssSelector("div.scf-composer-actions input.composer-button"));
        System.out.println(replyButton.getAttribute("value"));
        replyButton.click();

        Thread.sleep(5000);
        driver.navigate().refresh();
        Thread.sleep(5000);

    }

    // Cleaning up after ourselves by removing the comments page created.
    @After
    public void cleanup() {
        super.cleanup();
    }

}
