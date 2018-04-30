package caruso.nicholas.com.formbuilder.PreInstalledPlugin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import caruso.nicholas.com.formbuilder.DataBox;
import caruso.nicholas.com.formbuilder.PreInstalledUtils.SubItemAdapter;
import caruso.nicholas.com.formbuilder.PreInstalledUtils.SubItemFragment;
import caruso.nicholas.com.formbuilder.R;
import caruso.nicholas.com.formbuilder.Validation;
import caruso.nicholas.com.formbuilder.helper_functions.OnPageChangeSelectedListener;
import caruso.nicholas.com.formbuilder.helper_functions.WrapViewPager;
import caruso.nicholas.com.itm_database.Item;
import corps.technology.diversified.workorder.R;
import corps.technology.diversified.workorder.forms.FromUtils.DataBox;
import corps.technology.diversified.workorder.forms.FromUtils.SubItemAdapter;
import corps.technology.diversified.workorder.forms.FromUtils.SubItemFragment;
import corps.technology.diversified.workorder.forms.FromUtils.Validation;
import corps.technology.diversified.workorder.helper_functions.OnPageChangeSelectedListener;
import corps.technology.diversified.workorder.helper_functions.WrapViewPager;

/**
 * Nick:3/1/2018
 * WorkOrder.
 */

public class PagerPlugin {
    private static final String PLUGIN_KEY = "PagerPlugin";
    private static final String INVALID_FRAGMENTS_KEY = "invalid_fragments_key";
    private static final String PREVIOUS_KEY = "previous_key";

    private Fragment fragment;
    private Listener listener;
    private PagerObject pagerO;
    private List<Item> items;
    private int previous = 0;

    private SubItemAdapter mAdapter;
    private WrapViewPager mPager;
    private TextView pagerTextView;
    private int mPagerLength;

    private Validation[] InvalidFragments;
    private boolean usesValidation;


    public interface Listener extends SubItemFragment.subItemListener {
        /**
         * @return the holder of The ViewPager
         */
        int HolderPagerKey();

        /**
         * @return the SubItemFragment Layout
         */
        int LayoutPagerKey();
    }

    private PagerPlugin(@NonNull Fragment fragment, @NonNull Listener listener, @NonNull PagerObject pagerObject) {
        this.listener = listener;
        this.fragment = fragment;
        this.pagerO = pagerObject;
        this.items = new ArrayList<>();
        this.usesValidation = ValidationPlugin.is(fragment);
    }

    public void setItems(List<? extends Item> items) {
        this.items.addAll(items);
    }

    public static PagerPlugin create(@NonNull Fragment fragment, PagerObject pagerObject) {
        Listener listener;
        try {
            listener = (Listener) fragment;
        } catch (ClassCastException e) {
            listener = nullListener;
        }
        if (pagerObject == null) {
            pagerObject = new PagerObject();
        }
        return new PagerPlugin(fragment, listener, pagerObject);
    }

    public final void buildPager(View rootView) {
        mPager = rootView.findViewById(listener.HolderPagerKey());

        mPager.addOnPageChangeListener(getOnPageListener());
        mAdapter = buildAdapter(items);
        mPager.setAdapter(mAdapter);
        pagerTextView = rootView.findViewById(pagerO.pager_text_view);

        mPager.post(() -> getOnPageListener().onPageSelected(previous));
        rootView.findViewById(pagerO.pager_left).setOnClickListener(getPagerArrowClickListener());
        rootView.findViewById(pagerO.pager_right).setOnClickListener(getPagerArrowClickListener());

        if (mPager.getAdapter() != null) {
            mPagerLength = mPager.getAdapter().getCount();
            setValidationArray();
        }
    }

    public Item getCurrentItem() {
        int meterIndex = getCurrentIndex();
        return mAdapter.getItemFromSubItemFragment(mPager.getId(), meterIndex);
    }

    public DataBox getCurrentDatabox() {
        int boxIndex = getCurrentIndex();
        return mAdapter.getDataBoxFromSubItemFragment(mPager.getId(), boxIndex);
    }

    public List<? extends Item> getItemList() {
        items.set(getCurrentIndex(), getCurrentItem());
        return items;
    }

    public int getCurrentIndex() {
        return mPager.getCurrentItem();
    }

    private SubItemAdapter buildAdapter(List<? extends Item> items) {
        return new SubItemAdapter(fragment.getChildFragmentManager(), //Fragment View
                items, //Item to get data from
                listener.LayoutPagerKey()
        );
    }

