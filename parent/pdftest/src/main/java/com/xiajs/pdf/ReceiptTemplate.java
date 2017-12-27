package com.xiajs.pdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class ReceiptTemplate {

	Color lineColor = new Color(178, 34, 34); //收据画线颜色
	float lineWidth = 0.01f;
	
	PDPageContentStream contents;
	PDFont font;
	
	public ReceiptTemplate(PDPageContentStream pcs){
		contents = pcs;
	}

	public PDFont getFont() {
		return font;
	}

	public void setFont(PDFont font) {
		this.font = font;
	}

	/**
	 * @throws IOException
	 */
	public void draw() throws IOException{

		int posX = 20;
		int posY = 45;
		int width = 540;
		int height = 220;
		drawRectangle( posX, posY, width, height);
		drawLine( posX, posY+45, posX+width, posY+45);
		drawLine( posX, posY+height-45, posX+width, posY+height-45);
		drawLine( posX+width/2, posY+height, posX+width/2, posY+height-45);
		//drawRectangle( 20, 250, 540, 65);
		//drawRectangle( 20, 45, 540, 50);
		//drawRectangle( 20, 95, 540, 20);
		
		drawText( 100, 340, "中国移动通信集团浙江有限公司收据", font, 24f);
		drawText( 270, 300, "收据联", font, 14f);
		drawText( 350, 290, "收据代码", font, 14f);
		drawText( 350, 310, "收据号码", font, 14f);
		

		drawText( 30, 270, "开票日期：", font, 12f);
		drawText( 30, 240, "客户名称：", font, 12f);
		drawText( 320, 240, "客户号码：", font, 12f);

		drawText( 130, 200, "项目", font, 12f);
		drawText( 330, 200, "金额（元）", font, 12f);

		drawText( 45, 65, "合计人民币（大写）：", font, 12f);

		drawText( 25, 25, "操作员：", font, 12f);
		drawText( 105, 25, "收款员：", font, 12f);
		drawText( 205, 25, "地址：", font, 12f);
		drawText( 430, 25, "收款单位：", font, 12f);
		
//		drawTextV( 30, 80, "销售方", font, 12f);
	}
	
	void drawLine(int srcPosX, int srcPosY, int destPosX, int destPosY) throws IOException{
		contents.setStrokingColor(lineColor);
		contents.setLineWidth(lineWidth);
		contents.moveTo(srcPosX, srcPosY);
		contents.lineTo(destPosX, destPosY);
		contents.stroke();
	}
	

	/**
	 * 画矩形
	 * @param posX
	 * @param posY
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	void drawRectangle(int posX, int posY, int width, int height)
			throws IOException {

		// contents.setStrokingColor(Color.RED);
		contents.setStrokingColor(lineColor);
		contents.setLineWidth(lineWidth);
		contents.moveTo(posX, posY);
		contents.lineTo(posX + width, posY);
		contents.lineTo(posX + width, posY + height);
		contents.lineTo(posX, posY + height);
		contents.lineTo(posX, posY);
		contents.stroke();

	}

	/**
	 * 横向书写文字
	 * @param posX
	 * @param posY
	 * @param message
	 * @param font
	 * @param fontsize
	 * @throws IOException
	 */
	void drawText(int posX, int posY, String message, PDFont font, float fontsize)
			throws IOException {
		contents.setNonStrokingColor(lineColor);
		contents.beginText();
		contents.setFont(font, fontsize);
		contents.newLineAtOffset(posX, posY);
		contents.showText(message);
		contents.endText();
	}

	/**
	 * 纵向书写文字
	 * @param posX
	 * @param posY
	 * @param message
	 * @param font
	 * @param fontsize
	 * @throws IOException
	 */
	void drawTextV(int posX, int posY, String message, PDFont font, float fontsize)
			throws IOException {
		contents.setNonStrokingColor(lineColor);
		contents.beginText();
		contents.setFont(font, fontsize);
		contents.newLineAtOffset(posX, posY);
		contents.setLeading(15);
		String[] ss = message.split("");
		for (String s : ss) {
			contents.showText(s);
			contents.newLine();
		}
		contents.endText();
	}
}
