package com.fb.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OfferSheetServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1936161014701326941L;

	protected static Logger logger = Logger.getLogger(OfferSheetServlet.class);

	private static DecimalFormat df = new DecimalFormat("#0.00");
	private static final int maxDetailLength = SheetUtil.getSheetRowCount();
	
	private static Font lawFont;	//法條
	private static Font bottomFont;	//蓋章
	private static float lawFixedHeight;
	private static float bottomFixedHeight;
	
	public void init() throws ServletException {
		try {
			lawFont		= SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize() - 2, true);
			bottomFont	= SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize() - 2, true);
			lawFixedHeight = SheetUtil.getSheetRowFixedHeight() + 4f;
			bottomFixedHeight = SheetUtil.getSheetRowFixedHeight() - 2f;
		} catch (FamilyBizException e) {
			logger.error("Servlet init fail: " + e.getMessage(), e);
		}
		super.init();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			int masterId = Integer.parseInt(request.getParameter("id"));
			OfferService service = (OfferService)SheetUtil.getServiceFactory().getService("Offer");
			OfferMasterVO offer = service.getOffer(masterId);
			
			if (offer == null) {
				throw new FamilyBizException("查無此出貨單");
			}
			
			Map<String, OfferMasterVO> paramMap = new HashMap<String, OfferMasterVO>();
			paramMap.put("offer", offer);
			ByteArrayOutputStream baos = generatePDFDocumentBytes(paramMap);

			StringBuffer sbFilename = new StringBuffer();
			sbFilename.append("offer");
			sbFilename.append(masterId);
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
			logger.info("OfferSheet pdf: " + sbFilename.toString());
			
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
	        
	protected ByteArrayOutputStream generatePDFDocumentBytes(Map<String, OfferMasterVO> paramMap) throws FamilyBizException, DocumentException {
		Document document = new Document(PageSize.getRectangle(SheetUtil.getSheetPageWidth() + " " + SheetUtil.getSheetPageHeight()), SheetUtil.getSheetMarginLeft(), SheetUtil.getSheetMarginRight(), SheetUtil.getSheetMarginTop(), SheetUtil.getSheetMarginBottom());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
		PdfWriter.getInstance(document, baos);
		
		OfferMasterVO offer = (OfferMasterVO)paramMap.get("offer");
		float width[] = {1.8f, 1.4f, 1.8f};
		
		double rowCount = (double)offer.getDetails().size();
		int pageTotalCount = new Double(Math.ceil(rowCount / (double)maxDetailLength)).intValue();
		
		document.open();
		for(int page = 0; page < pageTotalCount; page++) {
			document.add(SheetUtil.buildTitleTable("出    貨    單", width));
			document.add(buildHeaderTable(offer));
			document.add(buildDetailTable(offer.getDetails(), page));
			document.add(buildFooterTable(offer, page, pageTotalCount));
			document.add(buildBottomTable(offer));
		}
		document.close();

		return baos;
	}
	
	private PdfPTable buildHeaderTable(OfferMasterVO offer) throws FamilyBizException {
		float width[] = {1f, 3f, 1f, 2f, 1f, 2f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell th1 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, true, false);
		th1.setPhrase(new Phrase("客戶名稱", SheetUtil.getTHFont()));
		table.addCell(th1);
		PdfPCell td1 = SheetUtil.getTableCell(true, false, false, false);
		td1.setPhrase(new Phrase(offer.getCust().getCustNme(), SheetUtil.getTDFont()));
		table.addCell(td1);
		PdfPCell th2 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, false, false);
		th2.setPhrase(new Phrase("客戶編號", SheetUtil.getTHFont()));
		table.addCell(th2);
		PdfPCell td2 = SheetUtil.getTableCell(true, false, false, false);
		td2.setPhrase(new Phrase(offer.getCust().getCustId().toString(), SheetUtil.getTDFont()));
		table.addCell(td2);
		PdfPCell th3 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, false, false);
		th3.setPhrase(new Phrase("單據日期", SheetUtil.getTHFont()));
		table.addCell(th3);
		PdfPCell td3 = SheetUtil.getTableCell(true, false, false, true);
		td3.setPhrase(new Phrase(DateUtil.getDateString(offer.getOfferDate()), SheetUtil.getTDFont()));
		table.addCell(td3);
		
		PdfPCell th4 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, false, true, false);
		th4.setPhrase(new Phrase("統一編號", SheetUtil.getTHFont()));
		table.addCell(th4);
		PdfPCell td4 = SheetUtil.getTableCell(false, false, false, false);
		td4.setPhrase(new Phrase(offer.getCust().getBizNo(), SheetUtil.getTDFont()));
		table.addCell(td4);
		PdfPCell th5 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, false, false, false);
		th5.setPhrase(new Phrase("聯絡電話", SheetUtil.getTHFont()));
		table.addCell(th5);
		PdfPCell td5 = SheetUtil.getTableCell(false, false, false, true, 3, 1);
		td5.setPhrase(new Phrase(offer.getCust().getTel(), SheetUtil.getTDFont()));
		table.addCell(td5);

		PdfPCell th6 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, true, false);
		th6.setPhrase(new Phrase("送貨地址", SheetUtil.getTHFont()));
		table.addCell(th6);
		PdfPCell td6 = SheetUtil.getTableCell(false, true, false, false, 3, 1);
		td6.setPhrase(new Phrase(offer.getCust().getDeliverAddr(), SheetUtil.getTDFont()));
		table.addCell(td6);
		PdfPCell th7 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, false);
		th7.setPhrase(new Phrase("單據號碼", SheetUtil.getTHFont()));
		table.addCell(th7);
		PdfPCell td7 = SheetUtil.getTableCell(false, true, false, true);
		td7.setPhrase(new Phrase(offer.getId(), SheetUtil.getTDFont()));
		table.addCell(td7);
		
