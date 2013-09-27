package com.lhbdev.moonminer.object;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lhbdev.moonminer.manager.ResourcesManager;

public abstract class Player extends AnimatedSprite
{
	public PhysicsHandler physicsHandler;
	
	//----------------------------------------------------- 
	//CONSTRUCTOR
	//-----------------------------------------------------
	
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX,pY, ResourcesManager.getInstance().player_region,vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
		this.physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.physicsHandler);
	}
	
	
	//-------------------------------------------------------
	//VARIABLES
	//-------------------------------------------------------
	private Body body;
	private boolean canFly = true;
	private int maxFuel = 100;
	private int currentFuel =100;
	
	
	//--------------------------------------------------------
	//METHODS
	//--------------------------------------------------------
	public abstract void onDie();
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0,0,0));
		body.setUserData("player");
		body.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				if(getY()<=0)
				{
					onDie();
				}
			}
		});
	}
	
	public void jump()
	{
		if(currentFuel < 0)
		{
			return;
		}
		//body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
		final long[] PLAYER_ANIMATE = new long[] {1000, 100, 500, 100, 500, 100, 100};
		animate(PLAYER_ANIMATE, 0, 6, true);
	}
	
	public void increaseFuel()
	{
		currentFuel = 100;
	}
	
	public void decreaseFuel()
	{
		currentFuel -= 10;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		//moveRight();
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	float currspeed = 0;
	//float accel = 2.5f;
	public void moveLeft() 
	{
		//this.physicsHandler.setVelocityX(10);
		//currspeed = accel + speed;
		//if(currspeed > 1.5);
		//currspeed += 0.01f;
		
		this.body.setLinearVelocity(-1.0f, 0);
	}

	public void moveRight() 
	{
		
		this.body.setLinearVelocity(1.5f, 0);
	}
	
	public void moveDown()
	{
		this.body.setLinearVelocity(0, -1.5f);
	}
	
	public void fly()
	{
		this.body.setLinearVelocity(0, 1.5f);
	}

	public void stop() 
	{
		this.body.setLinearVelocity(0, 0);
	}

	public void moveUpRight() {
		// TODO Auto-generated method stub
		this.body.setLinearVelocity(1.5f, 1.5f);
		
	}
}
