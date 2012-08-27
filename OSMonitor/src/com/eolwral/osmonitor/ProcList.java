package com.eolwral.osmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.util.SparseArray;

public class ProcList {
	// Options controlling how collection is done.
	private static int Filter;
	private static int Algorithm;
	private static int Sort;
	private static int Order;

	// Constants for some of the options.
	public static final int doSortPID = 1; 
	public static final int doSortLoad = 2; 
	public static final int doSortMem = 3; 
	public static final int doSortThreads = 4; 
	public static final int doSortName = 5;

	public static final int doOrderASC = 0; 
	public static final int doOrderDESC = 1; 

	// Option setters.
	public static void SetProcessFilter(int Filter) {
		ProcList.Filter = Filter;
	}

	public static void SetProcessAlgorithm(int Algorithm) {
		ProcList.Algorithm = Algorithm;
	}

	public static void SetProcessSort(int Sort) {
		ProcList.Sort = Sort;
	}

	public static void SetProcessOrder(int Order) {
		ProcList.Order = Order;
	}

	// Return an empty snapshot of processes (0 processes).
	public static ProcList Empty() {
		ProcList snapshot = new ProcList();
		snapshot.pids = new int[] {};
		return snapshot;
	}

	// Information about a single process.
	private static class Proc {
		public int uid;
		public String owner;
		public char status;
		public long utime;
		public long stime;
		public long nice;
		public int threads;
		public int time;  // start time, jiffies since boot
		public long rss;

		public String name;
		public int load;
	};

	// Collect a snapshot of processes. The returned object is immutable
	// outside this class.
	public static ProcList Collect() {
		ProcList snapshot = new ProcList();
		snapshot.proc = new SparseArray<Proc>();
		List<Integer> pids = new ArrayList<Integer>();
		for (String name : new File("/proc").list()) {
			int pid;
			
			try {
				// TODO: make sure this forces base10
				pid = Integer.parseInt(name);
			} catch (NumberFormatException e) {
				continue;  // some other entry in /proc
			}
			
			final String pidStr = name;
			Proc proc = new Proc();

			proc.uid = -1;  // FIXME
			proc.owner = "-1";  // FIXME(proc.uid)
			
			// Fills status, utime, stime, nice, threads, start_time, rss.
			try {
				parseStatIntoProc(pidStr, proc);
			} catch (IOException e) {
				proc.status = '?';
				proc.utime = proc.stime = proc.rss = -1;
				proc.threads = proc.time = -1;
			}
			
			// Fill name from cmdline.
			try {
				FileReader r = new FileReader("/proc/" + pidStr + "/cmdline");
				proc.name = IOUtils.readUpToNull(r);
			} catch (IOException e) {
				Log.e("ProcList", "exception reading cmdline", e);
				proc.name = "<error>";
			}
			
			// TODO: proc.name: fall back to stat
			
			snapshot.proc.put(pid, proc);
			pids.add(Integer.valueOf(pid));
		}
		snapshot.pids = new int[pids.size()];
		int index = 0;
		for (int pid : pids) {
			snapshot.pids[index++] = pid;
		}
		return snapshot;
	}

	private static void parseStatIntoProc(String pidStr, Proc proc) throws IOException {
		BufferedReader r = new BufferedReader(
				new FileReader("/proc/" + pidStr + "/stat"));
		String line = r.readLine();
		
		String[] tokens = line.split(" ");
		
		proc.status = tokens[2].charAt(0);
		proc.utime = Long.parseLong(tokens[13]);
		proc.stime = Long.parseLong(tokens[14]);
		proc.threads = Integer.parseInt(tokens[19]);
		proc.time = Integer.parseInt(tokens[20]);
		proc.rss = Long.parseLong(tokens[22]);
	}
	
	private static String nameFromCmdline(String pidStr) throws IOException {
		FileReader r = new FileReader("/proc/" + pidStr + "/cmdline");
		try {
			return IOUtils.readUpToNull(r);
		} finally {
			r.close();
		}
	}

	// A process list snapshot is a mapping of {position -> PID} and a mapping
	// of {PID -> process info}.
	private int[] pids;
	private SparseArray<Proc> proc;
	
	// All nonstatic public methods are accessors only - can't change a process
	// list snapshot after it's been collected.

	// Accessors for the {position -> PID} mapping.
	public int GetProcessCounts() {
		return pids.length;
	}

	public int GetProcessPID(int position) {
		return pids[position];
	}
	
	// Accessors for the {PID -> process info} mapping.
    public int GetProcessUID(int pid) {
    	return proc.get(pid).uid;
    }

    public int GetProcessLoad(int pid) {
    	return proc.get(pid).load;
    }

    public long GetProcessUTime(int pid) {
    	return proc.get(pid).utime;
    }

    public long GetProcessSTime(int pid) {
    	return proc.get(pid).stime;
    }

    public String GetProcessTime(int pid) {
    	return Integer.toString(proc.get(pid).time);
    }

    public int GetProcessThreads(int pid) {
    	return proc.get(pid).threads;
    }

    public long GetProcessRSS(int pid) {
    	return proc.get(pid).rss;
    }

    public long GetProcessNice(int pid) {
    	return proc.get(pid).nice;
    }

    public String GetProcessName(int pid) {
    	return proc.get(pid).name;
    }

    public String GetProcessOwner(int pid) {
    	return proc.get(pid).owner;
    }

    public String GetProcessStatus(int pid) {
    	return Character.toString(proc.get(pid).status);
    }
}
