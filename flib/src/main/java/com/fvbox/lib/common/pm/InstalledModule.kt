package com.fvbox.lib.common.pm

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Parcel
import android.os.Parcelable

class InstalledModule : Parcelable {
    var packageName: String? = null
    var name: String? = null
    var desc: String? = null
    var main: String? = null
    var enable = false

    constructor() {}

    val application: ApplicationInfo?
        get() {
            return null
        }

    val packageInfo: PackageInfo?
        get() {
            return null
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(packageName)
        dest.writeString(name)
        dest.writeString(desc)
        dest.writeString(main)
        dest.writeByte(if (enable) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        packageName = `in`.readString()
        name = `in`.readString()
        desc = `in`.readString()
        main = `in`.readString()
        enable = `in`.readByte().toInt() != 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InstalledModule?> = object : Parcelable.Creator<InstalledModule?> {
            override fun createFromParcel(source: Parcel): InstalledModule? {
                return InstalledModule(source)
            }

            override fun newArray(size: Int): Array<InstalledModule?> {
                return arrayOfNulls(size)
            }
        }
    }
}