package com.lhbdev.moonminer.object;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.Entity;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.lhbdev.moonminer.scene.GameScene;
import com.lhbdev.moonminer.manager.ResourcesManager;

public class Dpad extends Entity
{
	final float UpdateTime = 0.1f;
	final int UP = 1;
	final int RIGHT = 2;
	final int DOWN = 3;
	final int LEFT = 4;

	public Dpad(float pX, float pY, Camera camera, VertexBufferObjectManager vbom, ITextureRegion dpad_region)
	{
		
	}
	
	
}
