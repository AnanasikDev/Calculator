package calculator;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static <T> T getFirst(ArrayList<T> list){
        return list.get(0);
    }
    public static <T> T getLast(ArrayList<T> list){
        return list.get(list.size() - 1);
    }
    public static <T> T getFirst(List<T> list){
        return list.get(0);
    }
    public static <T> T getLast(List<T> list){
        return list.get(list.size() - 1);
    }
}
