package com.eolwral.osmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IOStat {
	
	private static final String DeviceBasePath = "/sys/block";
	private static final String DeviceSchedulerPath = "/sys/block/%s/queue/scheduler"; 
	
	private static String ReadFileLine(String file)
	{
		String result = null;
		try
		{
			InputStream fileStream = new FileInputStream(file);
			BufferedReader bufferedStream = new BufferedReader(
					new InputStreamReader(fileStream));
			result = bufferedStream.readLine();
			fileStream.close();
			
			return result;
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	private static List<String> GetDiskDevices()
	{
		ArrayList<String> files = new ArrayList<String>();
		{
			File diskDevices = new File(DeviceBasePath);
			File[] deviceList = diskDevices.listFiles();
			
			for (int i = 0; i < deviceList.length; ++i)
				if (!deviceList[i].getName().startsWith("loop"))
					files.add(deviceList[i].getName());
		}
		
		return files;
	}
	
	private static List<String> GetSupportedSchedulersForDevice(String device)
	{
		//Get the list of schedulers supported for the current device.
		String schedulerLine = ReadFileLine(String.format(DeviceSchedulerPath, device));
		
		//Parse the string. It looks like:
		//noop anticipatory deadline [cfq]
		ArrayList<String> result = new ArrayList<String>();
		StringTokenizer tokeniser = new StringTokenizer(schedulerLine);
		
		while (tokeniser.hasMoreTokens())
		{
			String token = tokeniser.nextToken();
			if (token.length() == 0)
				continue;
			else if (token.substring(0, 1).equals("["))
				result.add(token.substring(1, token.length() - 1));
			else
				result.add(token);
		}
		
		return result;
	}
	
	private static String GetActiveSchedulerForDevice(String device)
	{
		//Get the list of schedulers supported for the current device.
		String schedulerLine = ReadFileLine(String.format(DeviceSchedulerPath, device));
		
		//Parse the string. It looks like:
		//noop anticipatory deadline [cfq]
		StringTokenizer tokeniser = new StringTokenizer(schedulerLine);
		
		while (tokeniser.hasMoreTokens())
		{
			String token = tokeniser.nextToken();
			if (token.length() == 0)
				continue;
			else if (token.substring(0, 1).equals("["))
				return token.substring(1, token.length() - 1);
		}
		
		return null;
	}
	
	public static List<String> GetSupportedSchedulers()
	{
		List<String> devices = GetDiskDevices();
		if (devices.size() == 0)
			return null;
		
		ArrayList<String> supported = new ArrayList<String>();
		supported.addAll(GetSupportedSchedulersForDevice(devices.get(0)));
		
		//Check every device
		for (int i = 1, j = devices.size(); i < j; ++i)
		{
			//Get the list of schedulers supported by this device. Since we are finding the
			//common schedulers supported, we just need to remove schedulers not supported
			//by the current device.
			List<String> deviceSchedulers = GetSupportedSchedulersForDevice(devices.get(i));
			for (int k = 0, l = supported.size(); k < l; ++k)
			{
				String scheduler = supported.get(k);
				if (!deviceSchedulers.contains(scheduler))
					supported.remove(scheduler);
			}
		}

		return supported;
	}
	
	public static String GetActiveScheduler()
	{
		List<String> devices = GetDiskDevices();
		if (devices.size() == 0)
			return null;
		
		return GetActiveSchedulerForDevice(devices.get(0));
	}
}
