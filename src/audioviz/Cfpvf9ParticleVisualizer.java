
package audioviz;

import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Connor Penrod
 */

//This visualizer uses a custom class called Cfpvf9Particle which is basically just a 5x5 pixel white ImageView. Runs much faster than a Rectangle.
public class Cfpvf9ParticleVisualizer implements Visualizer {
    
    private final String name = "Particle Visualizer (Recommended Bands > 20)";
    private AnchorPane vizPane = null;
        
    private float width = 0f;
    private float height = 0f;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 260.0;
    
    private ArrayList<Cfpvf9Particle> particles;
    private int numCircles = 8;
    private float particleSize = 4;
    private int revisedBands;
    private int numFloaters = 10;
    
    private boolean hasStarted = false;

    
    public Cfpvf9ParticleVisualizer() {
        particles = new ArrayList<>();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        if(hasStarted){return;}
        else{hasStarted = true;}
        end();
        
        //initalize and prepare all particles
        
        this.revisedBands = Math.round((float)numBands * 3/4);
        this.vizPane = vizPane;
        
        height = (float)vizPane.getHeight();
        width = (float)vizPane.getWidth();
        
        //particles = new ArrayList<Cfpvf9Particle>();
        double maxDistance = width/4;
        double distance;
        
        for(int j = 0; j < numCircles; j++)
        {
            distance = maxDistance/(numCircles+1) * (j+1);
            for (int i = 0; i < revisedBands; i++)
            {
                Cfpvf9Particle p = new Cfpvf9Particle(particleSize,particleSize);
                p.setColor(Color.WHITE);

                particles.add(p);
                vizPane.getChildren().add(p.getView());
            }
        }
        
        String hex = "#000000";
        vizPane.setStyle("-fx-background-color: " + hex);
        
    }
    
    @Override
    public void end() {        
        //resets the pane background
        if(this.vizPane != null)
        {
            this.vizPane.setStyle("-fx-background-color: transparent");
            if(particles != null)
            {
                //for(Cfpvf9Particle p : particles)
                //{
                    //vizPane.getChildren().remove(p.getView());
                //}
                vizPane.getChildren().clear();
                particles.clear();
            }
        }
        
    }
        
    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (particles == null) {
            return;
        }   
        float distScale = 1/height;
        float ringScaleFactor;
        float ringCount = 0;
        int initCutoff = 2;
        if(revisedBands <= 16)
        {
            initCutoff = 0;
        }
        float hueScaleFactor = -10;
        
        //this changes radius, color, and opacity based on the wave magnitudes of the music
        for (int i = 0; i < revisedBands*numCircles; i++)
        {
            ringScaleFactor =  1 - (ringCount+1)/numCircles;
            float distance = (float)(Math.abs(magnitudes[i % (revisedBands) + initCutoff]) * 4 * ringScaleFactor);
            
            if(distance < 0) {distance = 0;}
            if(distance > height) {distance = height;}
                        
            //this offsets the rotation on every 2nd concentric circle. Makes it look better
            float angle = ((i % (numCircles+1)) % 2 == 0) ? 2 * i * (float)Math.PI / revisedBands : 2 * i * (float)Math.PI / revisedBands + (float)Math.PI / revisedBands;
            
            float xOffset = distance * (float)Math.cos(angle);
            float yOffset = distance * (float)Math.sin(angle);
            float x = width/2 + xOffset;
            float y = height/2 + yOffset;
            
            Cfpvf9Particle p = particles.get(i);
            p.setX(x);
            p.setY(y);
            p.setColor(Color.hsb(startHue - (magnitudes[i % (revisedBands) + initCutoff] * hueScaleFactor), 1.0, 1.0, 1.0));
            p.setTransparency(1 - (ringScaleFactor * 3/4));
            
            if(distance < 10) {p.getView().setVisible(false);}

            if(i % revisedBands == 0)
            {
                ringCount += 1;
            }
        }
    }
    
}

