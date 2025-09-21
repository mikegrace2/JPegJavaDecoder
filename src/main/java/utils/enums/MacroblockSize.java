package utils.enums;

/**
 * Enumeration for the Macroblock size.
 * @author michael
 */
public enum MacroblockSize {
	/**
	 * Macroblock Sizes
	 */
	UNSET(-1), // Not set yet
	/**
	 * 8x8
	 */
	EIGHT_TIMES_EIGHT(8), // 8x8=64 (4:4:4 no subsampling)
	/**
	 * 16x8
	 */
	SIXTEEN_TIMES_EIGHT(8), // 16x8=128 (4:2:2)
	/**
	 * 16x16
	 */
	SIXTEEN_TIMES_SIXTEEN(16); // most commonly 16x16=256 (4:2:0)
	
    private int value;
    
    private MacroblockSize(int value) {
        this.value = value;
    }
    
    /**
     * Get the value.
     * @return value
     */
    public int getValue(){
    	return this.value;
    }
}