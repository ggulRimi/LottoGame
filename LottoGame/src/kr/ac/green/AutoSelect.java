package kr.ac.green;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class AutoSelect {
	// 기계가 자동으로 추첨한 번호를 담는 배열
	private List<String> autoNum = new Vector<String>();
	private String bonus;
	// 번호 자동선택
	public void autoSelect() {
		// 랜덤 번호 생성
		Random r = new Random();
		// 랜덤 번호를 담는 배열(중복X)
		HashSet<Integer> set = new HashSet<Integer>();
		// 중복없이 랜덤 번호가 7개
		while(set.size() <= 6) {
			// 1~45
			set.add(r.nextInt(45) + 1);
		}
		// HashSet에 담아둔 숫자를 Integer배열에 담아준다.
		// set을 Array로 바꿔서 Integer에 넣고 이것을 nums라고 부른다.
		Integer[] nums = set.toArray(new Integer[0]);
		// 6번째인 보너스 번호
		bonus = String.valueOf(nums[6]);
		// index 0~5번까지 정렬
		Arrays.sort(nums, 0, 6);
		for (int i = 0; i < 6; i++) {
			// int -> String으로 변환
			String str = String.valueOf(nums[i]);
			// List<String>인 autoNum에 담아준다.
			autoNum.add(str);
		}
	}
	public String getBonus() {
		return bonus;
	}
	public List<String> getAutoNum() {
		return autoNum;
	}

}
