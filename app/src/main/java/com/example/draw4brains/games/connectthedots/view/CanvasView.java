package com.example.draw4brains.games.connectthedots.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class CanvasView extends View {

    public static Bitmap mBitmap;
    private Canvas mCanvas;
    Random r = new Random();
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 3;
    private ArrayList<Integer> dimensions;
    private RelativeLayout relLayout;

    private float startX, startY, stopX, stopY;

    ArrayList<Path> paths = new ArrayList<Path>();


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(12f);
    }

    public void createCanvas(RelativeLayout relLayout, int canvasWidth, int canvasHeight) {
        this.relLayout = relLayout;
        mBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
//        mCanvas.drawColor(Color.TRANSPARENT);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path p : paths) {
            canvas.drawPath(p, mPaint);
        }
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, mPaint);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    public void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        invalidate();
    }

    // when ACTION_MOVE move touch according to the x,y values
    public void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
        invalidate();
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    public void upTouch() {
        mPath.lineTo(mX, mY);
        paths.add(mPath);
        mPath = new Path();
        invalidate();
    }

    public void doNotRecordAction() {
        mPath = new Path();
        invalidate();
    }

    public void drawLine(float startX, float startY, float stopX, float stopY) {
        Paint temp = new Paint();
        temp.setAntiAlias(true);
        temp.setColor(Color.BLACK);
        temp.setStyle(Paint.Style.STROKE);
        temp.setStrokeJoin(Paint.Join.ROUND);
        temp.setStrokeWidth(12f);
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        Log.d("DrawLine", String.format("(%f,%f) to (%f,%f)", startX, startY, stopX, stopY));
        mCanvas.drawLine(startX, startY, stopX, stopY, temp);
    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            paths.remove(paths.size() - 1);
            invalidate();
        } else {
            Toast.makeText(context, "No more strokes to undo!", Toast.LENGTH_LONG).show();
            Log.d("undoNomore", "no");
        }
    }

//    public int onClickRedo (){
//        if (undonePaths.size()>0)
//        {
//            paths.add(undonePaths.remove(undonePaths.size()-1));
//            invalidate();
//        }
//        else
//        {
//            Toast.makeText(context, "No more strokes to redo!", Toast.LENGTH_LONG).show();
//            Log.d("redoNomore","no");
//
//        }
//        return paths.size();
//    }

}

