package com.cliqz.mobile.test.pages;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.cliqz.mobile.test.CliqzRunner;
import com.cliqz.mobile.test.driver.NativeDriver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.uiautomator.By.clazz;

/**
 * Copyright © Cliqz 2019
 */

@RunWith(CliqzRunner.class)
public class SettingsPage extends BasePage{

    // *** Test Data and/or Requirements ***
    private static final String SETTINGS_TEXT = "Settings"; //Assert Equals
    private static final String GENERAL_TEXT = "General";
    private static final String PRIVACY_TEXT = "Privacy";
    private static final String ACCESSIBILITY_TEXT = "Accessibility";
    private static final String ADVANCED_TEXT = "Advanced";
    private static final String MAKE_DEFAULT_TEXT = "Make default browser";
    private static final String ABOUT_US_TEXT = "About us";
    private static final String HELP_TEXT = "Help";
    private static final String RATE_US_TEXT = "Rate Us";

    // *** Locators ***
    private static final BySelector TOP_TOOL_BAR = By.res(
            NativeDriver.APP_PACKAGE + ":id/browser_toolbar");
    private static final BySelector MENU_BUTTON = By.res(
            NativeDriver.APP_PACKAGE + ":id/menu");
    private static final BySelector MENU_PANEL = By.res(
            NativeDriver.APP_PACKAGE + ":id/menu_panel");
    private static final BySelector MENU_PANEL_LIST = clazz(
            "android.widget.TextView");
    private static final BySelector SETTINGS_HEADING = By.res(
            NativeDriver.APP_PACKAGE + ":id/action_bar");
    private static final BySelector SETTINGSLIST = By.res(
            "android:id/title");
    private static final BySelector SETTINGS = clazz(
            "android.widget.RelativeLayout").pkg(
            NativeDriver.APP_PACKAGE);
    private static final BySelector LISTELEMENTS = By.hasChild(
            clazz("android.widget.RelativeLayout").hasChild(
                    clazz("android.widget.TextView"))).pkg(
                    NativeDriver.APP_PACKAGE);

