public class CountVowels {
	public static void main(String args[]) {
		String s = "Abir";
		CountVowels c = new CountVowels();
		System.out.println(c.countVowels(s));
		
	}
	
	public int countVowels(String s) {	
	Character[] c = new Character[]{'a','e','i','o','u'};
	List<Character> list = Arrays.asList(c);
	int count = 0;
	if(!s.isEmpty()) {
		String sToLower = s.toLowerCase();
		System.out.println(sToLower);
		
			for(int i = 0; i <= sToLower.length()-1; i++) {
				if(list.contains(sToLower.charAt(i))) {
					count ++;
					
				}
			}
	}
			return count;
	}
	

}
