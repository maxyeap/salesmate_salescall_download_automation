package com.example.salesmate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SalesmateAutomation {
    private WebDriver driver;
    private List<String> clients = new ArrayList<>();

    private String client;

    public SalesmateAutomation(String driver, String client) {
        if (driver.equals("Chrome")) {
            setDriver(new ChromeDriver());
        } else {
            setDriver(new SafariDriver());
        }
        clients.add("RMT");
        clients.add("SAGE");
        clients.add("BM");
        clients.remove(client);
        setClient(client);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getClient() { return client; }

    public List<String> getClients() {
        return clients;
    }


    public void setClient(String client) { this.client = client; }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String email, String password, String url) throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get(url);
        driver.manage().window().maximize();
        Thread.sleep(5000);
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        emailInput.sendKeys(email);
        passwordInput.sendKeys((password));
        driver.findElement(By.id("btnSubmit")).click();
        Thread.sleep(10000);
    }

    public List<HashMap> getDownloadLinks(int maxIndex) throws InterruptedException {
        WebDriver driver = getDriver();
        String client = getClient();
//        Thread.sleep(5000);
        WebElement container = driver.findElement(By.cssSelector(".ag-body-viewport"));
        WebElement innerContainer = driver.findElement(By.className("ag-center-cols-container"));
        Actions action = new Actions(driver);
        action.moveToElement(container);
        int verticalOrdinate = 1000;
        int rowIndex = 0;
        List<HashMap> result = new ArrayList<>();
        while (true) {
            if (rowIndex == maxIndex + 1) { return result; }

            if (!containerExist(innerContainer, rowIndex)) {
                scroll(driver, container, verticalOrdinate);
                verticalOrdinate += 1200;
                Thread.sleep(2000);
            }
            WebElement child = innerContainer.findElement(By.cssSelector(String.valueOf("div[row-index=\"" +
                    rowIndex +
                    "\"]")
            ));

            // Check and see whether the call is recorded or not
            List<WebElement> recordingLinkNode = getRecordingLink(child);
            String durationText = getDuration(child).getText();
            WebElement nameNode = getName(child);
            String nameText = nameNode.getText();
            // Move to next call log if the call is not recorded
            // or duration is less than a minute
            if (recordingLinkNode.size() == 0 || !durationText.contains("m") || isOtherClient(nameText)) {
                rowIndex += 1;
                continue;
            }
            if (nameText.contains(client)) { nameText = extractLeadName(nameNode); }

            try {
                if (nameText.equals("Add Lead") || isOtherClientsLead(nameNode)) {
                    rowIndex += 1;
                    continue;
                }
            } catch (Exception e) {
                continue;
            }
            System.out.println("Call with " +
                    nameText +
                    " on " +
                    getDate(child).getText() +
                    " is retrieved successfully.");
            String dateText = getDate(child).getText();
            String agentText = getAgent(child).getText();
            String recordingLink = recordingLinkNode.get(0).getAttribute("href");
            result.add(generateCallDetails(dateText, durationText, nameText, recordingLink, agentText));
            rowIndex += 1;
        }
    }

    public void scroll(WebDriver driver, WebElement container, int verticalOrdinate) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollTop = arguments[1]",
                container,
                verticalOrdinate);
    }

    public WebElement getDate(WebElement node) {
        return node.findElement(By.cssSelector("div[col-id=\"date\"]"));
    }

    public List<WebElement> getRecordingLink(WebElement node) {
        WebElement recording = node.findElement(By.cssSelector("div[col-id=\"recording\"]"));
        return recording.findElements(By.id("downloadLink"));
    }

    public WebElement getDuration(WebElement node) {
        return node.findElement(By.cssSelector("div[col-id=\"duration\"]"));
    }

    public WebElement getAgent(WebElement node) {
        return node.findElement(By.cssSelector("div[col-id=\"user\"]"));
    }

    public WebElement getName(WebElement node) {
        WebElement name = node.findElement(By.cssSelector("div[col-id=\"name\"]"));
        return name.findElement(By.cssSelector("span a"));
    }

    public HashMap generateCallDetails(String date, String duration, String name, String url, String agent) {
        HashMap callDetails = new HashMap(5);
        callDetails.put("date", date);
        callDetails.put("duration", duration);
        callDetails.put("name", name);
        callDetails.put("url", url);
        callDetails.put("agent", agent);
        return callDetails;
    }
    
    public String extractLeadName(WebElement nameNode) throws InterruptedException {
        WebDriver driver = getDriver();
        nameNode.click();
        Thread.sleep(5000);
        WebElement contactFormNode = driver.findElement(By.id("add-contact-form"));
        String leadName = contactFormNode.findElements(By.cssSelector("b")).get(0).getText();
        WebElement closeButton = driver.findElement(By.cssSelector("a[class=\"icon btn-icon pull-left m-l-n-xxl chrome-back-btn m-t-xs\"]"));
        closeButton.click();
        Thread.sleep(3000);
        return leadName;
    }

    public Boolean isOtherClient(String name) {
        for (String client : getClients()) {
            if (name.contains(client)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isOtherClientsLead(WebElement nameNode) throws InterruptedException {
        WebDriver driver = getDriver();
        nameNode.click();
        Thread.sleep(5000);
//        WebElement contactFormNode = driver.findElement(By.id("add-contact-form"));
        String campaignName = driver.findElement(By.cssSelector(".custom-thumbnail + b")).getText();
        WebElement closeButton = driver.findElement(By.cssSelector(".icon.btn-icon.pull-left"));
        closeButton.click();
        Thread.sleep(3000);
        if (isOtherClient(campaignName)) {
            return true;
        }
        return false;
    }

    public Boolean containerExist(WebElement container, int rowIndex) {
        List<WebElement> checkElementExist = container.findElements(By.cssSelector(String.valueOf("div[row-index=\"" +
                rowIndex +
                "\"]")
        ));

        if (checkElementExist.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
