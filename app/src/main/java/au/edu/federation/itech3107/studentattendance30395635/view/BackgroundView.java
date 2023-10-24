package au.edu.federation.itech3107.studentattendance30395635.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class BackgroundView extends FrameLayout {
    public BackgroundView(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
