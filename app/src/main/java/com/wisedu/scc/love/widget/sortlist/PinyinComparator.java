package com.wisedu.scc.love.widget.sortlist;

import java.util.Comparator;

public class PinyinComparator implements Comparator<PersonDto> {

	@Override
	public int compare(PersonDto o1, PersonDto o2) {
		if (o1.getSortLetters().equals("☆")) {
			return -1;
		} else if (o2.getSortLetters().equals("☆")) {
			return 1;
		} else if (o1.getSortLetters().equals("#")) {
			return -1;
		} else if (o2.getSortLetters().equals("#")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
