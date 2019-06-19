package de.innocow.innocow_v001.utilities.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.util.ArrayList;
import java.util.List;

import de.innocow.innocow_v001.activities.BarnPhysicalDimensionProvider;
import de.innocow.innocow_v001.activities.BitmapLayoutProvider;
import de.innocow.innocow_v001.activities.RelativeCowPositionProvider;
import de.innocow.innocow_v001.pojo.barn_details.BarnDimensions;
import de.innocow.innocow_v001.pojo.barn_details.CowPositionInformation;
import de.innocow.innocow_v001.pojo.barn_details.RelativeCowPosition;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;

public class CowPositionCanvasView extends View {

    Rect rect;
    private float startTouchX = 0f;
    private float startTouchY = 0f;
    private float translatedTouchX = 0f;
    private float translatedTouchY = 0f;
    private float previousTranslatedTouchX = 0f;
    private float previousTranslatedTouchY = 0f;
    private boolean isDragged = false;
    private int mode;
    private SVG svg;
    private Paint paint;
    //  This Class is used to detect the pinch zoom event.
    private ScaleGestureDetector gestureDetector;
    private float scaleFactor = 1f;
    private BitmapLayoutProvider bitmapLayoutProvider;
    private RelativeCowPositionProvider cowPositionProvider;
    private BarnPhysicalDimensionProvider barnPhysicalDimensionProvider;
    private BarnDimensions barnDimensions;
    private float barnWidthInPx = 0f;
    private float barnHeightInPx = 0f;
    private float offsetInPx = 0f;
    private List<List<Float>> relativePositionList;


    public CowPositionCanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        gestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        paint = new Paint();
        paint.setColor(Color.BLACK);
        this.bitmapLayoutProvider = (BitmapLayoutProvider) context;
        this.cowPositionProvider = (RelativeCowPositionProvider) context;
        this.barnPhysicalDimensionProvider = (BarnPhysicalDimensionProvider) context;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, getWidth() / 2f, getHeight() / 2f);
        canvas.scale(scaleFactor, scaleFactor);

        if (scaleFactor != 1.0)
            canvas.translate(-translatedTouchY / scaleFactor, translatedTouchX / scaleFactor);

        canvas.drawPicture(svg.renderToPicture(), rect);
        if (relativePositionList != null) {
            float RADIUS = 6;
            for (List<Float> relativePosition : relativePositionList) {

                switch (String.valueOf(relativePosition.get(2))) {
                    case "1.0":
                        paint.setColor(Color.rgb(103,102,100));
                        break;
                    case "2.0":
                        paint.setColor(Color.rgb(67,173,192));
                        break;
                    default:
                        paint.setColor(Color.BLACK);
                }
                canvas.drawCircle(relativePosition.get(0), relativePosition.get(1), RADIUS, paint);
            }
        }
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);
        mCanvas.rotate(-90, getWidth() / 2f, getHeight() / 2f);
        rect = mCanvas.getClipBounds();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int NONE = 0;
        int DRAG = 1;
        int ZOOM = 2;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                if (scaleFactor > 1.0) {

                    startTouchX = event.getX() - previousTranslatedTouchX;
                    startTouchY = event.getY() - previousTranslatedTouchY;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (scaleFactor > 1.0) {
                    translatedTouchX = event.getX() - startTouchX;
                    translatedTouchY = event.getY() - startTouchY;
                }
                if (Math.sqrt(Math.pow(translatedTouchX - startTouchX, 2) +
                        Math.pow(translatedTouchX - startTouchX, 2)) > 0)
                    isDragged = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_UP:

                if (scaleFactor > 1.0f) {
                    previousTranslatedTouchX = translatedTouchX;
                    previousTranslatedTouchY = translatedTouchY;
                }
                mode = NONE;
                isDragged = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = ZOOM;
                previousTranslatedTouchX = translatedTouchX;
                previousTranslatedTouchY = translatedTouchY;
                break;
        }

        gestureDetector.onTouchEvent(event);

        if ((mode == DRAG && scaleFactor != 1f && isDragged) || mode == ZOOM)
            invalidate();

        return true;
    }

    public void init() {
        try {
            svg = bitmapLayoutProvider.provideBarnLayoutSVG();
            CowPositionInformation cowPositionInformation = cowPositionProvider.provideCowPosition();

            if (cowPositionInformation != null && barnDimensions != null) {
                relativePositionList = new ArrayList<>();
                for (RelativeCowPosition cowPosition : cowPositionInformation.getCowPositionList()) {
                    List<Float> relativePosition = new ArrayList<>();
                    float cowPosXPx = cowPosition.getxPosition() * StringConstants.METER_TO_PIXELS;
                    float cowPosYPx = cowPosition.getyPosition() * StringConstants.METER_TO_PIXELS;
                    float relativeXPos = (((cowPosXPx + offsetInPx) / barnWidthInPx) * getHeight())
                            + rect.left;
                    float relativeYPos = ((cowPosYPx / barnHeightInPx) * getWidth()) + rect.top;
                    relativePosition.add(relativeXPos);
                    relativePosition.add(relativeYPos);
                    relativePosition.add(cowPosition.getColorIndicator());
                    relativePositionList.add(relativePosition);
                }
            }
            if (barnDimensions == null) {

                barnDimensions = barnPhysicalDimensionProvider.providePhysicalBarnDimensions();
                if (barnDimensions != null) {

                    barnWidthInPx = barnDimensions.getDimensions().getDimensionX() *
                            StringConstants.METER_TO_PIXELS;
                    barnHeightInPx = barnDimensions.getDimensions().getDimensionY() *
                            StringConstants.METER_TO_PIXELS;
                    offsetInPx = barnDimensions.getDimensions().getOriginX() *
                            StringConstants.METER_TO_PIXELS;
                }
            }
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
//          This is done to make sure that the scale is within the bond limits.
            float MIN_ZOOM = 1f;
            float MAX_ZOOM = 5f;
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;
        }
    }

}
