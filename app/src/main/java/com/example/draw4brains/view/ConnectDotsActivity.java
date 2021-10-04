package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.JsonMgr;
import com.example.draw4brains.controller.NodeMgr;
import com.example.draw4brains.model.ConnectDots;
import com.example.draw4brains.model.Node;

import java.util.ArrayList;
import java.util.List;

public class ConnectDotsActivity extends AppCompatActivity {

    private static RelativeLayout relLayout;
    private CanvasView canvasView;
    private Button giveUpButton;
    Intent intent;

    // For Game Logic Checking
    private List<List<Integer>> circlePos = new ArrayList<>();
    private float circleStartX, circleStartY; // To track and check circle
    private boolean ggezwin;

    // The following information will be integrated into Node class and stored in NodeMgr
    private NodeMgr nodeMgr = new NodeMgr();
    private static int canvasWidth;
    private static int canvasHeight;

    // Used in initialization
    float diameterForGame;

    float X1, Y1, X2, Y2;

    // Change FILE AND LEVEL HERE
    private static final String LEVEL = "3"; // Testing purposes until intent is passed from other activity
    private static final String FILE_NAME = "nodeData.json"; // Testing purposes until intent is passed from other activity

    // Constants
    private static final int PERCENTAGE_FILL_REQUIRED = 80; // To check if the nodes are spaced big enough
    private static final float SCALING_FACTOR = 1f;
    private static final float SCALING_FACTOR_MODIFIER = 1.5f;
    private static final float DIAMETER_LIMIT_MIN = 70f;
    private static final float DIAMETER_CALCULATION_TOLERANCE_FACTOR = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_connect_dots);
        relLayout = findViewById(R.id.relativeLayout);
        canvasView = findViewById(R.id.canvasView);
        giveUpButton = findViewById(R.id.btn_give_up);

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast = Toast.makeText(ConnectDotsActivity.this, "Giving Up", Toast.LENGTH_SHORT);
//                toast.show();
                intent = new Intent(ConnectDotsActivity.this, GuessImageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        // Initialize base canvas
        int[] dimensions = getCanvasDimensions(relLayout); // Dimensions of relative layout
        canvasWidth = dimensions[0];
        canvasHeight = dimensions[1];
        canvasView.createCanvas(relLayout, canvasWidth, canvasHeight);
        ArrayList<Node> processingNodes = JsonMgr.getNodeInfoFromSource(getApplicationContext(), FILE_NAME, LEVEL);
        Log.d("Initialization", "Get processing node");
        this.initialize_level(processingNodes);
    }

    private void initialize_level(ArrayList<Node> processingNodes) {
        this.ggezwin = false;
        this.circleStartX = 0;
        this.circleStartY = 0;

        Log.d("Initialization", "Start initialization");
        ArrayList<Node> scaledNodes = calibrateNodeToCanvasSize(processingNodes, canvasWidth, canvasHeight, PERCENTAGE_FILL_REQUIRED, SCALING_FACTOR, SCALING_FACTOR_MODIFIER);
        Log.d("Initialization", "Finished Scaling Nodes to Canvas Size");
        Log.d("Diameter", "Diameter" + String.valueOf(diameterForGame));

        // Add creation of circle here
        createCircles(scaledNodes);
        displayCirclesOnLayout(nodeMgr, relLayout);
    }

    private static int[] getCanvasDimensions(RelativeLayout relLayout) {
        // Get dimensions of relative layout rendered as dimensions of canvas
        // Size of relative layout == Size of canvas by design since the canvas takes up the whole relative layout
        int[] dimensions = new int[2]; // index 0,1 stores x,y values respectively
        dimensions[0] = relLayout.getWidth();
        dimensions[1] = relLayout.getHeight();
        Log.d("CanvasDimensions", String.format("getCanvasDimensions: %d by %d", dimensions[0], dimensions[1]));
        return dimensions;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float raw_x = event.getRawX();
        float raw_y = event.getRawY();

        int offset = getYOffset(relLayout);
        float x = raw_x;
        float y = raw_y - offset;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                X1 = x;
                Y1 = y;
                this.canvasView.startTouch(x, y);
                Log.d("Pos X: ", String.valueOf(x));
                Log.d("Pos Y: ", String.valueOf(y));
                checkCircles(x, y);
                this.canvasView.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                checkCircles(x, y);
                this.canvasView.moveTouch(x, y);
                this.canvasView.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                X2 = x;
                Y2 = y;
                this.canvasView.upTouch(true);
                this.canvasView.drawLine(X1,Y1, X2, Y2);
//                this.canvasView.invalidate();
//                this.canvasView.clearCanvas();
                if (this.ggezwin) {
                    this.canvasView.clearCanvas();
                    Toast.makeText(ConnectDotsActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                }
                //else
                //    this.resetTouched();
                this.canvasView.invalidate();
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.d("IS_ON_POINT: ", "YES");
        }
        return true;
    }

    private int getYOffset(RelativeLayout view) {
        int offset;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        offset = location[1];
        return offset;
    }

