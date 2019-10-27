package example;

import Automation.BDaq.DeviceInformation;
import Automation.BDaq.DiSnapEventArgs;
import Automation.BDaq.DiSnapEventListener;
import Automation.BDaq.DiintChannel;
import Automation.BDaq.InstantDiCtrl;

public class DI_Interrupt {
	
	static class InterruptListener implements DiSnapEventListener
	{
	   @Override
	   public void DiSnapEvent(Object sender, DiSnapEventArgs args) {
	      System.out.println("interrupt occurred on channel#" + args.SrcNum);
	   }
	}
	public static void ChannelInterruptDi()
	{
	   // Step 1: create 'InstantDiCtrl' object
	   InstantDiCtrl ctrl = new InstantDiCtrl();
	   
	   // Step 2: register the event handler
	   ctrl.addInterruptListener(new InterruptListener());
	   
	   try {
	      // Step 3: select the device with 'ModeWriteWithReset' mode.
	      int deviceNumber = 0;
	      ctrl.setSelectedDevice(new DeviceInformation(deviceNumber));   
	      
	      // Step 4: enable the interrupt function of interesting channels
	      DiintChannel[] channels = ctrl.getDiintChannels();
	      if (channels == null) {
	         System.out.println("The device doesn't support DI interrupt!");
	         return;
	      }
	      channels[0].setEnabled(true);
	   } catch (Exception e) {
	      return;
	   }
	   
	   // Step 5: start DI snap
	   ctrl.SnapStart(); 
	   try {
	      while (System.in.available() == 0) {
	         Thread.sleep(1000);
	      }
	   } catch (Exception e) { }
	   
	   // Step 6: stop DI snap
	   ctrl.SnapStop();
	   
	   // Step 7: close device and release any allocated resource
	   ctrl.Cleanup();
	}

}
