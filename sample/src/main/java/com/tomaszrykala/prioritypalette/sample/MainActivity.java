package com.tomaszrykala.prioritypalette.sample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tomaszrykala.prioritypalette.PriorityPalette;

public class MainActivity extends AppCompatActivity {

    private View mView;
    private ImageView mImageView;
    private TextView mImageViewText, mViewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.view);
        mViewTextView = (TextView) findViewById(R.id.view_text);
        mImageView = (ImageView) findViewById(R.id.image);
        mImageViewText = (TextView) findViewById(R.id.image_view_text);

        loadImageViewSource();
    }

    private void loadImageViewSource() {
        Picasso.with(this).load("https://raw.githubusercontent.com/square/picasso/master/website/static/sample.png")
                .into(mImageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        doPriorityPalette(mImageView, mImageViewText, null);
                        loadSolidColorSource();
                    }

                    @Override
                    public void onError() {
                        // no-op
                    }
                });
    }

    private void loadSolidColorSource() {
        final Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackground(bitmapDrawable);
        } else {
            mView.setBackgroundDrawable(bitmapDrawable);
        }

        doPriorityPalette(mView, mViewTextView, new PriorityPalette.PriorityPaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette, Palette.Swatch swatch) {
                applySwatchToTargetTextView(swatch, mViewTextView);
            }

            @Override
            public void onGenerated(Palette palette) {
                // no-op
            }
        });
    }

    private void doPriorityPalette(@NonNull View sourceView, @NonNull TextView targetTextView,
                                   @Nullable PriorityPalette.PriorityPaletteAsyncListener listener) {

        final Drawable background = sourceView.getBackground();
        final Drawable drawable = background != null ? background : ((ImageView) sourceView).getDrawable();
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        final PriorityPalette priorityPalette = PriorityPalette.from(bitmap)
                .priority(
                        PriorityPalette.PrioritySwatch.VIBRANT,
                        PriorityPalette.PrioritySwatch.VIBRANT_DARK,
                        PriorityPalette.PrioritySwatch.VIBRANT_LIGHT,
                        PriorityPalette.PrioritySwatch.MUTED,
                        PriorityPalette.PrioritySwatch.MUTED_DARK,
                        PriorityPalette.PrioritySwatch.MUTED_LIGHT)
                .error(Color.BLUE);

        if (listener != null) {
            priorityPalette.generate(listener);
        } else {
            applySwatchToTargetTextView(priorityPalette.generate(), targetTextView);
        }
    }

    private void applySwatchToTargetTextView(@Nullable Palette.Swatch swatch, @NonNull TextView targetTextView) {
        if (swatch != null) {
            targetTextView.setText(swatch.toString());
            targetTextView.setBackgroundColor(swatch.getRgb());
            targetTextView.setTextColor(swatch.getBodyTextColor());
        }
    }
}