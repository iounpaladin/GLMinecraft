package co.megadodo.mc;

import org.joml.Vector2f;
import org.joml.Vector3f;

import co.megadodo.mc.block.Chunk;
import co.megadodo.mc.gl.Framebuffer;
import co.megadodo.mc.gl.Mesh;
import co.megadodo.mc.gl.MouseMoveCallback;
import co.megadodo.mc.gl.Shader;
import co.megadodo.mc.gl.Text;
import co.megadodo.mc.gl.Window;

public class Game {
	
	public Mesh mesh;
	public Shader shader;
	public Mesh quad;
	public Shader postprocess;
	public Text txt;
	public Camera cam;
	public Framebuffer fboWorld;
	public FPSTimer fps;
	public Timer frameTimer;
	public float dt;
	public ChunkManager chunkManager;
	
	public void init(Window window) {
		chunkManager=new ChunkManager();
		chunkManager.instantiateChunk(0, 0);
		mesh=new Mesh();
		mesh.addBuffer3f(0, new float[] {
				0,0,0,
				1,0,0,
				0,1,0,
				1,1,0,

				0,0,1,
				1,0,1,
				0,1,1,
				1,1,1,

				0,0,0,
				1,0,0,
				0,0,1,
				1,0,1,

				0,1,0,
				1,1,0,
				0,1,1,
				1,1,1,

				0,0,0,
				0,1,0,
				0,0,1,
				0,1,1,

				1,0,0,
				1,1,0,
				1,0,1,
				1,1,1,
		});
		mesh.addBuffer3f(1, new float[] {
				0,0,0,
				1,0,0,
				0,1,0,
				1,1,0,

				0,0,1,
				1,0,1,
				0,1,1,
				1,1,1,

				0,0,0,
				1,0,0,
				0,0,1,
				1,0,1,

				0,1,0,
				1,1,0,
				0,1,1,
				1,1,1,

				0,0,0,
				0,1,0,
				0,0,1,
				0,1,1,

				1,0,0,
				1,1,0,
				1,0,1,
				1,1,1,
		});
		mesh.setIndices(new int[] {
				0,1,2,
				1,2,3,
				
				4,5,6,
				5,6,7,
				
				8,9,10,
				9,10,11,
				
				12,13,14,
				13,14,15,
				
				16,17,18,
				17,18,19,
				
				20,21,22,
				21,22,23,
		});		
		shader=new Shader("test");
		

		cam=new Camera();
		cam.pos=new Vector3f(2,2,2);
		cam.dir=new Vector3f(0,0,1);
		
		
		quad=new Mesh();
		quad.addBuffer2f(0, new float[] {
				0,0,
				1,0,
				0,1,
				1,1
		});
		quad.setIndices(new int[] {
				0,1,2,
				1,2,3
		});
		
		postprocess=new Shader("postprocess");

		
		fboWorld=new Framebuffer();

		txt=new Text(fboWorld);
		
		fps=new FPSTimer();
		
		frameTimer=new Timer();
		frameTimer.mark();
		
		window.hideMouse();
		
		window.addOnMouseMove(new MouseMoveCallback() {
			
			@Override
			public void onMouseMove(Window window, float mx, float my) {
				if(new Vector2f(mx-window.width/2,my-window.height/2).length()<5)return;
				System.out.println("mouse move "+mx+" "+my);
				cam.handleMouseMove(new Vector2f(mx-window.width/2,my-window.height/2));
//				window.setMouse(window.width/2, window.height/2);
			}
		});
	}
	
	public void update(Window window) {
		fps.countFrame();
		
		dt=frameTimer.delta();
		cam.dt=dt;
		frameTimer.mark();
		cam.handleInput(window);
		

		
		if(window.isKeyDown('/'))window.close();
	}
	
	public void render(Window window) {			
		fboWorld.start();
		Utils.initGLFrame(fboWorld);
		Utils.setDepth(true);
		
//		shader.bind();
//		shader.setMat4f("perspective", cam.getPerspective());
//		shader.setMat4f("view", cam.getView());
//		mesh.renderElements();
		chunkManager.render(cam);
//		Chunk c=new Chunk();
//		c.createMesh();
//		c.render();
		
		fboWorld.end();
		
		
		Utils.initGLFrame(window);
		Utils.setDepth(false);

		postprocess.bind();
		fboWorld.bindColor(0);
		postprocess.setSampler("tex", 0);
		quad.renderElements();
		
		

		txt.setText("Minecraft\n"
				 + "Camera position: ["+Utils.formatFloat(cam.pos.x)+", "+Utils.formatFloat(cam.pos.y)+", "+Utils.formatFloat(cam.pos.z)+"]\n"
				 + "Camera direction: ["+Utils.formatFloat(cam.dir.x)+", "+Utils.formatFloat(cam.dir.y)+", "+Utils.formatFloat(cam.dir.z)+"]\n"
				 + "FPS: "+Utils.formatFloat(fps.getFPS())+"\n"
				 + "SPF: "+Utils.formatFloat(fps.getSPF())+"\n"
				 + "DT: "+Utils.formatFloat(dt));
		txt.render(-1+0.0025f,1-0.0025f, 0.03f);
	}

}
