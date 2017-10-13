package kempodev.distinct.utilities;



import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;

public class VertexRenderer {

	public static VertexRenderer instance = new VertexRenderer(2097152);

	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private ShortBuffer shortBuffer;

	private int[] rawBuffer;

	private int vertexCount;

	private int bufferSize;

	private int field_147569_p;

	private int drawMode;

	private int addedVertices;

	private double xOffset;

	private double yOffset;

	private double zOffset;
	
	private boolean hasTexture;
	
	private boolean isDrawing;
	/** The first coordinate to be used for the texture. */
    private double textureU;

    /** The second coordinate to be used for the texture. */
    private double textureV;
	private VertexRenderer(int par1) {
		this.bufferSize = par1;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(par1 * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.shortBuffer = this.byteBuffer.asShortBuffer();
		this.rawBuffer = new int[par1];
	}

	public int draw() {
		if (!this.isDrawing)
        {
            throw new IllegalStateException("Not tesselating!");
        }
        else
        {
            this.isDrawing = false;
		if (this.vertexCount > 0) {
			this.intBuffer.clear();
			this.intBuffer.put(this.rawBuffer, 0, this.field_147569_p);
			this.byteBuffer.position(0);
			this.byteBuffer.limit(this.field_147569_p * 4);

			this.floatBuffer.position(0);
			GL11.glVertexPointer(3, 32, this.floatBuffer);
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		}
		int var1 = this.field_147569_p * 4;
		this.reset();
		return var1;
        }
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.field_147569_p = 0;
		this.addedVertices = 0;
	}

	public void startDrawingQuads() {
		this.startDrawing(7);
	}

	public void startDrawing(int par1) {
		if (this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = par1;
		}
	}

	public void addVertex(double par1, double par3, double par5) {
		++this.addedVertices;
		if (this.hasTexture)
        {
            this.rawBuffer[this.field_147569_p + 3] = Float.floatToRawIntBits((float)this.textureU);
            this.rawBuffer[this.field_147569_p + 4] = Float.floatToRawIntBits((float)this.textureV);
        }
		this.rawBuffer[this.field_147569_p + 0] = Float.floatToRawIntBits((float) (par1 + this.xOffset));
		this.rawBuffer[this.field_147569_p + 1] = Float.floatToRawIntBits((float) (par3 + this.yOffset));
		this.rawBuffer[this.field_147569_p + 2] = Float.floatToRawIntBits((float) (par5 + this.zOffset));
		this.field_147569_p += 8;
		++this.vertexCount;

		if (this.vertexCount % 4 == 0
				&& this.field_147569_p >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}
	}
	/**
     * Adds a vertex specifying both x,y,z and the texture u,v for it.
     */
    public void addVertexWithUV(double x, double y, double z, double u, double v)
    {
        this.setTextureUV(u, v);
        this.addVertex(x, y, z);
    }
    public void setTextureUV(double par1, double par3)
    {
        this.hasTexture = true;
        this.textureU = par1;
        this.textureV = par3;
    }
}
