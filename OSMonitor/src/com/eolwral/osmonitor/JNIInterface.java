package com.eolwral.osmonitor;

public class JNIInterface
{ 
	private static JNIInterface singletone = null;
	
	// Design Pattern "Singletone"
	public static JNIInterface getInstance()
	{
		if(singletone == null) 
		{
			try {
				System.loadLibrary("osmonitor");
			} 
			catch (UnsatisfiedLinkError e)
			{ 
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			singletone = new JNIInterface();
		}
		 
		return singletone;
	}

    /* Load Module */
	public final int doTaskProcess = 1; 
	public final int doTaskInterface = 2; 
	public final int doTaskNetwork = 3; 
	public final int doTaskMisc = 4;
	public final int doTaskDMesg = 5;
	public final int doTaskLogcat = 6;

    public native int doTaskStop();
    public native int doTaskStart(int TaskType);
    public native int doDataLoad();
    public native int doDataRefresh();
    public native int doDataTime(int Time);
    public native int doCPUUpdate(int Update);
 
    /* Root */
    public native int GetRooted();
    
    /* CPU */
    public native int GetCPUUsageValue();
    
    /* Processor */
    public native int GetProcessorNum();
    public native int GetProcessorMax(int num);
    public native int GetProcessorMin(int num);
    public native int GetProcessorScalMax(int num);
    public native int GetProcessorScalMin(int num);
    public native int GetProcessorScalCur(int num);
    public native float GetProcessorCPUTemp();
    public native String GetProcessorScalGov(int num);
    
    public native boolean GetTegra3IsTegra3();
    public native boolean GetTegra3IsLowPowerGroupActive();
    public native int GetTegra3EnabledCoreCount();
    public native String GetTegra3ActiveCpuGroup();
    
    /* Memory */
    public native long GetMemTotal();
    public native long GetMemFree();
    public native long GetMemCached();
    public native long GetMemBuffer();

    /* Power */
    public native int GetPowerCapacity();
    public native int GetPowerVoltage();
    public native int GetPowerTemperature();
    public native int GetUSBOnline();
    public native int GetACOnline();
    public native String GetPowerHealth();
    public native String GetPowerStatus();
    public native String GetPowerTechnology();

	/* Disk */
    public native double GetSystemMemTotal();
    public native double GetDataMemTotal();
    public native double GetSDCardMemTotal();
    public native double GetCacheMemTotal();
    public native double GetSystemMemUsed();
    public native double GetDataMemUsed();
    public native double GetSDCardMemUsed();
    public native double GetCacheMemUsed();
    public native double GetSystemMemAvail();
    public native double GetDataMemAvail();
    public native double GetSDCardMemAvail();
    public native double GetCacheMemAvail();


	/* Process */
    public native String GetProcessNamebyUID(int uid);

    public native int doInterfaceNext();
    public native int doInterfaceReset();
    public native int GetInterfaceCounts();
    public native int GetInterfaceOutSize(int position);
    public native int GetInterfaceInSize(int position);
    public native String GetInterfaceName(int position);
    public native String GetInterfaceAddr(int position);
    public native String GetInterfaceAddr6(int position);
    public native String GetInterfaceNetMask(int position);
    public native String GetInterfaceNetMask6(int position);
    public native String GetInterfaceMac(int position);
    public native String GetInterfaceScope(int position);
    public native String GetInterfaceFlags(int position);

    public native int SetNetworkIP6To4(int value);
    public native int GetNetworkCounts();
    public native String GetNetworkProtocol(int position);
    public native String GetNetworkLocalIP(int position);
    public native int GetNetworkLocalPort(int position);
    public native String GetNetworkRemoteIP(int position);
    public native int GetNetworkRemotePort(int position);
    public native String GetNetworkStatus(int position);
    public native int GetNetworkUID(int position);

     
	/*
	0: KERN_EMERG
	1: KERN_ALERT
	2: KERN_CRIT
	3: KERN_ERR
	4: KERN_WARNING
	5: KERN_NOTICE
	6: KERN_INFO
	7: KERN_DEBUG
	*/
	public final int doDMesgEMERG = 1; 
	public final int doDMesgALERT = 2; 
	public final int doDMesgERR = 3; 
	public final int doDMesgWARNING = 4; 
	public final int doDMesgNOTICE = 5; 
	public final int doDMesgINFO = 6; 
	public final int doDMesgDEBUG = 7; 
	public final int doDMesgNONE = 8; 
 
	public native int TruncateDebugMessage();
	public native int GetDebugMessageCounts();
	public native String GetDebugMessageTime(int position);
	public native String GetDebugMessageLevel(int position);
	public native String GetDebugMessage(int position);
	public native int SetDebugMessageLevel(int value);
	public native int SetDebugMessage(String filter);
	public native int SetDebugMessageFilter(int value);
    public native String GetDebugMessage();

    public final int doLogcatNONE = 0;
    public final int doLogcatVERBOSE = 2;
    public final int doLogcatDEBUG = 3;
    public final int doLogcatINFO = 4;
    public final int doLogcatWARN = 5;
    public final int doLogcatERROR = 6;
    public final int doLogcatFATAL = 7;
    
	public native int TruncateLogcat();
	public native int GetLogcatCounts();
	public native int GetLogcatSize();
	public native int GetLogcatCurrentSize();
	public native String GetLogcatLevel(int position);
	public native int GetLogcatPID(int position);
	public native String GetLogcatTime(int position);
	public native String GetLogcatTag(int position);
	public native String GetLogcatMessage(int position);
	public native int SetLogcatSource(int value);
	public native int SetLogcatFilter(int value);
	public native int SetLogcatPID(int value);
	public native int SetLogcatLevel(int value);
	public native int SetLogcatMessage(String filter);
	
	public native int GetFileOwner(String file);
	public native String GetUidName(int uid);
}

