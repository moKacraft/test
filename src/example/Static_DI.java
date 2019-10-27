package example;

import Automation.BDaq.DeviceInformation;
import Automation.BDaq.ErrorCode;
import Automation.BDaq.InstantDiCtrl;

public class Static_DI {
	void InstantDi() {
		// Step 1: create 'InstantDiCtrl' object
		InstantDiCtrl ctrl = new InstantDiCtrl();

		try {
			// Step 2: select the device with 'ModeWriteWithReset' mode.
			int deviceNumber = 0;
			ctrl.setSelectedDevice(new DeviceInformation(deviceNumber));
		} catch (Exception e) {
			return;
		}

		// Step 3: Scan the ports
		int portStart = 0;
		int portCount = 1;
		byte[] values = new byte[portCount];
		try {
			while (System.in.available() == 0) {
				ErrorCode ret = ctrl.Read(portStart, portCount, values);
				if (ret == ErrorCode.Success) {
					for (int i = 0; i < portCount; ++i) {
						System.out.printf("Port#%d = %x", i, values[i]);
					}
				} else {
					System.out.println(ret.toString());
					break;
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}

		// Step 4: close device and release any allocated resource
		ctrl.Cleanup();
	}

}
