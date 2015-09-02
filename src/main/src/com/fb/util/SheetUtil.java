package com.fb.util;

import org.apache.log4j.Logger;

import com.fb.service.ServiceFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class SheetUtil  {
	
	protected static Logger logger = Logger.getLogger(SheetUtil.class);
	
	private static ServiceFactory serviceFactory;
	
	private static float	sheetPageWidth;
	private static float	sheetPageHeight;
	private static boolean	sheetBorder;
	private static float	sheetMarginLeft;
	private static float	sheetMarginRight;
	private static float	sheetMarginTop;
	private static float	sheetMarginBottom;
	
	private static String	sheetFontFile;
	private static int		sheetBaseFontSize;
	
	private static int		sheetRowCount;
	private static float	sheetRowFixedHeight;
	
	public static ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	public void setServiceFactory(ServiceFactory serviceFactory) {
		SheetUtil.serviceFactory = serviceFactory;
	}
	public static float getSheetPageWidth() {
		return sheetPageWidth;
	}
	public void setSheetPageWidth(float sheetPageWidth) {
		SheetUtil.sheetPageWidth = sheetPageWidth;
	}
	public static float getSheetPageHeight() {
		return sheetPageHeight;
	}
	public void setSheetPageHeight(float sheetPageHeight) {
		SheetUtil.sheetPageHeight = sheetPageHeight;
	}
	public static boolean isSheetBorder() {
		return sheetBorder;
	}
	public void setSheetBorder(boolean sheetBorder) {
		SheetUtil.sheetBorder = sheetBorder;
	}
	public static float getSheetMarginLeft() {
		return sheetMarginLeft;
	}
	public void setSheetMarginLeft(float sheetMarginLeft) {
		SheetUtil.sheetMarginLeft = sheetMarginLeft;
	}
	public static float getSheetMarginRight() {
		return sheetMarginRight;
	}
	public void setSheetMarginRight(float sheetMarginRight) {
		SheetUtil.sheetMarginRight = sheetMarginRight;
	}
	public static float getSheetMarginTop() {
		return sheetMarginTop;
	}
	public void setSheetMarginTop(float sheetMarginTop) {
		SheetUtil.sheetMarginTop = sheetMarginTop;
	}
	public static float getSheetMarginBottom() {
		return sheetMarginBottom;
	}
	public void setSheetMarginBottom(float sheetMarginBottom) {
		SheetUtil.sheetMarginBottom = sheetMarginBottom;
	}
	private static String getSheetFontFile() {
		return sheetFontFile;
	}
	public void setSheetFontFile(String sheetFontFile) {
		SheetUtil.sheetFontFile = sheetFontFile;
	}
	public static int getSheetBaseFontSize() {
		return sheetBaseFontSize;
	}
	public void setSheetBaseFontSize(int sheetBaseFontSize) {
		SheetUtil.sheetBaseFontSize = sheetBaseFontSize;
	}
	public static int getSheetRowCount() {
		return sheetRowCount;
	}
	public void setSheetRowCount(int sheetRowCount) {
		SheetUtil.sheetRowCount = sheetRowCount;
	}
	public static float getSheetRowFixedHeight() {
		return sheetRowFixedHeight;
	}
	public static float getSheetDetailRowHeight() {
		return getSheetRowFixedHeight() + 2f;
	}
	public void setSheetRowFixedHeight(float sheetRowFixedHeight) {
		SheetUtil.sheetRowFixedHeight = sheetRowFixedHeight;
	}
	private static BaseFont getChineseBaseFont() throws FamilyBizException {
		return getChineseBaseFont(BaseFont.IDENTITY_H);
	}

	public static PdfPCell getTableCell(boolean top, boolean bottom, boolean left, boolean right) {
		return getTableCell(PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, top, bottom, left, right);
	}

	public static PdfPCell getTableCell(int alignH, boolean top, boolean bottom, boolean left, boolean right) {
		return getTableCell(alignH, PdfPCell.ALIGN_MIDDLE, top, bottom, left, right);
	}

	public static PdfPCell getTableCell(int alignH, int alignV, boolean top, boolean bottom, boolean left, boolean right) {
		return getTableCell(alignH, alignV, 1, 1, top, bottom, left, right);
	}

	public static PdfPCell getTableCell(int alignH, boolean top, boolean bottom, boolean left, boolean right, int colspan, int rowspan) {
		return getTableCell(alignH, PdfPCell.ALIGN_MIDDLE, colspan, rowspan, top, bottom, left, right);
	}

	public static PdfPCell getTableCell(int alignH, int alignV, boolean top, boolean bottom, boolean left, boolean right, int colspan, int rowspan) {
		return getTableCell(alignH, alignV, colspan, rowspan, top, bottom, left, right);
	}

	public static PdfPCell getTableCell(boolean top, boolean bottom, boolean left, boolean right, int colspan, int rowspan) {
		return getTableCell(PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, colspan, rowspan, top, bottom, left, right);
	}

	private static PdfPCell getTableCell(int alignH, int alignV, int colspan, int rowspan, boolean top, boolean bottom, boolean left, boolean right) {
		PdfPCell cell = new PdfPCell();
		cell.setFixedHeight(getSheetRowFixedHeight());
		
		if (alignH != PdfPCell.ALIGN_UNDEFINED) cell.setHorizontalAlignment(alignH);
		if (alignV != PdfPCell.ALIGN_UNDEFINED) cell.setVerticalAlignment(alignV);
		
		if (colspan > 1) cell.setColspan(colspan);
		if (rowspan > 1) cell.setRowspan(rowspan);
		
		if (SheetUtil.isSheetBorder() == false) {
			cell.setBorder(PdfPCell.NO_BORDER);
		} else {
			if (top		== false) cell.setBorderWidthTop(PdfPCell.NO_BORDER);
			if (bottom	== false) cell.setBorderWidthBottom(PdfPCell.NO_BORDER);
			if (left	== false) cell.setBorderWidthLeft(PdfPCell.NO_BORDER);
			if (right	== false) cell.setBorderWidthRight(PdfPCell.NO_BORDER);
		}
		return cell;
	}
	
	private static BaseFont getChineseBaseFont(String encoding) throws FamilyBizException {
		BaseFont result;
		try {
			result = BaseFont.createFont(getSheetFontFile(), encoding, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			logger.warn("can not load font ttf file: " + getSheetFontFile());
			throw new FamilyBizException("can not load font ttf file: " + getSheetFontFile(), e);
		}
		return result;
	}

	public static Font getTHFont() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		if (isSheetBorder()) {
			return new Font(chineseFont, getSheetBaseFontSize(), Font.NORMAL);
		} else {
			return new Font(chineseFont, getSheetBaseFontSize(), Font.NORMAL, BaseColor.WHITE);
		}
	}
	
	public static Font getTDFont() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		return new Font(chineseFont, getSheetBaseFontSize(), Font.NORMAL);
	}

	public static Font getDetailFont() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		return new Font(chineseFont, getSheetBaseFontSize() + 2, Font.NORMAL);
	}

	public static Font getTitleFont1() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		if (isSheetBorder()) {
			return new Font(chineseFont, getSheetBaseFontSize() + 5, Font.UNDERLINE);
		} else {
			return new Font(chineseFont, getSheetBaseFontSize() + 5, Font.NORMAL, BaseColor.WHITE);
		}
	}
	
	public static Font getTitleFont2() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		if (isSheetBorder()) {
			return new Font(chineseFont, getSheetBaseFontSize() + 3, Font.NORMAL);
		} else {
			return new Font(chineseFont, getSheetBaseFontSize() + 3, Font.NORMAL, BaseColor.WHITE);
		}
	}
	
	public static Font getTitleFont3() throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont();
		if (isSheetBorder()) {
			return new Font(chineseFont, getSheetBaseFontSize() - 1, Font.NORMAL);
		} else {
			return new Font(chineseFont, getSheetBaseFontSize() - 1, Font.NORMAL, BaseColor.WHITE);
		}
	}

	public static Font getCustomizeFont(float fontSize, boolean displayByBorder) throws FamilyBizException {
		return getCustomizeFont(fontSize, displayByBorder, Font.NORMAL, BaseFont.IDENTITY_H);
	}

	public static Font getCustomizeFont(float fontSize, boolean displayByBorder, String encoding) throws FamilyBizException {
		return getCustomizeFont(fontSize, displayByBorder, Font.NORMAL, encoding);
	}

	public static Font getCustomizeFont(float fontSize, boolean displayByBorder, int style) throws FamilyBizException {
		return getCustomizeFont(fontSize, displayByBorder, style, BaseFont.IDENTITY_H);
	}

	public static Font getCustomizeFont(float fontSize, boolean displayByBorder, int style, String encoding) throws FamilyBizException {
		BaseFont chineseFont = getChineseBaseFont(encoding);
		if (displayByBorder == true) {
			if (SheetUtil.isSheetBorder()) {
				return new Font(chineseFont, fontSize, style);
			} else {
				return new Font(chineseFont, fontSize, style, BaseColor.WHITE);
			}
		} else {
			return new Font(chineseFont, fontSize, style);
		}
	}
	
	public static PdfPTable getTableInstance(float[] width) {
		PdfPTable table = new PdfPTable(width.length);
		try {
			table.setWidths(width);
		} catch (DocumentException e) {
			logger.error("PdfPTable setWidths fail: ", e);
		}
		table.setWidthPercentage(95f);
		return table;
	}
	
	public static PdfPTable buildTitleTable(String title, float[] width) throws FamilyBizException {
		PdfPTable table = getTableInstance(width);
		
		PdfPCell titleCell1 = new PdfPCell();
		titleCell1.setFixedHeight(35f);
		titleCell1.setBorder(PdfPCell.NO_BORDER);
		titleCell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		titleCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		PdfPCell titleCell2 = new PdfPCell();
		titleCell2.setFixedHeight(35f);
		titleCell2.setBorder(PdfPCell.NO_BORDER);
		titleCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		titleCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		PdfPCell titleCell3 = new PdfPCell();
		titleCell3.setFixedHeight(35f);
		titleCell3.setBorder(PdfPCell.NO_BORDER);
		titleCell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		titleCell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

		titleCell1.addElement(new Paragraph("李連發食品行．錢豐雜糧行", getTitleFont2()));
		titleCell1.addElement(new Paragraph("台北市士林區永平街75巷9號", getTitleFont3()));
		table.addCell(titleCell1);

		titleCell2.setPhrase(new Phrase(title, getTitleFont1()));
		table.addCell(titleCell2);

		titleCell3.addElement(new Paragraph("訂貨專線：(02) 2812-2897 (代表號)", getTitleFont3()));
		titleCell3.addElement(new Paragraph("傳真：(02) 8811-3854   手機：0952-599-767", getTitleFont3()));
		table.addCell(titleCell3);

		return table;
	}
}
