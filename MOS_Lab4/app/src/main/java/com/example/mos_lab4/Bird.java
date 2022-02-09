package com.example.mos_lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Bird {
    protected PointF coordinates;
    protected Size sizeInUnits = new Size(2, 2);

    protected float speed = 0;
    protected float maxSpeed = (float) 1.25;
    protected float minSpeed = (float) -1.5;
    protected float velocity = (float) 0.2;

    protected int imageId = R.drawable.bird;
    protected Bitmap bitmap;

    public Bird(Context context) {
        coordinates = new PointF( (GameView.gameUnitCount.width - sizeInUnits.width) / (float) 2.0, (GameView.gameUnitCount.height - sizeInUnits.height) / (float) 2.0);
        initImage(context);
    }

    private void initImage(Context context) {
        Bitmap bitmapImage = BitmapFactory.decodeResource(context.getResources(), imageId);
        bitmap = Bitmap.createScaledBitmap(bitmapImage, (int) (sizeInUnits.width * GameView.unitSize.width), (int) (sizeInUnits.height * GameView.unitSize.height), false);
        bitmapImage.recycle();
    }

    public void touchBird() {
        speed = maxSpeed;
    }

    public void draw(Paint paint, Canvas canvas) {
        canvas.drawBitmap(bitmap, coordinates.x * GameView.unitSize.width, coordinates.y * GameView.unitSize.height, paint);
    }

    public void update() {
        coordinates.y -= speed;
        speed = Math.max(speed - velocity, minSpeed);
    }

    public boolean isHitFloorOrSky() {
        return coordinates.y < 0 || coordinates.y + sizeInUnits.height > GameView.gameUnitCount.height;
    }
}
