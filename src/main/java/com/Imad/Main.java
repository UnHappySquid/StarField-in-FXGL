package com.Imad;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.scene.input.KeyCode;


public class Main extends GameApplication {

  // Get Screen Size
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  double width = screenSize.getWidth();
  double height = screenSize.getHeight();


  // set Number of stars (note that 5 times that amount will be spawned)
  int numStars = 300;


  @Override
  protected void initGame() {
    // Remove Cursor
    FXGL.getGameScene().setCursorInvisible();
    // Add factory
    FXGL.getGameWorld().addEntityFactory(new myFactory());
    // Black background
    FXGL.spawn("Background");

    // Spawns first set of stars, closer to view
    for (int i = 0 ; i<numStars;i++){
      FXGL.spawn("Star");
    }

    // spawns more stars but far away from the view
    for (int i = 0 ; i<numStars*4;i++){
      FXGL.spawn("Star",new SpawnData().put("farAway",true));
    }
    // adds entity used to control view  (limited to looking left and right)
    FXGL.spawn("Frontground");
  }

  @Override
  protected void initInput() {
    // make W make all stars go faster
   FXGL.getInput().addAction(new UserAction("faster") {
     @Override
     protected void onAction() {
       StarComponent.faster();
     }
   }, KeyCode.W);

   // make S slow down all stars
    FXGL.getInput().addAction(new UserAction("slower") {
      @Override
      protected void onAction() {
        StarComponent.slower();
      }
    }, KeyCode.S);

  }


  // initiating settings : set title, version, make full screen and set icon
  @Override
  protected void initSettings(GameSettings gameSettings) {
    gameSettings.setTitle("Stars");
    gameSettings.setAppIcon("smallstar.png");
    gameSettings.setVersion("");
    gameSettings.setWidth((int)width);
    gameSettings.setHeight((int)height);
    gameSettings.setFullScreenFromStart(true);
    gameSettings.setFullScreenAllowed(true);
  }

  public static void main(String[] args) {
    launch(args);
  }

}