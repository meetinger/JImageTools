package code; //Change to your package

//
// Copyright (c) Yanchik(https://github.com/yanchikdev). All rights reserved.
// Licensed under the GNU AGPL. See LICENSE file in the project root for full license information.
// Version: 1.0.0a
//

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class JImageTools {

    public static BufferedImage loadImage(String path) {
        return FXToBufferedImage(new Image(path));
    }

    public static BufferedImage scaleImage(BufferedImage toScale, double kx, double ky) {
        int w = toScale.getWidth();
        int h = toScale.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(kx, ky);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        result = scaleOp.filter(toScale, result);
        return result;
    }

    public static BufferedImage resizeImage(BufferedImage toResize, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, toResize.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(toResize, 0, 0, w, h, 0, 0, toResize.getWidth(),
                toResize.getHeight(), null);
        g.dispose();
        return resized;
    }

    public static BufferedImage fitByWidth(BufferedImage toFit, int w) {
        return resizeImage(toFit, w, (int) ((int) toFit.getHeight() * w / toFit.getWidth()));
    }

    public static BufferedImage fitByHeight(BufferedImage toFit, int h) {
        return resizeImage(toFit, (int) (toFit.getWidth() * h / toFit.getHeight()), h);
    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle, boolean correctSize) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();

        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        if (!correctSize) {
            newWidth = w;
            newHeight = h;
        }
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }

    public static BufferedImage FXToBufferedImage(Image img) {
        return SwingFXUtils.fromFXImage(img, null);
    }

    public static Image BufferedImageToFX(BufferedImage img) {
        return SwingFXUtils.toFXImage(img, null);
    }

    public static BufferedImage cropImage(BufferedImage img, int x1, int y1, int x2, int y2) {
        return img.getSubimage(x1, y1, x2 - x1, y2 - y1);
    }

}