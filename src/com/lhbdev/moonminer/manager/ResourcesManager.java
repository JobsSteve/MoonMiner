package com.lhbdev.moonminer.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.lhbdev.moonminer2.MainActivity;

public class ResourcesManager 
{
//------------------------------------------------------------------------------------------
//VARIABLES
//------------------------------------------------------------------------------------------
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public MainActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
//-----------------------------------------------------------------------------------------
//END VARIABLES
//------------------------------------------------------------------------------------------
	
	
//-----------------------------------
//TEXTURES & TEXTURE REGIONS
//-----------------------------------
	//-----------------------------------------------------
	//Splash Scene Textures
	//-----------------------------------------------------
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	//-----------------------------------------------------
	//END Splash Scene Textures
	//-----------------------------------------------------
	
	//-----------------------------------------------------
	//Main Menu Textures
	//-----------------------------------------------------
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	//-----------------------------------------------------
	//END Main Menu Textures
	//-----------------------------------------------------
	
	//-----------------------------------------------------
	//FONTS
	//-----------------------------------------------------
	public Font font;
	//-----------------------------------------------------
	//END Fonts
	//-----------------------------------------------------	
	
	//-----------------------------------------------------
	//Game Textures
	//-----------------------------------------------------
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion rockTop_region;
	public ITextureRegion rockMid_region;
	public ITextureRegion rockOre_region;
	//-----------------------------------------------------
	//END Game Textures
	//-----------------------------------------------------
	
	//-----------------------------------------------------
	//Player Textures
	//-----------------------------------------------------
	public ITiledTextureRegion player_region;
	//----------------------------------------------------
	//END Player Textures
	//----------------------------------------------------
	
	//----------------------------------------------------
	//HUD CONTROL TEXTURE
	//----------------------------------------------------
	public BuildableBitmapTextureAtlas controlTextureAtlas;
	public ITextureRegion left_region;
	public ITextureRegion right_region;
	
	
	public ITextureRegion dpad_region;
	//----------------------------------------------------
	//END HUD
	//----------------------------------------------------
	
//-----------------------------------------------------------------------------------------
//END TEXTURES & TEXTURE REGIONS
//-----------------------------------------------------------------------------------------
	
	//------------------------------------------------------------------------------------
	//CLASS LOGIC
	//-------------------------------------------------------------------------------------
	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256,256, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png",0,0);
    	splashTextureAtlas.load();
	}
	
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	
	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}
	
	
	
	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
		try
		{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
			this.menuTextureAtlas.load();
		}
		catch(final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}
	
	private void loadMenuAudio()
	{
		//TODO menu audio
	}
	
	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),256,256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}
	
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}
	
	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	
	public void loadGameResources()
	{
		//loadControls();
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadGameAudio() 
	{
		
	}

	private void loadGameFonts() 
	{
		
	}

	private void loadGameGraphics() 
	{
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_NEAREST_PREMULTIPLYALPHA);
        rockTop_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Red_Rock_Top.png");
        rockMid_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Red_Rock_Middle.png");
        rockOre_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Ore_Rock.png");
        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 1, 1);
        dpad_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Dpad_Notpressed.png");
        try
        {
        	this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
        	this.gameTextureAtlas.load();
        }
        catch(final TextureAtlasBuilderException e)
        {
        	Debug.e(e);
        }
	}
	
	
	public void unloadGameTextures()
	{
		
	}
	
	public static void prepareManager(Engine engine, MainActivity activity, BoundCamera camera2, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera2;
		getInstance().vbom = vbom;
	}
	
	//=========================================
	//GETTERS & SETTERS
	//=========================================
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
	

}
