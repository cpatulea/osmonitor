package com.eolwral.osmonitor;

public class CpuStat {

	private static final String CPUINFO_MAX = "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq";
	private static final String CPUINFO_MIN = "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_min_freq";
	private static final String CPU_SCALING_CUR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
	private static final String CPU_SCALING_MAX = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq";
	private static final String CPU_SCALING_MIN = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq";
	private static final String CPU_SCALING_GOR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor";
	
	public static int GetCpuMaxFreq(int cpu)
	{
		try {
			String file = CommonUtil.ReadFile(String.format(CPUINFO_MAX, cpu)).trim();
			return Integer.parseInt(file);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int GetCpuMinFreq(int cpu)
	{
		try {
			String file = CommonUtil.ReadFile(String.format(CPUINFO_MIN, cpu)).trim();
			return Integer.parseInt(file);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int GetCpuFreq(int cpu)
	{
		try {
			String file = CommonUtil.ReadFile(String.format(CPU_SCALING_CUR, cpu)).trim();
			return Integer.parseInt(file);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int GetCpuMaxScalingFreq(int cpu)
	{
		try {
			String file = CommonUtil.ReadFile(String.format(CPU_SCALING_MAX, cpu)).trim();
			return Integer.parseInt(file);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int GetCpuMinScalingFreq(int cpu)
	{
		try {
			String file = CommonUtil.ReadFile(String.format(CPU_SCALING_MIN, cpu)).trim();
			return Integer.parseInt(file);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static String GetCpuGovernor(int cpu)
	{
		return CommonUtil.ReadFile(String.format(CPU_SCALING_GOR, cpu)).trim();
	}
}
