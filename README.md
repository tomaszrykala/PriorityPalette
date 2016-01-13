PriorityPalette
=======

#Description

**PriorityPalette** lets you specify order (priority) of Palette Swatches and returns first one that's available. It also allows to specify an fallback Swatch color to return should none of the Swatches were able to be extracted, like from a solid black Bitmap source. This is useful when extracting Swatches from bitmaps with unknown color palettes, as the library handles the null Swatches itself, and tries to extract next one until the list is exhausted.

#Download

// TODO


#Sample
## Init

```java
final PriorityPalette priorityPalette = PriorityPalette.from(bitmap)
    .priority(
        PriorityPalette.PriorityPaletteSwatch.VIBRANT,
        PriorityPalette.PriorityPaletteSwatch.VIBRANT_DARK,
        PriorityPalette.PriorityPaletteSwatch.VIBRANT_LIGHT,
        PriorityPalette.PriorityPaletteSwatch.MUTED,
        PriorityPalette.PriorityPaletteSwatch.MUTED_DARK,
        PriorityPalette.PriorityPaletteSwatch.MUTED_LIGHT)
    .error(Color.BLUE); // create custom Swatch to return on error
```
## Generate Swatch

```java
// Asynchronous
priorityPalette.generate(new PriorityPalette.PriorityPaletteAsyncListener() {
    @Override
    public void onGenerated(Palette palette, Palette.Swatch swatch) {
        // returns Palette, and the generate Swatch
    }
    
    @Override
    public void onGenerated(Palette palette) {
        // returns Palette
    }
    
// Synchronous
priorityPalette.generate();
```

#Dependencies

- Palette by Google : http://developer.android.com/reference/android/support/v7/graphics/Palette.html

#License

    Copyright 2015 tomaszrykala, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
