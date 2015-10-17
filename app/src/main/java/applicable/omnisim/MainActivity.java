package applicable.omnisim;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity{
    private static Context GetStaticContext(){
        try{
            final Class<?> activityThreadClass=Class.forName("android.app.ActivityThread");
            final Method method=activityThreadClass.getMethod("currentApplication");
            return (Application)method.invoke(null,(Object[])null);
        }catch(final ClassNotFoundException e){
        }catch(final NoSuchMethodException e){
        }catch(final IllegalArgumentException e){
        }catch(final IllegalAccessException e){
        }catch(final InvocationTargetException e){
        }
        return null;
    }
    private static final Handler HandlerA=new Handler();
    private static final Runnable RunnableA=new Runnable(){  public void run(){  MainLoop();}};
    public static final String TAG=MainActivity.class.getSimpleName();
    public static final Context ContextA=GetStaticContext();
    public static final Display DisplayA=ContextA==null?null:((WindowManager)MainActivity.ContextA.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    public static final int XD=(DisplayA==null?256:DisplayA.getWidth() );
    public static final int YD=(DisplayA==null?256:DisplayA.getHeight());
    public static final int XD16=XD+(15&(16-(15&XD)));
    public static final int YD16=YD+(15&(16-(15&YD)));
    public static MainRS MainRSA;
    public static Viewer viewer;
    public static Bitmap font1,text;

    private static void MainLoop(){
        Profiler.profiler[Profiler.ProfilerEnd-1].Start("MainLoop");
        Profiler.index=-1;
        Clock.Update();
        Controls.Update();
        Profiler.profiler[Profiler.ProfilerEnd-2].Start("RenderScript");
        MainRSA.Update();
        Profiler.profiler[Profiler.ProfilerEnd-2].Stop();

        Render.coutReset();
        //Memtrix.debug();
        //for(int i=0;i!=Profiler.ProfilerEnd;++i)Render.cout(Profiler.profiler[i].print(),0xFF00FF00);
        viewer.requestRender();
        Profiler.profiler[Profiler.ProfilerEnd-1].Stop();
        HandlerA.postDelayed(RunnableA,Clock.Cooldown());
    }
    @Override public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(viewer=new Viewer(this));

        font1=BitmapFactory.decodeResource(getResources(),R.drawable.font2);
        text=font1.copy(Bitmap.Config.ARGB_8888,true);
        final int xEnd=text.getWidth();
        final int yEnd=text.getHeight();
        for(int x=0;x!=xEnd;++x)for(int y=0;y!=yEnd;++y)text.setPixel(x,y,((text.getPixel(x,y)&0xFF)<0x7F?0x00000000:0xFFFFFFFF));

        MainRSA=new MainRS(this);
        Log.d(TAG,"   ###   View added   ###   ");
        MainLoop();
    }
    @Override protected void onDestroy(){
        Log.d(TAG, "   ###   Destroying...   ###   ");
        super.onDestroy();
    }

    @Override protected void onStop(){
        Log.d(TAG, "   ###   Stopping...   ###   ");
        super.onStop();
    }
    /*
    @Override
    protected void onPause(){  super.onPause();  viewer.onPause();}
    @Override
    protected void onResume(){  super.onResume();  viewer.onResume();}
    */
    @Override public boolean onKeyDown(final int keyCode,final KeyEvent event){
        if((keyCode==KeyEvent.KEYCODE_BACK)){
            Log.d(TAG, "   ###   Killing...   ###   ");
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode,event);
    }
}