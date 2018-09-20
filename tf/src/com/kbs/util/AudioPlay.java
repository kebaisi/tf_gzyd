package com.kbs.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class AudioPlay {
 private static AudioInputStream audioInputStream;
//// public static final String AUDIO_SETPANEL = "pleaseSetCard.wav";
//
// public static void main(String[] args) throws LineUnavailableException,
//   UnsupportedAudioFileException, IOException {
//
//	 AudioPlay.speak("12.0元谢谢");
////  // 获取音频输入流
////  AudioInputStream audioInputStream = AudioSystem
////    .getAudioInputStream(new File(GetProjectRealPath.getPath(AUDIO_SETPANEL)));
////  // 获取音频编码对象
////  AudioFormat audioFormat = audioInputStream.getFormat();
////
////  // 设置数据输入
////  DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
////    audioFormat, AudioSystem.NOT_SPECIFIED);
////  SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
////    .getLine(dataLineInfo);
////  sourceDataLine.open(audioFormat);
////  sourceDataLine.start();
////
////  /*
////   * 从输入流中读取数据发送到混音器
////   */
////  int count;
////  byte tempBuffer[] = new byte[1024];
////  while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
////   if (count > 0) {
////    sourceDataLine.write(tempBuffer, 0, count);
////   }
////  }
////
////  // 清空数据缓冲,并关闭输入
////  sourceDataLine.drain();
////  sourceDataLine.close();
////	 new AudioPlay().playAudio(AUDIO_SETPANEL);
// }
//
 public static void playAudio(final String name){
	 Runnable runnable = new Runnable() {  
         @Override  
         public void run() {  
        	 try {
        		 try {
        				// 获取音频输入流
        				  audioInputStream = AudioSystem
        				    .getAudioInputStream(new File(GetProjectRealPath.getPath(name)));
        				  // 获取音频编码对象
        				  AudioFormat audioFormat = audioInputStream.getFormat();

        				  // 设置数据输入
        				  DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
        				    audioFormat, AudioSystem.NOT_SPECIFIED);
        				  SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
        				    .getLine(dataLineInfo);
        				  sourceDataLine.open(audioFormat);
        				  sourceDataLine.start();

        				  /*
        				   * 从输入流中读取数据发送到混音器
        				   */
        				  int count;
        				  byte tempBuffer[] = new byte[1024];
        				  while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
        				   if (count > 0) {
        				    sourceDataLine.write(tempBuffer, 0, count);
        				   }
        				  }

        				  // 清空数据缓冲,并关闭输入
        				  sourceDataLine.drain();
        				  sourceDataLine.close();
        			} catch (UnsupportedAudioFileException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (LineUnavailableException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}catch(Exception e){
        				e.printStackTrace();
        			}
				} catch (Exception e1) {
					e1.printStackTrace();
				}  
         } 
     };  
     new Thread(runnable).start(); 
	
 }
 	
 public static void speak(final String str){
	 if(TF.dsJson.getIntValue("isVoice")==0){
		 return;
	 }
	 Runnable runnable = new Runnable() {  
         @Override  
         public void run() {ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
 	    // Dispatch是做什么的？
 	    Dispatch sapo = sap.getObject();
 	    try {
 	 
 	        // 音量 0-100
 	        sap.setProperty("Volume", new Variant(100));
 	        // 语音朗读速度 -10 到 +10
 	        sap.setProperty("Rate", new Variant(0));
 	 
 	        Variant defalutVoice = sap.getProperty("Voice");
 	 
 	        Dispatch dispdefaultVoice = defalutVoice.toDispatch();
 	        Variant allVoices = Dispatch.call(sapo, "GetVoices");
 	        Dispatch dispVoices = allVoices.toDispatch();
 	 
 	        Dispatch setvoice = Dispatch.call(dispVoices, "Item", new Variant(1)).toDispatch();
 	        ActiveXComponent voiceActivex = new ActiveXComponent(dispdefaultVoice);
 	        ActiveXComponent setvoiceActivex = new ActiveXComponent(setvoice);
 	 
 	        Variant item = Dispatch.call(setvoiceActivex, "GetDescription");
 	        // 执行朗读
 	        Dispatch.call(sapo, "Speak", new Variant(str));
 	        
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	    } finally {
 	        sapo.safeRelease();
 	        sap.safeRelease();
 	    }} 
     };  
     new Thread(runnable).start(); 
	}
}