package com.nixcvf18.myunitdemo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nixcvf18.myunitdemo.Animal;
import com.nixcvf18.myunitdemo.AnimalAdapter;
import com.nixcvf18.myunitdemo.R;
import com.nixcvf18.myunitdemo.ui.fragment.AboutMeFragment;
import com.nixcvf18.myunitdemo.ui.fragment.AliceFragment;
import com.nixcvf18.myunitdemo.ui.fragment.BobFragment;
import com.nixcvf18.myunitdemo.ui.fragment.CindyFragment;
import com.nixcvf18.myunitdemo.ui.fragment.DavidFragment;
import com.nixcvf18.myunitdemo.ui.fragment.EvansFragment;
import com.nixcvf18.myunitdemo.ui.fragment.FredyFragment;
import com.nixcvf18.myunitdemo.ui.fragment.GanHuoAndroidBeanFragment;
import com.nixcvf18.myunitdemo.ui.fragment.GirlBeanFragment;
import com.nixcvf18.myunitdemo.ui.fragment.GirlFragment;
import com.nixcvf18.myunitdemo.ui.fragment.GreenFragment;
import com.nixcvf18.myunitdemo.ui.fragment.HarryFragment;
import com.nixcvf18.myunitdemo.ui.fragment.SettingFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements  NavigationView.OnNavigationItemSelectedListener{
    //private ArrayList<Animal> arrayList = new ArrayList<Animal>();
private  Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView  navigationView;
    private ConstraintLayout constraintLayout;
    private FragmentManager  fragmentManager;

   // private FloatingActionButton floatingActionButton;
   // private AnimalAdapter animalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化碎片管理器对象
        fragmentManager = getSupportFragmentManager();

        initView();
        initData();


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_switch18dp);

        }










    }

    private void initData() {

      fragmentManager.beginTransaction()
      .replace(R.id.content_ConstraintLayout, AboutMeFragment.newInstance()).commit();

        toolbar.setTitle("关于我");


    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        //将toolbar设置为Activity的标题栏
        setSupportActionBar(toolbar);

        //实例化控件
        drawerLayout=findViewById(R.id.drawerLayout);
        constraintLayout = findViewById(R.id.content_ConstraintLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


/*        floatingActionButton=findViewById(R.id.floatingActionButton);
        //给悬浮按钮设置点击事件
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//点击悬浮按钮 会弹出一个矩形提示框
                Snackbar.make(v,"mtrzyfyt",Snackbar.LENGTH_SHORT)
                        //fyt0926被点击后  会弹出一个吐司（显示字符串wxhn）
                        .setAction("fyt0926", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "wxhn", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });*/

    }

   /* private void initAnimals() {


        arrayList.add(new Animal("b1", R.drawable.a1));
        arrayList.add(new Animal("b2", R.drawable.a2));
        arrayList.add(new Animal("b3", R.drawable.a3));
        arrayList.add(new Animal("b4", R.drawable.a4));
        arrayList.add(new Animal("b5", R.drawable.a5));
        arrayList.add(new Animal("b6", R.drawable.a1));
    }*/


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {


            case android.R.id.home:
                //执行拉开抽屉的 操作
                drawerLayout.openDrawer(GravityCompat.START);

                break;





            default:
                break;




        }
        //该事件被activity消费后 返回true
        return true;
    }


    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {


            case R.id.one:
//获取一个 fragmentmanager  用于处理fragment的编辑操作
                fragmentManager.beginTransaction()
                        //替换一个已经被添加进容器内的fragment实例
                        //容器的标识符                           即将要新加入进容器内的fragment实例
                        .replace(R.id.content_ConstraintLayout, GirlBeanFragment.newInstance())
                        //不会及时执行  会在下一次的时候由主线程执行
                        .commit();
                toolbar.setTitle("看妹纸");
                break;



            case R.id.two:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, GanHuoAndroidBeanFragment.newInstance())
                        .commit();
                toolbar.setTitle("看干货");
                break;

            case R.id.three:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, CindyFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于3");
                break;
            case R.id.four:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, DavidFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于4");
                break;
            case R.id.five:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, EvansFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于5");
                break;
            case R.id.one1:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, FredyFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于6");
                break;
            case R.id.two1:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, GreenFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于7");
                break;
            case R.id.three1:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, HarryFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于8");
                break;
            case R.id.four1:

                fragmentManager.beginTransaction()
                        .replace(R.id.content_ConstraintLayout, AboutMeFragment.newInstance())
                        .commit();
                toolbar.setTitle("关于我");
                break;

                case R.id.five1:

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_ConstraintLayout, SettingFragment.newInstance())
                            .commit();

                    toolbar.setTitle("设置");

                break;


            default:


                break;

















        }

        //关闭左边的抽屉 或者 关闭右边的抽屉
        drawerLayout.closeDrawer(GravityCompat.START);







        //该条目 作为被选中状态出现时   返回true
        return true;
    }


    @Override
    public void onBackPressed() {
//判断抽屉是否为 拉开状态
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //为拉开状态  就执行 关闭操作
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            //底层的默认实现  是结束当前活动界面
            super.onBackPressed();
        }






    }
}