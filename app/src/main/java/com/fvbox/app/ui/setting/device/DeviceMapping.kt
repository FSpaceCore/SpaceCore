package com.fvbox.app.ui.setting.device

import com.fvbox.lib.FCore
import com.fvbox.lib.common.FDevice


class DeviceMapping(private val userID: Int) {

    private var mDevice: FDevice? = FCore.get().getDevice(userID)

    fun refresh() {
        val lastEnable = enable
        mDevice = FCore.get().getDevice(userID)
        this.enable = lastEnable
    }

    var enable: Boolean = false
        get() {
            return mDevice?.enable ?: false
        }
        set(value) {
            field = value
            mDevice?.enable = value
            save()
        }

    // umi
    var board: String = ""
        get() {
            return mDevice?.BOARD.toString()
        }
        set(value) {
            field = value
            mDevice?.BOARD = value
            save()
        }

    // Xiaomi
    var brand: String = ""
        get() {
            return mDevice?.BRAND.toString()
        }
        set(value) {
            field = value
            mDevice?.BRAND = value
            save()
        }

    // Xiaomi
    var manufacturer: String = ""
        get() {
            return mDevice?.MANUFACTURER.toString()
        }
        set(value) {
            field = value
            mDevice?.MANUFACTURER = value
            save()
        }

    // umi
    var product: String = ""
        get() {
            return mDevice?.PRODUCT.toString()
        }
        set(value) {
            field = value
            mDevice?.PRODUCT = value
            save()
        }

    // Mi 10
    var model: String = ""
        get() {
            return mDevice?.MODEL.toString()
        }
        set(value) {
            field = value
            mDevice?.MODEL = value
            save()
        }

    // umi
    var device: String = ""
        get() {
            return mDevice?.DEVICE.toString()
        }
        set(value) {
            field = value
            mDevice?.DEVICE = value
            save()
        }

    // qcom
    var hardware: String = ""
        get() {
            return mDevice?.HARDWARE.toString()
        }
        set(value) {
            field = value
            mDevice?.HARDWARE = value
            save()
        }

    // unknown
    var serial: String = ""
        get() {
            return mDevice?.SERIAL.toString()
        }
        set(value) {
            field = value
            mDevice?.SERIAL = value
            save()
        }

    var androidId: String = ""
        get() {
            return mDevice?.androidId.toString()
        }
        set(value) {
            field = value
            mDevice?.androidId = value
            save()
        }

    var deviceId: String = ""
        get() {
            return mDevice?.deviceId.toString()
        }
        set(value) {
            field = value
            mDevice?.deviceId = value
            save()
        }

    var id: String = ""
        get() {
            return mDevice?.ID.toString()
        }
        set(value) {
            field = value
            mDevice?.ID = value
            save()
        }

    var bootId: String = ""
        get() {
            return mDevice?.bootId.toString()
        }
        set(value) {
            field = value
            mDevice?.bootId = value
            save()
        }

    var wifiMac: String = ""
        get() {
            return mDevice?.wifiMac.toString()
        }
        set(value) {
            field = value
            mDevice?.wifiMac = value
            save()
        }


    private fun save() {
        mDevice?.let {
            FCore.get().setDevice(userID, it)
        }
    }
}