//		thCell.setPhrase(new Phrase("備註", SheetUtil.getTHFont()));
//		table.addCell(thCell);
//		tdCell2.setPhrase(new Phrase(offer.getCust().getMemo(), SheetUtil.getTDFont()));
//		table.addCell(tdCell2);
//		thCell.setPhrase(new Phrase("發票號碼", SheetUtil.getTHFont()));
//		table.addCell(thCell);
//		tdCell1.setPhrase(new Phrase(offer.getInvoiceNbr(), SheetUtil.getTDFont()));
//		table.addCell(tdCell1);

		return table;
	}

	private PdfPTable buildDetailTable(List<OfferDetailVO> details, int page) throws FamilyBizException {
		float width[] = {4.5f, 1f, 0.5f, 1.5f, 1.5f, 1f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell th1 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, true, true);
		th1.setPhrase(new Phrase("品名/規格", SheetUtil.getTHFont()));
		table.addCell(th1);
		PdfPCell th2 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th2.setPhrase(new Phrase("數量", SheetUtil.getTHFont()));
		table.addCell(th2);
		PdfPCell th3 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th3.setPhrase(new Phrase("單位", SheetUtil.getTHFont()));
		table.addCell(th3);
		PdfPCell th4 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th4.setPhrase(new Phrase("單價", SheetUtil.getTHFont()));
		table.addCell(th4);
		PdfPCell th5 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th5.setPhrase(new Phrase("金額", SheetUtil.getTHFont()));
		table.addCell(th5);
		PdfPCell th6 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th6.setPhrase(new Phrase("備註", SheetUtil.getTHFont()));
		table.addCell(th6);

		int startIdx	= page * maxDetailLength;
		int endIdx		= startIdx + maxDetailLength;
		
		for(int i = startIdx; i < endIdx; i++) {
			OfferDetailVO detail = null;
			if (i < details.size()) detail = (OfferDetailVO) details.get(i);
			
			if (detail != null) {
				PdfPCell td1 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, false, true, true);
				td1.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td1.setPhrase(new Phrase(detail.getProd().getProdNme(), SheetUtil.getDetailFont()));
				table.addCell(td1);
				PdfPCell td2 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td2.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td2.setPhrase(new Phrase(detail.getQty().toString(), SheetUtil.getDetailFont()));
				table.addCell(td2);
				PdfPCell td3 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td3.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td3.setPhrase(new Phrase(detail.getProd().getUnit(), SheetUtil.getDetailFont()));
				table.addCell(td3);
				PdfPCell td4 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td4.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td4.setPhrase(new Phrase(df.format(detail.getAmt() / detail.getQty()), SheetUtil.getDetailFont()));
				table.addCell(td4);
				PdfPCell td5 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td5.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td5.setPhrase(new Phrase(df.format(detail.getAmt()), SheetUtil.getDetailFont()));
				table.addCell(td5);
				PdfPCell td6 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, false, false, true);
				td6.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td6.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td6);
			} else {
				PdfPCell td1 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, false, true, true);
				td1.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td1.setPhrase(new Phrase((i == details.size())? "<以下空白>": " ", SheetUtil.getDetailFont()));
				table.addCell(td1);
				PdfPCell td2 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td2.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td2.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td2);
				PdfPCell td3 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td3.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td3.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td3);
				PdfPCell td4 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td4.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td4.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td4);
				PdfPCell td5 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
				td5.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td5.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td5);
				PdfPCell td6 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, false, false, true);
				td6.setFixedHeight(SheetUtil.getSheetDetailRowHeight());
				td6.setPhrase(new Phrase("", SheetUtil.getDetailFont()));
				table.addCell(td6);
			}
		}

		return table;
	}
	
	private PdfPTable buildFooterTable(OfferMasterVO offer, int page, int pageTotalCount) throws FamilyBizException {
		boolean displayMoneyFlag = (page + 1) == pageTotalCount;
		float width[] = {2f, 3f, 2f, 3f, 3f};
		PdfPTable table = SheetUtil.getTableInstance(width);
		
		PdfPCell th1 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, true, true);
		th1.setPhrase(new Phrase("折讓", SheetUtil.getTHFont()));
		table.addCell(th1);
		PdfPCell td1 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, true, false, false, true);
		td1.setPhrase(new Phrase((displayMoneyFlag)? df.format(offer.getDiscount()): "", SheetUtil.getTDFont()));
		table.addCell(td1);
		PdfPCell th2 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, false, true);
		th2.setPhrase(new Phrase("合計", SheetUtil.getTHFont()));
		table.addCell(th2);
		PdfPCell td2 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, true, false, false, false);
		td2.setPhrase(new Phrase((displayMoneyFlag)? df.format(offer.getAmt()): "", SheetUtil.getTDFont()));
		table.addCell(td2);
		PdfPCell th3 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, true, false, true, true);
		th3.setPhrase(new Phrase("客戶簽收", SheetUtil.getTHFont()));
		table.addCell(th3);

		PdfPCell th4 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, false, true, true);
		th4.setPhrase(new Phrase("己清款", SheetUtil.getTHFont()));
		table.addCell(th4);
		PdfPCell td3 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, true);
		td3.setPhrase(new Phrase("", SheetUtil.getTDFont()));
		table.addCell(td3);
		PdfPCell th5 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, false, false, true);
		th5.setPhrase(new Phrase("營業稅", SheetUtil.getTHFont()));
		table.addCell(th5);
		PdfPCell td4 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, false, false, false);
		td4.setPhrase(new Phrase("", SheetUtil.getTDFont()));
		table.addCell(td4);
		PdfPCell td5 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, PdfPCell.ALIGN_BOTTOM, false, true, true, true, 1, 4);
		td5.setPhrase(new Phrase("第" + (page + 1) + "/" + pageTotalCount + "頁", SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize() - 2, false)));
		table.addCell(td5);

		PdfPCell th6 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, true, true);
		th6.setPhrase(new Phrase("未清款", SheetUtil.getTHFont()));
		table.addCell(th6);
		PdfPCell td6 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, true, false, true);
		td6.setPhrase(new Phrase("", SheetUtil.getTDFont()));
		table.addCell(td6);
		PdfPCell th7 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, false, true, false, true);
		th7.setPhrase(new Phrase("總計", SheetUtil.getTHFont()));
		table.addCell(th7);
		PdfPCell td7 = SheetUtil.getTableCell(PdfPCell.ALIGN_RIGHT, false, true, false, false);
		td7.setPhrase(new Phrase((displayMoneyFlag)? df.format(offer.getTotal()): "", SheetUtil.getTDFont()));
		table.addCell(td7);

		PdfPCell th8 = SheetUtil.getTableCell(PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, false, true, true, true);
		th8.setFixedHeight(SheetUtil.getSheetRowFixedHeight() * 1.5f);
		th8.setPhrase(new Phrase("貨單附註", SheetUtil.getTHFont()));
		table.addCell(th8);
		PdfPCell td8 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, true, false, false, 3, 1);
		td8.setPhrase(new Phrase(offer.getMemo(), SheetUtil.getDetailFont()));
		table.addCell(td8);

		PdfPCell td9 = SheetUtil.getTableCell(PdfPCell.ALIGN_LEFT, false, true, true, false, 4, 1);
		td9.setFixedHeight(lawFixedHeight);
		td9.setPhrase(new Phrase("本交易依動產擔保交易法之規定為附條件買賣，在貨款未付清或票據未兌現前買受人無條件同意所有權歸賣方並無異議同意賣方得隨時不經任何法律程序取商品或代物清償，買方且放棄先訴抗辯權。", lawFont));
		table.addCell(td9);

		return table;
	}

	private PdfPTable buildBottomTable(OfferMasterVO offer) throws FamilyBizException {
		float width[] = {0.5f, 1.5f, 0.5f, 1.5f, 0.5f, 1.5f, 0.5f, 1.5f, 0.5f, 1.5f};
		PdfPTable table = SheetUtil.getTableInstance(width);

		PdfPCell td1 = SheetUtil.getTableCell(false, false, false, false);
		td1.setFixedHeight(bottomFixedHeight);
		td1.setPhrase(new Phrase("主管：", bottomFont));
		table.addCell(td1);
		PdfPCell td11 = SheetUtil.getTableCell(false, false, false, false);
		td11.setFixedHeight(bottomFixedHeight);
		td11.setPhrase(new Phrase("", SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false)));
		table.addCell(td11);

		PdfPCell td2 = SheetUtil.getTableCell(false, false, false, false);
		td2.setFixedHeight(bottomFixedHeight);
		td2.setPhrase(new Phrase("業務：", bottomFont));
		table.addCell(td2);
		PdfPCell td21 = SheetUtil.getTableCell(false, false, false, false);
		td21.setFixedHeight(bottomFixedHeight);
		td21.setPhrase(new Phrase("", SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false)));
		table.addCell(td21);

		PdfPCell td3 = SheetUtil.getTableCell(false, false, false, false);
		td3.setFixedHeight(bottomFixedHeight);
		td3.setPhrase(new Phrase("會計：", bottomFont));
		table.addCell(td3);
		PdfPCell td31 = SheetUtil.getTableCell(false, false, false, false);
		td31.setFixedHeight(bottomFixedHeight);
		td31.setPhrase(new Phrase("", SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false)));
		table.addCell(td31);

		PdfPCell td4 = SheetUtil.getTableCell(false, false, false, false);
		td4.setFixedHeight(bottomFixedHeight);
		td4.setPhrase(new Phrase("倉管：", bottomFont));
		table.addCell(td4);
		PdfPCell td41 = SheetUtil.getTableCell(false, false, false, false);
		td41.setFixedHeight(bottomFixedHeight);
		td41.setPhrase(new Phrase("", SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false)));
		table.addCell(td41);

		PdfPCell td5 = SheetUtil.getTableCell(false, false, false, false);
		td5.setFixedHeight(bottomFixedHeight);
		td5.setPhrase(new Phrase("司機：", bottomFont));
		table.addCell(td5);
		PdfPCell td51 = SheetUtil.getTableCell(false, false, false, false);
		td51.setFixedHeight(bottomFixedHeight);
		td51.setPhrase(new Phrase(offer.getDeliveryUser().getUserNme(), SheetUtil.getCustomizeFont(SheetUtil.getSheetBaseFontSize(), false)));
		table.addCell(td51);

		return table;
	}
}
