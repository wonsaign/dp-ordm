package com.zeusas.common.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.zeusas.common.utils.ImageInfo;
import com.zeusas.common.utils.ImageUtils;

public class UploadSave {

	/*
	 * final static String path0 = "C:\\Users\\lin\\Desktop\\新品 (2)\\新品"; final
	 * static String path1 = "C:\\Users\\lin\\Desktop\\新品02\\ Z:\\产品图片\\ Z:\\已编号\\ Z:\\物料汇总\\";
	 */
	final static String src = "E:\\tmp1\\";
	final static String dst = "E:\\tmp2\\";
	final static String err = "E:\\tmp3\\";

	public static void main(String[] args) {
		File picDir2 = new File(src);
		File src_child[] = picDir2.listFiles();
		try {
			for (File x : src_child) {
				dir(x);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void dir(File f) {
		if (f.isDirectory()) {
			File ff[] = f.listFiles();
			for (File x : ff) {
				dir(x);
			}
		} else {
			String fname = f.getName().toLowerCase();
			if (((fname.endsWith("pg") || fname.endsWith("ng") || fname.endsWith("tif")))
					&& (fname.charAt(0) >= '0' && fname.charAt(0) <= '9')) {
				System.err.println("处理图：" + f.getAbsolutePath());
				proc(f);
			} else {
				System.err.println("不处理：" + f.getAbsolutePath());
			}
		}
	}

	static void proc(File f) {
		byte b[] = new byte[(int) f.length()];
		InputStream is;
		try {
			is = new FileInputStream(f);
			is.read(b);
			is.close();

			ByteArrayInputStream byteIn = new ByteArrayInputStream(b);
			try {
				BufferedImage bi = ImageIO.read(byteIn);
				BufferedImage bi800 = ImageUtils.resizeImage(bi, BufferedImage.TYPE_3BYTE_BGR, 800, 800);

				String nm = f.getName().toLowerCase();
				if (!nm.endsWith("jpg")) {
					nm += ".jpg";
				}
				File fdest = new File(dst, nm);
				ImageUtils.saveImage(bi800, fdest.getAbsolutePath(), ImageUtils.IMAGE_JPEG);
			} catch (Exception e) {
				System.err.println(e.getMessage() + "\n" + f.getName());
				writeTo(err + f.getName(), b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void writeTo(String name, byte b[]) {
		FileOutputStream out;
		try {
			System.err.println("SAVE TO: " + name);
			out = new FileOutputStream(name);
			out.write(b);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
