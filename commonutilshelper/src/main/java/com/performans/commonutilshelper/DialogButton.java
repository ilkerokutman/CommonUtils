package com.performans.commonutilshelper;

import android.content.DialogInterface;

public class DialogButton {
    private String mText;
    private DialogInterface.OnClickListener mOnClickListener;
    private ButtonTypes mButtonType;

    public DialogButton(String text) {
        this(text, ButtonTypes.POSITIVE);
    }

    public DialogButton(String text, ButtonTypes buttonType) {
        this(text, buttonType, null);
    }

    public DialogButton(String text, ButtonTypes buttonType, DialogInterface.OnClickListener onClickListener) {
        if (buttonType == null) buttonType = ButtonTypes.POSITIVE;

        setButtonType(buttonType);
        setText(text);
        setOnClickListener(onClickListener);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public ButtonTypes getButtonType() {
        if (mButtonType == null) mButtonType = ButtonTypes.POSITIVE;
        return mButtonType;
    }

    public void setButtonType(ButtonTypes buttonType) {
        if (buttonType == null) buttonType = ButtonTypes.POSITIVE;
        mButtonType = buttonType;
    }

    public DialogInterface.OnClickListener getOnClickListener() {
        if (mOnClickListener == null) mOnClickListener = (dialog, which) -> {

        };

        return mOnClickListener;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        if (onClickListener == null) onClickListener = (dialog, which) -> {

        };
        mOnClickListener = onClickListener;
    }

    public enum ButtonTypes {
        NEUTRAL,
        NEGATIVE,
        POSITIVE
    }
}
