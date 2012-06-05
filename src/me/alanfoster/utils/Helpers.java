package me.alanfoster.utils;

public final class Helpers {
	// Private constructor so that the class can not be instantiated
	private Helpers(){}
	
	public static <T extends Comparable<T>> boolean Contains(T[] arr, T obj) {
		for(T t : arr)
			if(t.equals(obj))
				return true;
		return false;
	}
	
	public static <T> String join(T[] arr){
		return join(arr, ", ");
	}
	
	public static <T> String join(T[] arr, String deliminator){
		if(arr == null || arr.length == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(T val : arr){
			sb.append(val).append(", ");
		}
		sb.replace(sb.length() - deliminator.length(), sb.length(), "");
		
		return sb.toString();
	}
	
	public static String concat(Object ... args){
		StringBuffer obj = new StringBuffer();
		for(Object o : args)
			obj.append(o);
		return obj.toString();
	}
	
	public static String addLeadingZero(long num) {
		return String.format("%02d", num);
	}
}
