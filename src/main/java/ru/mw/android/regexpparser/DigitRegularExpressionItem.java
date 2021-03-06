package ru.mw.android.regexpparser;

import android.text.Editable;

/**
 * Created by nixan on 9/9/13.
 */
public class DigitRegularExpressionItem extends RegularExpressionItem {

    public DigitRegularExpressionItem(int minLength, int maxLength) {
        super(minLength, maxLength);
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.DIGIT;
    }

    @Override
    public String toString() {
        return "\\d" + (isMaxLengthUnlimited() ? (getMinLength() == 0 ? "*" : "+")
                : (getMaxLength() == getMinLength() ? ("{" + String.valueOf(getMaxLength()) + "}")
                        : ("{" + String.valueOf(getMinLength()) + "," + String
                                .valueOf(getMaxLength()) + "}")));
    }

    @Override
    public int format(Editable input, int startPosition) {
        return format(input, startPosition, input.length());
    }

    @Override
    public int format(Editable input, int startPosition, int endPosition) {
        int position = 0;
        while ((isMaxLengthUnlimited() || position < getMaxLength())
                && startPosition + position < endPosition) {
            boolean isDigit = Character.isDigit(input.charAt(startPosition + position));
            if (isDigit) {
                position++;
            } else if (!isDigit && position < getMinLength()) {
                input.delete(startPosition + position, startPosition + position + 1);
                endPosition--;
            } else {
                return position;
            }
        }
        return position;
    }

    public int formatFromTheEnd(Editable input, int startPosition, int endPosition) {
        return format(input, startPosition, endPosition);
    }

    public int formatFromTheEnd(Editable input, int startPosition) {
        return format(input, startPosition);
    }

    @Override
    public MatchResult matches(String string, int startPosition, int endPosition) {
        int numberOfSymbolsMatched = 0;
        boolean inputIsShorter = false;
        boolean inputIsLonger = false;
        if (string.length() < startPosition) {
            return MatchResult.NO.setNumberOfSymbolsMatched(numberOfSymbolsMatched);
        }
        if (string.length() < endPosition) {
            inputIsShorter = true;
        }
        if (endPosition - startPosition < getMinLength()) {
            inputIsShorter = true;
        }
        if (!isMaxLengthUnlimited() && endPosition - startPosition > getMaxLength()) {
            inputIsLonger = true;
        }
        for (int i = startPosition; i < Math.min(string.length(), endPosition); i++) {
            if ('9' < string.charAt(i) || string.charAt(i) < '0') {
                return MatchResult.SHORTER.setNumberOfSymbolsMatched(numberOfSymbolsMatched);
            } else {
                numberOfSymbolsMatched++;
            }
        }
        if (inputIsShorter) {
            return MatchResult.SHORTER.setNumberOfSymbolsMatched(numberOfSymbolsMatched);
        } else if (inputIsLonger) {
            return MatchResult.LONGER.setNumberOfSymbolsMatched(numberOfSymbolsMatched);
        } else {
            return MatchResult.FULL.setNumberOfSymbolsMatched(numberOfSymbolsMatched);
        }
    }

    @Override
    public String toReversedString() {
        return toString();
    }

    @Override
    public RegularExpressionItem reverse() {
        return this;
    }
}
