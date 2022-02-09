package com.example.mos_lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

public class Pipe {
    protected PointF holeCoordinates;
    protected Size holeSizeInUnits = new Size(3, 10);
    protected Size pipeEndSizeInUnits = new Size(holeSizeInUnits.width, 2);

    protected float holeDifferenceInUnits = 14;
    protected float pipeDistanceInUnits = 10;
    protected float pipeDistanceFromField = pipeEndSizeInUnits.height * 2;
    protected float speed = (float) 0.3;
    protected boolean isScored = false;

    protected int pipeImageId = R.drawable.pipe;
    protected int pipeEndImageId = R.drawable.pipe_end;
    protected Bitmap bitmapUpperPipe;
    protected Bitmap bitmapLowerPipe;
    protected Bitmap bitmapPipeEnd;

    public Pipe(Context context) {
        PointF previousCenterCoordinates = new PointF(GameView.gameUnitCount.width - pipeDistanceInUnits, (GameView.gameUnitCount.height - holeSizeInUnits.height) / (float) 2.0);
        init(context, previousCenterCoordinates);
    }

    public Pipe(Context context, PointF previousCenterCoordinates) {
        init(context, previousCenterCoordinates);
    }

    protected void init(Context context, PointF previousCenterCoordinates) {
        Random random = new Random();
        float randomHeightDifference = (2 * random.nextFloat() - 1) * holeDifferenceInUnits;
        float newHoleCoordinateY = previousCenterCoordinates.y + randomHeightDifference;

        // Random hole coordinate, that does not depend on the previous
        // float newHoleCoordinateY = pipeDistanceFromField + random.nextFloat() * (GameView.gameUnitCount.height - 2 * pipeDistanceFromField - holeSizeInUnits.height);

        newHoleCoordinateY = Math.max(pipeDistanceFromField, newHoleCoordinateY);
        newHoleCoordinateY = Math.min(GameView.gameUnitCount.height - pipeDistanceFromField - holeSizeInUnits.height, newHoleCoordinateY);

        holeCoordinates = new PointF(previousCenterCoordinates.x + holeSizeInUnits.width + pipeDistanceInUnits, newHoleCoordinateY);

        initImage(context);
    }

    void initImage(Context context) {

        Bitmap bitmapImage = BitmapFactory.decodeResource(context.getResources(), pipeEndImageId);
        bitmapPipeEnd = Bitmap.createScaledBitmap(bitmapImage, (int) (pipeEndSizeInUnits.width * GameView.unitSize.width), (int) ((pipeEndSizeInUnits.height * GameView.unitSize.height) + 1), false);
        bitmapImage.recycle();

        bitmapImage = BitmapFactory.decodeResource(context.getResources(), pipeImageId);
        bitmapUpperPipe = Bitmap.createScaledBitmap(bitmapImage, (int) (pipeEndSizeInUnits.width * GameView.unitSize.width), (int) (((holeCoordinates.y - pipeEndSizeInUnits.height) * GameView.unitSize.height) + 1), false);
        bitmapImage.recycle();

        bitmapImage = BitmapFactory.decodeResource(context.getResources(), pipeImageId);
        bitmapLowerPipe = Bitmap.createScaledBitmap(bitmapImage, (int) (pipeEndSizeInUnits.width * GameView.unitSize.width), (int) ((GameView.gameUnitCount.height - holeCoordinates.y - pipeEndSizeInUnits.height - holeSizeInUnits.height) * GameView.unitSize.height), false);
        bitmapImage.recycle();
    }

    public void draw(Paint paint, Canvas canvas) {
        canvas.drawBitmap(bitmapUpperPipe, holeCoordinates.x * GameView.unitSize.width, 0, paint);
        canvas.drawBitmap(bitmapLowerPipe, holeCoordinates.x * GameView.unitSize.width, (holeCoordinates.y + holeSizeInUnits.height + pipeEndSizeInUnits.height) * GameView.unitSize.height - 1, paint);
        canvas.drawBitmap(bitmapPipeEnd, holeCoordinates.x * GameView.unitSize.width, (holeCoordinates.y - pipeEndSizeInUnits.height) * GameView.unitSize.height, paint);
        canvas.drawBitmap(bitmapPipeEnd, holeCoordinates.x * GameView.unitSize.width, (holeCoordinates.y + holeSizeInUnits.height) * GameView.unitSize.height, paint);
    }

    public boolean isEnded() {
        return holeCoordinates.x + holeSizeInUnits.width < -2;
    }

    public boolean isCollision(PointF birdLocation, Size birdSizeInUnits) {
        if(birdLocation.x + birdSizeInUnits.width < holeCoordinates.x || birdLocation.x > holeCoordinates.x + holeSizeInUnits.width)
            return false;

        if(birdLocation.y > holeCoordinates.y && birdLocation.y + birdSizeInUnits.height < holeCoordinates.y + holeSizeInUnits.height)
            return false;

        return true;
    }

    public boolean isScoreUpdate(PointF birdLocation, Size birdSizeInUnits)
    {
        if(!isScored && birdLocation.x > holeCoordinates.x + holeSizeInUnits.width / (float) 2.0) {
            isScored = true;
            return true;
        }

        return false;
    }

    public void update() {
        holeCoordinates.x -= speed;
    }
}