    public static int restorePrevious(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(PREVIOUS_KEY)) {
            return savedInstanceState.getInt(PREVIOUS_KEY);
        }
        return 0;
    }


    public static PagerPlugin restorePlugin(Fragment fragment, Bundle savedInstanceState) {
        PagerPlugin pagerPlugin = null;
        PagerObject obj = (PagerObject) savedInstanceState.getSerializable(PLUGIN_KEY);
        if (obj != null) {
            pagerPlugin = PagerPlugin.create(fragment, obj);
            pagerPlugin.previous = restorePrevious(savedInstanceState);
            if (ValidationPlugin.is(fragment) && savedInstanceState.containsKey(INVALID_FRAGMENTS_KEY)) {
                pagerPlugin.InvalidFragments = (Validation[]) savedInstanceState.getParcelableArray(INVALID_FRAGMENTS_KEY);
            }
        }
        return pagerPlugin;
    }


    public final void saveInstance(Bundle outState) {
//        outState.putSerializable(PLUGIN_KEY, this);
        if (ValidationPlugin.is(fragment)) {
            outState.putParcelableArray(INVALID_FRAGMENTS_KEY, InvalidFragments);
        }
        outState.putSerializable(PLUGIN_KEY, pagerO);
        outState.putInt(PREVIOUS_KEY, previous);
    }

    private View.OnClickListener getPagerArrowClickListener() {
        return view -> {
            int current = mPager.getCurrentItem();
            boolean min = current == 0;
            boolean max = current == mPagerLength;
            if (view.getId() == pagerO.pager_left && !min) {
                mPager.setCurrentItem(current - 1);
            } else if (view.getId() == pagerO.pager_right && !max) {
                mPager.setCurrentItem(current + 1);
            }
            current = mPager.getCurrentItem();
            setTextView(current);
        };
    }

    private OnPageChangeSelectedListener getOnPageListener() {
        return new OnPageChangeSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                Item hiddenItem = mAdapter.getItemFromSubItemFragment(mPager.getId(), previous);
                if (hiddenItem != null) {
                    items.set(previous, hiddenItem);
                }
                setTextView(position);
                previous = position;
            }

        };
    }

    private void setTextView(int current) {
        pagerTextView.setError(null);
        if (mPagerLength != 0) {
            //Get x of y String() . if none returned use just x of y
            pagerTextView.setText(String.format(XofY(), current + 1, mPagerLength));
        } else
            //Get none string . if null returned use Empty()
            pagerTextView.setText(pagerO.no_item);
    }

    private String XofY() {
        String XofY = pagerO.XofYString;
        if (XofY != null) {
            int first = XofY.indexOf("%d");
            if (first != -1 && XofY.substring(first + 2).contains("%d")) {
                return XofY;
            }
        }
        return fragment.getString(R.string.item_x_of_y);
    }

    public static final class PagerObject implements Serializable {
        public int pager_left = R.id.pager_left;
        public int pager_right = R.id.pager_right;
        public int pager_text_view = R.id.pager_current_page;
        public String XofYString;
        public String no_item;


        public PagerObject() {

        }

        public PagerObject(int pager_left, int pager_right, int pager_text_view, String XofYString, String no_item) {
            this.pager_left = pager_left;
            this.pager_right = pager_right;
            this.pager_text_view = pager_text_view;
            this.XofYString = XofYString;
            this.no_item = no_item;
        }

        public PagerPlugin build(Fragment fragment) {
            return PagerPlugin.create(fragment, this);
        }
    }

    private static Listener nullListener = new Listener() {
        @Override
        public int HolderPagerKey() {
            return -1;
        }

        @Override
        public int LayoutPagerKey() {
            return -1;
        }

        @Override
        public DataBox fillAdapterFieldsWithData(Item item, View adapterView) {
            return null;
        }

        @Override
        public Item getDataFromAdapterFields(Item item, View adapterView) {
            return null;
        }

        @Override
        public void buildInflatedView(View view) {

        }
    };

    private void setValidationArray() {
        if (usesValidation && InvalidFragments == null) {
            InvalidFragments = new Validation[mPagerLength];
            for (int i = 0; i < mPagerLength; i++) {
                InvalidFragments[i] = null;
            }
        }
    }

    public void updateValidationArray(DataBox dataBox) {
        if (usesValidation) {
            Validation v = dataBox.getValidation().withPage(previous);
            InvalidFragments[previous] = v;
            dataBox.resetValidation();
        }
    }

    public Validation[] getInvalidFragments() {
        return InvalidFragments;
    }

    public void handleWithPage(Validation validation) {
        //They missed something on this page
        mPager.setCurrentItem(validation.getFirstPage());
        pagerTextView.setError(validation.getFirstReason());
//        pagerTextView.setError("Missing Fields");
        View v = mAdapter.getViewFromSubItemFragment(mPager.getId(), mPager.getCurrentItem());
        ((EditText) v.findViewById(validation.getFirst())).setError(validation.getFirstReason());
    }

    public void handleJustPage(Validation validation) {
        mPager.setCurrentItem(validation.getFirstPage());
        pagerTextView.setError("Required");
    }

}
