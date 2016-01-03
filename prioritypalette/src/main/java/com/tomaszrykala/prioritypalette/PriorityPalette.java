package com.tomaszrykala.prioritypalette;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriorityPalette {

    public enum PrioritySwatch {VIBRANT, VIBRANT_DARK, VIBRANT_LIGHT, MUTED, MUTED_DARK, MUTED_LIGHT}

    public interface PriorityPaletteAsyncListener extends Palette.PaletteAsyncListener {
        void onGenerated(Palette palette, Palette.Swatch swatch);
    }

    private List<PrioritySwatch> mSwatches;
    @ColorInt private int mErrorColor;
    private Bitmap mBitmap;

    private PriorityPalette(@NonNull Bitmap bitmap) {
        mSwatches = new ArrayList<>();
        mBitmap = bitmap;
    }

    @NonNull
    public static PriorityPalette from(@Nullable Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            throw new IllegalArgumentException("Bitmap is not valid");
        } else {
            return new PriorityPalette(bitmap);
        }
    }

    @NonNull
    public PriorityPalette priority(@Nullable PrioritySwatch... swatches) {
        if (swatches != null) {
            final List<PrioritySwatch> swatchList = Arrays.asList(swatches);
            for (int i = 0, size = swatchList.size(); i < size; i++) {
                final PrioritySwatch prioritySwatch = swatchList.get(i);
                if (!mSwatches.contains(prioritySwatch)) {
                    mSwatches.add(prioritySwatch);
                }
            }
        }
        return this;
    }

    @NonNull
    public PriorityPalette error(@ColorInt int error) {
        mErrorColor = error;
        return this;
    }

    @Nullable
    public Palette.Swatch generate() {
        Palette palette = Palette.from(mBitmap).generate();
        return generatePrioritySwatch(palette);
    }

    public void generate(@Nullable final PriorityPaletteAsyncListener listener) {
        Palette.from(mBitmap).generate(
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        if (listener != null) {
                            final Palette.Swatch swatch = generatePrioritySwatch(palette);
                            listener.onGenerated(palette, swatch);
                        }
                    }
                }
        );
    }

    @Nullable
    private Palette.Swatch generatePrioritySwatch(@NonNull Palette palette) {
        Palette.Swatch swatch = null;
        for (PrioritySwatch prioritySwatch : mSwatches) {
            switch (prioritySwatch) {
                case VIBRANT:
                    swatch = palette.getVibrantSwatch();
                    break;
                case VIBRANT_DARK:
                    swatch = palette.getDarkVibrantSwatch();
                    break;
                case VIBRANT_LIGHT:
                    swatch = palette.getLightVibrantSwatch();
                    break;
                case MUTED:
                    swatch = palette.getMutedSwatch();
                    break;
                case MUTED_DARK:
                    swatch = palette.getDarkMutedSwatch();
                    break;
                case MUTED_LIGHT:
                    swatch = palette.getLightMutedSwatch();
                    break;
            }
            if (swatch != null) break;
        }

        if (swatch == null && mErrorColor != 0) {
            swatch = new Palette.Swatch(mErrorColor, 0);
        }

        return swatch;
    }
}