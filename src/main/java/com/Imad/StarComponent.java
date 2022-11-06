package com.Imad;



import com.almasb.fxgl.entity.component.Component;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import static com.almasb.fxgl.core.math.FXGLMath.map;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

public class StarComponent extends Component {
  // app Width (can't be initiallized here due to slight problem with jUnit)
  int aWidth;
  //  app Height (can't be initiallized here due to slight problem with jUnit)
  int aHeight;
  // x position
  double x;
  // y position
  double y;
  // star depth
  double z;
  //  previous x after z relocation
  double sx;
  // previous y after z relocation
  double sy;
  //  current x after z relocation
  double dx;
  // current y after z relocation
  double dy;


  // "speed" of movement through z
  static double zChange = 5;
  //  CursorX  position, used for "looking around"
  static double aCursorX;
  // Cursor Y position, used for "looking around"
  static double aCursorY;
  // Scalor X used to displace screen, used for "looking around"
  static double  aWidthScalor;

  // Scalor Y used to displace screen, used for "looking around"
  static double  aHeightScalor;
  // if z gets too small then it is nice to know that so that we reposition it somewhere
  // far away
  boolean WentOffBound =false;
  // allows to spawn star at a larger z (further away)
  boolean veryFarAway;

// Size of the star, depends on z
  double starSize;

// This allows to access the star from anywhere
Star myStar;

  //default constructor, spawns star somewhat close
  public StarComponent(){
    veryFarAway = false;
  }

  // alternative constructor, spawns star far away
  public StarComponent(boolean pveryFarAway) {
    veryFarAway = pveryFarAway;
  }

  // this is the view of a star, given that it didnt go off bounds (otherwise line will show from bound to spawn location)
  private class Star extends Pane{
    // These parameters are to allow to modify the star without having to delete it and make a new one
    Line starLine;
    Circle star;
    Star(double dx,double dy,double size){

      starLine = new Line(0,0, dx,dy);
      starLine.setStrokeWidth(1.5*size);
      starLine.setStroke(Color.WHITE);
      star = new Circle(size, Color.WHITE);
      star.setCenterX(dx);
      star.setCenterY(dy);
      getChildren().addAll(starLine,star);
    }

    /**
     * Updates the star so that it now looks like a line going from 0,0 to newX,newY
     * With a size nSize
     * @param newX
     * @param newY
     * @param nSize
     * @pre nSize has to be positive
     */
    void update(double newX,double newY, double nSize ){
    starLine.setEndX(newX);
    starLine.setEndY(newY);
    starLine.setStrokeWidth(1.5*nSize);
    star.setCenterX(newX);
    star.setCenterY(newY);
    star.setRadius(nSize);
    }
  }

  // Allows to go faster
  public static void faster(){
    // this is the max speed am allowing, gets ugly after this speed
    if (zChange < 20440)zChange*=1.01;
  }

  // Slows down
  public static void slower(){
    // this is the minimum speed am allowing, slower than this it takes too
    // long to accelerate
    if (zChange >2)  zChange*=0.9;
  }

  @Override
  public void onAdded() {
    // initiate x, y, z, aWidth, aHeight, aWidthScalor and aHeightScalor
    aWidth =getAppWidth() ;
    aHeight =getAppHeight() ;
    aWidthScalor = getAppWidth()/2.;
    aHeightScalor = getAppHeight()/2.;

    // if very far away then put it at a random position with a large z
    if (veryFarAway){
      PlaceVeryFarAway();
    }else {
      //  else then put in a closer z
    x = map(Math.random(),0,1,-aWidth , aWidth );
    y = map(Math.random(),0,1,-aHeight, aHeight);
    z = map(Math.random(),0,1,0, aWidth*8);
  }


    starSize =(2000/(z));
    myStar = new Star(0,0, starSize);
    entity.getViewComponent().addChild(myStar);
  }

  // Place star very far away
  private void PlaceVeryFarAway(){
    x = map(Math.random(),0,1,-aWidth*8, aWidth*8);
    y = map(Math.random(),0,1,-aHeight*8 , aHeight*8 );
    z = map(Math.random(),0,1,aWidth*8, aWidth*15);
  }


  @Override
  public void onUpdate(double tpf) {

    // take cursor position and make it something we can work with (aScalor) or limit
    // if you want more liberty to look around, change last 2 arguments of map,
    // note that you aren't really looking around, you are just translating the origin of the
    // star movement to somewhere else in the 2d scene, but it looks close enough

    aWidthScalor = map(aCursorX,0,aWidth,0.25,0.75);
    aHeightScalor = map(aCursorY,0,aHeight,0.25,0.75);


    ////////////////  if not a fan of the above functionnality, comment it out and uncomment v ///////

//    aWidthScalor =0.5;
//    aHeightScalor= 0.5;

    ////////////////////////////////////////////////////////////////////////^///////

    // this is where the magic happens, makes the stars move with respect to their distance
    // from the view (farther away means less movement, closer == more movement)
    dx = map((x)/z,0,1,0.,aWidth);
    dy = map((y)/z,0,1,0.,aHeight);

    // Translate point so that it starts somewhere other than 0,0
    dx +=aWidth*aWidthScalor;
    dy += aHeight*aHeightScalor;

    // change z of point depending on zChange (the speed of the camera)
    z -= zChange;

    // determine star size, very small at first gets very big when z is closer
    starSize =(5000/(z));

    // this if statement is to avoid drawing a line from where the line is when it goes off bound and
    // where it is when it is repositioned at the center
    if (!WentOffBound){
      double px  = dx - sx;
      double py = dy-sy;
      myStar.update(px,py,starSize);
    }
    else{
      myStar.update(0,0,starSize);
    }

    // This is for putting back the stars that are past the camera back very far away from it
    if  (z <=2*zChange){
      PlaceVeryFarAway();
      WentOffBound = true;
    }
    else {
      WentOffBound =false;
    sx = dx;
    sy = dy;
    }

    // finally put the entity at its projected position
    entity.setPosition(dx, dy);
  }





}
