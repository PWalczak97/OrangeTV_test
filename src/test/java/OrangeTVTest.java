import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OrangeTVTest {

    static AndroidDriver<MobileElement> driver;
    public static void main(String[] args) {
        try {
            openOrangeTV();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public static void openOrangeTV() throws MalformedURLException {

        DesiredCapabilities cap = new DesiredCapabilities();

        //  Device capabilities
        cap.setCapability("deviceName","HUAWEI");
        cap.setCapability("udid","9WVDU18B09011705");
        cap.setCapability("platformName","Android");
        cap.setCapability("platformVersion","9");

        //  App capabilities
        cap.setCapability("appPackage","com.orange.pl.orangetvgo");
        cap.setCapability("appActivity","pl.orange.ypt.gui.activity.ActivitySplash");

        //  Sending desired capabilities to server
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AndroidDriver<MobileElement>(url,cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //  Click "POMIN" button
        MobileElement skip = driver.findElement(By.id("com.orange.pl.orangetvgo:id/menu_login"));
        skip.click();

        //  Click "ZACZYNAMY" button
        MobileElement start = driver.findElement(By.id("com.orange.pl.orangetvgo:id/welcome_btn_start"));
        start.click();

        // Click VOD
        MobileElement vod = driver.findElement(By.id("com.orange.pl.orangetvgo:id/main_btn_vod"));
        vod.click();

        //  Click search button
        MobileElement search = driver.findElement(By.id("com.orange.pl.orangetvgo:id/menu_search"));
        search.click();

        //  Searching for The Meg
        MobileElement searchField = driver.findElement(By.id("com.orange.pl.orangetvgo:id/search_field"));
        searchField.setValue("The Meg");
        driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "Go"));

        // Click The Meg element
        MobileElement theMeg = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.FrameLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.ImageView");
        theMeg.click();


    // Scrolls to the bottom of description
        for(int i =0;i<4;i++)
            scroll();

        printCast();

    }

    //  Scrolls down
    public static void scroll(){
        Dimension size = driver.manage().window().getSize();
        int starty = (int) (size.height * 0.70);
        int endy = (int) (size.height * 0.20);
        int startx = size.width / 2;

        TouchAction action = new TouchAction(driver);
        action.press(new PointOption().withCoordinates(startx, starty)).moveTo(new PointOption().withCoordinates(startx, endy)).release().perform();
    }

    //  Created to swipe cast members
    public static void swipe(){
        Dimension size = driver.manage().window().getSize();
        int startx = 764;
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;

        TouchAction action = new TouchAction(driver);
        action.press(new PointOption().withCoordinates(startx, starty)).moveTo(new PointOption().withCoordinates(endx, starty)).release().perform();
    }

    // Search and print cast members
    public static void printCast(){

        boolean bool = true;
        Set<String> castString = new HashSet<>();

        while(bool) {
            List<MobileElement> cast = driver.findElements(By.id("com.orange.pl.orangetvgo:id/actor_name"));

            //      Zmiana na Set<String> przez error v
            //      Cached elements 'By.id: com.orange.pl.orangetvgo:id/actor_name' do not exist in DOM anymore
            for (int i = 0; i < cast.size(); i++)
                castString.add(cast.get(i).getText());

            swipe();

            List<MobileElement> cast2 = driver.findElements(By.id("com.orange.pl.orangetvgo:id/actor_name"));
            for (int i = 0; i < cast.size(); i++)
                castString.add(cast2.get(i).getText());

            if(cast.equals(cast2))
                bool=false;
        }

        for (String actor : castString)
            System.out.println(actor);
    }


    @After
    public void End(){
        driver.quit();
    }
}
