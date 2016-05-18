package it.unibo.studio.paolosarti.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DeltaTimer 
{
	private long startNanos;
	
	public DeltaTimer()
	{
		startNanos=System.nanoTime();
	}

	public long deltaMillis()
	{
		return (System.nanoTime()-startNanos)/1000000;
	}
	
	public long deltaNanos()
	{
		return System.nanoTime()-startNanos;
	}
	
	public void start()
	{
		startNanos=System.nanoTime();
	}
	
	public long deltaMillis(Object invoker,Method m, Object[] args)
	{
		startNanos=System.nanoTime();
		try {
			m.invoke(invoker, args);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.nanoTime()-startNanos;
	}
	
}
