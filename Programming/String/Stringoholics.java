package String;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author - archit.s
 * Date - 10/10/18
 * Time - 1:17 PM
 */
public class Stringoholics {

    final int M = (int) 1e9+7;

    int maxLenSubString(String t){
        int[] lps = new int[t.length()];
        lps[0] = 0;
        int len = 0;
        int n = t.length();
        int i =1;
        int max= 0;

        while(i<n){
            if(t.charAt(i) == t.charAt(len)){
                len++;
                lps[i] = len;
                i++;
                max = Math.max(max,len);
            }
            else{
                if(len == 0){
                    lps[i] = 0;
                    i++;
                }
                else{
                    len = lps[len-1];
                }
            }
        }

        return max;
    }

    long pow(long a, long p){

        long ans = 1;
        while(p>0){
            if(p%2L == 1L){
                ans = (ans * a)%M;
            }
            a = (a*a)%M;
            p /= 2;
        }

        return ans%M;
    }

    

	/* We could use the property of lcm(a,b)= a*b/gcd(a,b). But the constraints of given problem
	   do not allow to go for this method. Instead we do the following steps:

	 1. We take out prime factors of every number and then take their multiplication. We use the same 
		concept of a*b/gcd(a,b), but as a and b both are prime their gcd(a,b)=1.

	 2. Now question is pow(2,3)=8 which is not prime, well it is taken care by only inserting count 
		in map if its previous value is less than current count value. Hence, if both pow(2,2)=4 and pow(2,3)=8 
		we will only put 8 in the map. 
    */
		

    long getLcm(ArrayList<Integer> lens){

        Map<Integer, Integer> m = new HashMap<>();

		/* Prime factorisation*/
        for(Integer num : lens){
            updateLcmMap(m, num);
        }

		/* gcd(a,b)= a*b/gcd(a,b) */
        long prod = 1;
        for(Map.Entry<Integer, Integer> entry : m.entrySet()){

            int k = entry.getKey();
            int v = entry.getValue();

            long p = pow(k,v) % M;

            prod = (prod * p) % M;
        }

        return prod % M;
    }


	void updateLcmMap(Map<Integer, Integer> m, Integer num){

		    int i = 2;

		    while(i<=num && i > 1){
		        int count = 0;
			
				/* i will always be a prime number*/
		        while(num % i == 0){
		            count++;
		            num /= i;
		        }

		        if(count == 0){
		            i++;
		            continue;
		        }

				/* Step 2. Considering only pow(2,3) instead of both pow(2,3) and pow(2,2)*/

		        if(m.containsKey(i)){
		            int v = m.get(i);
		            if(v < count){
		                m.put(i,count);
		            }
		        }
		        else{
		            m.put(i,count);
		        }

		        i++;
		    }
	}

	
    public int solve(ArrayList<String> A) {

        ArrayList<Integer> lens = new ArrayList<>();

        for(String t: A){

			/*	maxLenSubString is a function based on KMP string search.
			  	It is used to check that given substring has a prefix which is 
			  	also a suffix. 
				
			  	The idea behind using such logic is to check whether the string 
				has AA property or not. The reason to check this can be understood 
				using following two cases:

				1. Let us consider a normal scenario that a string is not of AA type. 
				   Under such condition it is simple to find the number of rotation 
				   needed. We get the original string when total number of bits rotated 
				   is some multiple of length of string.

				2. Now, suppose a string is of AA type. This time we have to only rotate 
				   half of the string.
				   
				Thus, for case (2) we need to use KMP's "longest prefix which is also a 
                suffix (lps)" concept.
			*/
            int maxLen = maxLenSubString(t);

            int n = t.length();

			/* If maxLen is half of total length i.e String is of AA type we reduce 
               string length to n.
            */
            if(n%(n-maxLen) == 0){
                n -= maxLen;
            }

            long sum = 0;
            int i =1;
            do{
                sum += i;
                i++;
            }while(sum % ((long) n) != 0L);

			/* Condition of while checks if sum is multiple of string length or not.*/

            lens.add(i-1);
        }

        long lcm = getLcm(lens) % M;

        return (int)lcm % M;
    }

    public static void main(String[] args) {

        ArrayList<String> t = new ArrayList<String>(){{
            add("a");
            add("ababa");
            add("aba");
        }};

        System.out.println(new Stringoholics().solve(t));
    }
}
