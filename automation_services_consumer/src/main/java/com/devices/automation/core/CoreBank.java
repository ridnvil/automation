package com.devices.automation.core;

import com.devices.automation.config.Config;
import com.devices.automation.model.Transfer;
import com.devices.automation.repository.APIRepository;
import com.devices.automation.repository.CustomeInterface;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;

public class CoreBank {
    private static final Logger logger = LoggerFactory.getLogger(CoreBank.class);
    private AndroidDriver<MobileElement> driver;
    boolean loginstatus = false;

    public CoreBank(Transfer transfer) {
        try{
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            final URL url = new URL("http://"+ Config.APPIUMSERVER +"/wd/hub");
            logger.info("Sending Capabilities...");
            switch (transfer.getBank()) {
                case "bca":
                    desiredCapabilities.setCapability("udid", transfer.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "10");
                    desiredCapabilities.setCapability("appPackage", "com.bca");
                    desiredCapabilities.setCapability("appActivity", ".mobile.MainActivity");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", transfer.getPort());
                    break;
                case "mandiri":
                    desiredCapabilities.setCapability("udid", transfer.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "9");
                    desiredCapabilities.setCapability("appPackage", "com.bankmandiri.mandirionline");
                    desiredCapabilities.setCapability("appActivity", "com.bankmandiri.mandirionline.activity.beforelogin.SplashScreenActivity");
                    desiredCapabilities.setCapability("appWaitActivity", "com.bankmandiri.mandirionline.activity.beforelogin.LoginActivity");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", transfer.getPort());
                    break;
                case "bni":
                    desiredCapabilities.setCapability("udid", transfer.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "10");
                    desiredCapabilities.setCapability("appPackage", "src.com.bni");
                    desiredCapabilities.setCapability("appActivity", "src.com.bni.MainActivityMBRC");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 20);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", transfer.getPort());
                    break;
                case "bri":
                    desiredCapabilities.setCapability("udid", transfer.getUdid());
                    desiredCapabilities.setCapability("platformName", "Android");
                    desiredCapabilities.setCapability("platformVersion", "9");
                    desiredCapabilities.setCapability("appPackage", "id.co.bri.brimo");
                    desiredCapabilities.setCapability("appActivity", "id.co.bri.brimo.BRIMO");
                    desiredCapabilities.setCapability("noReset", true);
                    desiredCapabilities.setCapability("dontStopAppOnReset", true);
                    desiredCapabilities.setCapability("newCommandTimeOut", 40);
                    desiredCapabilities.setCapability("appWaitDuration", 30000);
                    desiredCapabilities.setCapability("systemPort", transfer.getPort());
                    break;
                default:
                    logger.info(transfer.getBank() + " is not in the list bank!");
                    break;
            }

            try{
                driver = new AndroidDriver<MobileElement>(url, desiredCapabilities);
            }catch (Exception e) {
                logger.error("Problem connection from device with system!");
            }

            switch (transfer.getBank()) {
                case "bca":

                    break;
                case "mandiri":
                    mandiriCore(transfer);
                    break;
                case "bni":

                    break;
                case "bri":

                    break;
                default:
                    logger.info(transfer.getBank() + " is not in the list bank!");
                    break;
            }
        }catch (Exception e) {
            logger.error("Problem connection from device with system!");
        }
    }

    private void mandiriCore(Transfer transfer) {
        try{
            Thread.sleep(2000);
            String user = transfer.getUsername();
            String pass = transfer.getPassword();
            String mpin = transfer.getMpin();

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
                        (new TouchAction(driver)).tap(CustomeInterface.setUpperCase().get(user.split("")[i])).perform().release();
                    }else {
                        driver.pressKey(keyEvent.withKey(CustomeInterface.characters(user.split("")[i])));
                    }
                }

                password.click();
                Thread.sleep(2000);
                for (int i = 0; i < pass.length(); i++) {
                    char data = pass.charAt(i);
                    if (Character.isUpperCase(data)) {
                        (new TouchAction(driver)).tap(PointOption.point(85, 2027)).perform().release();
                        (new TouchAction(driver)).tap(CustomeInterface.setUpperCase().get(pass.split("")[i])).perform().release();
                    }else {
                        driver.pressKey(keyEvent.withKey(CustomeInterface.characters(pass.split("")[i])));
                    }
                }

                MobileElement btnLogin = driver.findElement(By.xpath("//*[contains(@text, 'LOGIN')]"));
                btnLogin.click();
                Thread.sleep(3000);
                loginstatus = true;
            }catch (Exception ex) {
                logger.info(ex.getMessage());
                loginstatus = false;
                close();
            }

            if (loginstatus) {
                transfer.getTujuan().forEach(tujuan -> {
                    try {
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
                        rekening.sendKeys(tujuan.getRekening());
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
                        nominalMoney.sendKeys(String.valueOf(tujuan.getNominal()));
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
                        APIRepository.updateStatus("Success", transfer.getUniqueid());
                        Thread.sleep(6000);
                        MobileElement exit = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.LinearLayout[1]/android.widget.ImageView");
                        exit.click();
                    }catch (Exception e) {
                        logger.info(e.getMessage());
                        APIRepository.devicesBusy(transfer.getUdid(), 0);
                        APIRepository.updateStatus("Failed", transfer.getUniqueid());
//                        close();
                    }
                });
            }
            APIRepository.devicesBusy(transfer.getUdid(), 0);
            APIRepository.updateStatus("Success", transfer.getUniqueid());
            Thread.sleep(5000);
            close();
        }catch (Exception exp){
            logger.info(exp.getMessage());
            APIRepository.devicesBusy(transfer.getUdid(), 0);
            APIRepository.updateStatus("Failed", transfer.getUniqueid());
            close();
        }
    }

    private void close() {
        try{
            driver.closeApp();
            driver.quit();
        }catch (Exception ex){
            logger.info("Bad Close");
        }
    }
}
