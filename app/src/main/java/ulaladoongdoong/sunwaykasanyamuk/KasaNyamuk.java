package ulaladoongdoong.sunwaykasanyamuk;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Heartless on 05/12/2017.
 */


class KasaNyamuk {
    float pricePerMeter = 0;
    float screenPortPrice = 0;
    //
//    final static int COLOR_WHITE = 0;
//    final static int COLOR_BROWN = 1;
//    final static int COLOR_BLACK = 2;
//    final static int COLOR_GREY = 3;
//
    private String location = "LT1 : Dapur";
    private float length, width, quantity, additionalPerimeter;
    private String variance;
    private float screenPortQuantity, panjar = 0;

    private String name;
    private String address;

    private String dll = "dll";

    private float dllPrice = 0;

    private String key = "";

    KasaNyamuk() {

    }

    KasaNyamuk(String location, float length, float width, float quantity, float additionalPerimeter) {
        this.location = location;
        this.length = length;
        this.width = width;
        this.quantity = quantity;
        this.additionalPerimeter = additionalPerimeter;
    }

    void setKey() {
        key = String.copyValueOf(name.toCharArray(), 0, 1) + System.currentTimeMillis();
        Log.d("KasaNyamuk", key);
    }

    void setDll(String dll) {
        this.dll = dll;
    }

    void setDllPrice(float dllPrice) {
        this.dllPrice = dllPrice;
    }

    void setName(String name) {
        this.name = name;
    }

    void setLength(float length) {
        this.length = length;
    }

    void setWidth(float width) {
        this.width = width;
    }

    void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    void setVariance(String variance) {
        this.variance = variance;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setAddress(String address) {
        this.address = address;
    }

    void setAdditionalPerimeter(float additionalPerimeter) {
        this.additionalPerimeter = additionalPerimeter;
    }

    float getAdditionalPerimeter() {
        return additionalPerimeter;
    }

    String getLocation() {
        return location;
    }

    float getLength() {
        return length;
    }

    float getWidth() {
        return width;
    }

    float getQuantity() {
        return quantity;
    }

    String getName() {
        return name;
    }

    String getAddress() {
        return address;
    }

    String getVariance() {
        return variance;
    }

    String getKey() {
        return key;
    }

    float getPerimeterWithoutAdditionalPerimeter() {
        return ((length + width) * 2) * quantity;
    }

    float getPerimeter() {
        return ((length + width) * 2 + additionalPerimeter) * quantity;
    }

    float getPrice(float pricePerMeter) {
        return getPerimeter() * pricePerMeter;
    }

    //----------- this is for screenport only-------------
    void setScreenPortQuantity(float quantity) {
        this.screenPortQuantity = quantity;
    }


    float getScreenPortQuantity() {
        return this.screenPortQuantity;
    }

    void setScreenPortPrice(float price) {
        this.screenPortPrice = price;
    }

    float getTotalScreenPortPrice() {
        return screenPortPrice * screenPortQuantity;
    }

    //------------------for 'panjar' only-----------------
    void setPanjar(float panjar) {
        this.panjar = panjar;
    }


    float getPanjar() {
        return this.panjar;
    }

    String getDate(String format) {
        return String.format(Locale.US, "%s", new SimpleDateFormat(format, Locale.getDefault()).format(Long.parseLong(key.substring(1))));
    }

    String getDll() {
        return dll;
    }

    float getDllPrice() {
        return dllPrice;
    }

}
