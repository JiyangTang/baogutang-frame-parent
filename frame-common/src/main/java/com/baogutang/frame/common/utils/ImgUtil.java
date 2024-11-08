package com.baogutang.frame.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
public class ImgUtil {

  public static byte[] PNG2JPG(InputStream input) {
    try {
      BufferedImage image = ImageIO.read(input);

      return PNG2JPG(image);
    } catch (IOException e) {
      log.error("Conventer png to jpg failed.", e);
    }

    return null;
  }

  private static byte[] PNG2JPG(BufferedImage image) throws IOException {

    BufferedImage result =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(result, "jpg", output);

    return output.toByteArray();
  }

  public static String imageBase64(InputStream input) {
    try {
      BufferedImage image = ImageIO.read(input);
      byte[] bytes = PNG2JPG(image);
      return Base64.getEncoder().encodeToString(bytes);
    } catch (IOException e) {
      log.error("img base64 failed.", e);
    }
    return null;
  }

  public static byte[] getCompressedImage(
      InputStream input, int maxWidth, int maxHeight, int maxSize) {
    byte[] suitableImage = getSuitableSizeImage(input, maxWidth, maxHeight);
    if (suitableImage == null || suitableImage.length <= maxSize) {
      return suitableImage;
    }
    try {
      while (suitableImage.length > maxSize) {
        suitableImage = compress(suitableImage, 0.8);
      }
    } catch (Exception e) {
      log.error("compress image to specify size occur error.", e);
    }
    return suitableImage;
  }

  public static byte[] getSuitableSizeImage(InputStream input, int maxWidth, int maxHeight) {
    try {
      BufferedImage image = ImageIO.read(input);
      int width = Math.min(image.getWidth(), maxWidth);
      int height = Math.min(image.getHeight(), maxHeight);
      BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      ImageIO.write(result, "jpg", output);
      return output.toByteArray();
    } catch (IOException e) {
      log.error("get suitable image error.", e);
    }
    return null;
  }

  public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(srcImgData));
    int width = (int) (bi.getWidth() * scale);
    int height = (int) (bi.getHeight() * scale);
    Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    tag.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(tag, "JPEG", output);
    return output.toByteArray();
  }
}
