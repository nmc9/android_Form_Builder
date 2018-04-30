package caruso.nicholas.com.formbuilder.PreInstalledUtils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import caruso.nicholas.com.formbuilder.DataBox;
import caruso.nicholas.com.itm_database.Item;

/**
 * Nick:1/11/2018
 * WorkOrder.
 */

public class SubItemAdapter extends FragmentPagerAdapter implements Serializable {
    private List<Item> itemList = new ArrayList<>();
    private int layout;
    private FragmentManager mgr;


    public SubItemAdapter(FragmentManager mgr, List<? extends Item> items, int layout) {
//Exacy same being built again in the same way
        super(mgr);

        this.mgr = mgr;
        itemList.addAll(items);
        this.layout = layout;

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public SubItemFragment getItem(int position) {
        return SubItemFragment.newInstance(itemList.get(position), layout);
    }

    public SubItemFragment getSubItemFragment(int viewPagerId, int fragmentPosition) {
        return (SubItemFragment) mgr.findFragmentByTag("android:switcher:" + viewPagerId + ":" + fragmentPosition);
    }

    public Item getItemFromSubItemFragment(int viewPagerId, int fragmentPosition) {
        SubItemFragment subItemFragment = getSubItemFragment(viewPagerId, fragmentPosition);
        return subItemFragment.getItem();
    }

    public View getViewFromSubItemFragment(int viewPagerId, int fragmentPosition) {
        SubItemFragment subItemFragment = getSubItemFragment(viewPagerId, fragmentPosition);
        return subItemFragment.getView();
    }

    public DataBox getDataBoxFromSubItemFragment(int viewPagerId, int fragmentPosition) {
        SubItemFragment subItemFragment = getSubItemFragment(viewPagerId, fragmentPosition);
        return subItemFragment.getDatabox();
    }

}
