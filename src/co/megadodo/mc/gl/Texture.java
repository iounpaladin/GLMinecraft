package co.megadodo.mc.gl;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;

public class Texture {
	
	public int id;
	public int width;
	public int height;
	
	public Texture(String fn) {
		id=glGenTextures();
		glBindTexture(GL_TEXTURE_2D,id);
		//set params here
		
		
		
		int[]w=new int[1];
		int[]h=new int[1];
		int[]comps=new int[1];
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer data=stbi_load("Textures/"+fn,w,h,comps,0);
		if(data==null) {
			throw new RuntimeException("Failed to load image "+fn);
		}
		
		
		width=w[0];
		height=h[0];
		glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,width,height,0,GL_RGB,GL_UNSIGNED_BYTE,data);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		stbi_image_free(data);

	}
	
	public void bind(int unit) {
		glActiveTexture(GL_TEXTURE0+unit);
		glBindTexture(GL_TEXTURE_2D,id);
	}

}