import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;
 
/** 
 * Program to convolve pictures or posterize pictures using the k-means method.
 * 
 * @author by Naho Kitade 9/27/2013
 */ 
public class Picture extends SimplePicture {  
   
  /** 
   * Constructor that takes no arguments  
   */ 
  public Picture () { 
    super();   
  } 
   
  /** 
   * Constructor that takes a file name and creates the picture  
   * @param fileName the name of the file to create the picture from 
   */ 
  public Picture(String fileName) { 
    super(fileName); 
  } 
   
  /** 
   * Constructor that takes the width and height 
   * @param width the width of the desired picture 
   * @param height the height of the desired picture 
   */ 
  public Picture(int width, int height) { 
    super(width,height); 
  } 
   
  /** 
   * Constructor that takes a picture and creates a  
   * copy of that picture 
   */ 
  public Picture(Picture copyPicture) { 
    super(copyPicture); 
  } 
   
  /** 
   * Constructor that takes a buffered image 
   * @param image the buffered image to use 
   */ 
  public Picture(BufferedImage image) { 
    super(image); 
  } 
 
 /**
  * method that calculates the distance^2 (for faster calculation) given two different Color objects.
  * helper used in the closColor and closColorIndex methods.  
  * @param color1 
  * @param color2
  * @return double that is the distance^2 of the two given colors.
  */
 private static double relativeColorDistance(Color color1,Color color2){
  double redDistance = color1.getRed() - color2.getRed();
  double greenDistance = color1.getGreen() - color2.getGreen();
  double blueDistance = color1.getBlue() - color2.getBlue();
  double relativeDistance = redDistance * redDistance + 
                    				greenDistance * greenDistance +
                    				blueDistance * blueDistance;
  return relativeDistance;
  }
 	
