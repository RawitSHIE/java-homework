package com.wongnai.interview.movie;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static  <T> Set<T> intersect(Set<T> a, Set<T> b) {
        Set newSet = new HashSet<T>();
        newSet.addAll(a);
        newSet.retainAll(b);
        return newSet;
    }
}
