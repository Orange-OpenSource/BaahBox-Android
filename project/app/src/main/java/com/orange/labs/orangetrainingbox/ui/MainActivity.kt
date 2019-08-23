/**
    Orange Baah Box
    Copyright (C) 2017 – 2019 Orange SA

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.orange.labs.orangetrainingbox.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.*
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.bluetooth.le.ScanSettings.SCAN_MODE_BALANCED
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.ParcelUuid
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import java.util.*
import org.jetbrains.anko.toast
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import com.orange.labs.orangetrainingbox.game.InputsParser
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import com.orange.labs.orangetrainingbox.utils.properties.BleConfiguration
import com.orange.labs.orangetrainingbox.utils.properties.PropertiesKeys
import com.orange.labs.orangetrainingbox.utils.properties.readBleSensorsConfiguration
import com.orange.labs.orangetrainingbox.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*

// **********
// Properties
// **********

/**
 * Computed property for Bluetooth state flag
 */
private val BluetoothAdapter.isDisabled: Boolean
    get() = !isEnabled

// *******
// Classes
// *******

/**
 * Main activity of the application.
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 23/10/2018
 * @version 2.1.1
 */
class MainActivity : AppCompatActivity() {

    // **********
    // Properties
    // **********

    /**
     * Flag indicating if the pop-up for appearing should be displayed or not
     */
    private var showingPopup = false

    /**
     * Flag indicating if a BLE connection is on going
     */
    private var connected: Boolean = false

    private val REQUEST_ENABLE_BT = 42
    private val REQUEST_BT_PERMISSION = 41
    private val REQUEST_SETTINGS = 1337


    /**
     * A reference to the model of the app, here with references to the BLE devices.
     */
    private lateinit var model: TrainingBoxViewModel

    /**
     * The BLE configuration to apply for sensors and callbacks
     */
    private val bleConfiguration: BleConfiguration? by lazy(LazyThreadSafetyMode.NONE) {
        readBleSensorsConfiguration()
    }

    /**
     * The object providing features for the Bluetooth things
     */
    private lateinit var bluetoothGatt: BluetoothGatt

    /**
     * A reference to the Bluetooth adapter so as tod eal with its feature
     */
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    /**
     * Callback used for Bluetooth Low Energy scan with triggers for failed scan and scanned results.
     */
    private val btleScanCallback = object : android.bluetooth.le.ScanCallback() {

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Logger.e("Scan of BLE devices failed: $errorCode")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.device?.let {
                Logger.v("Scanned device: $it")
                model.addBox(it)
                checkBtleConnection()
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Logger.d("Batch scan results: $results")
        }

    }

    /**
     * Callback dedicated to Bluetooth services management with trigger for connection states,
     * changes of services etc.
     */
    private val btleGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        /**
         * Triggered if the connection state has been changed.
         * If connected, update flag and look for services.
         * If disconnected just update flag.
         *
         * @param gatt
         * @param status
         * @param newState
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Logger.v("Connection state change: $newState (connected is ${BluetoothProfile.STATE_CONNECTED})")
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    bluetoothGatt.discoverServices()
                    connected = true
                    Logger.d("BLE connected")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connected = false
                    Logger.d("BLE disconnected")
                }
            }
        }

        /**
         * Triggered when services have been discovered.
         * For each sensor gotten through discovered services, configure the sensor
         *
         * @param gatt
         * @param status
         */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val service = gatt?.getService(UUID.fromString(bleConfiguration?.serviceUUID))
            val charSensors = service?.getCharacteristic(UUID.fromString(bleConfiguration?.sensorsCharUUID))
            charSensors?.let {
                charSensors.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                val result = gatt.setCharacteristicNotification(charSensors, true)
                val descriptor = charSensors.getDescriptor(UUID.fromString(bleConfiguration?.sensorCharDescriptorUUID))
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }

