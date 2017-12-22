package netty_udp_;

public class SystemCopy {
	public static void main(String[] args) {
		int[] fun ={0,1,2,3,4,5,6};
		int[] fun1 ={7,8,9,10,11,12,13};
		
		System.arraycopy(fun, 0, fun1, 3, 4);
		//[0, 1, 2, 0, 1, 2, 6]  ==fun, 0, fun, 3, 3
		//[0, 1, 2, 0, 1, 5, 6]	 ==fun, 0, fun, 3, 2
		System.out.println(fun);
	}
}
