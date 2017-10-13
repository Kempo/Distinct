package kempodev.distinct.modules.misc;

public class Location {
	private int x,y,z;
	private int blockID;
	private int color;
	public Location(int id, int x, int y, int z,int color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockID = id;
		this.color = color;
	}
	public int getColor() {
		return color;
	}
	public int getPosX() {
		return x;
	}
	public int getPosY() {
		return y;
	}
	public int getPosZ() {
		return z;
	}
	public int getID() {
		return blockID;
	}
	public boolean equals(Object var1)
    {
        if (!(var1 instanceof Location))
        {
            return true;
        }
        else
        {
            Location var2 = (Location)var1;
            return var2.getPosX() == this.getPosX() && var2.getPosY() == this.getPosY() && var2.getPosZ() == this.getPosZ();
        }
    }
}
