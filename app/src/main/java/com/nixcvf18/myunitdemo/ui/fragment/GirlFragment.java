package com.nixcvf18.myunitdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.nixcvf18.myunitdemo.R;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GirlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GirlFragment extends Fragment {

private Context  context;
private TabLayout tabLayout;
private ViewPager  viewPager;

protected CompositeDisposable  compositeDisposable;
















    public GirlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @paramparam1 Parameter 1.
     * @paramparam2 Parameter 2.
     * @return A new instance of fragment GirlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GirlFragment newInstance() {
        GirlFragment fragment = new GirlFragment();
   /*     Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_girl, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.vp_content);

        return view;



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        compositeDisposable = new CompositeDisposable();

        MyCustomTabFragmentPagerAdapter myCustomTabFragmentPagerAdapter = new MyCustomTabFragmentPagerAdapter(this.getChildFragmentManager());
        viewPager.setAdapter(myCustomTabFragmentPagerAdapter);


        tabLayout.setupWithViewPager(viewPager);





    }


    @Override
    public void onDestroy() {
        super.onDestroy();






    }



    private class MyCustomTabFragmentPagerAdapter extends FragmentPagerAdapter{

        private  String[] tablayoutTitles={"gank.io"};


        /**
         * Constructor for {@link FragmentPagerAdapter} that sets the fragment manager for the adapter.
         * This is the equivalent of calling {@link # FragmentPagerAdapter(FragmentManager, int)} and
         * passing in {@link #BEHAVIOR_SET_USER_VISIBLE_HINT}.
         *
         * <p>Fragments will have {@link Fragment#setUserVisibleHint(boolean)} called whenever the
         * current Fragment changes.</p>
         *
         * @param fm fragment manager that will interact with this adapter
         * @deprecated use {@link # FragmentPagerAdapter(FragmentManager, int)} with
         * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
         */
        private MyCustomTabFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return GirlBeanFragment.newInstance();
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return tablayoutTitles.length ;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tablayoutTitles[position];
            
        }





    }






}