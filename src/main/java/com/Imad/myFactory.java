package com.Imad;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static com.almasb.fxgl.dsl.FXGL.*;

public class myFactory implements EntityFactory {

  // star entity
  @Spawns("Star")
  public Entity newStar(SpawnData data){
    // if the star is far Away, use a different constructor to
    if (data.getData().containsKey("farAway")){
      return FXGL.entityBuilder(data)
          .with(new StarComponent(true))
          .build();
    }
    // if the star is far Away, use a normal constructor
    return FXGL.entityBuilder(data)
        .with(new StarComponent())
        .build();

  }

// Background
  @Spawns("Background")
  public Entity newBackground(SpawnData data){
    Rectangle myBG = new Rectangle(getAppWidth(),getAppHeight(),Color.BLACK.brighter());
    return  FXGL.entityBuilder().view(myBG).build();
  }

  // Frontground, for moving mouse
  @Spawns("Frontground")
  public Entity newFrontGround(SpawnData data){

    // invisible rectangle with the ability to look around
    Rectangle myRectangle = new Rectangle(getAppWidth(),getAppHeight(), Color.BLACK);
    myRectangle.setOpacity(0);

    myRectangle.setOnMouseMoved(e -> {
      double x = e.getSceneX();
      double y =  e.getSceneY();
      // cursor position is useful for looking around
      StarComponent.aCursorX =x;
      StarComponent.aCursorY =y;
    });

    return  FXGL.entityBuilder().view(myRectangle).build();
  }


}
