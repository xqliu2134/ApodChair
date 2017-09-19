package com.apod.chair.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;

import com.apod.chair.bo.Word;
import com.apod.chair.manager.Config;

public class WordUtils {
	private final static String TAG = Config.TAG + ".WordUtils";
	private Range range = null;
	private HWPFDocument hwpf = null;
	private List<Picture> pictures;
	private TableIterator tableIterator;
	private int presentPicture = 0;
	private Word mWord;

	public WordUtils(Word word) {
		mWord = word;
	}

	public Word readSync() {
		File urlFile = new File(mWord.getUrl());
		// 判断当前word文档的html格式是否已经存在，如果存在就不需要在转换
		if (urlFile.exists()) {
			Log.d(TAG, "url file is exists,so return show.");
			return mWord;
		}
		String filepath = mWord.getFilepath();
		if (!urlFile.exists()) {
			if (filepath.endsWith(".doc")) {
				this.getRange();
				this.makeFile();
				this.readDOC(urlFile);
			}
			if (filepath.endsWith(".docx")) {
				this.makeFile();
				this.readDOCX(urlFile);
			}
		}
		return mWord;
	}

	public void readDOC(File urlFile) {
		try {
			FileOutputStream output = new FileOutputStream(urlFile);
			presentPicture = 0;
			String head = "<html><meta charset=\"utf-8\"><body>";
			String tagBegin = "<p>";
			String tagEnd = "</p>";
			output.write(head.getBytes());
			int numParagraphs = range.numParagraphs();
			for (int i = 0; i < numParagraphs; i++) {
				Paragraph p = range.getParagraph(i);
				if (p.isInTable()) {
					int temp = i;
					if (tableIterator.hasNext()) {
						String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
						String tableEnd = "</table>";
						String rowBegin = "<tr>";
						String rowEnd = "</tr>";
						String colBegin = "<td>";
						String colEnd = "</td>";
						Table table = tableIterator.next();
						output.write(tableBegin.getBytes());
						int rows = table.numRows();
						for (int r = 0; r < rows; r++) {
							output.write(rowBegin.getBytes());
							TableRow row = table.getRow(r);
							int cols = row.numCells();
							int rowNumParagraphs = row.numParagraphs();
							int colsNumParagraphs = 0;
							for (int c = 0; c < cols; c++) {
								output.write(colBegin.getBytes());
								TableCell cell = row.getCell(c);
								int max = temp + cell.numParagraphs();
								colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
								for (int cp = temp; cp < max; cp++) {
									Paragraph p1 = range.getParagraph(cp);
									output.write(tagBegin.getBytes());
									writeParagraphContent(output, p1);
									output.write(tagEnd.getBytes());
									temp++;
								}
								output.write(colEnd.getBytes());
							}
							int max1 = temp + rowNumParagraphs;
							for (int m = temp + colsNumParagraphs; m < max1; m++) {
								temp++;
							}
							output.write(rowEnd.getBytes());
						}
						output.write(tableEnd.getBytes());
					}
					i = temp;
				} else {
					output.write(tagBegin.getBytes());
					writeParagraphContent(output, p);
					output.write(tagEnd.getBytes());
				}
			}
			String end = "</body></html>";
			output.write(end.getBytes());
			output.close();
		} catch (Exception e) {
			System.out.println("readAndWrite Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void readDOCX(File urlFile) {
		String river = "";
		try {
			FileOutputStream output = new FileOutputStream(urlFile);
			presentPicture = 0;
			String head = "<!DOCTYPE><html><meta charset=\"utf-8\"><body>";
			String end = "</body></html>";
			String tagBegin = "<p>";
			String tagEnd = "</p>";
			String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
			String tableEnd = "</table>";
			String rowBegin = "<tr>";
			String rowEnd = "</tr>";
			String colBegin = "<td>";
			String colEnd = "</td>";
			String style = "style=\"";
			output.write(head.getBytes());
			ZipFile xlsxFile = new ZipFile(new File(mWord.getFilepath()));
			ZipEntry sharedStringXML = xlsxFile.getEntry("word/document.xml");
			InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(inputStream, "utf-8");
			int evtType = xmlParser.getEventType();
			boolean isTable = false;
			boolean isSize = false;
			boolean isColor = false;
			boolean isCenter = false;
			boolean isRight = false;
			boolean isItalic = false;
			boolean isUnderline = false;
			boolean isBold = false;
			boolean isR = false;
			boolean isStyle = false;
			int pictureIndex = 1;
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {

				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();

					if (tag.equalsIgnoreCase("r")) {
						isR = true;
					}
					if (tag.equalsIgnoreCase("u")) {
						isUnderline = true;
					}
					if (tag.equalsIgnoreCase("jc")) {
						String align = xmlParser.getAttributeValue(0);
						if (align.equals("center")) {
							output.write("<center>".getBytes());
							isCenter = true;
						}
						if (align.equals("right")) {
							output.write("<div align=\"right\">".getBytes());
							isRight = true;
						}
					}

					if (tag.equalsIgnoreCase("color")) {

						String color = xmlParser.getAttributeValue(0);

						output.write(("<span style=\"color:" + color + ";\">").getBytes());
						isColor = true;
					}
					if (tag.equalsIgnoreCase("sz")) {
						if (isR == true) {
							int size = decideSize(Integer.valueOf(xmlParser.getAttributeValue(0)));
							output.write(("<font size=" + size + ">").getBytes());
							isSize = true;
						}
					}
					if (tag.equalsIgnoreCase("tbl")) {
						output.write(tableBegin.getBytes());
						isTable = true;
					}
					if (tag.equalsIgnoreCase("tr")) {
						output.write(rowBegin.getBytes());
					}
					if (tag.equalsIgnoreCase("tc")) {
						output.write(colBegin.getBytes());
					}

					if (tag.equalsIgnoreCase("pic")) {
						String entryName_jpeg = "word/media/image" + pictureIndex + ".jpeg";
						String entryName_png = "word/media/image" + pictureIndex + ".png";
						String entryName_gif = "word/media/image" + pictureIndex + ".gif";
						String entryName_wmf = "word/media/image" + pictureIndex + ".wmf";
						ZipEntry sharePicture = null;
						InputStream pictIS = null;
						sharePicture = xlsxFile.getEntry(entryName_jpeg);
						if (sharePicture == null) {
							sharePicture = xlsxFile.getEntry(entryName_png);
						}
						if (sharePicture == null) {
							sharePicture = xlsxFile.getEntry(entryName_gif);
						}
						if (sharePicture == null) {
							sharePicture = xlsxFile.getEntry(entryName_wmf);
						}

						if (sharePicture != null) {
							pictIS = xlsxFile.getInputStream(sharePicture);
							ByteArrayOutputStream pOut = new ByteArrayOutputStream();
							byte[] bt = null;
							byte[] b = new byte[1000];
							int len = 0;
							while ((len = pictIS.read(b)) != -1) {
								pOut.write(b, 0, len);
							}
							pictIS.close();
							pOut.close();
							bt = pOut.toByteArray();
							Log.i(TAG, "" + bt);
							if (pictIS != null)
								pictIS.close();
							if (pOut != null)
								pOut.close();
							writeDOCXPicture(output, bt);
						}

						pictureIndex++;
					}

					if (tag.equalsIgnoreCase("b")) {
						isBold = true;
					}
					if (tag.equalsIgnoreCase("p")) {
						if (isTable == false) {
							output.write(tagBegin.getBytes());
						}
					}
					if (tag.equalsIgnoreCase("i")) {
						isItalic = true;
					}
					if (tag.equalsIgnoreCase("t")) {
						if (isBold == true) {
							output.write("<b>".getBytes());
						}
						if (isUnderline == true) {
							output.write("<u>".getBytes());
						}
						if (isItalic == true) {
							output.write("<i>".getBytes());
						}
						river = xmlParser.nextText();
						output.write(river.getBytes());
						if (isItalic == true) {
							output.write("</i>".getBytes());
							isItalic = false;
						}
						if (isUnderline == true) {
							output.write("</u>".getBytes());
							isUnderline = false;
						}
						if (isBold == true) {
							output.write("</b>".getBytes());
							isBold = false;
						}
						if (isSize == true) {
							output.write("</font>".getBytes());
							isSize = false;
						}
						if (isColor == true) {
							output.write("</span>".getBytes());
							isColor = false;
						}
						if (isCenter == true) {
							output.write("</center>".getBytes());
							isCenter = false;
						}
						if (isRight == true) {
							output.write("</div>".getBytes());
							isRight = false;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String tag2 = xmlParser.getName();
					if (tag2.equalsIgnoreCase("tbl")) {
						output.write(tableEnd.getBytes());
						isTable = false;
					}
					if (tag2.equalsIgnoreCase("tr")) {
						output.write(rowEnd.getBytes());
					}
					if (tag2.equalsIgnoreCase("tc")) {
						output.write(colEnd.getBytes());
					}
					if (tag2.equalsIgnoreCase("p")) {
						if (isTable == false) {
							output.write(tagEnd.getBytes());
						}
					}
					if (tag2.equalsIgnoreCase("r")) {
						isR = false;
					}
					break;
				default:
					break;
				}
				evtType = xmlParser.next();
			}
			output.write(end.getBytes());
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		if (river == null) {
			river = "error";
		}
	}

	public void makeFile() {
		try {
			File myFile = new File(mWord.getUrl());
			if (myFile.exists()) {
				myFile.delete();
			}
			myFile.createNewFile();
		} catch (Exception e) {
		}
	}

	public String makePictureFile() {
		try {
			File picDirFile = new File(mWord.getPigDir());
			if (!picDirFile.exists()) {
				picDirFile.mkdir();
			}
			String pigpath = mWord.getPigDir() + presentPicture + ".jpg";
			File pictureFile = new File(pigpath);
			if (!pictureFile.exists()) {
				pictureFile.createNewFile();
			}
			return pigpath;
		} catch (Exception e) {
			System.out.println("PictureFile Catch Exception");
		}
		return null;
	}

	public String getFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}

	}

	public void writePicture(FileOutputStream output) {
		Picture picture = (Picture) pictures.get(presentPicture);
		byte[] pictureBytes = picture.getContent();
		Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
		String picturePath = makePictureFile();
		if (picturePath == null) {
			return;
		}
		presentPicture++;
		File myPicture = new File(picturePath);
		try {
			FileOutputStream outputPicture = new FileOutputStream(myPicture);
			outputPicture.write(pictureBytes);
			outputPicture.close();
		} catch (Exception e) {
			System.out.println("outputPicture Exception");
		}

		String imageString = "<img src=\"" + picturePath + "\"";
		imageString = imageString + ">";

		try {
			output.write(imageString.getBytes());
		} catch (Exception e) {
			System.out.println("output Exception");
		}
	}

	public int decideSize(int size) {

		if (size >= 1 && size <= 8) {
			return 1;
		}
		if (size >= 9 && size <= 11) {
			return 2;
		}
		if (size >= 12 && size <= 14) {
			return 3;
		}
		if (size >= 15 && size <= 19) {
			return 4;
		}
		if (size >= 20 && size <= 29) {
			return 5;
		}
		if (size >= 30 && size <= 39) {
			return 6;
		}
		if (size >= 40) {
			return 7;
		}
		return 3;
	}

	private String decideColor(int a) {
		int color = a;
		switch (color) {
		case 1:
			return "#000000";
		case 2:
			return "#0000FF";
		case 3:
		case 4:
			return "#00FF00";
		case 5:
		case 6:
			return "#FF0000";
		case 7:
			return "#FFFF00";
		case 8:
			return "#FFFFFF";
		case 9:
			return "#CCCCCC";
		case 10:
		case 11:
			return "#00FF00";
		case 12:
			return "#080808";
		case 13:
		case 14:
			return "#FFFF00";
		case 15:
			return "#CCCCCC";
		case 16:
			return "#080808";
		default:
			return "#000000";
		}
	}

	private void getRange() {
		FileInputStream in = null;
		POIFSFileSystem pfs = null;
		try {
			in = new FileInputStream(mWord.getFilepath());
			pfs = new POIFSFileSystem(in);
			hwpf = new HWPFDocument(pfs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		range = hwpf.getRange();
		pictures = hwpf.getPicturesTable().getAllPictures();
		tableIterator = new TableIterator(range);
	}

	public void writeDOCXPicture(FileOutputStream output, byte[] pictureBytes) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
		String picturePath = makePictureFile();
		if (picturePath == null) {
			return;
		}
		this.presentPicture++;
		File myPicture = new File(picturePath);
		try {
			FileOutputStream outputPicture = new FileOutputStream(myPicture);
			outputPicture.write(pictureBytes);
			outputPicture.close();
		} catch (Exception e) {
			System.out.println("outputPicture Exception");
		}
		String imageString = "<img src=\"" + picturePath + "\"";

		imageString = imageString + ">";
		try {
			output.write(imageString.getBytes());
		} catch (Exception e) {
			System.out.println("output Exception");
		}
	}

	public void writeParagraphContent(FileOutputStream output, Paragraph paragraph) {
		Paragraph p = paragraph;
		int pnumCharacterRuns = p.numCharacterRuns();

		for (int j = 0; j < pnumCharacterRuns; j++) {

			CharacterRun run = p.getCharacterRun(j);

			if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
				if (presentPicture < pictures.size()) {
					writePicture(output);
				}
			} else {
				try {
					String text = run.text();
					if (text.length() >= 2 && pnumCharacterRuns < 2) {
						output.write(text.getBytes());
					} else {
						int size = run.getFontSize();
						int color = run.getColor();
						String fontSizeBegin = "<font size=\"" + decideSize(size) + "\">";
						String fontColorBegin = "<font color=\"" + decideColor(color) + "\">";
						String fontEnd = "</font>";
						String boldBegin = "<b>";
						String boldEnd = "</b>";
						String islaBegin = "<i>";
						String islaEnd = "</i>";

						output.write(fontSizeBegin.getBytes());
						output.write(fontColorBegin.getBytes());

						if (run.isBold()) {
							output.write(boldBegin.getBytes());
						}
						if (run.isItalic()) {
							output.write(islaBegin.getBytes());
						}

						output.write(text.getBytes());

						if (run.isBold()) {
							output.write(boldEnd.getBytes());
						}
						if (run.isItalic()) {
							output.write(islaEnd.getBytes());
						}
						output.write(fontEnd.getBytes());
						output.write(fontEnd.getBytes());
					}
				} catch (Exception e) {
					System.out.println("Write File Exception");
				}
			}
		}
	}
}
