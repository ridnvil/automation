package com.devices.automation.repository;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.touch.offset.PointOption;

import java.util.HashMap;
import java.util.Map;

public interface CustomeInterface {

    static Map<String, PointOption> setUpperCase() {
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

    static AndroidKey characters(String huruf){
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
}
