package com.wisedu.scc.love.widget.dialog;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.HashMap;

/**
 * Created by DONG Shengdong on 14-11-24.
 */
public class PopupMenu {

    Popup popup;

    private static HashMap<Location, PopupMenu> popups = new HashMap<Location, PopupMenu>();

    private PopupMenu(Location location, View content, View parent) {
        PopupWindow popupWindow = new PopupWindow(content);
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setOutsideTouchable(true);
        if (location == Location.BELOW) {
            popup = new BelowPopup(popupWindow, content, parent);
        }
    }

    public static PopupMenu getMenu(Location location, View content, View parent) {
        PopupMenu popupMenu = popups.get(location);
        if (popupMenu == null) {
            popupMenu = new PopupMenu(location, content, parent);
            popups.put(location, popupMenu);
        }
        return popupMenu;
    }

    public boolean isShow() {
        return popup.isShow();
    }

    public void showWindow() {
        popup.postShowWindow();
    }

    public void dismissWindow() {
        popup.cancelShowingWindow();
    }


    public enum Location {
        BELOW,
    }

    abstract class Popup extends Handler implements Runnable {

        protected int mParentLocation[] = new int[2];

        protected PopupWindow mPopupWindow;

        protected View mContent;

        protected View mParent;


        public Popup(PopupWindow popupWindow, View content, View parent) {
            mPopupWindow = popupWindow;
            mContent = content;
            mParent = parent;
        }

        public void postShowWindow() {
            post(this);
        }

        public void cancelShowingWindow() {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            removeCallbacks(this);
        }

        public boolean isShow() {
            return mPopupWindow.isShowing();
        }

        @Override
        public void run() {

        }
    }

    class BelowPopup extends Popup {

        public BelowPopup(PopupWindow popupWindow, View content, View parent) {
            super(popupWindow, content, parent);
        }

        @Override
        public void run() {
            mParent.getLocationInWindow(mParentLocation);
            mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            if (!mPopupWindow.isShowing()) {
                Log.v("dd", "show" + " mParentLocation[1]" + mParentLocation[1] + " " + mPopupWindow.getHeight());
                mPopupWindow.showAtLocation(mParent,
                        Gravity.NO_GRAVITY, mParentLocation[0],
                        mParentLocation[1] + 50);
            } else {
                Log.v("dd", "update");
                mPopupWindow.update();
            }
        }
    }

}
