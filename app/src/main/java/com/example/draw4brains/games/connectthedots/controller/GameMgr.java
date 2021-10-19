package com.example.draw4brains.games.connectthedots.controller;

import android.util.Log;


import com.example.draw4brains.games.connectthedots.model.ConnectDots;
import com.example.draw4brains.games.connectthedots.model.Constants;
import com.example.draw4brains.games.connectthedots.model.Level;
import com.example.draw4brains.games.connectthedots.model.Node;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMgr implements Serializable {

    // Basic Level Information
    private Level levelInfo;
    private ConnectDots connectDotsLevelObject;

    // Score Tracking
    private int scoreConnect;
    private int scoreGuess;

    // Node Information
    private ArrayList<Node> nodeList;
    private String startNode = null;
    private String endNode = null;


    public GameMgr(Level levelInfo) {
        this.levelInfo = levelInfo;
        scoreConnect = 0;
        scoreGuess = 0;
        nodeList = new ArrayList<>();
    }

    // Functions related to data about game level

    public Level getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(Level levelInfo) {
        this.levelInfo = levelInfo;
    }

    public ConnectDots getConnectDotsLevelObject() {
        return connectDotsLevelObject;
    }

    public void setConnectDotsLevelObject(ConnectDots connectDotsLevelObject) {
        this.connectDotsLevelObject = connectDotsLevelObject;
    }


    /////// Score Calculation for scoreConnect and scoreGuess

    public void calculateConnectScore(long connectTime, int numCircle) {
        int averageCircleTime = ((int) connectTime) / numCircle;
        if (averageCircleTime < 1) {
            averageCircleTime = 1;
        }
        Log.d("averageT", String.valueOf(averageCircleTime));
        int scoreConnect = (int) ((1.0 / averageCircleTime) * 50.0);
        this.scoreConnect = scoreConnect;
    }

    public void calculateGuessScore(long guessTime, int guessTrials, String wordToGuess) {
        double timePerLetter = 2.0;
        double totalWordTime = timePerLetter * wordToGuess.length();
        int guessScore = 0;
        double scoreMatrix = totalWordTime / guessTime;
        if (scoreMatrix > 1) {
            scoreMatrix = 1;
        }
        if (guessTrials <= 5) {
            guessScore = (int) ((scoreMatrix) * 25 + 25 - (guessTrials - 1) * 5);
        } else {
            guessScore = (int) ((scoreMatrix) * 25);
        }
        this.scoreGuess = guessScore;
        Log.d("guessScore", String.valueOf(guessScore));
    }


    public int getConnectScore() {
        return scoreConnect;
    }

    public int getGuessScore() {
        return scoreGuess;
    }




    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public String getStartNode() {
        return this.startNode;
    }

    public String getEndNode() {
        return this.endNode;
    }

    public ArrayList<Node> getNodeList() {
        return this.nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public void dropImageViewsAfterGameEnd() {
        // Because ImageView is not serializable (For convenience sake)
        for (Node node: nodeList) {
            node.setNodeImage(null);
        }
    }

    // Functions related to scaling of nodes
    /**
     * Main function overseeing scaling and transformation of node. Perform the automatic calibration and scaling of nodes to the given canvas size.
     *
     * @param canvasWidth            Width of canvas in which the nodes are allowed to be placed on
     * @param canvasHeight           Height of canvas in which the nodes are allowed to be placed on
     * @param percentageFillRequired Percentage of the canvas expected to be filled by the nodes.
     * @param scaleFactor            Scale factor used in scaling
     * @param scaleFactorModifier    Modifier used to modify the scale factor for scaling up/down.
     * @return List of calibrated nodes
     */
    public void calibrateNodeToCanvasSize(
            // Reason for method: The scaling factor will be used when getting the pixel X and pixel Y.
            int canvasWidth,
            int canvasHeight,
            int percentageFillRequired,
            float scaleFactor,
            float scaleFactorModifier
    ) {

        ArrayList<Node> innerNodeList = this.nodeList;
        // Calculate the original bound width and height of nodes before calibration
        int originalBoundX, originalBoundY, originalDisplacementFromOriginX, originalDisplacementFromOriginY;
        int[] originalBoundInfo = getBoundDimensions(innerNodeList, true);
        originalBoundX = originalBoundInfo[0];
        originalBoundY = originalBoundInfo[1];
        // Get the ideal filled bounds required to scale up (or down) to.
        int idealBoundX = Math.round(canvasWidth * (percentageFillRequired / 100f));
        int idealBoundY = Math.round(canvasHeight * (percentageFillRequired / 100f));

        Log.d("Initialization", String.format("calibrate-canvasWidth: %d, calibrate-canvasHeight: %d", canvasWidth, canvasHeight));
        Log.d("Initialization", String.format("calibrate-originalBoundX: %d, calibrate-originalBoundY: %d", originalBoundX, originalBoundY));
        Log.d("Initialization", String.format("idealBoundX: %d, idealBoundY: %d", idealBoundX, idealBoundY));


        int[] boundDimension = getGeometricBoundDisplacementFromOrigin(innerNodeList);
        originalDisplacementFromOriginX = boundDimension[0];
        originalDisplacementFromOriginY = boundDimension[1];


        // Information for method structure
        // SCALING - Steps breakdown
        // 1) Check if the dot bounds exceeds the canvasDisplayBound. If exceed do step 2, else step 3
        // 2) Exceed bound - while still out of bound, scale down , then check if bound is within. Record the scale factor
        // 3) Does not exceed bound - while within bound, scale up, then check if bound exceeded. Record the scale factor
        // 4) Scale node and return processed nodes list

        // 1) Step 1:
        int scaledWidth = originalBoundX;
        int scaledHeight = originalBoundY;
        boolean isWithinIdeal = isWithinBound(scaledWidth, scaledHeight, idealBoundX, idealBoundY);
        Log.d("Initialization", String.format("Before Scale -> scaledWidth: %d, scaledHeight: %d", scaledWidth, scaledHeight));
        ArrayList<Node> scaledNodes;
        if (!isWithinIdeal) {
            // Step 2 (Node bound exceed ideal bound): Scale Down iteratively, check for bound, return best fit scale factor
            Log.d("Initialization", "Scaling - Starting Scale Down");

            while (true) {
                // Scale and Get Bounds
                scaledNodes = scaleProcessingNodes(innerNodeList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);
                int[] scaledBoundInfo = getBoundDimensions(scaledNodes, false);
                int scaledBoundWidth = scaledBoundInfo[0];
                int scaledBoundHeight = scaledBoundInfo[1];
                Log.d("Initialization", String.format("Scaling - scaledBoundWidth: %d, scaledBoundHeight: %d", scaledBoundWidth, scaledBoundHeight));
                Log.d("Initialization", String.format("Scaling - ScaleFactor: %f", scaleFactor));

                // Condition Checking
                isWithinIdeal = isWithinBound(scaledBoundWidth, scaledBoundHeight, idealBoundX, idealBoundY);
                if (isWithinIdeal) {
                    // Current scale factor is the first one that is within bounds. Select this current as scale factor and break
                    break;
                } else {
                    scaleFactor /= scaleFactorModifier; // Reduce scale factor using modifier and continue scaling down
                }
            }
        } else {
            Log.d("Initialization", "Scaling - Starting Scale Up");
            // Step 3 (Node bound is within ideal bound): Scale Up iteratively, check for bound, return best fit scale factor
            while (true) {

                // Scale and Get Bounds
                scaledNodes = scaleProcessingNodes(innerNodeList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);
                int[] scaledBoundInfo = getBoundDimensions(scaledNodes, false);
                int scaledBoundWidth = scaledBoundInfo[0];
                int scaledBoundHeight = scaledBoundInfo[1];
                Log.d("Initialization", String.format("Scaling - scaledBoundWidth: %d, scaledBoundHeight: %d", scaledBoundWidth, scaledBoundHeight));
                Log.d("Initialization", String.format("Scaling - ScaleFactor: %f", scaleFactor));

                // Condition Checking
                isWithinIdeal = isWithinBound(scaledBoundWidth, scaledBoundHeight, idealBoundX, idealBoundY);
                if (!isWithinIdeal) {
                    // Current scale factor exceeds bound, select previous scale factor and break
                    scaleFactor /= scaleFactorModifier;
                    break;
                } else {
                    scaleFactor *= scaleFactorModifier; // Increase scale factor using modifier and continue scaling up
                }
            }
        }
        Log.d("Scaling", String.format("ScaleFactor chosen: %f", scaleFactor));

        // Rescale all nodes using chosen scale factor
        scaledNodes = scaleProcessingNodes(innerNodeList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);

        // Transform node using self-created evaluation metric
        ArrayList<Node> transformedNodes = transformNodeAutomatic(scaledNodes, canvasWidth, canvasHeight, percentageFillRequired);

        this.nodeList = transformedNodes;
    }

    /**
     * Calculate the diameter based on a custom self-made metric. See method code for details
     *
     * @param nodesList The list of nodes to scale.
     * @return Diameter that the nodes will use
     */
    public float getDiameterForNodes(ArrayList<Node> nodesList) {
        int[] boundInfo = getBoundDimensions(nodesList, false);
        int boundWidth = boundInfo[0];
        int boundHeight = boundInfo[1];
        int noOfNodes = nodesList.size();
        // Choose the smaller pixel bound for Y
        int chosenBoundFactor = boundWidth < boundHeight ? boundWidth : boundHeight;

        float calculatedDiameter = chosenBoundFactor / (noOfNodes * Constants.DIAMETER_LIMIT_MIN);
        float finalDiameter = Math.max(calculatedDiameter, Constants.DIAMETER_LIMIT_MIN); // Set a limit of 60f if diameter smaller than 60f
        return finalDiameter;
    }

    /**
     * Performing automatic transformation of nodes based on a custom self-made metric. See method code for details
     *
     * @param nodesList              The list of nodes to transform
     * @param canvasWidth            Width of canvas in which the nodes are allowed to be placed on
     * @param canvasHeight           Height of canvas in which the nodes are allowed to be placed on
     * @param percentageFillRequired Percentage of the canvas expected to be filled by the nodes.
     */

    private ArrayList<Node> transformNodeAutomatic(ArrayList<Node> nodesList, int canvasWidth, int canvasHeight, int percentageFillRequired) {
        // The bound has been displaced to origin. Now we have to transform the nodes appropriately back and try to center it on the screen.

        int[] finalBound = getBoundDimensions(nodesList, false);

        int transformX = Math.round((canvasWidth - finalBound[0]) / 2f);
        int transformY = Math.round((canvasHeight - finalBound[1]) / 2f);

        for (Node node : nodesList) {
            int x = node.getCenter_x() + transformX;
            int y = node.getCenter_y() + transformY;
            node.setCenter(x, y);
        }
        return nodesList;

    }

    /**
     * To scale the nodes based on a certain scale factor and reassign the center x,y coordinates for the node position in pixels
     *
     * @param nodesList                       The list containing the nodes to be scaled.
     * @param originalDisplacementFromOriginX Horizontal displacement of top left point of bound, from origin. This is the original value before any scaling is done
     * @param originalDisplacementFromOriginY Vertical displacement of top left point of bound, from origin. This is the original value before any scaling is done.
     * @param scaleFactor                     Scale factor that is a float
     */
    private ArrayList<Node> scaleProcessingNodes(ArrayList<Node> nodesList, int originalDisplacementFromOriginX, int originalDisplacementFromOriginY, float scaleFactor) {

        // Scale item first using scale factor and set as its center attributes. This hold the final scale center coordinates in pixel scale.
        for (Node node : nodesList) {
            // Shift x nearer to origin for better displacement
            int displacedGeometricX = (node.getGeometric_x() - originalDisplacementFromOriginX);
            int displacedGeometricY = (node.getGeometric_y() - originalDisplacementFromOriginY);
            int x = Math.round(displacedGeometricX * scaleFactor);
            int y = Math.round(displacedGeometricY * scaleFactor);
            node.setCenter(x, y);
        }

        return nodesList;
    }

    /**
     * Check if the bound size of a bound is within another. i.e Bound A is in Bound B
     *
     * @param boundToCheckX        Width of bound size to check - Bound A
     * @param boundToCheckY        Height of bound size to check - Bound A
     * @param boundToCheckAgainstX Width of bound size to check - Bound B
     * @param boundToCheckAgainstY Height of bound size to check - Bound B
     * @return A boolean indicating true will be returned if the given bound is within another.
     */
    private boolean isWithinBound(int boundToCheckX, int boundToCheckY, int boundToCheckAgainstX, int boundToCheckAgainstY) {

        if ((boundToCheckX > boundToCheckAgainstX) || (boundToCheckY > boundToCheckAgainstY)) {
            Log.d("Scaling", String.format("WithinBoundCheck: %d, yToCheck: %d, xIdeal: %d, yIdeal: %d, isWithin: %b", boundToCheckX, boundToCheckY, boundToCheckAgainstX, boundToCheckAgainstY, false));
            return false;
        } else {
            Log.d("Scaling", String.format("WithinBoundCheck: %d, yToCheck: %d, xIdeal: %d, yIdeal: %d, isWithin: %b", boundToCheckX, boundToCheckY, boundToCheckAgainstX, boundToCheckAgainstY, true));
            return true;
        }
    }

    /**
     * Calculate the displacement of a bound (based on the top left point) from origin.
     *
     * @param nodeList The list containing the nodes.
     * @return Displacement bounds in the format [x_displacement, y_displacement]
     */
    private int[] getGeometricBoundDisplacementFromOrigin(ArrayList<Node> nodeList) {

        int lowestX = Integer.MAX_VALUE;
        int lowestY = Integer.MAX_VALUE;

        for (Node node : nodeList) {
            int geoX = node.getGeometric_x();
            int geoY = node.getGeometric_y();
            if (geoX < lowestX) lowestX = geoX;
            if (geoY < lowestY) lowestY = geoY;
        }

        int displacementFromOriginX = lowestX;
        int displacementFromOriginY = lowestY;
        Log.d("Scaling", String.format("displacementFromOriginX: %d, displacementFromOriginY: %d", displacementFromOriginX, displacementFromOriginY));
        Log.d("Scaling", String.format("lowestX: %d, lowestY: %d ", lowestX, lowestY));
        return (new int[]{displacementFromOriginX, displacementFromOriginY});

    }

    /**
     * Calculate the dimensions of a bound of the nodes from the nodes in a list of nodes.
     * <p>
     * This is equivalent to how much area the nodes take up
     *
     * @param nodesList The list containing the nodes.
     * @return Width and Height of bounds in the format [width, height]
     */
    private int[] getBoundDimensions(ArrayList<Node> nodesList, boolean isForGeometric) {
        // Determine the bounds in pixel scale. Does not return the lowestX or lowestY because the bound we are checking has already been displaced earlier
        int lowestX = Integer.MAX_VALUE;
        int lowestY = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;

        if (isForGeometric) {
            for (Node node : nodesList) {
                int originalX = node.getGeometric_x();
                int originalY = node.getGeometric_y();
                if (originalX < lowestX) lowestX = originalX;
                if (originalY < lowestY) lowestY = originalY;
                if (originalX > highestX) highestX = originalX;
                if (originalY > highestY) highestY = originalY;
            }
        } else {
            for (Node node : nodesList) {
                int scaledX = node.getCenter_x();
                int scaledY = node.getCenter_y();
                if (scaledX < lowestX) lowestX = scaledX;
                if (scaledY < lowestY) lowestY = scaledY;
                if (scaledX > highestX) highestX = scaledX;
                if (scaledY > highestY) highestY = scaledY;
            }
        }

        // Transform the x boundaries and y boundaries
        int boundWidth = (highestX - lowestX);
        int boundHeight = (highestY - lowestY);
        return new int[]{boundWidth, boundHeight};
    }




}
