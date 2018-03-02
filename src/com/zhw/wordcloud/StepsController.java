package com.zhw.wordcloud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

@ControllerAdvice
@RequestMapping("/")
public class StepsController {
	@RequestMapping(value = "step1", method = RequestMethod.GET)
	public String getStep1Page(){
		return "step1";
	}
	@RequestMapping(value = "step2", method = RequestMethod.POST)
	public String getStep2Page(@RequestParam("file") MultipartFile file,HttpServletRequest req){
		try{
			//String inputText = req.getParameter("inputText");
			//System.out.println(inputText);
	        byte[] bytes = file.getBytes();
	        Path path = Paths.get(req.getServletContext().getRealPath("/resources")+"//" + file.getOriginalFilename());
	        Files.write(path, bytes);
			File newFile = new File(path.toString());
			String text = createWordCloudImage(req,newFile);
			req.setAttribute("text",text);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "step2";
	}
	private String createWordCloudImage(HttpServletRequest req, File file){
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
		/*final Dimension dimension = new Dimension(500, 312);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		//wordCloud.setBackground(new PixelBoundryBackground("backgrounds/whale_small.png"));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setKumoFont(new KumoFont("Zawgyi-One", FontWeight.BOLD));
		wordCloud.setFontScalar(new LinearFontScalar(10, 40));
		wordCloud.build(wordFrequencies);*/
		//String path = "";
		//System.out.println(path=req.getServletContext().getRealPath("/resources"));
		String text = "";
		for(WordFrequency fq:wordFrequencies){
			text += fq.getWord()+" "+fq.getFrequency()+"\n";
		}
		//wordCloud.writeToFile(path+"\\+pic.png");
		return text;
	}
}
