/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import javafx.scene.CacheHint;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author Conno
 */
public class Cfpvf9Particle extends Region{
    
    private ImageView imView;
    private Image image;
    private float width, height;
    private float velocityY = 0f;
    
    public static int numObjCollected = 0;
    
    public Cfpvf9Particle(float w, float h)
    {
        image = new Image(getClass().getResource("Cfpvf9ParticleOptimized.jpg").toExternalForm());
        this.imView = new ImageView();
        this.imView.setImage(image);
        this.imView.setFitWidth(w);
        this.imView.setFitHeight(h);
        //this.imView.setPreserveRatio(true);
        //this.imView.setSmooth(true);
        this.imView.setCache(true);
        this.imView.setCacheHint(CacheHint.SPEED);
        width = w;
        height = h;
    }
    
    public Cfpvf9Particle()
    {
        this(5,5);
    }
    
    public void setX(float x)
    {
        this.imView.setTranslateX(x - width/2);
    }
    public void setX(double x)
    {
        this.imView.setTranslateX(x - width/2);
    }
    
    public void setY(float y)
    {
        this.imView.setTranslateY(y - height/2);
    }
    
    public void setY(double y)
    {
        this.imView.setTranslateY(y - height/2);
    }
    
    public ImageView getView()
    {
        return imView;
    }
    
    public void setColor(Color c)
    {
        Lighting light = new Lighting();
        light.setDiffuseConstant(1);
        light.setSpecularConstant(0);
        light.setSpecularExponent(0);
        light.setSurfaceScale(0);
        light.setLight(new Light.Distant(45,45,c));
        this.imView.setEffect(light);
    }
    
    public void setRadius(float radius)
    {
        float oldWidth = this.width;
        float oldHeight = this.height;
        
        this.imView.setFitWidth(radius);
        this.imView.setFitHeight(radius);
        this.width = radius;
        this.height = radius;
        
        this.imView.setTranslateX(imView.getTranslateX() + oldWidth/2 - width/2);
        this.imView.setTranslateY(imView.getTranslateY() + oldHeight/2 - height/2);

    }
    
    public float getSize()
    {
        return this.width;
    }
    
    public void setTransparency(float val)
    {
        this.getView().setOpacity(val);
    }
    
    public void setVelocity(float v)
    {
        this.velocityY = v;
    }
    
    public float getVelocity()
    {
        return this.velocityY;
    }
    
    protected void finalize() throws Throwable
    {
        numObjCollected+=1;
    }
}
