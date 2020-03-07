package com.health.hl.params;



import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.health.hl.app.App;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CubicBLEDevice extends BLEDevice {

	public CubicBLEDevice(Context context, BluetoothDevice bluetoothDevice) {
		// TODO Auto-generated constructor stub
		super(context, bluetoothDevice);
	}
	/**
	 * 初始化服务中的特征
	 */
	@Override
	protected void discoverCharacteristicsFromService() {
		// TODO Auto-generated method stub
		Log.d(App.TAG, "load all the services ");
		if(bleService!=null&&device!=null&&bleService
				.getSupportedGattServices(device)!=null)
		for (BluetoothGattService bluetoothGattService : bleService
				.getSupportedGattServices(device)) {
			String serviceUUID = Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits())
					.substring(0, 4);
			for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
					.getCharacteristics()) {
				String characterUUID = Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
								.getMostSignificantBits()).substring(0, 4);

			}
		}
	}

	/**
	 * 
	 * @param serviceUUID
	 * @param characteristicUUID
	 * @param data
	 */
	public void writeValue(String serviceUUID, String characteristicUUID,
			byte[] value) {
		// TODO Auto-generated method stub
		if(bleService!=null&&device!=null&&bleService
				.getSupportedGattServices(device)!=null)
		for (BluetoothGattService bluetoothGattService : bleService
				.getSupportedGattServices(device)) {
			Log.i("gattServiceUUID", "------------"+Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits()).substring(0, 4));
			String gattServiceUUID = Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits())
					.substring(0, 4);
			for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
					.getCharacteristics()) {
				Log.i("characterUUID", "------------"+Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
						.getMostSignificantBits()).substring(0, 4));
				String characterUUID = Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
								.getMostSignificantBits()).substring(0, 4);
				if (gattServiceUUID.equals(serviceUUID)
						&& characteristicUUID.equals(characterUUID)) {
					bluetoothGattCharacteristic.setValue(value);
					this.writeValue(bluetoothGattCharacteristic);
				}
			}
		}
	}

	public void writeValue1(byte[] value) {
		// TODO Auto-generated method stub
		if(bleService!=null&&device!=null&&bleService
				.getSupportedGattServices(device)!=null)
		for (BluetoothGattService bluetoothGattService : bleService
				.getSupportedGattServices(device)) {
			Log.i("gattServiceUUID", "------------"+Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits()).substring(0, 4));
			String gattServiceUUID = Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits())
					.substring(0, 4);
			for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
					.getCharacteristics()) {
				Log.i("characterUUID", "------------"+Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
						.getMostSignificantBits()).substring(0, 4));
				String characterUUID = Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
								.getMostSignificantBits()).substring(0, 4);
				bluetoothGattCharacteristic.setValue(value);
				this.writeValue(bluetoothGattCharacteristic);
				
			}
		}
	}
	
	
	/**
	 * 
	 * @param serviceUUID
	 * @param characteristicUUID
	 */
	public void readValue(String serviceUUID, String characteristicUUID) {
		if(bleService!=null&&device!=null&&bleService
				.getSupportedGattServices(device)!=null)
		for (BluetoothGattService bluetoothGattService : bleService
				.getSupportedGattServices(device)) {
			String gattServiceUUID = Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits())
					.substring(0, 4);
			for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
					.getCharacteristics()) {
				String characterUUID = Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
								.getMostSignificantBits()).substring(0, 4);
				if (gattServiceUUID.equals(serviceUUID)
						&& characteristicUUID.equals(characterUUID)) {
					Log.i(App.TAG, "charaterUUID read is success  : "
							+ Long.toHexString(bluetoothGattCharacteristic.getUuid().getMostSignificantBits()));
					this.readValue(bluetoothGattCharacteristic);
				}
			}
		}
	}

	/**
	 * 
	 * @param serviceUUID
	 * @param characteristicUUID
	 */
	public void setNotification(String serviceUUID, String characteristicUUID,
			boolean enable) {
		if(bleService!=null&&device!=null)
		for (BluetoothGattService bluetoothGattService : bleService
				.getSupportedGattServices(device)) {
			String gattServiceUUID = Long.toHexString(
					bluetoothGattService.getUuid().getMostSignificantBits())
					.substring(0, 4); //getMostSignificantBits() 方法用来返回此UUID的128位最显著64位值
			for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
					.getCharacteristics()) {
				String characterUUID = Long.toHexString(
						bluetoothGattCharacteristic.getUuid()
								.getMostSignificantBits()).substring(0, 4);
				if (gattServiceUUID.equals(serviceUUID)
						&& characteristicUUID.equals(characterUUID)) {
//					this.setCharacteristicNotification(
//							bluetoothGattCharacteristic, enable);
					this.setCharacteristicNotification(bluetoothGattService,
							bluetoothGattCharacteristic, enable);
				}
			}
		}
	}

//	private void writeDefaultValue(){
//		
//	}
	
	
	
}