//    private void resetCanvas() {
//        this.ggezwin = false;
//        this.circlePos.clear();
//        this.circleStartX = 0;
//        this.circleStartY = 0;
//
//    }


    private void resetTouched() {

        for (Node node : this.nodeMgr.getNodeList()) {
            ImageView circleImage = node.getNodeImage();
            circleImage.setBackgroundColor(Color.TRANSPARENT);
        }
        this.circleStartX = 0;
        this.circleStartY = 0;
    }


    private void checkCircles(float x, float y) {

        for (Node node : this.nodeMgr.getNodeList()) {
            ImageView circleImage = node.getNodeImage();
            double dist = Math.sqrt(Math.pow(circleImage.getX() + 62.5d - x, 2.0d) + Math.pow(circleImage.getY() + 62.5d - y, 2.0d));

            ColorDrawable colorDrawable = (ColorDrawable) circleImage.getBackground();

            if (((colorDrawable == null) || (colorDrawable.getColor() != Color.GREEN)) && dist < 62.5d) {
                if (this.circleStartX == 0 && this.circleStartY == 0) {
                    this.circleStartX = circleImage.getX();
                    this.circleStartY = circleImage.getY();
                    circleImage.setBackgroundColor(Color.GREEN);
                    Log.d("checked dot: ", "YES");
                } else if (circleImage.getX() != this.circleStartX && circleImage.getY() != this.circleStartY) {
                    circleImage.setBackgroundColor(Color.GREEN);
                    this.ggezwin = true;
                }
            }

        }
    }

        private void updateScore(TextView pointView) {
        // Some game metric calculation passed into here.

    }



    /**
     * Create circles and display them on a relative layout.
     * @param nodesList The list containing nodes to calibrate and scale
     */
    private void createCircles(ArrayList<Node> nodesList) {

        int noOfNodes = nodesList.size();
        Log.d("Initialization", String.format("Node Size of scaledNodes: %d", nodesList.size()));

        String nodeName;
        int xCoord;
        int yCoord;
        boolean isFirst = false;
        boolean isLast = false;
        float diameter = getDiameterForNodes(nodesList);

        // Styling Code
        Paint nodePaint = new Paint();
        int nodeColor = Color.rgb(0,150,150);
        nodePaint.setColor(nodeColor);
        nodePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint nodeTextPaint = new Paint();
        int textColor = Color.rgb(0,0,0); // Black
        nodeTextPaint.setColor(textColor);
        nodeTextPaint.setTextSize(diameter/2);
        nodeTextPaint.setAntiAlias(true);
        nodeTextPaint.setTextAlign(Paint.Align.CENTER);
        nodeTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Iterate through scaled node list to create circle.
        for (int i=1; i <= noOfNodes; i++) {
            Node currentNode = nodesList.get(i-1);

            nodeName = currentNode.getName();
            xCoord = currentNode.getCenter_x();
            yCoord = currentNode.getCenter_y();
            Log.d("Scaling", "x-coord: " + String.valueOf(xCoord));
            Log.d("Scaling", "y-coord: " + String.valueOf(yCoord));
            Log.d("Scaling", "Diameter: " + String.valueOf(diameter));

            // Assign boolean for first, last respectively for each node
            int currentNodeNumber = Integer.valueOf(nodeName);
            if (currentNodeNumber == 1) {
                isFirst = true;
                isLast = false;
            } else if (currentNodeNumber == nodesList.size()) {
                isFirst = false;
                isLast = true;
            } else {
                isFirst = false;
                isLast = false;
            }

            int diameterInInt = Math.round(diameter); // Since bitmap only except integer
            float ecfHalfDiameter = (diameterInInt/2f); // Error carry forward from rounding of diameter

            // Create bitmap based on diameter required for circle
            Bitmap bmp = Bitmap.createBitmap(diameterInInt, diameterInInt, Bitmap.Config.ARGB_8888); // Draws the white color part
            Canvas canvas = new Canvas(bmp);

            // Draw Circle & Text
            float startingPositionOnBitMapX = ecfHalfDiameter;
            float startingPositionOnBitMapY = ecfHalfDiameter;

            canvas.drawCircle(ecfHalfDiameter, ecfHalfDiameter, ecfHalfDiameter, nodePaint); // Parameter: center of x on bitmap, center of y on bitmap, radius size, dotPaint style
            canvas.drawText(nodeName, startingPositionOnBitMapX, startingPositionOnBitMapY, nodeTextPaint);

            // Convert circle and draw to drawable and set to image view
            Drawable drawable = new BitmapDrawable(getResources(), bmp);
            ImageView imgView = new ImageView(this);
            imgView.setImageDrawable(drawable);

            // Create Circle Node
            Node node = new Node(nodeName, xCoord, yCoord, bmp.getWidth()/2, imgView);

            // Add node to nodeMgr
            nodeMgr.addNodeToManager(node, isFirst, isLast);

            // Add point to track (NOT SURE IF IMPORTANT OR NOT)
            List<Integer> circleXY = new ArrayList<>();
            circleXY.add(node.getCenter_x());
            circleXY.add(node.getCenter_y());
            this.circlePos.add(circleXY);
        }

    }

    private void displayCirclesOnLayout(NodeMgr nodeMgr, RelativeLayout relLayout) {

        for (Node node: nodeMgr.getNodeList()) {
            ImageView circleImage = node.getNodeImage();
            if(circleImage.getParent()!=null) {
                ((ViewGroup)circleImage.getParent()).removeView(circleImage); // <- fix
            }
            relLayout.addView(circleImage);
            circleImage.setX(node.getCenter_x());
            circleImage.setY(node.getCenter_y());
        }


    }




    /**
     * Main function overseeing scaling and transformation of node. Perform the automatic calibration and scaling of nodes to the given canvas size.
     * @param nodesList The list containing nodes to calibrate and scale
     * @param canvasWidth Width of canvas in which the nodes are allowed to be placed on
     * @param canvasHeight Height of canvas in which the nodes are allowed to be placed on
     * @param percentageFillRequired Percentage of the canvas expected to be filled by the nodes.
     * @param scaleFactor Scale factor used in scaling
     * @param scaleFactorModifier Modifier used to modify the scale factor for scaling up/down.
     * @return List of calibrated nodes
     */
    private ArrayList<Node> calibrateNodeToCanvasSize(
            // Reason for method: The scaling factor will be used when getting the pixel X and pixel Y.
            ArrayList<Node> nodesList,
            int canvasWidth,
            int canvasHeight,
            int percentageFillRequired,
            float scaleFactor,
            float scaleFactorModifier
    ) {



        // Calculate the original bound width and height of nodes before calibration
        int originalBoundX, originalBoundY, originalDisplacementFromOriginX, originalDisplacementFromOriginY;
        int[] originalBoundInfo = getBoundDimensions(nodesList);
        originalBoundX=originalBoundInfo[0];
        originalBoundY=originalBoundInfo[1];
        // Get the ideal filled bounds required to scale up (or down) to.
        int idealBoundX = Math.round(canvasWidth * (percentageFillRequired/100f));
        int idealBoundY = Math.round(canvasHeight * (percentageFillRequired/100f));

        Log.d("Initialization", String.format("calibrate-canvasWidth: %d, calibrate-canvasHeight: %d", canvasWidth, canvasHeight));
        Log.d("Initialization", String.format("calibrate-originalBoundX: %d, calibrate-originalBoundY: %d", originalBoundX, originalBoundY));
        Log.d("Initialization", String.format("idealBoundX: %d, idealBoundY: %d", idealBoundX, idealBoundY));


        int[] boundDimension = getGeometricBoundDisplacementFromOrigin(nodesList);
        originalDisplacementFromOriginX=boundDimension[0];
        originalDisplacementFromOriginY=boundDimension[1];



        // Information for method structure
        // SCALING - Steps breakdown
        // 1) Check if the dot bounds exceeds the canvasDisplayBound. If exceed do step 2, else step 3
        // 2) Exceed bound - while still out of bound, scale down , then check if bound is within. Record the scale factor
        // 3) Does not exceed bound - while within bound, scale up, then check if bound exceeded. Record the scale factor
        // 4) Scale node and return processed nodes list

        // 1) Step 1:
        int scaledWidth = originalBoundX;
        int scaledHeight = originalBoundY;
        boolean isWithinIdeal = isWithinBound(scaledWidth,scaledHeight,idealBoundX, idealBoundY);
        Log.d("Initialization", String.format("Before Scale -> scaledWidth: %d, scaledHeight: %d", scaledWidth, scaledHeight));
        ArrayList<Node> scaledNodes;
        if (!isWithinIdeal) {
            // Step 2 (Node bound exceed ideal bound): Scale Down iteratively, check for bound, return best fit scale factor
            Log.d("Initialization", "Scaling - Starting Scale Down");

            while (true) {
                // Scale and Get Bounds
                scaledNodes = scaleProcessingNodes(nodesList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);
                int[] scaledBoundInfo = getBoundDimensions(scaledNodes);
                int scaledBoundWidth = scaledBoundInfo[0];
                int scaledBoundHeight = scaledBoundInfo[1];
                Log.d("Initialization", String.format("Scaling - scaledBoundWidth: %d, scaledBoundHeight: %d", scaledBoundWidth, scaledBoundHeight));
                Log.d("Initialization", String.format("Scaling - ScaleFactor: %f", scaleFactor));

                // Condition Checking
                isWithinIdeal = isWithinBound(scaledBoundWidth,scaledBoundHeight,idealBoundX, idealBoundY);
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
                scaledNodes = scaleProcessingNodes(nodesList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);
                int[] scaledBoundInfo = getBoundDimensions(scaledNodes);
                int scaledBoundWidth = scaledBoundInfo[0];
                int scaledBoundHeight = scaledBoundInfo[1];
                Log.d("Initialization", String.format("Scaling - scaledBoundWidth: %d, scaledBoundHeight: %d", scaledBoundWidth, scaledBoundHeight));
                Log.d("Initialization", String.format("Scaling - ScaleFactor: %f", scaleFactor));

                // Condition Checking
                isWithinIdeal = isWithinBound(scaledBoundWidth,scaledBoundHeight,idealBoundX, idealBoundY);
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
        scaledNodes = scaleProcessingNodes(nodesList, originalDisplacementFromOriginX, originalDisplacementFromOriginY, scaleFactor);

        // Transform node using self-created evaluation metric
        ArrayList<Node> transformNodes = transformNodeAutomatic(scaledNodes, canvasWidth, canvasHeight, percentageFillRequired);

        return transformNodes;
    }

    /**
     * Calculate the diameter based on a custom self-made metric. See method code for details
     * @param nodesList The list of nodes to scale.
     * @return Diameter that the nodes will use
     */
    private float getDiameterForNodes(ArrayList<Node> nodesList) {
        int[] boundInfo = getBoundDimensions(nodesList);
        int boundWidth = boundInfo[0];
        int boundHeight = boundInfo[1];
        int noOfNodes = nodesList.size();
        // Choose the smaller pixel bound for Y
        int chosenBoundFactor = boundWidth < boundHeight ? boundWidth : boundHeight;

        float calculatedDiameter = chosenBoundFactor /(noOfNodes* DIAMETER_CALCULATION_TOLERANCE_FACTOR);
        float finalDiameter = Math.max(calculatedDiameter,DIAMETER_LIMIT_MIN); // Set a limit of 60f if diameter smaller than 60f
        return finalDiameter;
    }

    /**
     * Performing automatic transformation of nodes based on a custom self-made metric. See method code for details
     * @param nodesList The list of nodes to transform
     * @param canvasWidth Width of canvas in which the nodes are allowed to be placed on
     * @param canvasHeight Height of canvas in which the nodes are allowed to be placed on
     * @param percentageFillRequired Percentage of the canvas expected to be filled by the nodes.
     */
    private ArrayList<Node> transformNodeAutomatic(ArrayList<Node> nodesList, int canvasWidth, int canvasHeight, int percentageFillRequired) {
        // The bound has been displaced to origin. Now we have to transform the nodes appropriately back and try to center it on the screen.

        int[] finalBound = getBoundDimensions(nodesList);
        float scaledPercentageX = ((float)finalBound[0]/canvasWidth * 100);
        float scaledPercentageY = ((float)finalBound[1]/canvasHeight * 100);
        Log.d("Percentage", String.format("finalBoundX, finalBoundY: %d, %d", finalBound[0], finalBound[1]));
        Log.d("Percentage", String.format("PercentX, PercentY: %f, %f", scaledPercentageX, scaledPercentageY));
        float higher = scaledPercentageX > scaledPercentageY ? scaledPercentageX : scaledPercentageY;

        Log.d("Percentage", "Lower" + String.valueOf(higher));
        Log.d("Percentage", "Required" + String.valueOf(percentageFillRequired));
        if ((percentageFillRequired - higher) > percentageFillRequired/4f) { // If the scaled percentage is less than 1/3 of required, then do transformation
            Log.d("Scaling", String.format("Final Bound: (%d , %d)", finalBound[0], finalBound[1]));
            int transformX = Math.round((canvasWidth-finalBound[0])/2f);
            int transformY = Math.round((canvasHeight-finalBound[1])/2f);
            Log.d("Scaling", String.format("Canvas WidthHeight Check: (%d , %d)", canvasWidth, canvasHeight));
            Log.d("Scaling", String.format("FinalTransformX: (%d)", transformX));
            Log.d("Scaling", String.format("FinalTransformY: (%d)", transformY));


            for (Node node: nodesList) {
                int x = node.getCenter_x() + transformX;
                int y = node.getCenter_y() + transformY;
                node.setCenter(x,y);
            }
        } else {
            // Just shift a little
            int transformX = Math.round((canvasWidth-finalBound[0])/10f);
            int transformY = Math.round((canvasHeight-finalBound[1])/10f);

            for (Node node: nodesList) {
                int x = node.getCenter_x() + transformX;
                int y = node.getCenter_y() + transformY;
                node.setCenter(x, y);
            }
        }

        return nodesList;
    }

    /**
     * To scale the nodes based on a certain scale factor and reassign the center x,y coordinates for the node position in pixels
     * @param nodesList The list containing the nodes to be scaled.
     * @param originalDisplacementFromOriginX Horizontal displacement of top left point of bound, from origin. This is the original value before any scaling is done
     * @param originalDisplacementFromOriginY Vertical displacement of top left point of bound, from origin. This is the original value before any scaling is done.
     * @param scaleFactor Scale factor that is a float
     */
    private ArrayList<Node> scaleProcessingNodes(ArrayList<Node> nodesList, int originalDisplacementFromOriginX, int originalDisplacementFromOriginY, float scaleFactor) {

        // Scale item first using scale factor and set as its center attributes. This hold the final scale center coordinates in pixel scale.
        for (Node node: nodesList) {
            // Shift x nearer to origin for better displacement
            int displacedGeometricX = (node.getGeometric_x()- originalDisplacementFromOriginX);
            int displacedGeometricY = (node.getGeometric_y()- originalDisplacementFromOriginY);
            int x = Math.round(displacedGeometricX*scaleFactor);
            int y = Math.round(displacedGeometricY*scaleFactor);
            node.setCenter(x,y);
        }

        return nodesList;
    }

    /**
     * Check if the bound size of a bound is within another. i.e Bound A is in Bound B
     * @param boundToCheckX Width of bound size to check - Bound A
     * @param boundToCheckY Height of bound size to check - Bound A
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
     *
     * This is equivalent to how much area the nodes take up
     *
     * @param nodesList The list containing the nodes.
     * @return Width and Height of bounds in the format [width, height]
     */
    private int[] getBoundDimensions(ArrayList<Node> nodesList) {
        // Determine the bounds in pixel scale. Does not return the lowestX or lowestY because the bound we are checking has already been displaced earlier
        int lowestX = Integer.MAX_VALUE;
        int lowestY = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;

        for (Node node : nodesList) {
            int scaledX = node.getCenter_x();
            int scaledY = node.getCenter_y();
            if (scaledX < lowestX) lowestX = scaledX;
            if (scaledY < lowestY) lowestY = scaledY;
            if (scaledX > highestX) highestX = scaledX;
            if (scaledY > highestY) highestY = scaledY;
        }

        // Transform the x boundaries and y boundaries
        int boundWidth = (highestX - lowestX);
        int boundHeight = (highestY - lowestY);
        return new int[]{boundWidth, boundHeight};
    }


}