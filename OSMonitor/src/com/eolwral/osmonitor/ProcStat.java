package com.eolwral.osmonitor;

import java.io.*;
import java.util.StringTokenizer;

import android.util.Log;

public class ProcStat {
	private int lastTotal;
	private int lastIdle;
	private int currentTotal;
	private int currentIdle;

	public ProcStat() {
		lastTotal = lastIdle = -1;
		currentTotal = currentIdle = -1;
		Update();
	}

	private static class BadHeadingException extends Exception {
	};

	public void Update() {
		int total, idle;

		try {
			String line = CommonUtil.ReadFile("/proc/stat");
			StringTokenizer tok = new StringTokenizer(line);
			String heading = tok.nextToken();
			if (!heading.equals("cpu")) {
				throw new BadHeadingException();
			}

			int user = Integer.parseInt(tok.nextToken());
			int nice = Integer.parseInt(tok.nextToken());
			int system = Integer.parseInt(tok.nextToken());
			idle = Integer.parseInt(tok.nextToken());
			int iowait = Integer.parseInt(tok.nextToken());
			int irq = Integer.parseInt(tok.nextToken());
			int softirq = Integer.parseInt(tok.nextToken());

			total = user + nice + system + idle + iowait + irq + softirq;
		} catch (BadHeadingException e) {
			Log.e("osmonitor", "bad heading in /proc/stat", e);
			return;
		} catch (NumberFormatException e) {
			Log.e("osmonitor", "error parsing /proc/stat", e);
			return;
		}

		lastTotal = currentTotal;
		lastIdle = currentIdle;

		currentTotal = total;
		currentIdle = idle;
	}

	public float GetCPUUsageValueFloat() {
		if (lastTotal == -1) {  // need 2 Update()s before we have a value
			return 0;
		} else {
			int deltaTotal = currentTotal - lastTotal;
			int deltaIdle = currentIdle - lastIdle;
			return (deltaTotal - deltaIdle) / (float)deltaTotal;
		}
	}

	public int GetCPUUsageValue() {
		return (int)(100.0 * GetCPUUsageValueFloat());
	}
}
