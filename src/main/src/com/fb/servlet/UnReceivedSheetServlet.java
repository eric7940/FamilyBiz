package com.fb.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fb.service.OfferService;
import com.fb.util.CommonUtil;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.util.SheetUtil;
import com.fb.vo.CustProfVO;
import com.fb.vo.OfferMasterVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.PdfWriter;

public class UnReceivedSheetServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1936161014701326941L;

	protected static Logger logger = Logger.getLogger(UnReceivedSheetServlet.class);

	private static DecimalFormat df = new DecimalFormat("#0.00");
	private static Font font3;		//累計應收帳款
	private static int rowSize = 16;
	
	public void init() throws ServletException {
		try {
			font3 = SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false, Font.BOLD);
		} catch (FamilyBizException e) {
			logger.error("Servlet init fail: " + e.getMessage(), e);
		}
		super.init();
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try {
			String ids = request.getParameter("id");
			List<String> masterIds = CommonUtil.convertStringToList(ids.substring(1), ",");
			
			List<String> beforeIds = new ArrayList<String>();
			String bids = request.getParameter("before");
			if (bids.length() > 0) {
				beforeIds = CommonUtil.convertStringToList(bids.substring(1), ",");	
			}

			OfferService service = (OfferService)SheetUtil.getServiceFactory().getService("Offer");
			List<OfferMasterVO> unreceivedOffers = service.getOffers(masterIds, false);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("list", unreceivedOffers);
			paramMap.put("before", beforeIds);
			ByteArrayOutputStream baos = generatePDFDocumentBytes(paramMap);

			StringBuffer sbFilename = new StringBuffer();
			sbFilename.append("unreceived");
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
	protected ByteArrayOutputStream generatePDFDocumentBytes(Map paramMap) throws FamilyBizException, DocumentException {
		Document document = new Document(PageSize.getRectangle(SheetUtil.getSheetPageWidth() + " " + SheetUtil.getSheetPageHeight()), SheetUtil.getSheetMarginLeft(), SheetUtil.getSheetMarginRight(), SheetUtil.getSheetMarginTop(), SheetUtil.getSheetMarginBottom());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
		PdfWriter.getInstance(document, baos);

		Date today = new Date();
		float width[] = {1.8f, 1.4f, 1.8f};
		
		document.open();
		boolean isBorder = SheetUtil.isSheetBorder();
		(new SheetUtil()).setSheetBorder(true);
		
		List unreceivedOffers = (List) paramMap.get("list");
		List beforeIds = (List)paramMap.get("before");
		
		List custs = groupByCust(unreceivedOffers);
		int pageSize = getPageSize(custs);
		
		Iterator it = custs.iterator();
		
		int pageIdx = 0;
		while(it.hasNext()) {
			Map m = (Map) it.next();
			CustProfVO cust = (CustProfVO) m.get("cust");
			List list = (List) m.get("offers");
			
			if (cust == null || list == null || list.size() == 0) continue;

			int custPageCount = (int) Math.ceil(Integer.valueOf(list.size()).doubleValue() / Integer.valueOf(rowSize).doubleValue());

			double beforeTotalCount = 0;
			double beforeReceivedAmtCount = 0;
			double thisTotalCount = 0;
			double thisReceivedAmtCount = 0;

			for(int custPageIdx = 1; custPageIdx <= custPageCount; custPageIdx++) {
				pageIdx++;
				if (custPageIdx > 1) document.newPage();
	
				document.add(SheetUtil.buildTitleTable("應收帳款對帳單", width));
				document.add(buildPrintTable(today, pageIdx, pageSize));
				document.add(buildCustTable(cust));
	
				int fromIdx = (custPageIdx - 1) * rowSize;
				int toIdx = fromIdx + rowSize;
				if (toIdx > list.size()) toIdx = list.size();
				
				Object[] o = buildDetailTable(list.subList(fromIdx, toIdx), beforeIds);
				beforeTotalCount += ((Double)o[1]).doubleValue();
				beforeReceivedAmtCount += ((Double)o[2]).doubleValue();
				thisTotalCount += ((Double)o[3]).doubleValue();
				thisReceivedAmtCount += ((Double)o[4]).doubleValue();
				
				document.add((PdfPTable)o[0]);
			}
			document.add(buildFooterTable(beforeTotalCount, beforeReceivedAmtCount, thisTotalCount, thisReceivedAmtCount));
		}

		(new SheetUtil()).setSheetBorder(isBorder);
		document.close();

		return baos;
	}

	@SuppressWarnings("unchecked")
	private List<Map> groupByCust(List<OfferMasterVO> offers) {
		List<Map> result = new ArrayList();
		Iterator<OfferMasterVO> it = offers.iterator();
		while(it.hasNext()) {
			OfferMasterVO offer = it.next();
			CustProfVO cust = offer.getCust();
			
			int idx = -1;
			for(int i = 0; i < result.size(); i++) {
				Map m = (Map)result.get(i);
				if (cust.getCustId().equals(m.get("custId"))) {
					idx = i;
					break;
				}
			}
			if (idx == -1) {
				Map m = new HashMap();
				List l = new ArrayList();
				l.add(offer);
				m.put("custId", cust.getCustId());
				m.put("cust", cust);
				m.put("offers", l);
				result.add(m);
			} else {
				Map m = (Map)result.get(idx);
				List l = (List)m.get("offers");
				l.add(offer);
				m.put("offers", l);
			}

		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private int getPageSize(List l) {
		int result = l.size();
		Iterator<Map> it = l.iterator();
		while(it.hasNext()) {
			Map m = (Map) it.next();
			List offers = (List)m.get("offers");
			if (offers.size() > rowSize) {
				result--;
				result += (int) Math.ceil(Integer.valueOf(offers.size()).doubleValue() / Integer.valueOf(rowSize).doubleValue());
			}
		}
		
		return result;
		
	}
	
	private PdfPTable buildCustTable(CustProfVO cust) throws FamilyBizException {
		float width[] = {1.2f, 4.8f, 1.2f, 2.8f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell thCell = new PdfPCell();
		thCell.setBorder(PdfPCell.NO_BORDER);
		
		PdfPCell tdCell1 = new PdfPCell();
		tdCell1.setBorder(PdfPCell.NO_BORDER);

		PdfPCell tdCell2 = new PdfPCell();
		tdCell2.setColspan(3);
		tdCell2.setBorder(PdfPCell.NO_BORDER);
		
		
		thCell.setPhrase(new Phrase("客戶名稱：", SheetUtil.getTHFont()));
		table.addCell(thCell);
		tdCell2.setPhrase(new Phrase(cust.getCustNme(), SheetUtil.getTDFont()));
		table.addCell(tdCell2);
		
		thCell.setPhrase(new Phrase("聯絡電話：", SheetUtil.getTHFont()));
		table.addCell(thCell);
		tdCell1.setPhrase(new Phrase(cust.getTel(), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		thCell.setPhrase(new Phrase("統一編號：", SheetUtil.getTHFont()));
		table.addCell(thCell);
		tdCell1.setPhrase(new Phrase(cust.getBizNo(), SheetUtil.getTDFont()));
		table.addCell(tdCell1);

		thCell.setPhrase(new Phrase("地　　址：", SheetUtil.getTHFont()));
		table.addCell(thCell);
		tdCell2.setPhrase(new Phrase(cust.getDeliverAddr(), SheetUtil.getTDFont()));
		table.addCell(tdCell2);

		return table;
	}

	private PdfPTable buildPrintTable(Date date, int pageIdx, int pageSize) throws FamilyBizException {
		float width[] = {5f, 5f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell cell1 = new PdfPCell();
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

		PdfPCell cell2 = new PdfPCell();
		cell2.setBorder(PdfPCell.NO_BORDER);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		
		cell1.setPhrase(new Phrase("列印日期：" + DateUtil.getDateString(date), SheetUtil.getTitleFont3()));
		table.addCell(cell1);

		cell2.setPhrase(new Phrase("頁次：" + pageIdx + "/" + pageSize, SheetUtil.getTitleFont3()));
		table.addCell(cell2);

		return table;
	}

	private Object[] buildDetailTable(List<OfferMasterVO> details, List<String> beforeIds) throws FamilyBizException {
		float width[] = {0.6f, 1.3f, 1.4f, 1.8f, 1.3f, 1.8f, 1.8f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell thCell = new PdfPCell();
		thCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		PdfPCell tdCell1 = new PdfPCell();
		tdCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tdCell1.setBorder(PdfPCell.NO_BORDER);

		PdfPCell tdCell2 = new PdfPCell();
		tdCell2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tdCell2.setBorder(PdfPCell.NO_BORDER);

		PdfPCell tdCell3 = new PdfPCell();
		tdCell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tdCell3.setBorder(PdfPCell.NO_BORDER);

		thCell.setPhrase(new Phrase("序號", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("日期", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("帳款單號", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("未稅金額", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("稅金", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("帳款總額", SheetUtil.getTHFont()));
		table.addCell(thCell);
		thCell.setPhrase(new Phrase("己沖帳金額", SheetUtil.getTHFont()));
		table.addCell(thCell);

		double beforeTotalCount = 0;
		double beforeReceivedAmtCount = 0;
		double thisTotalCount = 0;
		double thisReceivedAmtCount = 0;
		for(int i = 0; i < 16; i++) {
			if (i < details.size()) {
				OfferMasterVO offer = (OfferMasterVO) details.get(i);
				tdCell1.setPhrase(new Phrase(String.valueOf(i + 1), SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell1.setPhrase(new Phrase(DateUtil.getDateString(offer.getOfferDate()), SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell1.setPhrase(new Phrase(offer.getId(), SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell3.setPhrase(new Phrase(df.format(offer.getTotal()), SheetUtil.getTDFont()));
				table.addCell(tdCell3);
				tdCell3.setPhrase(new Phrase(df.format(0), SheetUtil.getTDFont()));
				table.addCell(tdCell3);
				tdCell3.setPhrase(new Phrase(df.format(offer.getTotal()), SheetUtil.getTDFont()));
				table.addCell(tdCell3);
				tdCell3.setPhrase(new Phrase(df.format(offer.getReceiveAmt()), SheetUtil.getTDFont()));
				table.addCell(tdCell3);
				
				if (beforeIds.contains(offer.getId())) {
					beforeTotalCount += offer.getTotal();
					beforeReceivedAmtCount += offer.getReceiveAmt();
				} else {
					thisTotalCount += offer.getTotal();
					thisReceivedAmtCount += offer.getReceiveAmt();
				}
			} else {
				tdCell1.setPhrase(new Phrase(" ", SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell1.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell1.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell1);
				tdCell2.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell2);
				tdCell2.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell2);
				tdCell2.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell2);
				tdCell2.setPhrase(new Phrase("", SheetUtil.getTDFont()));
				table.addCell(tdCell2);
			}
		}

		Object[] result = {
					table, 
					Double.valueOf(beforeTotalCount), 
					Double.valueOf(beforeReceivedAmtCount), 
					Double.valueOf(thisTotalCount), 
					Double.valueOf(thisReceivedAmtCount)};
		return result;
	}
	
	private PdfPTable buildFooterTable(double beforeTotal, double beforeReceivedAmt, double thisTotal, double thisReceivedAmt) throws FamilyBizException  {
		float width[] = {0.8f, 1.7f, 0.8f, 1f, 1.25f, 1.6f, 1.25f, 1.6f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell thCell1 = new PdfPCell();
		thCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		PdfPCell tdCell1 = new PdfPCell();
		tdCell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

		PdfPCell tdCell2 = new PdfPCell();
		tdCell2.setColspan(3);
		tdCell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

		thCell1.setPhrase(new Phrase("本期未稅", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell1.setPhrase(new Phrase(df.format(thisTotal), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		thCell1.setPhrase(new Phrase("本期稅金", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell1.setPhrase(new Phrase(df.format(0), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		thCell1.setPhrase(new Phrase("本期帳款合計", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell1.setPhrase(new Phrase(df.format(thisTotal), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		thCell1.setPhrase(new Phrase("本期沖帳合計", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell1.setPhrase(new Phrase(df.format(thisReceivedAmt), SheetUtil.getTDFont()));
		table.addCell(tdCell1);
		
		thCell1.setPhrase(new Phrase("前期尚欠", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell2.setPhrase(new Phrase(df.format(beforeTotal - beforeReceivedAmt), SheetUtil.getTDFont()));
		table.addCell(tdCell2);
		thCell1.setPhrase(new Phrase("累計應收帳款", SheetUtil.getTHFont()));
		table.addCell(thCell1);
		tdCell2.setPhrase(new Phrase(df.format(thisTotal - thisReceivedAmt + beforeTotal - beforeReceivedAmt), font3));
		table.addCell(tdCell2);

		return table;
	}

}
