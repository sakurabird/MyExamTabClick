
package com.sakruaexample.myexamtabclick;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
    private TabInfo mLastTab = null;

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }

    class TabFactory implements TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         * 
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_layout);
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        // setIndicator(View view)を使用するので独自カスタムタブを使用する
        // Viewとそのタグはこの後でクリックイベントを取得する際に必要
        View customtab1 = new CustomTab(this, 0, "Tab1");
        MainActivity
                .addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1")
                        .setIndicator(customtab1), (tabInfo = new TabInfo("Tab1",
                        Tab1Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        View customtab2 = new CustomTab(this, 1, "Tab2");
        MainActivity
                .addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2")
                        .setIndicator(customtab2), (tabInfo = new TabInfo("Tab2",
                        Tab2Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        View customtab3 = new CustomTab(this, 2, "Tab3");
        MainActivity
                .addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3")
                        .setIndicator(customtab3), (tabInfo = new TabInfo("Tab3",
                        Tab3Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        View customtab4 = new CustomTab(this, 3, "Tab4");
        MainActivity
                .addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4")
                        .setIndicator(customtab4), (tabInfo = new TabInfo("Tab4",
                        Tab4Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        View customtab5 = new CustomTab(this, 4, "Tab5");
        MainActivity
                .addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab5")
                        .setIndicator(customtab5), (tabInfo = new TabInfo("Tab5",
                        Tab5Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        // Default to first tab
        this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);

        // クリックイベントを取得する
        int numberOfTabs = mTabHost.getTabWidget().getChildCount();
        Log.d("numberOfTabs=", "" + numberOfTabs);

        for (int t = 0; t < numberOfTabs; t++) {
            mTabHost.getTabWidget().getChildAt(t).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        String currentSelectedTag = mTabHost.getCurrentTabTag();
                        Log.d("currentSelectedTag=", currentSelectedTag);
                        String currentTag = (String) v.getTag();
                        Log.d("currentTag=", "" + currentTag);
                        // 同じタブが押された時の処理（ここではタブの先頭のFragmentを表示したいので
                        // タブが変更された時と同じ処理をしている）
                        if (currentSelectedTag.equalsIgnoreCase(currentTag)) {
                            mTabHost.setCurrentTabByTag(currentTag);
                            onTabChanged(currentSelectedTag);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    /**
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private static void addTab(MainActivity activity, TabHost tabHost,
            TabHost.TabSpec tabSpec, TabInfo tabInfo) {

        tabSpec.setContent(activity.new TabFactory(activity));
        String tag = tabSpec.getTag();

        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(tabInfo.fragment);
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }

        tabHost.addTab(tabSpec);
    }

    /**
     * (non-Javadoc)
     * 
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    @Override
    public void onTabChanged(String tag) {

        // バックスタックにあるFragmentを取り除く
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        TabInfo newTab = this.mapTabInfo.get(tag);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if (mLastTab != null) {
            if (mLastTab.fragment != null) {
                ft.detach(mLastTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                newTab.fragment = Fragment.instantiate(this,
                        newTab.clss.getName(), newTab.args);
                ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
            } else {
                ft.attach(newTab.fragment);
            }
        }

        mLastTab = newTab;
        ft.commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    // 独自カスタマイズタブ用
    private class CustomTab extends FrameLayout {

        public CustomTab(Context context) {
            super(context);
        }

        public CustomTab(Context context, int tab_index, String tag) {
            this(context);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(context);
            textView.setText(tag);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 20, 0, 20);

            addView(textView);
            setTag(tag);
        }
    }
}
