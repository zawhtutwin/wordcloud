package com.zhw.wordcloud;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.CharSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

@Controller
@RequestMapping("/")
public class StepsController {
	@RequestMapping(value = "step1", method = RequestMethod.GET)
	public String getStep1Page(){
		return "step1";
	}
	@RequestMapping(value = "step2", method = RequestMethod.POST)
	public String getStep2Page(HttpServletRequest req){
		String inputText = req.getParameter("inputText");
		System.out.println(inputText);
		File newFile = new File(req.getServletContext().getRealPath("/resources")+"//inputText.txt");
		
		try{
			FileWriterWithEncoding out = new FileWriterWithEncoding(newFile,"UTF-8");

				String text = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(inputText);
				out.write(text);
				
				out.flush();
				out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		createWordCloudImage(req,newFile);
		return "step1";
	}
	private void createWordCloudImage(HttpServletRequest req, File file){
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setWordFrequenciesToReturn(300);
		frequencyAnalyzer.setMinWordLength(7);
	

		List<WordFrequency> wordFrequencies = null;
		try {
			wordFrequencies = frequencyAnalyzer.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Dimension dimension = new Dimension(500, 312);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		//wordCloud.setBackground(new PixelBoundryBackground("backgrounds/whale_small.png"));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setKumoFont(new KumoFont("Zawgyi-One", FontWeight.BOLD));
		wordCloud.setFontScalar(new LinearFontScalar(10, 40));
		wordCloud.build(wordFrequencies);
		String path = "";
		System.out.println(path=req.getServletContext().getRealPath("/resources"));
		
		wordCloud.writeToFile(path+"\\+pic.png");
		
	}
}
