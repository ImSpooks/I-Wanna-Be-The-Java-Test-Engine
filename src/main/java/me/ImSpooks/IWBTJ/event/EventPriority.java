package me.ImSpooks.IWBTJ.event;

public enum EventPriority {
	LOWEST((byte)0),
	LOW((byte)1),
	NORMAL((byte)2),
	HIGH((byte)3),
	HIGHEST((byte)4),
	MONITOR((byte)5);

	private final byte slot;

	private EventPriority(byte slot) {
		this.slot = slot;
	}

	public byte getSlot() {
		return this.slot;
	}

	public static byte[] VALUE_ARRAY (){
		return new byte[] {LOWEST.getSlot(), LOW.getSlot(), NORMAL.getSlot(), HIGH.getSlot(), HIGHEST.getSlot(), MONITOR.getSlot()};
	}
}
