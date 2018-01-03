package com.game.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.game.Activity.Fragment.MainFragment;
import com.game.Helper.GameHelper;
import com.game.R;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    private final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    public static MainActivity mMainActivity;
    private MainFragment mainFragment;
    private long firsttime; // 监听两次返回
    private FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;

    /* Data */
    private List<MenuObject> mMenuObjects = new ArrayList<>();

    /* Getter */
    public static MainActivity getInstance() {
        return mMainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = this;

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuIcon();

        mainFragment = MainFragment.getInstance();
        addFragment(mainFragment, true, R.id.fragmentContainer);

    }

    //初始化toolbar
    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.btn_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarTextView.setText("2048");

    }

    //初始化menufragment
    private void initMenuIcon() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        initMenuObjects();
        menuParams.setMenuObjects(mMenuObjects);
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    //获取数据
    private void initMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject tutorial = new MenuObject(getResources().getString(R.string.menu_tutorial));
        tutorial.setResource(R.drawable.icn_1);

        MenuObject restart = new MenuObject(getResources().getString(R.string.menu_restart));
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_factory_reset);
        restart.setBitmap(b);

        MenuObject chartCheck = new MenuObject(getResources().getString(R.string.menu_charts));
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        chartCheck.setDrawable(bd);

        MenuObject author = new MenuObject(getResources().getString(R.string.menu_info));
        author.setResource(R.drawable.about_me);

        MenuObject quit = new MenuObject(getResources().getString(R.string.menu_exit));
        quit.setResource(R.drawable.icn_5);

        mMenuObjects.add(close);
//        mMenuObjects.add(send);
        mMenuObjects.add(restart);
        mMenuObjects.add(author);
//        mMenuObjects.add(addFav);
        mMenuObjects.add(quit);
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack) {
                transaction.addToBackStack(backStackName);
            }
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firsttime < 3000) {
                finish();
                return true;
            } else {
                firsttime = System.currentTimeMillis();
                Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    /* Listener */

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Intent i;
        switch (position) {

            //取消
            case 0:
                break;


            //查看教程
//            case 1:
//                i = new Intent(getApplicationContext(), WebHelpActivity.class);
//                startActivity(i);
//                break;

            //重新开始
            case 1:
                GameHelper.getInstance().startGame();
                break;

            //我要看榜
//            case 3:
//                i = new Intent(getApplicationContext(), ChartsActivity.class);
//                startActivity(i);
//                break;

            //关于作者
            case 2:
//                i = new Intent(getApplicationContext(), InfoActivity.class);
//                startActivity(i);
                displayDialogMsg();
                break;

            //退出游戏
            case 3:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(getApplicationContext(), "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    private AlertDialog mDialog;
    /**
     *
     */
    public void displayDialogMsg() {

        View dialogView = View.inflate(this, R.layout.dialog_dashboard_msg, null);
        TextView dashboardDialogResultTitle = (TextView) dialogView.findViewById(R.id.dashboardDialogResultTitle);
        TextView dashboardDialogResultTip = (TextView) dialogView.findViewById(R.id.dashboardDialogResultTip);

        dashboardDialogResultTitle.setText(R.string.menu_info);
        dashboardDialogResultTip.setText(R.string.info_name);

        dialogView.findViewById(R.id.dialogButtonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView).setCancelable(true);
        mDialog = dialogBuilder.create();
        mDialog.show();
    }

}
