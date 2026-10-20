package database;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLoader {
    private final String BASE_URL = "https://ventiqkhrs.cc.cd/";
    private final Map<String, BufferedImage> cache = new HashMap<>();

    public BufferedImage loadImage(String relativePath) {
        if (cache.containsKey(relativePath)) {
            return cache.get(relativePath);
        }
        try {
            URL url = new URL(BASE_URL + relativePath);
            BufferedImage img = ImageIO.read(url);
            cache.put(relativePath, img);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}