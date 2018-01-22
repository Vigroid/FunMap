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
                .withApiHost("https://httpbin.org/")
                .withIcon(new FontAwesomeModule())
                .configure();

        //fragmentation debug
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.SHAKE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
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