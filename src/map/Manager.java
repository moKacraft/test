package map;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import Automation.BDaq.DeviceInformation;
import Automation.BDaq.DiCosintPort;
import Automation.BDaq.DiSnapEventArgs;
import Automation.BDaq.DiSnapEventListener;
import Automation.BDaq.DiintChannel;
import Automation.BDaq.ErrorCode;
import Automation.BDaq.InstantDiCtrl;

public class Manager implements DiSnapEventListener {
	
	private final ConcurrentHashMap<Info, ArrayList<SignalListener>> map = new ConcurrentHashMap<>();
	
	private final String deviceName;

	public Manager(String deviceName) {
		super();
		this.deviceName = deviceName;
	}

	@Override
	public void DiSnapEvent(Object listener, DiSnapEventArgs event) {
		// Channel data?
		// If so why byte array if only 8 channel
		// DI port data which scanned when the DI Interrupt occured.
		final byte[] portData = event.PortData;

		// The channel number of the channel which DI snap event occurs.
		final int channelNumber = event.SrcNum;

		final Info i = new Info(deviceName, 1, (byte) 1);
		System.out.println("signal received from: " + i);

		if (map.containsKey(i)) {
			for (SignalListener l : map.get(i)) {
				l.signalReceived(i);
			}
		}
	}

	private static byte setNthBitOfByte(byte byt, int n) {
		return (byte) ((1 << n) | (byt & 0xff));
	}

	private InstantDiCtrl initNewCardForDIStatusChangeInterrupt(final String deviceName, final int selectedPortValue,
			final int selectedChannel) {
		InstantDiCtrl instantDiCtrl = new InstantDiCtrl();
		try {
			instantDiCtrl.setSelectedDevice(new DeviceInformation(deviceName));
			DiCosintPort[] ports = instantDiCtrl.getDiCosintPorts();
			if (ports == null) {
				System.err.println("Error occured");
			}

			DiCosintPort selectedPort = null;
			for (DiCosintPort port : ports) {
				if (port.getPort() == selectedPortValue) {
					selectedPort = port;
				}
			}
			if (selectedPort == null) {
				System.err.println("Error occured");
			} else {
				selectedPort.setMask(setNthBitOfByte((byte) 0, selectedChannel));
			}

		} catch (Exception ex) {
			System.err.println("Error occured");
		}
		return instantDiCtrl;
	}

	private InstantDiCtrl initNewCardForDIInterrupt(final String deviceName, final int selectedPortValue,
			final int selectedChannel) {
		InstantDiCtrl instantDiCtrl = new InstantDiCtrl();
		try {
			instantDiCtrl.setSelectedDevice(new DeviceInformation(deviceName));
			DiintChannel[] intChannels = instantDiCtrl.getDiintChannels();
			if (intChannels == null) {
				System.err.println("Error occured");
			}
			// From advantech DIInterupt Example
			// Port * (numbers of channels per port) + channel
			intChannels[selectedPortValue * 8 + selectedChannel].setEnabled(true);


		} catch (Exception ex) {
			System.err.println("Error occured");
		}
		return instantDiCtrl;
	}

	private void startDiCtrl(final InstantDiCtrl instantDiCtrl) {
		ErrorCode errorCode = instantDiCtrl.SnapStart();
		// if (Global.BioFaild(errorCode)) {
		System.out.println("Status from snapStart:" + errorCode.toString());
		// }
	}

	private void stopDiCtrl(final InstantDiCtrl instantDiCtrl) {
		ErrorCode errorCode = instantDiCtrl.SnapStop();
		// if (Global.BioFaild(errorCode)) {
		System.out.println("Status from snapStop:" + errorCode.toString());
		// }
	}

	public static void main(String[] args) {
		final String deviceName = "card1";
		
		Manager m = new Manager(deviceName);

		Info i = new Info(deviceName, 1, (byte) 1);
		SignalListener l = new SignalListenerImpl("l");
		SignalListener l1 = new SignalListenerImpl("l1");
		SignalListener l2 = new SignalListenerImpl("l2");
		SignalListener l3 = new SignalListenerImpl("l3");

		Info i1 = new Info(deviceName, 2, (byte) 1);
		SignalListener l4 = new SignalListenerImpl("l4");
		SignalListener l5 = new SignalListenerImpl("l5");
		SignalListener l6 = new SignalListenerImpl("l6");
		SignalListener l7 = new SignalListenerImpl("l7");

		m.addListener(i, l);
		m.addListener(i, l1);
		m.addListener(i, l2);
		m.addListener(i, l3);
		m.addListener(i, l4);
		
		m.addListener(i1, l4);
		m.addListener(i1, l5);
		m.addListener(i1, l6);
		m.addListener(i1, l7);

		System.out.println("map" + m.map.size());

		m.DiSnapEvent(null, new DiSnapEventArgs(1, new byte[] { 1 }));
	}
	
	private void addListener(final Info i, final SignalListener listener) {
		System.out.println("will attempt to add listener in map");

		if (!map.containsKey(i)) {

			System.out.println("map doesnt contains info");

			ArrayList<SignalListener> listenerArray = new ArrayList<>();
			listenerArray.add(listener);
			map.put(i, listenerArray);
		} else {

			System.out.println("map contains info");

			ArrayList<SignalListener> list = map.get(i);
			if (list.contains(listener)) {

				System.out.println("listener already in list");

			} else {

				System.out.println("adding listener in list");

				list.add(listener);
			}
		}
	}

}
