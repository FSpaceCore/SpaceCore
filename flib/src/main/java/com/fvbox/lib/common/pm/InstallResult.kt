package com.fvbox.lib.common.pm

import android.os.Parcel
import android.os.Parcelable

open class InstallResult : Parcelable {
    var success = true
    var packageName: String? = null
    var msg: String? = null
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (success) 1.toByte() else 0.toByte())
        dest.writeString(packageName)
        dest.writeString(msg)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        success = `in`.readByte().toInt() != 0
        packageName = `in`.readString()
        msg = `in`.readString()
    }

    fun installError(packageName: String?, msg: String?): InstallResult {
        this.msg = msg
        success = false
        this.packageName = packageName
        return this
    }

    fun installError(msg: String?): InstallResult {
        this.msg = msg
        success = false
        return this
    }

    override fun toString(): String {
        return "InstallResult(success=$success, packageName=$packageName, msg=$msg)"
    }

    companion object {
        const val TAG = "InstallResult"

        @JvmField
        val CREATOR: Parcelable.Creator<InstallResult?> = object : Parcelable.Creator<InstallResult?> {
            override fun createFromParcel(source: Parcel): InstallResult? {
                return InstallResult(source)
            }

            override fun newArray(size: Int): Array<InstallResult?> {
                return arrayOfNulls(size)
            }
        }
    }
}