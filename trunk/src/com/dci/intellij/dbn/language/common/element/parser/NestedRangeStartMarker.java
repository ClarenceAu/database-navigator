package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.PsiBuilder;

public class NestedRangeStartMarker {
    private PsiBuilder.Marker builderMarker;
    private TokenType beginTokenType;
    private TokenType endTokenType;

    public NestedRangeStartMarker(PsiBuilder.Marker builderMarker, TokenType beginTokenType, TokenType endTokenType) {
        this.builderMarker = builderMarker;
        this.beginTokenType = beginTokenType;
        this.endTokenType = endTokenType;
    }

    public PsiBuilder.Marker getBuilderMarker() {
        return builderMarker;
    }

    public TokenType getBeginTokenType() {
        return beginTokenType;
    }

    public TokenType getEndTokenType() {
        return endTokenType;
    }
}