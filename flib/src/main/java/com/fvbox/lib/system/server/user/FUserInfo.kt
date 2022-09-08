package com.fvbox.lib.system.server.user

import android.os.Parcel
import android.os.Parcelable

class FUserInfo : Parcelable {
    var id = 0
    var name: String? = null
    var createTime: Long = 0

    internal constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeLong(createTime)
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        val tmpStatus = `in`.readInt()
        name = `in`.readString()
        createTime = `in`.readLong()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FUserInfo?> = object : Parcelable.Creator<FUserInfo?> {
            override fun createFromParcel(source: Parcel): FUserInfo? {
                return FUserInfo(source)
            }

            override fun newArray(size: Int): Array<FUserInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}