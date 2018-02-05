package me.vigroid.funmap;

import android.app.Application;

import com.joanzapata.iconify.fonts.FontAwesomeModule;

import me.vigroid.funmap.core.app.FunMap;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by yangv on 1/20/2018.
 * Enrty point for the app, got longest lifecycle
 * intial some global variables here
 */

public class MapsApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FunMap.init(this)
                .withApiHost("http://10.0.0.4/FunMap/api/")
                .withIcon(new FontAwesomeModule())
                /**
                *TODO move these four args to activity, these are just dummy data
                 * 1. not signed, use default
                 * 2. signed with token, get from preferences, if no info in pref, get from net
                 * 3. signed with api call, get from internet callback
                */
                .withSignIn(true)
                .withUserID(0)
                .withUserName("vigroid")
                .withUserIcon("http://10.0.0.4/FunMap/icons/icon (15)")
                .configure();

        //fragmentation debug
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }
}
