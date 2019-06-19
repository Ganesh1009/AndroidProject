package de.innocow.innocow_v001.activities;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public interface BitmapLayoutProvider {

    SVG provideBarnLayoutSVG() throws SVGParseException;
}
