package tk.teemocode.commons.component.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageUtils {
	public static final int ZOOM_NO_STRETCH = 0;

	public static final int ZOOM_STRETCH = 1;

	public static final int ZOOM_FIT_WIDTH = 2;

	public static final int ZOOM_FIT_HEIGHT = 3;

	public static boolean isImageFile(String suffix) {
		if (suffix.equalsIgnoreCase(".JPG") || suffix.equalsIgnoreCase(".JPEG")
				|| suffix.equalsIgnoreCase(".PNG") || suffix.equalsIgnoreCase(".GIF")
				|| suffix.equalsIgnoreCase(".BMP") || suffix.equalsIgnoreCase(".TIF")){
			return true;
		}
		return false;
	}

	public static ByteArrayOutputStream resize(File srcFile, int width, int height) {
		return resize(srcFile, width, height, ZOOM_NO_STRETCH);
	}

	public static ByteArrayOutputStream resize(File srcFile, int width, int height, int zoomFlag) {
		// File srcFile = checkImageFile(oFile);
		ByteArrayOutputStream out = null;
		try {
			Image img = loadImage(srcFile);
			int w = img.getWidth(null);
			int h = img.getHeight(null);
			double scaleW = (double) width / w;
			double scaleH = (double) height / h;
			switch(zoomFlag) {
				case ZOOM_NO_STRETCH:
					if(scaleW > scaleH) {
						width = (int) (w * scaleH);
					} else if(scaleW < scaleH) {
						height = (int) (h * scaleW);
					}
					break;
				case ZOOM_FIT_WIDTH:
					if(scaleW != scaleH) {
						height = (int) (h * scaleW);
					}
					break;
				case ZOOM_FIT_HEIGHT:
					if(scaleW != scaleH) {
						width = (int) (w * scaleH);
					}
					break;
			}

			BufferedImage newImg = resize(img, width, height);
			out = export2JPEG(newImg, 0.8f);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static ByteArrayOutputStream resize(File srcFile, float scale) {
		// File srcFile = checkImageFile(oFile);
		String fName = srcFile.getName();
		String ext = fName.substring(fName.lastIndexOf('.') + 1);
		boolean isGif = ext.equalsIgnoreCase("gif");

		ByteArrayOutputStream out = null;
		try {
			Image img = loadImage(srcFile);
			int w = img.getWidth(null);
			int h = img.getHeight(null);

			int nw = (int) (w * scale);
			int nh = (int) (h * scale);

			BufferedImage newImg = resize(img, nw, nh);
			if(isGif) {
				// out = exportImg(newImg, ext);
				out = export2JPEG(newImg, 0.8f);
			} else {
				out = export2JPEG(newImg, 0.8f);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	private static Image loadImage(File original) throws IOException {
		ImageIcon ii = new ImageIcon(original.getCanonicalPath());
		Image i = ii.getImage();

		return i;
	}

	private static BufferedImage resize(Image i, int newWidth, int newHeight) throws IOException {

		Image resizedImage = null;
		resizedImage = i.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

		Image temp = new ImageIcon(resizedImage).getImage();

		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

		Graphics g = bufferedImage.createGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		float softenFactor = 0.05f;
		float[] softenArray = {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);

		return bufferedImage;
	}

	private static ByteArrayOutputStream export2JPEG(BufferedImage bufferImg, float quality) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferImg);

		param.setQuality(quality, true);

		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferImg);

		return out;
	}
}
