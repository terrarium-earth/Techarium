package earth.terrarium.techarium.block.multiblock;

import net.minecraft.util.StringRepresentable;


public enum MultiblockState implements StringRepresentable {

	NONE("none"),  // multiblock not selected (blue)
	INVALID("invalid"),  // multiblock selected but not valid (red)
	VALID("valid");  //  multiblock selected and valid (green)

	private final String name;

	MultiblockState(String name) {
		this.name = name;
	}

	public String toString() {
		return this.getSerializedName();
	}

	public String getSerializedName() {
		return this.name;
	}

}
