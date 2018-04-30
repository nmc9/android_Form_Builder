package caruso.nicholas.com.formbuilder.helper_functions;

import android.support.v4.view.ViewPager;

/**
 * Nick:2/22/2018
 * WorkOrder.
 * This Class is just to minimize code in other locations
 * Usually the only method Needed is The OnPageSelected() method
 * This method abstracts that so only that method is shown
 */

public abstract class OnPageChangeSelectedListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Do Nothing
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do Nothing
    }
}
