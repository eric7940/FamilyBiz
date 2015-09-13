package com.fb.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fb.service.OfferService;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.util.SheetUtil;
import com.fb.vo.PickOfferVO;
import com.fb.vo.PickProdVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PickSheetServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1936161014701326941L;

	protected static Logger logger = Logger.getLogger(PickSheetServlet.class);

	private static int rowSize = 24;
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try {
			String offerDate = request.getParameter("offerDate");
			
			OfferService service = (OfferService)SheetUtil.getServiceFactory().getService("Offer");
			List<PickProdVO> pickProds = service.getProdQty(DateUtil.getDateObject(offerDate));
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("list", pickProds);
			ByteArrayOutputStream baos = generatePDFDocumentBytes(paramMap);

			StringBuffer sbFilename = new StringBuffer();
			sbFilename.append("pick");
			sbFilename.append("_");
			sbFilename.append(System.currentTimeMillis());
			sbFilename.append(".pdf");

			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			
			StringBuffer sbContentDispValue = new StringBuffer();
			sbContentDispValue.append("attachment;");
			sbContentDispValue.append("filename=");
			sbContentDispValue.append(sbFilename);

			response.setHeader("Content-Disposition", "inline; filename=" + sbFilename.toString());
			logger.info("UnReceivedSheet pdf: " + sbFilename.toString());
			
			ServletOutputStream out = response.getOutputStream();

			baos.writeTo(out);
			out.flush();
		
		} catch (Exception e)  {
			logger.error("", e);
			response.setContentType("text/html");
			PrintWriter writer = response.getWriter();
			writer.println(this.getClass().getName() + " caught an exception: " + e.getClass().getName() + "<br>");
			writer.println("<pre>");
			e.printStackTrace(writer);
			writer.println("</pre>");
		}

	}
	        
	@SuppressWarnings("unchecked")
	protected ByteArrayOutputStream generatePDFDocumentBytes(Map<String,Object> paramMap) throws FamilyBizException, DocumentException {
		Document document = new Document(PageSize.getRectangle(SheetUtil.getSheetPageWidth() + " " + SheetUtil.getSheetPageHeight()), SheetUtil.getSheetMarginLeft(), SheetUtil.getSheetMarginRight(), SheetUtil.getSheetMarginTop(), SheetUtil.getSheetMarginBottom());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
		PdfWriter.getInstance(document, baos);

		Date today = new Date();
		
		document.open();
		boolean isBorder = SheetUtil.isSheetBorder();
		(new SheetUtil()).setSheetBorder(true);
		
		List<PickProdVO> list = (List<PickProdVO>) paramMap.get("list");
			
		int rowCount = 0;
		boolean newPage = false;
		for (int prodIdx = 0; prodIdx < list.size(); prodIdx++) {
			PickProdVO prod = (PickProdVO) list.get(prodIdx);
			rowCount += prod.getOffers().size();
			if (prodIdx == 0 || rowCount > rowSize) {
				document.newPage();
				document.add(buildPrintTable(today));
				
				rowCount = prod.getOffers().size();
				newPage = true;
			} else {
				newPage = false;
			}
			
			document.add(buildDetailTable(prod, newPage));
		} 
		
		(new SheetUtil()).setSheetBorder(isBorder);
		document.close();

		return baos;
	}

	private PdfPTable buildPrintTable(Date date) throws FamilyBizException {
		float width[] = {1f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell cell2 = new PdfPCell();
		cell2.setBorder(PdfPCell.NO_BORDER);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		
		cell2.setPhrase(new Phrase("統計時間：" + DateUtil.getDateTimeString(date), SheetUtil.getTDFont()));
		table.addCell(cell2);

		return table;
	}

	private PdfPTable buildDetailTable(PickProdVO prod, boolean newPage) throws FamilyBizException {
		float width[] = {1.3f, 0.4f, 0.8f, 1.5f};
		PdfPTable table = SheetUtil.getTableInstance(width);
		
		PdfPCell thCell = new PdfPCell();
		thCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		thCell.setBorder(PdfPCell.NO_BORDER);

		PdfPCell tdCell1 = SheetUtil.getTableCell(false, true, false, false);
		tdCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tdCell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		PdfPCell tdCell2 = SheetUtil.getTableCell(false, true, false, false);
		tdCell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tdCell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		PdfPCell tdCell3 = SheetUtil.getTableCell(false, true, false, false);
		tdCell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tdCell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		PdfPCell tdCell4 = new PdfPCell();
		tdCell4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tdCell4.setBorder(PdfPCell.NO_BORDER);

		if (newPage) {
			thCell.setPhrase(new Phrase("品名／型號", SheetUtil.getTDFont()));
			table.addCell(thCell);
			thCell.setPhrase(new Phrase("單位", SheetUtil.getTDFont()));
			table.addCell(thCell);
			thCell.setPhrase(new Phrase("合計", SheetUtil.getTDFont()));
			table.addCell(thCell);
			thCell.setPhrase(new Phrase("明細", SheetUtil.getTDFont()));
			table.addCell(thCell);
		}
		
		tdCell2.setPhrase(new Phrase(prod.getProdNme(), SheetUtil.getTDFont()));
		table.addCell(tdCell2);
		tdCell1.setPhrase(new Phrase(prod.getUnit(), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		tdCell3.setPhrase(new Phrase(prod.getSumQty().toString(), SheetUtil.getTDFont()));
		table.addCell(tdCell3);
				
		float width2[] = {1f, 1.3f, 0.8f};
		PdfPTable table2 = SheetUtil.getTableInstance(width2);
		for (PickOfferVO offer : prod.getOffers()) {
			tdCell4.setPhrase(new Phrase(offer.getMasterId(), SheetUtil.getTDFont()));
			table2.addCell(tdCell4);
			tdCell4.setPhrase(new Phrase(offer.getCustNme(), SheetUtil.getTDFont()));
			table2.addCell(tdCell4);
			tdCell4.setPhrase(new Phrase(offer.getQty().toString(), SheetUtil.getTDFont()));
			table2.addCell(tdCell4);
		}
		PdfPCell nestCell= new PdfPCell(table2);
		nestCell.setBorderWidthTop(PdfPCell.NO_BORDER);
		nestCell.setBorderWidthLeft(PdfPCell.NO_BORDER);
		nestCell.setBorderWidthRight(PdfPCell.NO_BORDER);
		
		table.addCell(nestCell);

		return table;
	}

}
