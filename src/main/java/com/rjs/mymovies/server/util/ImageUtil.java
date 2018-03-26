package com.rjs.mymovies.server.util;

import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageUtil {
    public static final String JPEG = "jpeg";
    public static final String PNG = "png";

    private static final Logger LOGGER = Logger.getLogger(ImageUtil.class.getName());
    private static final int THUMB_WIDTH = 92;

    public static BufferedImage createThumbImage(BufferedImage original) {
        double ratio = (double) THUMB_WIDTH / (double) original.getWidth();
        double thumbHeight = ratio * (double) original.getHeight();

        return Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, THUMB_WIDTH, (int) thumbHeight);
    }

    public static void saveImage(URL imageUrl, String savePathStr, String imageName) {
        if (StringUtils.isBlank(savePathStr)) {
            LOGGER.severe("Saving image error: savePathStr parameter is empty.");

            return;
        }

        File savePath = new File(savePathStr);

        if (!savePath.exists()) {
            if (savePath.mkdirs()) {
                LOGGER.info("Created local image path: " + savePathStr + ".");
            }
            else {
                LOGGER.severe("Unable to create local image path: " + savePathStr + ".");

                return;
            }
        }

        if (!savePath.isDirectory()) {
            LOGGER.severe("Saving image error: " + savePathStr + " is not a directory.");

            return;
        }

        saveImage(imageUrl, savePath, imageName);
    }

    public static void saveImage(URL imageUrl, File savePath, String imageName) {
        if (StringUtils.isBlank(imageName)) {
            LOGGER.severe("Saving image error: imageName parameter is empty.");

            return;
        }

        try {
            // Determine image format, so proper file extension can be inferred.
            String imageFileSuffix = null;
            URLConnection connection = imageUrl.openConnection();
            String contentType = connection.getContentType();
            Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(contentType);

            while (imageFileSuffix == null && readers.hasNext()) {
                ImageReaderSpi provider = readers.next().getOriginatingProvider();

                if (provider != null) {
                    String[] suffixes = provider.getFileSuffixes();

                    if (suffixes != null && suffixes.length > 0) {
                        imageFileSuffix = suffixes[0].startsWith(".") ? suffixes[0] : "." + suffixes[0];
                    }
                }
            }

            if (imageFileSuffix == null) {
                // I don't know. Just assume jpeg, I guess
                imageFileSuffix = "." + JPEG;
            }

            BufferedImage posterImg = ImageIO.read(connection.getInputStream());
            ImageIO.write(posterImg, imageFileSuffix.substring(1), new FileOutputStream(new File(savePath, imageName + imageFileSuffix)));
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving image from " + imageUrl.toString(), e);
        }
    }
}
