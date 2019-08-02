package com.unikom.islamedia;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

public class ButtonSheetDialog extends BottomSheetDialogFragment {
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contexView = View.inflate(getContext(), R.layout.detail_ustadz_popup, null);
        dialog.setContentView(contexView);
    }
}
