package com.devices.automation.automation;

import com.devices.automation.config.Config;
import com.devices.automation.model.BankTujuan;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Automation {
    private static final Logger logger = LoggerFactory.getLogger(Automation.class);
    private AndroidDriver<MobileElement> driver;
    boolean status = false;
    boolean loginstatus = false;
    private boolean nodevice = false;

    public void setup(BankTujuan bankTujuan, String keydata) throws MalformedURLException {
        try{
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            logger.info("Sending Capabilities...");
            switch (bankTujuan.getBank()) {
                case "bca":
                    desiredCapabilities.setCapability("udid", bankTujuan.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "10");
                    desiredCapabilities.setCapability("appPackage", "com.bca");
                    desiredCapabilities.setCapability("appActivity", ".mobile.MainActivity");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", bankTujuan.getPort());
                    break;
                case "mandiri":
                    desiredCapabilities.setCapability("udid", bankTujuan.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "9");
                    desiredCapabilities.setCapability("appPackage", "com.bankmandiri.mandirionline");
                    desiredCapabilities.setCapability("appActivity", "com.bankmandiri.mandirionline.activity.beforelogin.SplashScreenActivity");
                    desiredCapabilities.setCapability("appWaitActivity", "com.bankmandiri.mandirionline.activity.beforelogin.LoginActivity");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", bankTujuan.getPort());
                    break;
                case "bni":
                    desiredCapabilities.setCapability("udid", bankTujuan.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "10");
                    desiredCapabilities.setCapability("appPackage", "src.com.bni");
                    desiredCapabilities.setCapability("appActivity", "src.com.bni.MainActivityMBRC");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 20);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", bankTujuan.getPort());
                    break;
                case "bri":
                    desiredCapabilities.setCapability("udid", bankTujuan.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "9");
                    desiredCapabilities.setCapability("appPackage", "id.co.bri.brimo");
                    desiredCapabilities.setCapability("appActivity", "id.co.bri.brimo.BRIMO");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", bankTujuan.getPort());
                    break;
                default:
                    logger.info(bankTujuan.getBank() + " is not in the list bank!");
                    break;
            }

            final URL url = new URL("http://"+ Config.APPIUMSERVER +"/wd/hub");
            try{
                logger.info("Devices " + bankTujuan.getUdid() + " is busy now..");
                devicesBusy(bankTujuan.getUdid(), 1);
                logger.info("AndroidDriver Capabilities for bank " + bankTujuan.getBank() + " Found...");
                driver = new AndroidDriver<MobileElement>(url, desiredCapabilities);
                nodevice = true;
            }catch (Exception ex){
                logger.info("No Devices");
                updateStatus("Error from Device", keydata);
                devicesBusy(bankTujuan.getUdid(), 0);
            }

            if (nodevice) {
                logger.info("Automation Running for Account " + bankTujuan.getUsername() + "");
            }

            if(nodevice){
                if (bankTujuan.getBank().equals("bca")){
                    logger.info(bankTujuan.getBank());
                    BCA(bankTujuan, keydata);
                }else if (bankTujuan.getBank().equals("mandiri")) {
                    logger.info(bankTujuan.getBank());
                    MANDIRI(bankTujuan, keydata);
                }else if (bankTujuan.getBank().equals("bri")){
                    logger.info(bankTujuan.getBank());
                    BRI(bankTujuan, keydata);
                }else if (bankTujuan.getBank().equals("bni")){
                    logger.info(bankTujuan.getBank());
                    BNI(bankTujuan, keydata);
                }else {
                    logger.info(bankTujuan.getBank());
                    updateStatus("Bank Not Found", keydata);
                    devicesBusy(bankTujuan.getUdid(), 0);
                }
            }
        }catch (Exception exp){
            logger.info(exp.getMessage());
            devicesBusy(bankTujuan.getUdid(), 0);
        }
    }

    private void updateStatus(String message, String keydata){
        logger.info(message);
        Map<String, String> status = new HashMap<>();
        status.put("key", keydata);
        status.put("status", message);
        final String uri = "http://"+ Config.SERVICESDB+"/tb/update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, status, String.class);
    }

    private void devicesBusy(String udid, int busy) {
        final String uri = "http://"+ Config.SERVICESDB+"/device/busy";
        Map<String, String> devices = new HashMap<>();
        devices.put("udid", udid);
        devices.put("busy", String.valueOf(busy));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, devices, String.class);
    }

    private void BCA(BankTujuan bankTujuan, String keydata){
        try{
            Thread.sleep(3000);
            MobileElement el1 = driver.findElementById("com.bca:id/main_btn_bca");
            el1.click();
            Thread.sleep(3000);

            try{
                MobileElement el2 = driver.findElementById("com.bca:id/login_edit_text");
                el2.sendKeys(bankTujuan.getUsername());
                MobileElement el3 = driver.findElementByAccessibilityId("Login button");
                el3.click();

                try {
                    MobileElement message = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.widget.TextView");
                    logger.info(message.getText());
                    MobileElement btnBack = driver.findElementByAccessibilityId("PopUp Button - Back");
                    btnBack.click();
                    Thread.sleep(1000);
                    blockedAccount(bankTujuan.getUdid());
                    close();
                }catch (Exception ex) {
                    logger.info("login Done..");
                }

                Thread.sleep(3000);
                MobileElement el4 = driver.findElementByAccessibilityId("Icon m-Transfer");
                el4.click();
                Thread.sleep(1000);
                el4.click();
                Thread.sleep(3000);
                MobileElement el5 = driver.findElementByAccessibilityId("menu Daftar Transfer - Antar Rekening");
                el5.click();
                Thread.sleep(2000);
                MobileElement el6 = driver.findElementById("com.bca:id/antar_rekening_et1");
                el6.sendKeys(bankTujuan.getRekening());
                Thread.sleep(2000);
                MobileElement el7 = driver.findElementById("com.bca:id/button_title_right");
                el7.click();
                Thread.sleep(4000);
                MobileElement already = driver.findElementById("com.bca:id/confirm_screen_tv_1_2");

                if(already.getText().equals("ALREADY REGISTERED")){
                    logger.info("This Account is already in List, Going to Transfer...");
                    alreadyAccountList(bankTujuan.getRekening(), bankTujuan.getNominal(), bankTujuan.getMpin(), keydata);
                }else{
                    logger.info("This Account is New, with name " + already.getText() + ", Process to Add to List...");
                    newAccountentry(bankTujuan.getRekening(), bankTujuan.getNominal(), bankTujuan.getMpin(), keydata);
                }
                devicesBusy(bankTujuan.getUdid(), 0);
            }catch (Exception e){
                loginstatus = true;
                logger.info(e.getMessage());
                updateStatus("Failed", keydata);
                devicesBusy(bankTujuan.getUdid(), 0);
                close();
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            devicesBusy(bankTujuan.getUdid(), 0);
            updateStatus("Failed", keydata);
            close();
        }
    }

    private void alreadyAccountList(String rekening, String nominal, String mpin, String keydata){
        try{
            Thread.sleep(3000);
            driver.navigate().back();
            Thread.sleep(2000);
            driver.navigate().back();
            Thread.sleep(2000);
            MobileElement el9 = driver.findElementByAccessibilityId("menu Transfer - Antar Rekening");
            el9.click();
            Thread.sleep(2000);
            MobileElement el10 = driver.findElementByAccessibilityId("Ke Rekening (Pilih Rekening Tujuan)");
            el10.click();
            Thread.sleep(2000);
            MobileElement el11 = driver.findElementById("com.bca:id/search_src_text");
            el11.sendKeys(rekening);
            MobileElement el12 = driver.findElementById("com.bca:id/nama");
            el12.click();
            Thread.sleep(3000);
            MobileElement el13 = driver.findElementByAccessibilityId("Input jumlah uang");
            el13.click();
            Thread.sleep(3000);
            MobileElement el14 = driver.findElementById("com.bca:id/m_input_dialog_edtTxt_input1");
            el14.sendKeys(nominal);
            MobileElement el15 = driver.findElementByAccessibilityId("Ok btn input dialog-Jumlah Uang:");
            el15.click();
            Thread.sleep(3000);
            MobileElement el16 = driver.findElementById("com.bca:id/button_title_right");
            el16.click();
            Thread.sleep(3000);
            MobileElement el17 = driver.findElementByAccessibilityId("PopUp Button - Ok");
            el17.click();
            Thread.sleep(3000);
            MobileElement el18 = driver.findElementById("com.bca:id/input_text");
            el18.sendKeys(mpin);
            MobileElement el19 = driver.findElementByAccessibilityId("PopUp PIN Button - OK");
            el19.click();
            Thread.sleep(5000);
            status = true;
            updateStatus("Success", keydata);
            close();
        }catch (Exception e){
            logger.info(e.getMessage());
            updateStatus("Failed", keydata);
            close();
        }
    }

    private void newAccountentry(String rekening, String nominal, String mpin, String keydata){
        try{
            Thread.sleep(3000);
            MobileElement el8 = driver.findElementById("com.bca:id/confirm_screen_cb_1");
            el8.click();
            Thread.sleep(3000);
            MobileElement el9 = driver.findElementById("com.bca:id/button_title_right");
            el9.click();
            Thread.sleep(3000);
            MobileElement el10 = driver.findElementById("com.bca:id/input_text");
            el10.sendKeys(mpin);
            MobileElement el11 = driver.findElementByAccessibilityId("PopUp PIN Button - OK");
            el11.click();
            Thread.sleep(3000);
            MobileElement el12 = driver.findElementByAccessibilityId("PopUp Button - Ok");
            el12.click();
            Thread.sleep(3000);
            MobileElement el13 = driver.findElementByAccessibilityId("menu Transfer - Antar Rekening");
            el13.click();
            Thread.sleep(3000);
            MobileElement el14 = driver.findElementByAccessibilityId("Ke Rekening (Pilih Rekening Tujuan)");
            el14.click();
            Thread.sleep(3000);
            MobileElement el15 = driver.findElementById("com.bca:id/search_src_text");
            el15.sendKeys(rekening);
            MobileElement el16 = driver.findElementById("com.bca:id/nama");
            el16.click();
            Thread.sleep(3000);
            MobileElement el17 = driver.findElementByAccessibilityId("Input jumlah uang");
            el17.click();
            Thread.sleep(3000);
            MobileElement el18 = driver.findElementById("com.bca:id/m_input_dialog_edtTxt_input1");
            el18.sendKeys(nominal);
            MobileElement el19 = driver.findElementByAccessibilityId("Ok btn input dialog-Jumlah Uang:");
            el19.click();
            Thread.sleep(3000);
            MobileElement el20 = driver.findElementById("com.bca:id/button_title_right");
            el20.click();
            Thread.sleep(3000);
            MobileElement el21 = driver.findElementByAccessibilityId("PopUp Button - Ok");
            el21.click();
            Thread.sleep(3000);
            MobileElement el22 = driver.findElementById("com.bca:id/input_text");
            el22.sendKeys(mpin);
            MobileElement el23 = driver.findElementByAccessibilityId("PopUp PIN Button - OK");
            el23.click();
            Thread.sleep(3000);
            status = true;
            updateStatus("Success", keydata);
            close();
        }catch (Exception e){
            logger.info(e.getMessage());
            updateStatus("Failed", keydata);
            close();
        }
    }

    private void close(){
        try{
            driver.closeApp();
            driver.quit();
        }catch (Exception ex){
            logger.info("Bad Close");
        }
    }

    private void MANDIRI(BankTujuan bankTujuan, String keydata) {
        try{
            Thread.sleep(2000);
            String user = bankTujuan.getUsername();
            String pass = bankTujuan.getPassword();
            String mpin = bankTujuan.getMpin();

            try{
                MobileElement password = null;
                MobileElement username = null;
                try {
                    MobileElement version = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.TextView");
                    logger.info("Mandiri Online Apps Version: " + version.getText());
                    password = driver.findElement(By.xpath("//*[contains(@text, 'Password')]"));
                    username = driver.findElement(By.xpath("//*[contains(@text, 'User ID')]"));
                }catch (Exception e) {
                    logger.info("Devices can not access Login page..");
                    close();
                }


                KeyEvent keyEvent = new KeyEvent();
                username.click();
                Thread.sleep(2000);
                for (int i = 0; i < user.length(); i++) {
                    char data = user.charAt(i);
                    if (Character.isUpperCase(data)) {
                        (new TouchAction(driver)).tap(PointOption.point(85, 2027)).perform().release();
                        (new TouchAction(driver)).tap(keyUpperCase().get(user.split("")[i])).perform().release();
                    }else {
                        driver.pressKey(keyEvent.withKey(huruf(user.split("")[i])));
                    }
                }

                password.click();
                Thread.sleep(2000);
                for (int i = 0; i < pass.length(); i++) {
                    char data = pass.charAt(i);
                    if (Character.isUpperCase(data)) {
                        (new TouchAction(driver)).tap(PointOption.point(85, 2027)).perform().release();
                        (new TouchAction(driver)).tap(keyUpperCase().get(pass.split("")[i])).perform().release();
                    }else {
                        driver.pressKey(keyEvent.withKey(huruf(pass.split("")[i])));
                    }
                }

                MobileElement btnLogin = driver.findElement(By.xpath("//*[contains(@text, 'LOGIN')]"));
                btnLogin.click();
                loginstatus = true;
            }catch (Exception ex) {
                logger.info(ex.getMessage());
                loginstatus = false;
                close();
            }

            if (loginstatus) {
                Thread.sleep(2000);
                MobileElement btnTransfer = driver.findElement(By.xpath("//*[contains(@text, 'Transfer')]"));
                btnTransfer.click();
                Thread.sleep(2000);
                MobileElement btnBankMandiri = driver.findElement(By.xpath("//*[contains(@text, 'Ke Rekening Mandiri')]"));
                btnBankMandiri.click();
                Thread.sleep(2000);
                MobileElement pilihRekening = driver.findElement(By.xpath("//*[contains(@text, 'Pilih Rekening Tujuan')]"));
                pilihRekening.click();
                Thread.sleep(2000);
                MobileElement rekening = driver.findElement(By.xpath("//*[contains(@text, 'Masukkan no. rekening tujuan / nama terdaftar')]"));
                rekening.sendKeys(bankTujuan.getRekening());
                Thread.sleep(2000);
                try {
                    MobileElement btnTambahRek = driver.findElement(By.xpath("//*[contains(@text, 'TAMBAH SEBAGAI TUJUAN BARU')]"));
                    btnTambahRek.click();
                    Thread.sleep(3000);
                    MobileElement btnLanjut1 = driver.findElement(By.xpath("//*[contains(@text, 'LANJUT')]"));
                    btnLanjut1.click();
                }catch (Exception e) {
                    (new TouchAction(driver)).tap(PointOption.point(461, 672)).perform();
                }
                Thread.sleep(2000);
                MobileElement nominalMoney = driver.findElement(By.xpath("//*[contains(@text, 'Masukkan Jumlah')]"));
                nominalMoney.click();
                nominalMoney.sendKeys(bankTujuan.getNominal());
                driver.hideKeyboard();
                (new TouchAction(driver)).tap(PointOption.point(513, 1787)).perform().release();
                Thread.sleep(1000);
                MobileElement btnLanjut2 = driver.findElement(By.xpath("//*[contains(@text, 'LANJUT')]"));
                btnLanjut2.click();
                Thread.sleep(2000);
                MobileElement btnKirim = driver.findElement(By.xpath("//*[contains(@text, 'KIRIM')]"));
                btnKirim.click();
                Thread.sleep(2000);

                ArrayList<String> angka = new ArrayList<String>();
                ArrayList<String> mpinarr = new ArrayList<String>();
                angka.add("1");
                angka.add("2");
                angka.add("3");
                angka.add("4");
                angka.add("5");
                angka.add("6");
                angka.add("7");
                angka.add("8");
                angka.add("9");
                angka.add("0");
                String strarr = "" + mpin.charAt(0) + "," + mpin.charAt(1) + "," + mpin.charAt(2) + "," + mpin.charAt(3) + "," + mpin.charAt(4) + "," + mpin.charAt(5) + "";
                String str[] = strarr.split(",");
                mpinarr.add(str[0]);
                mpinarr.add(str[1]);
                mpinarr.add(str[2]);
                mpinarr.add(str[3]);
                mpinarr.add(str[4]);
                mpinarr.add(str[5]);
                mpinarr.forEach((mpins) -> {
                    angka.forEach((angkas) -> {
                        int a = Integer.parseInt(mpins);
                        int b = Integer.parseInt(angkas);
                        if (b == a) {
                            driver.findElement(By.xpath("//*[contains(@text, '" + angkas + "')]")).click();
                        }
                    });
                });
                status = true;
            }
            devicesBusy(bankTujuan.getUdid(), 0);
            updateStatus("Success", keydata);
            Thread.sleep(5000);
            close();
        }catch (Exception exp){
            logger.info(exp.getMessage());
            devicesBusy(bankTujuan.getUdid(), 0);
            updateStatus("Failed", keydata);
            close();
        }
    }

    private AndroidKey huruf(String huruf){
        AndroidKey androidKey;

        if (huruf.equals("a") || huruf.equals("A")) {
            return AndroidKey.A;
        }else if (huruf.equals("b")) {
            return AndroidKey.B;
        }else if (huruf.equals("c")){
            return AndroidKey.C;
        }else if (huruf.equals("d")) {
            return AndroidKey.D;
        }else if (huruf.equals("e")){
            return AndroidKey.E;
        }else if (huruf.equals("f") || huruf.equals("F")) {
            return AndroidKey.F;
        }else if (huruf.equals("g")) {
            return AndroidKey.G;
        }else if (huruf.equals("h")){
            return AndroidKey.H;
        }else if (huruf.equals("i")){
            return AndroidKey.I;
        }else if (huruf.equals("j")){
            return AndroidKey.J;
        }else if (huruf.equals("k")){
            return AndroidKey.K;
        }else if (huruf.equals("l")){
            return AndroidKey.L;
        }else if (huruf.equals("m")){
            return AndroidKey.M;
        }else if (huruf.equals("n")){
            return AndroidKey.N;
        }else if (huruf.equals("o")){
            return AndroidKey.O;
        }else if (huruf.equals("p")){
            return AndroidKey.P;
        }else if (huruf.equals("q")){
            return AndroidKey.Q;
        }else if (huruf.equals("r")){
            return AndroidKey.R;
        }else if (huruf.equals("s")){
            return AndroidKey.S;
        }else if (huruf.equals("t")){
            return AndroidKey.T;
        }else if (huruf.equals("u")){
            return AndroidKey.U;
        }else if (huruf.equals("v")){
            return AndroidKey.V;
        }else if (huruf.equals("w")){
            return AndroidKey.W;
        }else if (huruf.equals("x")){
            return AndroidKey.X;
        }else if (huruf.equals("y")){
            return AndroidKey.Y;
        }else if (huruf.equals("z")){
            return AndroidKey.Z;
        }else if (huruf.equals("0")){
            return AndroidKey.DIGIT_0;
        }else if (huruf.equals("1")){
            return AndroidKey.DIGIT_1;
        }else if (huruf.equals("2")){
            return AndroidKey.DIGIT_2;
        }else if (huruf.equals("3")){
            return AndroidKey.DIGIT_3;
        }else if (huruf.equals("4")){
            return AndroidKey.DIGIT_4;
        }else if (huruf.equals("5")){
            return AndroidKey.DIGIT_5;
        }else if (huruf.equals("6")){
            return AndroidKey.DIGIT_6;
        }else if (huruf.equals("7")){
            return AndroidKey.DIGIT_7;
        }else if (huruf.equals("8")){
            return AndroidKey.DIGIT_8;
        }else if (huruf.equals("9")){
            return AndroidKey.DIGIT_9;
        }else {
            return null;
        }
    }

    private static Map<String, PointOption> keyUpperCase() {
        Map<String, PointOption> coordinate = new HashMap<>();

        coordinate.put("A", PointOption.point(113, 1880));
        coordinate.put("B", PointOption.point(635, 2040));
        coordinate.put("C", PointOption.point(437, 2040));
        coordinate.put("D", PointOption.point(329, 1880));
        coordinate.put("E", PointOption.point(275, 1727));
        coordinate.put("F", PointOption.point(428, 1880));
        coordinate.put("G", PointOption.point(527, 1880));
        coordinate.put("H", PointOption.point(635, 1880));
        coordinate.put("I", PointOption.point(797, 1727));
        coordinate.put("J", PointOption.point(743, 1880));
        coordinate.put("K", PointOption.point(855, 1880));
        coordinate.put("L", PointOption.point(959, 1880));
        coordinate.put("M", PointOption.point(855, 2040));
        coordinate.put("N", PointOption.point(747, 2040));
        coordinate.put("O", PointOption.point(900, 1727));
        coordinate.put("P", PointOption.point(1008, 1727));
        coordinate.put("Q", PointOption.point(59, 1727));
        coordinate.put("R", PointOption.point(369, 1727));
        coordinate.put("S", PointOption.point(207, 1880));
        coordinate.put("T", PointOption.point(473, 1727));
        coordinate.put("U", PointOption.point(693, 1727));
        coordinate.put("V", PointOption.point(531, 2040));
        coordinate.put("W", PointOption.point(162, 1727));
        coordinate.put("X", PointOption.point(324, 2040));
        coordinate.put("Y", PointOption.point(585, 1727));
        coordinate.put("Z", PointOption.point(225, 2040));
        return coordinate;
    }

    private void BRI(BankTujuan bankTujuan, String keydata) {
        try {
            Thread.sleep(10000);
            try{
                try {
                    MobileElement el1 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.EditText");
                    el1.sendKeys(bankTujuan.getUsername());
                }catch (Exception e) {
                    logger.info("Devices can not access Login page..");
                    close();
                }

                MobileElement el2 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.widget.EditText");
                el2.sendKeys(bankTujuan.getPassword());
                MobileElement el3 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.Button");
                el3.click();
                Thread.sleep(5000);
                MobileElement el4 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.view.ViewGroup/android.widget.ImageView");
                el4.click();
                loginstatus = true;
            }catch (Exception e){
                logger.info(e.getMessage());
                devicesBusy(bankTujuan.getUdid(), 0);
                updateStatus("Failed", keydata);
                close();
            }

            if(loginstatus) {
                MobileElement el5 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup");
                el5.click();
                Thread.sleep(3000);
                MobileElement el6 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.widget.TextView");
                el6.click();
                Thread.sleep(3000);
                MobileElement el7 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.EditText");
                el7.sendKeys(bankTujuan.getRekening());
                MobileElement el8 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText");
                el8.sendKeys("BANK BRI");
                MobileElement el9 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup[5]/androidx.recyclerview.widget.RecyclerView/ny0k.iw[1]/android.view.ViewGroup");
                el9.click();
                Thread.sleep(3000);
                MobileElement el10 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.Button");
                el10.click();
                Thread.sleep(3000);
                MobileElement el11 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.Button");
                el11.click();
                Thread.sleep(3000);
                MobileElement el12 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.EditText");
                el12.sendKeys(bankTujuan.getNominal());
                MobileElement el13 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.widget.Button");
                el13.click();
                Thread.sleep(3000);
                MobileElement el14 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.Button");
                el14.click();
                Thread.sleep(3000);
                MobileElement el15 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.widget.EditText");
                el15.sendKeys(bankTujuan.getPassword());
                MobileElement el16 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.Button");
                el16.click();
                Thread.sleep(3000);
                MobileElement pesanBerhasil = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView");
                logger.info(pesanBerhasil.getText());
                if (pesanBerhasil.getText().equals("Transaksi Berhasil")) {
                    status = true;
                    Thread.sleep(3000);
                    updateStatus("Success", keydata);
                    devicesBusy(bankTujuan.getUdid(), 0);
                    close();
                } else {

                    updateStatus("Failed (Saldo -)", keydata);
                    devicesBusy(bankTujuan.getUdid(), 0);
                    close();
                }
            }
        }catch (Exception e){
            updateStatus("Failed", keydata);
            devicesBusy(bankTujuan.getUdid(), 0);
            close();
            logger.info(e.getMessage());
        }
    }

    private void BNI(BankTujuan bankTujuan, String keydata) {
        try{
            Thread.sleep(10000);
            try {
                try {
                    MobileElement version = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View[1]");
                    logger.info("BNI Mobile Apps Version: " + version.getText());
                    MobileElement el1 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View[2]/android.view.View[2]/android.widget.EditText");
                    el1.sendKeys(bankTujuan.getUsername());
                }catch (Exception e) {
                    logger.info("Devices can not access Login page..");
                    close();
                }

                MobileElement el2 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View[2]/android.view.View[3]/android.widget.EditText");
                el2.sendKeys(bankTujuan.getMpin());
                MobileElement el3 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View[2]/android.view.View[6]/android.widget.Button");
                el3.click();
                Thread.sleep(3000);
                MobileElement el4 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[4]/android.view.View[3]/android.view.View[2]/android.view.View");
                el4.click();
                loginstatus = true;
                Thread.sleep(5000);
            }catch (Exception e){
                logger.info(e.getMessage());
                updateStatus("Failed", keydata);
                devicesBusy(bankTujuan.getUdid(), 0);
                close();
            }

            if(loginstatus) {
                MobileElement el5 = driver.findElementByXPath("//android.view.View[@content-desc=\"BNI\"]/android.view.View");
                el5.click();
                Thread.sleep(3000);
                MobileElement el6 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.widget.TabWidget/android.view.View[2]/android.view.View");
                el6.click();
                Thread.sleep(3000);
                MobileElement el7 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText");
                el7.sendKeys(bankTujuan.getRekening());
                Thread.sleep(4000);
                (new TouchAction(driver)).press(PointOption.point(489, 1579)).moveTo(PointOption.point(504, 1373)).release().perform();
                MobileElement el8 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[5]/android.view.View[2]/android.widget.EditText");
                el8.sendKeys(bankTujuan.getNominal());
                Thread.sleep(4000);
                MobileElement el9 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[6]/android.widget.Button");
                el9.click();
                Thread.sleep(4000);
                MobileElement el10 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View[2]/android.view.View[2]/android.widget.EditText");
                el10.sendKeys(bankTujuan.getPassword());
                Thread.sleep(4000);
                MobileElement el11 = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View[3]/android.widget.Button");
                el11.click();
                Thread.sleep(5000);
                status = true;
                updateStatus("Success", keydata);
                devicesBusy(bankTujuan.getUdid(), 0);
                close();
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            devicesBusy(bankTujuan.getUdid(), 0);
            updateStatus("Failed", keydata);
            close();
        }
    }

    private void blockedAccount(String udid) {
        logger.info("Login Failed");
        final String uri = "http://"+ Config.SERVICESDB+"/block/" + udid;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> ststus = restTemplate.postForEntity(uri, status, String.class);
        logger.info(ststus.getBody());
    }
}
