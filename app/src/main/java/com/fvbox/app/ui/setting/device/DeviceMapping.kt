package com.fvbox.app.ui.setting.device

/**
 *
 * @Description: 设备信息修改，映射类
 * @Author: Jack
 * @CreateDate: 2022/6/1 23:28
 */
class DeviceMapping(private val userID: Int) {


    fun refresh() {

    }

    var enable: Boolean = false

    var board: String = ""

    var brand: String = ""

    var manufacturer: String = ""

    var product: String = ""

    var model: String = ""

    var device: String = ""

    var hardware: String = ""
    var serial: String = ""
    var androidId: String = ""
    var deviceId: String = ""
    var id: String = ""
    var bootId: String = ""
    var wifiMac: String = ""
    private fun save() {
    }
}
