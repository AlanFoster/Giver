package Utils;

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
		StringBuilder sb = new StringBuilder();

		for(T val : arr){
			sb.append(val).append(", ");
		}
		
		sb.replace(sb.length() - deliminator.length(), sb.length(), "");
		
		return sb.toString();
	}
}
