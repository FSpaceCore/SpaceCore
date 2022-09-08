package com.fvbox.lib.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FvBox on 2022/5/29.
 */
public class FDevice implements Parcelable {
    private final Map<String, String> sMap = new HashMap<>();
    public int version;
    public boolean enable;
    public String ID;
    public String BOARD;
    public String BRAND;
    public String MANUFACTURER;
    public String PRODUCT;
    public String MODEL;
    public String DEVICE;
    public String HARDWARE;
    public String SERIAL;

    public String androidId;
    public String deviceId;
    public String wifiMac;
    public String bootId;

    public void refresh() {
        sMap.put("ro.build.id", ID);
        sMap.put("ro.product.board", BOARD);
        sMap.put("ro.product.brand", BRAND);
        sMap.put("ro.product.manufacturer", MANUFACTURER);
        sMap.put("ro.product.name", PRODUCT);
        sMap.put("ro.product.model", MODEL);
        sMap.put("ro.product.device", DEVICE);
        sMap.put("ro.hardware", HARDWARE);
        sMap.put("ro.serialno", SERIAL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.version);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
        dest.writeString(this.ID);
        dest.writeString(this.BOARD);
        dest.writeString(this.BRAND);
        dest.writeString(this.MANUFACTURER);
        dest.writeString(this.PRODUCT);
        dest.writeString(this.MODEL);
        dest.writeString(this.DEVICE);
        dest.writeString(this.HARDWARE);
        dest.writeString(this.SERIAL);
        dest.writeString(this.androidId);
        dest.writeString(this.deviceId);
        dest.writeString(this.wifiMac);
        dest.writeString(this.bootId);
    }

    public void readFromParcel(Parcel source) {
        this.version = source.readInt();
        this.enable = source.readByte() != 0;
        this.ID = source.readString();
        this.BOARD = source.readString();
        this.BRAND = source.readString();
        this.MANUFACTURER = source.readString();
        this.PRODUCT = source.readString();
        this.MODEL = source.readString();
        this.DEVICE = source.readString();
        this.HARDWARE = source.readString();
        this.SERIAL = source.readString();
        this.androidId = source.readString();
        this.deviceId = source.readString();
        this.wifiMac = source.readString();
        this.bootId = source.readString();
    }

    public FDevice() {
    }

    public FDevice(Parcel in) {
        this.version = in.readInt();
        this.enable = in.readByte() != 0;
        this.ID = in.readString();
        this.BOARD = in.readString();
        this.BRAND = in.readString();
        this.MANUFACTURER = in.readString();
        this.PRODUCT = in.readString();
        this.MODEL = in.readString();
        this.DEVICE = in.readString();
        this.HARDWARE = in.readString();
        this.SERIAL = in.readString();
        this.androidId = in.readString();
        this.deviceId = in.readString();
        this.wifiMac = in.readString();
        this.bootId = in.readString();
    }

    public static final Creator<FDevice> CREATOR = new Creator<FDevice>() {
        @Override
        public FDevice createFromParcel(Parcel source) {
            return new FDevice(source);
        }

        @Override
        public FDevice[] newArray(int size) {
            return new FDevice[size];
        }
    };
}
