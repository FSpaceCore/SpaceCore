package com.fvbox.lib.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FvBox on 2022/6/4.
 */
public class FMessageResult implements Parcelable {
    public static final Parcelable.Creator<FMessageResult> CREATOR = new Parcelable.Creator<FMessageResult>() {
        @Override
        public FMessageResult createFromParcel(Parcel source) {
            return new FMessageResult(source);
        }

        @Override
        public FMessageResult[] newArray(int size) {
            return new FMessageResult[size];
        }
    };
    public boolean success;
    public String msg;

    public FMessageResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public FMessageResult() {
    }

    protected FMessageResult(Parcel in) {
        this.success = in.readByte() != 0;
        this.msg = in.readString();
    }

    public static FMessageResult buildError(String msg) {
        return new FMessageResult(false, msg);
    }

    public static FMessageResult buildSuccess(String msg) {
        return new FMessageResult(true, msg);
    }

    public static FMessageResult buildSuccess() {
        return new FMessageResult(true, "");
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
    }

    public void readFromParcel(Parcel source) {
        this.success = source.readByte() != 0;
        this.msg = source.readString();
    }
}
