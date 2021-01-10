package ru.ezhov.pixel.view;

import java.io.File;

interface Pixeler {
    void toPixel(File file, int pixelSize);
}