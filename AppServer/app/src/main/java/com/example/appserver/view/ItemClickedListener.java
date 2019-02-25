package com.example.appserver.view;

import android.view.View;

public interface ItemClickedListener {
    void onClick(View v, int pos, boolean isLongClicked);
}
