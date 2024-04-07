package com.app.industrialwatch.app.business;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

/***
 *  Communicate between fragment and activity
 */
public interface OnContentChangeListener {

    /**
     * Get {@link Toolbar} of {@link BaseActivity}
     *
     * @return Toolbar
     */
    Toolbar getToolbar();

    /**
     * removes the back stack till specific fragment
     */
    void onPopTillSpecificFragment(Fragment fragment);

    /**
     * Remove current fragment from {@link FragmentTransaction}
     */
    void onRemoveCurrentFragment();

    /**
     * Remove given fragment from {@link FragmentTransaction}
     *
     * @param fragment {@link Fragment}
     */
    void onRemoveCurrentFragment(Fragment fragment);

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment  {@link Fragment}
     * @param contentId Id of {@link android.widget.FrameLayout}
     */
    void onAddFragment(Fragment fragment, int contentId);

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment       {@link Fragment}
     * @param addToBackStack true add else do not add
     */
    void onAddFragment(Fragment fragment, boolean addToBackStack);

    /**
     * Add fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment       {@link Fragment}
     * @param contentId      Id of {@link android.widget.FrameLayout}
     * @param addToBackStack true add else do not add
     */
    void onAddFragment(Fragment fragment, int contentId, boolean addToBackStack);

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment  {@link Fragment}
     * @param contentId Id of {@link android.widget.FrameLayout}
     */
    void onReplaceFragment(Fragment fragment, int contentId);

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment       {@link Fragment}
     * @param addToBackStack true add else do not add
     */
    void onReplaceFragment(Fragment fragment, boolean addToBackStack);

    /**
     * Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param fragment       {@link Fragment}
     * @param contentId      Id of {@link android.widget.FrameLayout}
     * @param addToBackStack true add else do not add
     */
    void onReplaceFragment(Fragment fragment, int contentId, boolean addToBackStack);

    /**
     * Remove current fragment from {@link FragmentTransaction}
     * & Replace fragment in given {@link android.widget.FrameLayout}
     *
     * @param curFragment       Current Fragment
     * @param newFragment       {@link Fragment}
     * @param addToBackStack true add else do not add
     */
    void onRemoveAndReplaceFragment(Fragment curFragment, Fragment newFragment, boolean addToBackStack);

    /**
     * Get fragment from given id of {@link android.widget.FrameLayout}
     *
     * @param contentId Id of {@link android.widget.FrameLayout}
     * @return Fragment
     */
    Fragment getCurrentFragment(int contentId);

    /**
     * Get Fragment from default {@link android.widget.FrameLayout}
     *
     * @return Fragment
     */
    Fragment getCurrentFragment();

    /**
     * Remove all fragments from the stack
     *
     * @return Fragment
     */
    void onClearBackStack();


    /**
     * Get parent view of {@link BaseActivity}
     *
     * @return View
     */
    View getParentView();
}
