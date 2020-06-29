package com.gvt.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nu.pattern.OpenCV;

public class ChessboardRecognitionTest {

	private static Logger logger = LoggerFactory.getLogger(ChessboardRecognitionTest.class);

	@Before
	public void start() {
		OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
	}

	@Test
	public void testEdgeImage() {
		startRecognition();
	}

	private void startRecognition() {
		try {
//			Robot robot = new Robot();
			BufferedImage screenFullImage = ImageIO.read(new File(getClass().getClassLoader().getResource("onlyChessboard.bmp").getFile()));

			Mat imageSourceMat = BufferedImage2Mat(screenFullImage);
			Mat imageDestMat = new Mat();
//			Mat imageBluredMat = new Mat();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Mat hierarchy = new Mat();

			Imgproc.cvtColor(imageSourceMat, imageDestMat, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.blur(imageDestMat, imageBluredMat, new Size(3, 3));
			Imgproc.Canny(imageDestMat, imageDestMat, 0, 5);
//			Imgproc.threshold(imageDestMat, imageDestMat, 127, 255, 0);
			Imgproc.findContours(imageDestMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//			Imgproc.warpPerspective(imageDestMat, imageDestMat, M, dsize);

			Random rng = new Random(12345);

			/// Draw contours
			Mat drawing = Mat.zeros(imageDestMat.size(), CvType.CV_8UC3);
			for (int i = 0; i < contours.size(); i++) {
				Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
				Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, hierarchy, 0, new Point());
			}
//			Mat dest = new Mat();
//			Core.add(dest, Scalar.all(0), dest);
//
//			imageSourceMat.copyTo(dest, imageDestMat);

			saveImage(drawing);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void saveImage(Mat imageBluredMat) throws Exception {
		File file = new File("C:\\Users\\hdacosta\\Development\\eclipse-workspace-jsf2\\dgt-protocol\\src\\test\\resources\\output.png");

		ImageIO.write(Mat2BufferedImage(imageBluredMat), "png", file);
	}

	private Mat BufferedImage2Mat(BufferedImage image) throws Exception {
		Mat retValue = null;

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
			ImageIO.write(image, "png", byteArrayOutputStream);
			byteArrayOutputStream.flush();

			retValue = Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
		} catch (Exception e) {
			logger.error("Problem converting to Mat", e);

			throw e;
		}

		return retValue;
	}

	private BufferedImage Mat2BufferedImage(Mat matrix) throws Exception {
		BufferedImage bufImage = null;
		MatOfByte mob = new MatOfByte();

		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		Imgcodecs.imencode(".png", matrix, mob);

		// convert the "matrix of bytes" into a byte array
		try (InputStream in = new ByteArrayInputStream(mob.toArray())) {
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			logger.error("Problem converting to BufferedImage", e);

			throw e;
		}

		return bufImage;
	}

}
