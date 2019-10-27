package map;

public class Info {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + i;
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Info other = (Info) obj;
		if (b != other.b)
			return false;
		if (i != other.i)
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

	private final String s;
	private final int i;
	private final byte b;
	
	public Info(String s, int i, byte b) {
		super();
		this.s = s;
		this.i = i;
		this.b = b;
	}
	
	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}

	/**
	 * @return the i
	 */
	public int getI() {
		return i;
	}

	/**
	 * @return the b
	 */
	public byte getB() {
		return b;
	}

	@Override
	public String toString() {
		return "Info [s=" + s + ", i=" + i + ", b=" + b + "]";
	}
}