        /**
         * Triggered when new value are coming form the sensors in use.
         *
         * @param gatt
         * @param characteristic The bundle containing the new frames of data to parse and use for game logic
         */
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            val (muscle1, _, _) = InputsParser.extractValuesCharacteristic(characteristic)
            model.sensorB.postValue(muscle1)
        }

    }

    // *******************************
    // Methods inherited from Activity
    // *******************************

    /**
     * Activity lifecycle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(TrainingBoxViewModel::class.java)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    /**
     * Activity lifecycle.
     */
    override fun onResume() {
        super.onResume()
        checkBtlePermissions()
    }

    /**
     * Activity lifecycle.
     */
    override fun onPause() {
        super.onPause()
        scanLeDevice(false)
    }

    /**
     * Activity lifecycle.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (::bluetoothGatt.isInitialized) {
            bluetoothGatt.close()
        }
    }

    /**
     * Inflates the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Triggered when an option in the menu has been selected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bledevices -> {
                scanLeDevice(true)
                checkBtleConfig()
            }
            R.id.action_settings -> {
                startActivityForResult(Intent(this, SettingsActivity::class.java), REQUEST_SETTINGS)
            }
        }
        return true
    }

    /**
     * Activity lifecycle.
     * Using the result of the started activity, scans BLE devices if BLE permission enabled, or check
     * BLE configurations.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Logger.d("Bluetooth has been enabled, scanning...")
                        scanLeDevice(true)
                    }
                }
            }
            REQUEST_BT_PERMISSION -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Logger.d("Bluetooth permission granted")
                        checkBtleConfig()
                    }
                }
            }
            REQUEST_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                        when (preferences.getInt(PropertiesKeys.DIFFICULTY_FACTOR.key,1)) {
                                0 -> model.difficultyFactor = DifficultyFactor.LOW
                                2 -> model.difficultyFactor = DifficultyFactor.HIGH
                                else /* and 1 */ -> model.difficultyFactor = DifficultyFactor.MEDIUM
                        }
                        toast(this.getString(R.string.toast_difficulty_factor_changed, model.difficultyFactor))
                        Logger.d("Hardness factor has been changed: ${model.difficultyFactor}")
                    }
                }
            }
        }
    }

    // *************
    // Inner methods
    // *************

    /**
     * Checks and asks for permissions if needed.
     * Requested permission are mainly for Bluetooth.
     */
    private fun checkBtlePermissions() {
        // Here, "this" is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_BT_PERMISSION)
        }
    }

    /**
     * Checks the Bluetooth configuration.
     * Starts the dedicated activity if needed, scans devices and check connections.
     */
    private fun checkBtleConfig() {
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } ?: run {
            Logger.d("Bluetooth is enabled, scanning...")
            scanLeDevice(true)
            checkBtleConnection()
        }
    }

    /**
     * Checks the Bluetooth connection and the devices.
     * If nothing is connected and no pop-up displayed, get the devices, ask for appearing
     * and process the connection.
     */
    private fun checkBtleConnection() {
        if ((!this.connected) && (!showingPopup)) {
            val deviceNames = model.getBoxes().value?.map { x -> x.name }
            deviceNames?.let {
                displayDeviceSelectorDialog(it)
            }
        }
    }

    /**
     * Starts or stops the scan of Bluetooth LE devices
     *
     * @param enable If true scans devices, otherwise stops scan operations
     */
    private fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                bluetoothAdapter?.run {
                    Logger.d("BLE scanner starting its job")
                    val filter = ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(bleConfiguration?.serviceUUID)).build()
                    val settings = ScanSettings.Builder().setScanMode(SCAN_MODE_BALANCED).build()
                    this.bluetoothLeScanner.startScan(mutableListOf<ScanFilter>(filter), settings, btleScanCallback)
                }
            }
            else -> {
                bluetoothAdapter?.run {
                    Logger.d("BLE scanner stopping its job")
                    this.bluetoothLeScanner.stopScan(btleScanCallback)
                }
            }
        }
    }

    /**
     * Displays in a dialog the list of available devices which can be selected
     *
     * @param devices The found devices to display
     */
    private fun displayDeviceSelectorDialog(devices: List<String>) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.selector_connect_hand_title))

        builder.setSingleChoiceItems(devices.toTypedArray(), 0) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            val device = model.getBoxes().value?.let { value -> value.toTypedArray()[i] }
            device?.let { it ->
                bluetoothGatt = it.connectGatt(this, false, btleGattCallback)
                toast(this@MainActivity.getString(R.string.toast_connected_to_hand, it.name, it.address))
            }
            showingPopup = false
        }

        // Note Anko library's selector does not provide an implementation of "selector" with a dismiss listener to define
        builder.setOnDismissListener {
            showingPopup = false
        }

        builder.setOnCancelListener {
            showingPopup = false
        }

        val dialog = builder.create()
        showingPopup = true
        dialog.show()

    }

}

