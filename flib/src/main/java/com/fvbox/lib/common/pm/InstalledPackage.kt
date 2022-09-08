package com.fvbox.lib.common.pm

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class InstalledPackage : Parcelable {
    var userId = 0
    var packageName: String? = null

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
        dest.writeInt(userId)
        dest.writeString(packageName)
    }

    constructor() {}
    constructor(packageName: String?) {
        this.packageName = packageName
    }

    protected constructor(`in`: Parcel) {
        userId = `in`.readInt()
        packageName = `in`.readString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as InstalledPackage
        return packageName == that.packageName
    }

    override fun hashCode(): Int {
        return Objects.hash(packageName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InstalledPackage?> = object : Parcelable.Creator<InstalledPackage?> {
            override fun createFromParcel(source: Parcel): InstalledPackage? {
                return InstalledPackage(source)
            }

            override fun newArray(size: Int): Array<InstalledPackage?> {
                return arrayOfNulls(size)
            }
        }
    }
}