    @Override
    @Before
    public void setUp() throws Exception {
        super.clearData();
        super.onboardingPref(false);
        super.defaultBrowserPopupPref(false);
        super.setUp();
    }
    //Page Methods
    private UiObject2 getTopToolBar() {
        return driver.getElement(TOP_TOOL_BAR);
    }
    private UiObject2 getMenuButton() {
        return driver.getElement(getTopToolBar(), MENU_BUTTON);
    }
    private UiObject2 getMenuPanel() {
        return driver.getElement(MENU_PANEL);
    }
    private List<UiObject2> getMenuPanelList() {
        return driver.getElements(getMenuPanel(), MENU_PANEL_LIST);
    }
    private UiObject2 getSettingsActionBar(){
        return driver.getElement(SETTINGS_HEADING);
    }
    private List <UiObject2> getSettings(){
        return driver.getElements(SETTINGS);
    }
    private List<String> getSettingsList(){
        List<UiObject2> settingsList = driver.getElements(SETTINGSLIST);
        List<String> settings = new ArrayList<>();
        boolean found = true;
        for(int i=0; i<settingsList.size(); i++){
            settings.add(settingsList.get(i).getText());
        }
        while (found) {
            driver.pageSwipe(Direction.UP);
            settingsList = driver.getElements(SETTINGSLIST);
            for(int i=0; i<settingsList.size(); i++){
                if(!settings.contains(settingsList.get(i).getText())){
                    settings.add(settingsList.get(i).getText());
                }
                found = false;
            }
            //settingsList.add(moreItems.lastIndexOf());
        }
        return settings;
    }
    private UiObject2 findListElement(String s){
        List <UiObject2> elementsList = driver.getElements(LISTELEMENTS);
        UiScrollable appSettingsList = new UiScrollable(new UiSelector().scrollable(true));
        UiObject2 foundListElement = null;
        boolean found = false;
        for (int i=0; i<elementsList.size();i++){
            try{
                String element = elementsList.get(i).getChildren().get(0).getChildren().get(0).getText();
                if (s.equals(element)){
                   foundListElement = elementsList.get(i);
                   found = true;
                }
            }catch (Error e){
                driver.logWarn(e.toString());
            }
        }
        while (!found){
//            driver.pageSwipe(200,1055,200, 1005);
            try {
                appSettingsList.scrollForward(33);
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            elementsList = driver.getElements(LISTELEMENTS);
            for (int i=0; i<elementsList.size();i++){
                try{
                    String element = elementsList.get(i).getChildren().get(0).getChildren().get(0).getText();
                    if (s.equals(element)){
                        foundListElement = elementsList.get(i);
                        found = true;
                    }
                }catch (Error e){
                    driver.logWarn(e.toString());
                }

            }
        }
        return foundListElement;
    }
    private UiObject2 findListElementStrings(String s){
        UiObject2 foundListElementStrings = findListElement(s).getChildren().get(0);
        return foundListElementStrings;
    }
    private UiObject2 findListElementSelection(String s){
        UiObject2 foundListElementSelection = findListElement(
                s).getChildren().get(1).getChildren().get(0);
        return foundListElementSelection;
    }

    @Test
    public void testSettingsText() {
        getMenuButton().click();
        final List<UiObject2> menuPanelList = getMenuPanelList();
        Assert.assertTrue(driver.exists(MENU_PANEL));
        Assert.assertEquals("Check that Settings button exists on the menu panel ",
                SETTINGS_TEXT,
                menuPanelList.get(3).getText()
        );
        menuPanelList.get(3).click();
        Assert.assertEquals("Check that Settings page can be opened",
                SETTINGS_TEXT,
                getSettingsActionBar().getChildren().get(1).getText()
        );
        final List<String> settingsList = getSettingsList();
        Assert.assertEquals("Check that General settings text exists and is correct",
                GENERAL_TEXT,
                settingsList.get(0));
        Assert.assertEquals("Check that Privacy settings text exists and is correct",
                PRIVACY_TEXT,
                settingsList.get(1));
        Assert.assertEquals("Check that Accessibility settings text exists and is correct",
                ACCESSIBILITY_TEXT,
                settingsList.get(2));
        Assert.assertEquals("Check that Advanced settings text exists and is correct",
                ADVANCED_TEXT,
                settingsList.get(3));
        Assert.assertEquals("Check that Make default settings text exists and is correct",
                MAKE_DEFAULT_TEXT,
                settingsList.get(4));
        Assert.assertEquals("Check that About us settings text exists and is correct",
                ABOUT_US_TEXT,
                settingsList.get(5));
        Assert.assertEquals("Check that Help settings text exists and is correct",
                HELP_TEXT,
                settingsList.get(6));
        Assert.assertEquals("Check that Rate Us settings text exists and is correct",
                RATE_US_TEXT,
                settingsList.get(7));
        driver.log("Settings text is displayed correctly.");
    }

    @Test
    public void testDefaultSettingsSearch(){
        getMenuButton().click();
        getMenuPanelList().get(3).click();
        getSettings().get(0).click();
        Assert.assertEquals("Check Default Setting for Search Results For",
                findListElementStrings("Search results for").getChildren().get(1).getText(),
                "Germany");
        Assert.assertEquals("Check Default Setting for Block Explicit Content",
                findListElementSelection("Block explicit content").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Human Web",
                findListElementSelection("Search query suggestions").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Enable Autocompletion",
                findListElementSelection("Enable Autocompletion").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Ghost Search",
                findListElementSelection("Enable Ghost Search").isChecked(),
                true);
    }

    @Test
    public void testDefaultSettingsStartTab(){
        getMenuButton().click();
        getMenuPanelList().get(3).click();
        getSettings().get(0).click();
        Assert.assertEquals("Check Default Setting for Blue Theme",
                findListElementSelection("Blue Theme").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Background Image",
                findListElementSelection("Show background image").isChecked(),
                false);
        Assert.assertEquals("Check Default Setting for Top Sites",
                findListElementSelection("Show most visited websites").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for News",
                findListElementSelection("Show News").isChecked(),
                true);
    }

    @Test
    public void testDefaultSettingsPrivacy(){
        getMenuButton().click();
        getMenuPanelList().get(3).click();
        getSettings().get(1).click();
        Assert.assertEquals("Check Default Setting for Updating Tracker Library",
                findListElementSelection("Update Tracker Library Automatically").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Allowing trackers",
                findListElementSelection("Allow trackers created by site owners").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Blocking New Trackers",
                findListElementSelection("Block new trackers").isChecked(),
                false);
        Assert.assertEquals("Check Default Setting for Browsing",
                findListElementStrings("Cookies").getChildren().get(1).getText(),
                "Enabled");
        Assert.assertEquals("Check Default Setting for Clearing private data on exit",
                findListElementSelection("Clear private data on exit").isChecked(),
                false);
        Assert.assertEquals("Check Default Setting for Remember Logins",
                findListElementSelection("Remember logins").isChecked(),
                true);
        Assert.assertEquals("Check Default Setting for Using a master password",
                findListElementSelection("Use master password").isChecked(),
                false);
    }
}