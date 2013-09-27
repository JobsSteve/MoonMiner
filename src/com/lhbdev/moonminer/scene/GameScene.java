package com.lhbdev.moonminer.scene;

import java.io.IOException;
import java.util.Vector;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lhbdev.moonminer.base.BaseScene;
import com.lhbdev.moonminer.manager.ResourcesManager;
import com.lhbdev.moonminer.manager.SceneManager;
import com.lhbdev.moonminer.manager.SceneManager.SceneType;
import com.lhbdev.moonminer.object.Dpad;
import com.lhbdev.moonminer.object.Player;

public class GameScene extends BaseScene
{
	private HUD gameHUD;
	private Text scoreText;
	private int score = 0;
	private PhysicsWorld physicsWorld;
	
	//level loader
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TOPROCK = "topRock";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MIDROCK = "midRock";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_OREROCK = "oreRock";
	
	//player
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private Player player;
	private boolean firstTouch = false;
	private Text gameOverText;
	private boolean gameOverDisplayed = false;
	private Body playerbody;
	
	//collision
	private Body collide;
	
	
	@Override
	public void createScene()
	{
		camera.set(0, 0, 385, 231);
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(1);
		createGameOverText();
	}

	//send back to main menu
	@Override
	public void onBackKeyPressed()
	{
		System.out.println("back");
		SceneManager.getInstance().loadMenuScene(engine);
	}
	
	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}
	
	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		camera.setChaseEntity(null);
		camera.setCenter(400, 240);
		camera.set(0, 0, 800, 480);
		
		//TODO code for disposing gamescene
		//remove all game scene objects
	}
	
	private void createBackground()
	{
		//TODO change background from temp blue screen
		setBackground(new Background(Color.BLUE));
	}
	
	long speed;
	
	private void createHUD()
	{
		gameHUD = new HUD();
		
		//create score text
		scoreText = new Text(0, camera.getYMax() - 10, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),vbom);
		scoreText.setAnchorCenter(0, 0);
		scoreText.setScale(0.3f);
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);
		
		//create controls
		Dpad controls = new Dpad(665, 345, camera, vbom, ResourcesManager.getInstance().dpad_region );
		
		gameHUD.attachChild(controls);
		
		final Rectangle left = new Rectangle(20, 200, 60, 60, vbom)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionMove())
	            {
	            	if(contactListener() != null)
	            	{
	            		if(((int)playerbody.getPosition().y)== ((int)collide.getPosition().y))
	            		{
	            			removeTile(collide.getAttachedSprite1());
	            		}
	            	}
	            	player.moveLeft();
	            }
	            if(touchEvent.isActionUp())
	            {
	            	player.stop();
	            }
	            return true;
	        };
	    };
	    
	    final Rectangle up = new Rectangle(140, 200, 60, 60, vbom)
	    {
	    	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	    	{
	    		if(touchEvent.isActionMove())
	    		{
	    			player.fly();
	    			
	    		}
	    		
	    		return true;
	    	};
	    };
	    
	    
	    
	    final Rectangle right = new Rectangle(100, 200, 60, 60, vbom)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionMove())
	            {
	            	if(contactListener() != null)
	            	{
	            		if(((int)playerbody.getPosition().y) == ((int)collide.getPosition().y))
	            		{
	            			removeTile(collide.getAttachedSprite1());
	            		}
	            	}
	                player.moveRight();
	            }
	            if(touchEvent.isActionUp())
	            {
	            	player.stop();
	            }
	            return true;
	        };
	    };
	    
	    
	    final Rectangle down = new Rectangle(60, 160, 30, 30, vbom)
	    {
	    	
	    	public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	    	{
	    		if(touchEvent.isActionMove())
	    		{
	    			if(contactListener()!= null)
	    			{
	    				if(((int)playerbody.getPosition().x)==((int)collide.getPosition().x))
	    				{
	    					removeTile(collide.getAttachedSprite1());
	    				}
	    			}
	    			System.out.println("down");
	    			player.moveDown();
	    		}
	    		if(touchEvent.isActionUp())
	    		{
	    			player.stop();
	    		}
	    		return true;
	    	};
	    };
	    
	    gameHUD.registerTouchArea(left);
	    gameHUD.registerTouchArea(right);
	    gameHUD.registerTouchArea(down);
	    gameHUD.registerTouchArea(up);
	    
	    gameHUD.attachChild(left);
	    gameHUD.attachChild(right);
	    gameHUD.attachChild(down);
	    gameHUD.attachChild(up);
		
	    
	    
		camera.setHUD(gameHUD);
	}

	private void addToScore(int i)
	{
		score += i;
		scoreText.setText("Score: " + score);
		
	}

	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}
	
	private void loadLevel(int levelID)
	{
	    final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
	    
	    final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
	    
	    levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
	    {
	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, 
	        		final Attributes pAttributes, 
	        		final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
	        {
	            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
	            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
	            
	            camera.setBounds(0, 0, width, height); //set camera bounds
	            camera.setBoundsEnabled(true);

	            return GameScene.this;
	        }
	    });
	    
	    levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
	    {
	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
	        {
	            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
	            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
	            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
	            
	            final Sprite levelObject;
	            
	            //load topRock
	            if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TOPROCK))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.rockTop_region, vbom);
	                final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                body.setUserData("topRock");
	                body.setAttachedSprite(levelObject);
	                physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                
	            } 
	            
	            //load mid Rock
	            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MIDROCK))
	            {
	                levelObject = new Sprite(x, y, resourcesManager.rockMid_region, vbom);
	                final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                body.setUserData("midRock");
	                body.setAttachedSprite(levelObject);
	                physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	            }
	            
	            //load Ore Rocks
	            else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_OREROCK))
	            {
	            	levelObject = new Sprite(x,y, resourcesManager.rockOre_region, vbom);
	            	final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	            	body.setUserData("oreRock");
	            	body.setAttachedSprite(levelObject);
	            	physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	            }
	            
	            //load player
	            else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
	            {
	            	player = new Player(x, y, vbom, camera,physicsWorld)
	            	{
	            		@Override
	            		public void onDie()
	            		{
	            			if(!gameOverDisplayed)
	            			{
	            				displayGameOverText();
	            			}
	            		}
	            	};
	            	levelObject = player;
	            }
	            
	            else
	            {
	                throw new IllegalArgumentException();
	            }

	            levelObject.setCullingEnabled(true);

	            return levelObject;
	        }
	    });

	    levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
	}
	
	private void createGameOverText()
	{
		gameOverText = new Text(0,0,resourcesManager.font, "Game Over!",vbom);
	}
	
	private void displayGameOverText()
	{
		camera.setChaseEntity(null);
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}
	
	private ContactListener contactListener()
	{
		ContactListener contactListener = new ContactListener()
		{
			public void beginContact(Contact contact)
			{
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();	
				Body b1 = x1.getBody();
				Body b2 = x2.getBody();
				
				if(x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if(x2.getBody().getUserData().equals("player"))
					{
						
					}
				}
				
				if(x1.getBody().getUserData().equals("topRock") && x2.getBody().getUserData().equals("player"))
				{
					//what to do when player collides with top Rock
					//removeTile(b1.getAttachedSprite1());
					//if(b1.getPosition().y == player.)
					collide = b1;
					playerbody = b2;
					
				}
				
				else if(x1.getBody().getUserData().equals("midRock") && x2.getBody().getUserData().equals("player"))
				{
					collide = b1;
					playerbody = b2;
					
				}
				
				else if(x1.getBody().getUserData().equals("oreRock") && x2.getBody().getUserData().equals("player"))
				{
					//what to do when player collides with oreRock
				}
				
			}
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) 
			{
//				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) 
			{
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				if(x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if(x2.getBody().getUserData().equals("player"))
					{
						//decreaseFuel();
					}
				}
			}			
		};
		return contactListener;
	}
	
	public void removeTile(Sprite sprite)
	{
		final Sprite mySprite = sprite;
		engine.runOnUpdateThread(new Runnable()
		{
			final PhysicsConnector pcon = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(mySprite);
			final Body body = pcon.getBody();
			
			@Override
			public void run()
			{
				if(pcon!=null)
				{
					body.setActive(false);
					mySprite.detachSelf();
					physicsWorld.destroyBody(body);
				}
			}
		});
	}
	
	public void decreaseFuel()
	{
		player.decreaseFuel();
	}
}