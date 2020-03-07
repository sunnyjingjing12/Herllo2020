/**********************************
 * Encoder 这是一个编码类，用于把要发送的数据进行编码编为16位的PCM数据格式
 */

package com.health.hl.audio.util;

public class EncoderTx {
	public static int sampleRate=44100;//44100
	public static float sampleBaud=1378.125f;
	public static int sampleBit=24;//32
	public static int bitTxLength=16; //frame header: 3bits(1) + start bit: 1bit(0) + Data: 8bits + parity bit: 1bit + stop bit: 1bit(1) + frame tail: 3bits(1)
	public static int bitLength1=0;
	public static int bitLength2=0;
	public static int bitLength3=0;
	public static int dataLength=0;
	public static double W_PI2=2*3.1415f;
	public static double Hifreq=1378.125f; 
	public static double Lofreq=Hifreq/2.0f;
	public int audioAM = 32767;
	public int audioTxBufLength = 0; 
	public static short[] highLevel = new short[ sampleBit ];
	public static short[] lowLevel  = new short[ sampleBit ];
	private int  counter_i = 0;
	private int  counter_j = 0;
	private int  counter_k = 0;
	public EncoderTx(int bitLength1,int bitLength2,int bitLength3) {
		super();
		this.bitLength1 = bitLength1;
		this.bitLength2 = bitLength2;
		this.bitLength3 = bitLength3;
	}
	public int getaudioTxBufsize()
	{
		audioTxBufLength = sampleBit * (bitLength1+bitLength2+bitLength3)+352*2;//bitTxLength * sampleBit;//6 ones（包头3个ons,包尾3个ones） ,8bit数据,0start ,1stop,1parity
		return audioTxBufLength;
	}
	
/***********************************************
 initiate transmit 1 and 0 basic data 
 */
	public void initEncoderTxData()
	{
		for(counter_i=0;counter_i<sampleBit;counter_i++)
		{
			highLevel[counter_i] = (short) (audioAM * (-Math.sin(Math.PI * counter_i /sampleBit * 2)));
			lowLevel[counter_i]  = (short) (audioAM * (Math.sin(Math.PI * counter_i /sampleBit * 2)));
		}
	}
	
/***********************************************
 update audio transmit buffer basic data 
 */
	public short[] updateAudioTxBuf()
	{
		audioTxBufLength = sampleBit * (bitLength1+bitLength2+bitLength3)+352*2;	
		short[] audioTxBuf = new short[audioTxBufLength];
		counter_k = 0;

		initEncoderTxData();

		for(counter_i=0;counter_i<bitLength1;counter_i++)
		{
				for(counter_j=0;counter_j<sampleBit;counter_j++)
				{
					audioTxBuf[counter_k] = highLevel[counter_j];	//Samsung, Xiaomi(MIUI)
//					audioTxBuf[counter_k] = lowLevel[counter_j];	//else
					counter_k++;
				}
			
		}
		for (int i = 1; i < 352; i++) {
			audioTxBuf[counter_k+i]= (short)0;	
		}
		counter_k=counter_k+351;
		for(counter_i=0;counter_i<bitLength2;counter_i++)
		{
				for(counter_j=0;counter_j<sampleBit;counter_j++)
				{
					audioTxBuf[counter_k] = highLevel[counter_j];	//Samsung, Xiaomi(MIUI)
//					audioTxBuf[counter_k] = lowLevel[counter_j];	//else
					counter_k++;
				}			
			
		}	
		for (int i = 1; i < 352; i++) {
			audioTxBuf[counter_k+i]= (short) 0;			
		}
		counter_k=counter_k+351;
		for(counter_i=0;counter_i<bitLength3;counter_i++)
		{
				for(counter_j=0;counter_j<sampleBit;counter_j++)
				{
					audioTxBuf[counter_k] = highLevel[counter_j];	//Samsung, Xiaomi(MIUI)
//					audioTxBuf[counter_k] = lowLevel[counter_j];	//else
					counter_k++;
				}
			
		}
		counter_k=0;
		return audioTxBuf;
	}

}
