package com.pierfrancescosoffritti.remotevrclient.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.pierfrancescosoffritti.remotevrclient.R;
import com.pierfrancescosoffritti.remotevrclient.adapters.ViewPagerAdapter;
import com.pierfrancescosoffritti.remotevrclient.fragments.GameFragment;
import com.pierfrancescosoffritti.remotevrclient.fragments.LogFragment;
import com.pierfrancescosoffritti.remotevrclient.logging.ConsoleLogger;
import com.pierfrancescosoffritti.remotevrclient.logging.LoggerBus;
import com.pierfrancescosoffritti.remotevrclient.sensorFusion.MyOrientationProvider;
import com.pierfrancescosoffritti.remotevrclient.utils.Fragments;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_GAME_FRAGMENT = "TAG_GAME_FRAGMENT";
    private static final String TAG_LOG_FRAGMENT = "TAG_LOG_FRAGMENT";

    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ButterKnife.bind(this);

        setSupportActionBar(ButterKnife.findById(this, R.id.toolbar));

        GameFragment gameFragment = (GameFragment) getFragment(savedInstanceState, GameFragment.newInstance(), TAG_GAME_FRAGMENT);
        LogFragment logFragment = (LogFragment) getFragment(savedInstanceState, LogFragment.newInstance(), TAG_LOG_FRAGMENT);

        setUpViewPager(
                new Pair(gameFragment, getString(R.string.game)),
                new Pair(logFragment, getString(R.string.log))
        );

        DecimalFormat normalFormatter = new DecimalFormat("##.000");

//        ConsoleLogger logger = new ConsoleLogger();
//        MyOrientationProvider orientationProvider = new MyOrientationProvider(this);
//        Observable.interval(50, TimeUnit.MILLISECONDS, Schedulers.io())
//                .map(tick -> orientationProvider.getQuaternion())
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(orientationProvider::start)
//                .doOnUnsubscribe(orientationProvider::stop)
//                .doOnSubscribe(logger::register)
//                .doOnUnsubscribe(logger::unregister)
//                .doOnNext((quaternion) -> LoggerBus.getInstance().post(
//                        new LoggerBus.Log("Quaternion "
//                                +"x: " +normalFormatter.format(quaternion.getX())
//                                +" y: " +normalFormatter.format(quaternion.getY())
//                                +" z: " +normalFormatter.format(quaternion.getZ())
//                                +" w: " +normalFormatter.format(quaternion.getW()),
//                                "sender", 0)))
//                .subscribe();
    }

    private Fragment getFragment(Bundle savedInstanceState, Fragment fragment, String tag) {
        if(savedInstanceState == null)
            return Fragments.findFragment(getSupportFragmentManager(), fragment);
        else
            return getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(tag));
    }

    private void setUpViewPager(Pair<Fragment, String>... fragments) {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabs = ButterKnife.findById(this, R.id.tab_layout);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TAG_GAME_FRAGMENT, mPagerAdapter.getItem(0).getTag());
        outState.putString(TAG_LOG_FRAGMENT, mPagerAdapter.getItem(1).getTag());
    }
}