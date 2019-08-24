import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static Set<String> validate(String input) {
        checkStringForNullOrEmpty(input);
        Set<String> stringSetForCorrectBrackets = generateSetWithCorrectBrackets(input);
        String stringWithOtherSymbols = generateStringWithOtherSymbols(input);
        return getResultSet(stringSetForCorrectBrackets, stringWithOtherSymbols);
    }

    private static void checkStringForNullOrEmpty(String input){
        if(input == null){
            throw new NullPointerException("input can't be null");
        }

        if(input.isEmpty()){
            throw new NumberFormatException("input can't be empty");
        }
    }

    private static void swap(char[] resultChar, int firstIndex, int secondIndex){
        char temp = resultChar[firstIndex];
        resultChar[firstIndex] = resultChar[secondIndex];
        resultChar[secondIndex] = temp;
    }

    private static void replaceChar(StringBuilder stringBuilder, int lastCharIndex){
        char temp = stringBuilder.charAt(lastCharIndex);
        stringBuilder.deleteCharAt(lastCharIndex);
        stringBuilder.insert(lastCharIndex + 1, temp);
    }

    private static Matcher generateRegexForBrackets(String input){
        Pattern patternForBrackets = Pattern.compile("\\{|\\}");
        Matcher matcherForBrackets = patternForBrackets.matcher(input);
        return  matcherForBrackets;
    }

    private static Matcher generateRegexForOtherSymbols(String input){
        Pattern patternForOther = Pattern.compile("[^{}]+");
        Matcher matcherForOtherSymbols = patternForOther.matcher(input);
        return  matcherForOtherSymbols;
    }

    private static String generateStringWithOtherSymbols(String input){
        String stringForOtherSymbols = "";
        Matcher matcherForOtherSymbols = generateRegexForOtherSymbols(input);
        while(matcherForOtherSymbols.find()){
            stringForOtherSymbols += matcherForOtherSymbols.group();
        }

        return stringForOtherSymbols;
    }

    private static String generateStringWithAllBrackets(String input){
        String stringForAllBrackets = "";
        Matcher matcherForBrackets = generateRegexForBrackets(input);
        while(matcherForBrackets.find()){
            stringForAllBrackets += matcherForBrackets.group();
        }

        return stringForAllBrackets;
    }

    private static Set<String> generateSetWithCorrectBrackets(String input){
        int leftCountBracketFirstItar = 0;
        int leftCountBracketSecondItar = 0;
        int rightCountBracketFirstItar = 0;
        int rightCountBracketSecondItar = 0;
        String stringWithCorrectBracketsFromLeftToRight = "";
        Set<String> stringSetForCorrectBrackets = new LinkedHashSet<>();
        Stack<Character> stackForBrackets = new Stack<>();
        char [] arrayWithAllBrackets = generateStringWithAllBrackets(input).toCharArray();
        List<Integer> listWithCountOfBrackets = countBrackets(arrayWithAllBrackets);
        char [] arrayWithCorrectBrackets = listWithCountOfBrackets.get(0) < listWithCountOfBrackets.get(1) ?
                new char [listWithCountOfBrackets.get(0) * 2] :
                new char [listWithCountOfBrackets.get(1) * 2];
        if(listWithCountOfBrackets.get(0) < listWithCountOfBrackets.get(1)){
            for (int i = 0; i < arrayWithAllBrackets.length; i++) {
                if(i == arrayWithAllBrackets.length - 1 && arrayWithAllBrackets[i] == '{'){
                    break;
                }

                if (arrayWithAllBrackets[i] == '{') {
                    stackForBrackets.push(arrayWithAllBrackets[i]);
                    stringWithCorrectBracketsFromLeftToRight += arrayWithAllBrackets[i];
                } else if (stackForBrackets.size() != 0) {
                    if (arrayWithAllBrackets[i] == '}' && stackForBrackets.pop() == '{') {
                        stringWithCorrectBracketsFromLeftToRight += arrayWithAllBrackets[i];
                        rightCountBracketFirstItar++;
                    }
                }
            }

            for (int i = arrayWithAllBrackets.length - 1, j = 0; i >= 0; i--) {
                if(i == 0 && arrayWithAllBrackets[i] == '}'){
                    break;
                }

                if (arrayWithAllBrackets[i] == '}' && rightCountBracketFirstItar > rightCountBracketSecondItar) {
                    stackForBrackets.push(arrayWithAllBrackets[i]);
                    arrayWithCorrectBrackets[arrayWithCorrectBrackets.length - 1 - j++] = arrayWithAllBrackets[i];
                    rightCountBracketSecondItar++;
                } else if (stackForBrackets.size() != 0) {
                    if (arrayWithAllBrackets[i] == '{' && stackForBrackets.pop() == '}') {
                        arrayWithCorrectBrackets[arrayWithCorrectBrackets.length - 1 - j++] = arrayWithAllBrackets[i];
                    }
                }
            }
        } else {
            for (int i = arrayWithAllBrackets.length - 1, j = 0; i >= 0; i--) {
                if(i == 0 && arrayWithAllBrackets[i] == '}'){
                    break;
                }

                if (arrayWithAllBrackets[i] == '}') {
                    stackForBrackets.push(arrayWithAllBrackets[i]);
                    arrayWithCorrectBrackets[arrayWithCorrectBrackets.length - 1 - j++] = arrayWithAllBrackets[i];
                } else if (stackForBrackets.size() != 0) {
                    if (arrayWithAllBrackets[i] == '{' && stackForBrackets.pop() == '}') {
                        arrayWithCorrectBrackets[arrayWithCorrectBrackets.length - 1 - j++] = arrayWithAllBrackets[i];
                        leftCountBracketFirstItar++;
                    }
                }
            }

            for (int i = 0; i < arrayWithAllBrackets.length; i++) {
                if(i == arrayWithAllBrackets.length - 1 && arrayWithAllBrackets[i] == '{'){
                    break;
                }

                if (arrayWithAllBrackets[i] == '{' && leftCountBracketSecondItar < leftCountBracketFirstItar) {
                    stackForBrackets.push(arrayWithAllBrackets[i]);
                    stringWithCorrectBracketsFromLeftToRight += arrayWithAllBrackets[i];
                    leftCountBracketSecondItar++;
                } else if (stackForBrackets.size() != 0) {
                    if (arrayWithAllBrackets[i] == '}' && stackForBrackets.pop() == '{') {
                        stringWithCorrectBracketsFromLeftToRight += arrayWithAllBrackets[i];
                    }
                }
            }
        }

        stringSetForCorrectBrackets.add(new String(arrayWithCorrectBrackets));
        stringSetForCorrectBrackets.add(stringWithCorrectBracketsFromLeftToRight);

        return stringSetForCorrectBrackets;
    }

    private static List<Integer> countBrackets(char[] arrayWithAllBrackets){
        List<Integer> list = new ArrayList<>();
        int leftBrackets = 0;
        int rightBrackets = 0;
        for (int i = 0; i < arrayWithAllBrackets.length; i++) {
            if (i == 0 && arrayWithAllBrackets[i] == '}'){
                continue;
            }

            if(i == arrayWithAllBrackets.length - 1 && arrayWithAllBrackets[i] == '{'){
                continue;
            }

            if (arrayWithAllBrackets[i] == '{') {
                leftBrackets ++;
            } else {
                rightBrackets ++;
            }
        }

        list.add(leftBrackets);
        list.add(rightBrackets);
        return  list;
    }

    private static int helperWithTranspositionForIncreasing(StringBuilder stringBuilder, String stringWithOtherSymbols,
                                                            String stringOfCorrectBracketsInSet, int i, int offset,int lastCharIndex, Set<String> resultSet){
        for(int j = 0; j < stringWithOtherSymbols.length(); j++){
            stringBuilder.setLength(0);
            stringBuilder.append(stringOfCorrectBracketsInSet, 0, i + 1);
            stringBuilder.append(stringWithOtherSymbols, 0, j + 1);
            stringBuilder.append(stringOfCorrectBracketsInSet.toCharArray(), i + 1,
                    stringOfCorrectBracketsInSet.length() - (i + 1));
            stringBuilder.append(stringWithOtherSymbols.toCharArray(), j + 1,
                    stringWithOtherSymbols.length() - (j + 1));
            resultSet.add(stringBuilder.toString());
            lastCharIndex = j + 1 + offset;
        }

        return lastCharIndex;
    }

    private static void helperWithTranspositionForDecreasing(StringBuilder stringBuilder, String stringOfCorrectBracketsInSet, int i,
                                                      int lastCharIndex, Set<String> resultSet){
        for (int j = 0; j < stringOfCorrectBracketsInSet.length() - 2 * i; j++){
            if(lastCharIndex + 1 >= stringBuilder.length()){
                lastCharIndex = i + 2;
            }

            replaceChar(stringBuilder, lastCharIndex);
            if(resultSet.contains(stringBuilder.toString())){
                lastCharIndex = i + 2;
                replaceChar(stringBuilder, lastCharIndex);
            }

            lastCharIndex++;
            resultSet.add(stringBuilder.toString());
        }
    }

    private static void generateFirstCorrectStringWithSymbols(StringBuilder stringBuilder, String stringOfCorrectBracketsInSet,
                                                              String stringWithOtherSymbols, Set<String> resultSet){
        stringBuilder.append(stringOfCorrectBracketsInSet);
        stringBuilder.append(stringWithOtherSymbols);
        resultSet.add(stringBuilder.toString());
    }

    private static void getLastStringInSet(StringBuilder stringBuilder, Set<String> resultSet){
        Object[] array =  resultSet.toArray();
        stringBuilder.append(array[array.length - 1].toString());
    }

    private static void transposition(StringBuilder stringBuilder, String stringWithOtherSymbols, String stringOfCorrectBracketsInSet,
                                      Set<String> resultSet){
        generateFirstCorrectStringWithSymbols(stringBuilder, stringOfCorrectBracketsInSet, stringWithOtherSymbols, resultSet);
        int lastCharIndex = 0;
        int offset = -1;
        for(int i = 0; i < stringWithOtherSymbols.length(); i++){
            offset++;
            lastCharIndex = helperWithTranspositionForIncreasing(stringBuilder, stringWithOtherSymbols,
                    stringOfCorrectBracketsInSet, i, offset, lastCharIndex, resultSet);
            stringBuilder.setLength(0);
            getLastStringInSet(stringBuilder,resultSet);
            helperWithTranspositionForDecreasing(stringBuilder, stringOfCorrectBracketsInSet, i ,lastCharIndex,
                    resultSet);
        }

        stringBuilder.setLength(0);
    }

    private static Set<String> getResultSet(Set<String> stringSetForCorrectBrackets, String stringWithOtherSymbols){
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> resultSet = new LinkedHashSet<>();
        for (String stringOfCorrectBracketsInSet : stringSetForCorrectBrackets) {
            if (!stringWithOtherSymbols.isEmpty()) {
                transposition(stringBuilder,stringWithOtherSymbols, stringOfCorrectBracketsInSet, resultSet);
            }
            else {
                return stringSetForCorrectBrackets;
            }
        }

        return resultSet;
    }
}
