// Created by Luzius on Feb 15, 2014

package ch.fhnw.algd2;

public class Clock {
	private static final int NSperMS = 1000000;
	private static final long MUCH = 5;
	
	private long start, end;
	
	public Clock(){
		start();
	}

	public void start() {
		this.start = System.nanoTime();
	}
	
	public void stop() {
		this.end = System.nanoTime();
	}
	
	private long getTime() {
		return end - start;
	}

	public int getMillis() {
		return (int)(getTime()/NSperMS);
	}
	
	public boolean tookMuchLongerThan(Clock c2) {
		return getTime() > MUCH*c2.getTime();
	}

	public boolean timeSpanIsOver(int ms) {
		return (System.nanoTime() - start)/NSperMS > ms;
	}
	
	@Override
	public String toString(){
		if (end == 0){
			return "Clock";
		} else {
			long diff = getTime();
			if (diff > NSperMS){
				diff /= NSperMS;
				return diff + " ms";
			} else {
				return diff + " ns";
			}
		}
	}

}