 /**
  * method to see which color in the array the pixel color is closest to.
  * @param pixel pixel to calculate the closest color in the arraylist.
  * @param chosColor arraylist of colors to match with the pixel color.
  * @return color in the arraylist of colors that is closest to the color of the pixel
  */
 private Color closColor(Pixel pixel, ArrayList<Color> chosColor){
    double smallestDistance = -1;
    Color closestColor = null;
    
    //create new Color object with the same color as the pixel.
    Color pixCol = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()); 
    for (Color colors : chosColor){ //go through the given array
    	//find the color in the arraylist that is closest to the pixel color.
      double dist = relativeColorDistance(colors, pixCol);  
      //if there is no set smallestDistance, automatically make the distance 
      //between the current color in the array the smallestDistance
      if (smallestDistance == -1){ 
      	smallestDistance = dist;
        closestColor = colors;
      }
      //otherwise, find the color in the arraylist with the smallest distance.
      else if (dist < smallestDistance){
        smallestDistance = dist;
        closestColor = colors;
      }
    }
    return closestColor;
  }
 
 /**
  * basically same method as closColor but returns the index of the color in the arraylist instead of
  * the actual Color object.
  * @param pixel to calculate the closest color in the arraylist.
  * @param chosColor arraylist of colors to match with the pixel color.
  * @return the index of the color in the arraylist of colors that is closest to the color of the pixel
  */
  private int closColorIndex(Pixel pixel, ArrayList<Color> chosColor){
    double smallestDistance = -1;
    Color closestColor = null;
    
    Color pixCol = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()); 
    for (Color colors : chosColor){
      double dist = relativeColorDistance(colors, pixCol);
      if (smallestDistance == -1){
      	smallestDistance = dist;
        closestColor = colors;
      }
      else if (dist < smallestDistance){
        smallestDistance = dist;
        closestColor = colors;
      }
    }
    int closestColorIndex = chosColor.indexOf(closestColor);
    return closestColorIndex;
  }
  
  /**
   *method that reduces the picture's color to the ones given as an array list of colors. 
   * @param colors arraylist of colors that the picture's color will be reduced to.
   * @return a picture object that has the colors reduces to the ones in the arraylist of colors.
   */
  private Picture mapToColorList(ArrayList<Color> colors){
  	//create a copy of the picture and get the pixels in the picture as a single array.
    Picture mappedPicture = new Picture(this); 
    Pixel [] pixelsArray = mappedPicture.getPixels();
    for (Pixel pix : pixelsArray){
    	//call the helper method to set the pixel color to the color closest to it in the array.
      pix.setColor(mappedPicture.closColor(pix, colors)); 
    }
    return mappedPicture;
  }
  
  /**
   * method that creates an arraylist of randomly generated colors of a given number amount of elements.
   * @param numberColors number of colors to be generated
   * @return arraylist of randomly generated colors 
   */
  private static ArrayList<Color> randomColors(int numberColors){
    int numberLimit = 256; //limit to the number that a rgb can go up to.
    ArrayList<Color> colorArrayList = new ArrayList<Color>();
    
    Random randint = new Random();
      for (int colorGen = 0; colorGen < numberColors; colorGen++){
      
      int randomNumber1 = randint.nextInt(numberLimit); //store three random ints
      int randomNumber2 = randint.nextInt(numberLimit);
      int randomNumber3 = randint.nextInt(numberLimit);
      
      colorArrayList.add(new Color(randomNumber1, randomNumber2, randomNumber3)); //make those the rgb of new color.
    }
    return colorArrayList;
  }
  
  /**
   * method that returns an array list of random colors of a given number of elements 
   * from a given picture's pixel
   * @param numberColors number of color elements wanted in the array list. 
   * @return array list of random colors from a given picture's pixel
   */
  private ArrayList<Color> randomColorsPixels(int numberColors){
    Pixel[] pixels = this.getPixels();
    ArrayList<Color> colorArrayList = new ArrayList<Color>();
    
    int numberLimit = pixels.length;
    Random randomNumberGen = new Random();
    
    for (int colorGen = 0; colorGen < numberColors; colorGen++){
    	//generate a random number and make that the index of the pixel in 
    	//which we are going to take the color from.
      int index = randomNumberGen.nextInt(numberLimit); 
      int red = pixels[index].getRed();
      int green = pixels[index].getGreen();
      int blue = pixels[index].getBlue();
      Color newColorObj = new Color(red, green, blue);
      //check if the created array list contains the newly created color object already.
      if (!colorArrayList.contains(newColorObj)){
        colorArrayList.add(newColorObj);
      }
      else{
      	colorGen--;
      }
    }
    return colorArrayList;
  }
  
  /**
   * method that initializes the arraylist of arraylist of colors (helper method used as an 
   * initializer for the cluster.)
   * @param numberColors number of colors the user wants to reduce a picture to.
   * @return array list containing empty array list of colors to initialize a cluster 
   * used in a later method.
   */
  private static ArrayList<ArrayList<Color>> initializeClusterStorage(int numberColors){
    ArrayList<ArrayList<Color>> clusterStorage = new ArrayList<ArrayList<Color>>();
    // keep adding new array list of colors in an array list until specified number given in parameter.
    for (int loops = 0; loops < numberColors; loops++){
    clusterStorage.add(new ArrayList<Color>());
    }
    return clusterStorage;
  }
  
  /**
   * method that implements k-means to compute a good color list. (initialized using randomColorsPixels method)
   * @param number number of colors the picture should be reduced to.
   * @return an arraylist of color that has the "good colors" to reduce a given picture.
   */
  private ArrayList<Color> computeColors(int number){
    boolean loopAgain = true; //condition to keep computing the centroids of a cluster. 
    ArrayList<Color> colorList = randomColorsPixels(number); //generate an initial color list using randomColorsPixels method.
    
    while (loopAgain){ 
      ArrayList<ArrayList<Color>> clusters = initializeClusterStorage(number); //create initial cluster
    
      Pixel[] pixels = this.getPixels();
      // for every pixel in the picture see which color in the color arraylist it is closest to
      // and store that pixel color in the initialized cluster structure at the index
      // of the original arraylist of colors. 
      for (Pixel pixel : pixels){
        clusters.get(closColorIndex(pixel, colorList)).add(pixel.getColor());
        }
      //get the average of the arraylist of colors in the in the array list of array list of colors
      // and make that into a new arraylist of colors and store it in newColorList.
      ArrayList<Color> newColorList = avgColorArray(clusters); 
      number = newColorList.size(); //number of colors in the array list of colors changes.
      if (newColorList.equals(colorList)){ // if the cluster didn't change, stop looping.
        loopAgain = false;
      }
      else{ 
      	// otherwise, make the color list the newly computed average array list of colors and 
      	//loop again,
        colorList = newColorList;
      }
    }
    return colorList; //by this line, the k-means is complete. 
  }
  
  /**
   * method that implements k-means to compute a good color list. (initialized using randomColors method)
   * @param number number of colors the picture should be reduced to.
   * @return an arraylist of color that has the "good colors" to reduce a given picture.
   */
  private ArrayList<Color> computeColors2(int number){
    boolean loopAgain = true; //condition to keep computing the centroids of a cluster. 
    ArrayList<Color> colorList = randomColors(number); //generate an initial color list using randomColors method.
    
    while (loopAgain){ 
      ArrayList<ArrayList<Color>> clusters = initializeClusterStorage(number); //create initial cluster
    
      Pixel[] pixels = this.getPixels();
      // for every pixel in the picture see which color in the color arraylist it is closest to
      // and store that pixel color in the initialized cluster structure at the index
      // of the original arraylist of colors. 
      for (Pixel pixel : pixels){
        clusters.get(closColorIndex(pixel, colorList)).add(pixel.getColor());
        }
      //get the average of the arraylist of colors in the in the array list of array list of colors
      // and make that into a new arraylist of colors and store it in newColorList.
      ArrayList<Color> newColorList = avgColorArray(clusters); 
      number = newColorList.size(); //number of colors in the array list of colors changes.
      if (newColorList.equals(colorList)){ // if the cluster didn't change, stop looping.
        loopAgain = false;
      }
      else{ 
      	// otherwise, make the color list the newly computed average array list of colors and 
      	//loop again,
        colorList = newColorList;
      }
    }
    return colorList; //by this line, the k-means is complete. 
  }
  
  /**
   * method that calls the computeColors to initialize and compute the k-means and reduces the picture.
   * @param number number of colors to reduce the picture to
   * @return Picture that is reduced to the specified number of colors
   */
  public Picture reduceColors(int number){
    return this.mapToColorList(computeColors(number));
  }
  
  /**
   * method that calls the computeColors2 to initialize and compute the k-means and reduces the picture.
   * @param number number of colors to reduce the picture to
   * @return Picture that is reduced to the specified number of colors
   */
  public Picture reduceColors2(int number){
    return this.mapToColorList(computeColors2(number));
  }
  
  /**
   * method that takes in an array list of colors and returns an average color from 
   * all the colors in that array list. 
   * @param colorArray array list to take the average color from. 
   * @return the color that is the average of the given array list of colors.
   */
  private static Color averageColors(ArrayList<Color> colorArray) {
    float sumRed = 0.0f;
    float sumGreen = 0.0f;
    float sumBlue = 0.0f;
    
    for(Color colors : colorArray){
    	// sum up all the rgb amount of the colors in the give array 
      sumRed += colors.getRed();
      sumGreen += colors.getGreen();
      sumBlue += colors.getBlue();
    }
    
    //get the average by dividing the summed up color components by the size of 
    //the given array list of colors.
    int avgRed = Math.round(sumRed/colorArray.size());
    int avgGreen = Math.round(sumGreen/colorArray.size());
    int avgBlue = Math.round(sumBlue/colorArray.size());
    Color avgColor = new Color(avgRed, avgGreen, avgBlue);
    return avgColor;
  }
  
  
  /**
   * a method that is like a step up of the averageColors that takes an array list of array 
   * list of colors and returns an array list of colors of the averages of each of the array 
   * list of colors in the bigger array list. 
   * @param cluster arraylist of array list of colors to take the average colors from.
   * @return an array list of colors of the averages of each of the array list of colors in the 
   * bigger array list. 
   */
  private static ArrayList<Color> avgColorArray(ArrayList<ArrayList<Color>> cluster){
    ArrayList<Color> avgColorList = new ArrayList<Color>();
    // call averageColors method on every array list of colors in the given array list 
    // of array list of colors and add that into a new array list.
    for (ArrayList<Color> colorArrayList : cluster){
      if (!colorArrayList.isEmpty()){ //skip over the array list if it is empty. 
        Color avgColor = averageColors(colorArrayList);
        avgColorList.add(avgColor);
      }
    }
    return avgColorList; 
  }
  

  /** 
   * Method to return a string with information about this picture. 
   * @return a string with information about the picture such as fileName, 
   * height and width. 
   */ 
  public String toString() { 
    String output = "Picture, filename " + getFileName() +  
      " height " + getHeight()  
      + " width " + getWidth(); 
    return output; 
     
  } 
   
   /** 
   * Class method to let the user pick a file name and then create the picture  
   * and show it 
   * @return the picture object 
   */ 
  public static Picture pickAndShow() { 
    String fileName = FileChooser.pickAFile(); 
    Picture picture = new Picture(fileName); 
    picture.show(); 
    return picture; 
  } 
   
  /** 
   * Class method to create a picture object from the passed file name and  
   * then show it 
   * @param fileName the name of the file that has a picture in it 
   * @return the picture object 
   */ 
  public static Picture showNamed(String fileName) { 
    Picture picture = new Picture(fileName); 
    picture.show(); 
    return picture; 
  } 
  
  /** 
   * Private Method used in convolve method which will calculate the new red blue and 
   * green variables for the pixel at a given location on the picture. 
   * @param matrix 3 by 3 matrix of weights
   * @param x x location of pixel on picture 
   * @param y y location of pixel on picture 
   * @return array of floats containing three elements, the new red green and blue
   * values of the given pixel.
   */
  private float [] convolvePixel(float [][] matrix, int x, int y) { 
   //initialize int variables
   int red = 0; 
   int blue = 0; 
   int green = 0; 
   
   //going through the 9 pixels that is needed to calculate new color values.
   for (int a = 0; a < 3; a++){ 
    for (int b = 0; b < 3; b++){ 
      // add the red, blue and green values of each of the 9 pixels weighted by the 
      // given matrix.
     red += (this.getPixel((x - 1 + a), (y - 1 + b)).getRed())*matrix[a][b]; 
     blue += (this.getPixel((x - 1 + a), (y - 1 + b)).getBlue())*matrix[a][b] ;
     green += (this.getPixel((x - 1 + a), (y - 1 + b)).getGreen())*matrix[a][b] ;
      
    } 
   } 
   // pack the calculated color values in an array and return that array.
   float [] color = {red, blue, green};
   return color;
  } 
  
 /** 
   * Method used to convolve a picture with the given weights in a 3 x 3 matrix. 
   * @param matrix 3 by 3 matrix of weights
   * @return new convolved picture
   */ 
  public Picture convolve(float [][] matrix) { 
   // create a copy of the picture
   Picture convolved = new Picture(this); 
   
   // go through all of the pixels in the original picture besides the edge pixels
   for (int curPixelX = 1, endPixelX = this.getWidth() - 1; curPixelX < endPixelX;  
     curPixelX++){ 
    for (int curPixelY = 1, endPixelY = this.getHeight() - 1; curPixelY < endPixelY;  
      curPixelY++){ 
     // get the pixcel of the copied picture 
     Pixel currPixel = convolved.getPixel(curPixelX,curPixelY); 
     // call the private function convolvePixel on the original image
     float [] convPixelCol = this.convolvePixel(matrix, curPixelX, curPixelY); 
     //set the color value of the pixels in the copied picture to the one calculated 
     // through the private method.
       currPixel.setRed((int) convPixelCol[0]);
       currPixel.setBlue((int) convPixelCol[1]);
       currPixel.setGreen((int) convPixelCol[2]); 
  } 
 } 
   // return the new convolved image.
   return convolved; 
 
 } 
   
  /** 
   * A method create a copy of the current picture and return it 
   * @return the copied picture 
   */ 
  public Picture copy() 
  { 
    return new Picture(this); 
  } 
  
  /**
   * main method that lets the user choose a picture to explore, then reduces that chosen
   * picture to 8 colors using the k-means to calculate the best colors.
   * @param args
   */
  public static void main(String[] args) { 
    int numberOfColors = 8;
    Picture p = new Picture(FileChooser.pickAFile()); 
    p.explore(); 
    Picture q = p.reduceColors(numberOfColors); 
    q.explore(); 
  } 
         
} 
 