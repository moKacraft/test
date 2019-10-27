package map;

public class SignalListenerImpl implements SignalListener {

	private String name;

	public SignalListenerImpl(final String s) {
		name = s;
	}

	@Override
	public void signalReceived(Object o) {
		System.out.println("signal received on: " + name);
	}

}
