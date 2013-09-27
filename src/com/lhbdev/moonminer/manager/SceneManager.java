package com.lhbdev.moonminer.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.lhbdev.moonminer.base.BaseScene;
import com.lhbdev.moonminer.scene.GameScene;
import com.lhbdev.moonminer.scene.LoadingScene;
import com.lhbdev.moonminer.scene.MainMenuScene;
import com.lhbdev.moonminer.scene.SplashScene;

public class SceneManager 
{
	//---------------------------------------------------------------
	//SCENES
	//----------------------------------------------------------------
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	//-----------------------------------------------------------------
	//END SCENES
	//-----------------------------------------------------------------
	
	//-------------------------------------------------------------------
	//VARIABLES
	//-------------------------------------------------------------------
	private static final SceneManager INSTANCE = new SceneManager();
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;
	private Engine engine = ResourcesManager.getInstance().engine;
	
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING,
	}
	
	//---------------------------------------------------------------------
	//END VARIABLES
	//---------------------------------------------------------------------
	
	//---------------------------------------------------------------------
	//CLASS LOGIC
	//---------------------------------------------------------------------
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType)
	{
		switch(sceneType)
		{
		case SCENE_MENU:
			setScene(menuScene);break;
		case SCENE_GAME:
			setScene(gameScene);break;
		case SCENE_SPLASH:
			setScene(splashScene);break;
		case SCENE_LOADING:
			setScene(loadingScene);break;
		default:break;
		}
	}
	
	
	//------------------------------------------------------------------------
	//GETTERS AND SETTERS
	//------------------------------------------------------------------------
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
	
	public void createSplashScene(OnCreateSceneCallback pCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pCallback.onCreateSceneFinished(splashScene);
	}
	
	//------------------------------------------------------------------------
	//SCENE DISPOSAL
	//------------------------------------------------------------------------
	private void disposeSplashScene()
	{
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	//--------------------------------------------------------------------------
	//SCENE CREATION & LOADING
	//--------------------------------------------------------------------------
	public void createMenuScene()
	{
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(menuScene);
		disposeSplashScene();
	}

	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
		{
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				//mEngine.getCamera().set(0, 0, 500, 300);
				ResourcesManager.getInstance().loadGameResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
	}

	//display loading screen when coming from game scene
	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);
		gameScene.disposeScene();
		ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,new ITimerCallback()
		{
			public void onTimePassed(final TimerHandler pTimer)
			{
				mEngine.unregisterUpdateHandler(pTimer);
				ResourcesManager.getInstance().loadMenuTextures();
				setScene(menuScene);
			}
		}));
	}
	
}
