package com.example.mos_lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    public static Size unitCount = new Size(18, 36);
    public static Size gameUnitCount = new Size(18, 30);
    public static Size unitSize = new Size(1, 1);

    private int Score = 0;

    private boolean firstIteration = true;
    private boolean beforeGameStart = true;
    private boolean gameRunning = true;

    private Bird bird;
    private ArrayList<Pipe> pipes = new ArrayList<Pipe>();

    private Thread gameThread = null;
    private Paint paint;
    private Paint textPaint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    protected int backgroundUpId = R.drawable.background_up;
    protected int backgroundDownId = R.drawable.background_down;
    protected int tapToPlayId = R.drawable.tap_to_play;
    protected Bitmap backgroundUp;
    protected Bitmap backgroundDown;
    protected Bitmap tapToPlay;
    protected Size tapToPlaySize = new Size(3,2);

    public GameView(Context context) {
        super(context);

        surfaceHolder = getHolder();

        gameThread = new Thread(this);
        gameThread.start();

        paint = new Paint();
        setupTextPaint();
    }

    public void setupTextPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(48.0f);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public void performTouch()
    {
        if(beforeGameStart)
            beforeGameStart = false;

        if(bird != null)
            bird.touchBird();
    }

    @Override
    public void run() {
        while(beforeGameStart) {
            draw();
            pause();
        }
        while(gameRunning) {
            update();
            updateScore();
            updatePipesList();
            checkGameOverCollision();
            draw();
            pause();
        }
        ((Game) getContext()).showGameOver(Score);
    }

    private void update() {
        if(!firstIteration) {
            bird.update();
            for(Pipe pipe : pipes)
                pipe.update();
        }
    }

    private void updateScore()
    {
        for(Pipe pipe : pipes)
            if(pipe.isScoreUpdate(bird.coordinates, bird.sizeInUnits))
                Score += 1;
    }

    private void updatePipesList() {
        if(pipes.size() > 0 && pipes.get(0).isEnded()) {
            pipes.remove(0);
            pipes.add(new Pipe(getContext(), pipes.get(pipes.size() - 1).holeCoordinates));
        }
    }

    private void checkGameOverCollision() {
        if(bird != null && bird.isHitFloorOrSky())
            gameRunning = false;

        for(Pipe pipe : pipes) {
            if(pipe.isCollision(bird.coordinates, bird.sizeInUnits))
                gameRunning = false;
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            if(firstIteration) {
                firstIteration = false;

                unitSize.width = surfaceHolder.getSurfaceFrame().width()/unitCount.width;
                unitSize.height = surfaceHolder.getSurfaceFrame().height()/unitCount.height;

                initBackgrounds();
                initTapToPlayImage();

                bird = new Bird(getContext());

                pipes.add(new Pipe(getContext()));
                pipes.add(new Pipe(getContext(), pipes.get(pipes.size() - 1).holeCoordinates));
                pipes.add(new Pipe(getContext(), pipes.get(pipes.size() - 1).holeCoordinates));
            }

            canvas = surfaceHolder.lockCanvas();

            canvas.drawBitmap(backgroundUp, 0, 0, paint);
            canvas.drawBitmap(backgroundDown, 0, gameUnitCount.height * unitSize.height, paint);

            for(Pipe pipe : pipes)
                pipe.draw(paint, canvas);

            bird.draw(paint, canvas);

            if(beforeGameStart) {
                canvas.drawBitmap(tapToPlay,  (bird.coordinates.x + (float) 0.5) * unitSize.width, (bird.coordinates.y + bird.sizeInUnits.height + (float) 0.5) * unitSize.height, paint);
            }

            float textWidth = textPaint.measureText(String.valueOf(Score));
            canvas.drawText(String.valueOf(Score), gameUnitCount.width * unitSize.width - textWidth - 10, textPaint.getTextSize() + 10, textPaint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void initBackgrounds()
    {
        Bitmap bitmapImage = BitmapFactory.decodeResource(getContext().getResources(), backgroundUpId);
        backgroundUp = Bitmap.createScaledBitmap(bitmapImage, (int)(gameUnitCount.width * unitSize.width), (int)(gameUnitCount.height * unitSize.height + 1), false);
        bitmapImage.recycle();

        bitmapImage = BitmapFactory.decodeResource(getContext().getResources(), backgroundDownId);
        backgroundDown = Bitmap.createScaledBitmap(bitmapImage, (int)(unitCount.width * unitSize.width), (int)((unitCount.height - gameUnitCount.height) * unitSize.height), false);
        bitmapImage.recycle();
    }

    private void initTapToPlayImage() {
        Bitmap bitmapImage = BitmapFactory.decodeResource(getContext().getResources(), tapToPlayId);
        tapToPlay = Bitmap.createScaledBitmap(bitmapImage, (int)(tapToPlaySize.width * unitSize.width), (int)(tapToPlaySize.height * unitSize.height), false);
        bitmapImage.recycle();
    }

    private void pause() {
        try {
            gameThread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
