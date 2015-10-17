package applicable.omnisim;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Viewer extends GLSurfaceView{
    private final Render render;
    public Viewer(Context context){
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(new MyConfigChooser());
        getHolder().setFormat(PixelFormat.RGBA_8888);


        setRenderer(render=new Render());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    //public Viewer(final Context contextA){  super(contextA);}
    @Override protected void onMeasure(final int widthMeasureSpec,final int heightMeasureSpec){
        final int w=MeasureSpec.getSize(widthMeasureSpec);
        final int h=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w,h);
    }
    //@Override protected void onDraw(final Canvas canvasA){  canvasA.drawBitmap(MainRS.BmpA,0,0,null);}
    @Override public boolean onTouchEvent(MotionEvent event){
        final int index=event.getActionIndex();
        final int id=event.getPointerId(index);
        final int maskedAction=event.getActionMasked();
        //final int i=event.getPointerCount()>1?id:0;
        switch(maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{
                if(id<event.getPointerCount())Controls.TouchBgn(id,event.getX(id),event.getY(id));
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int a=0,b,e=event.getPointerCount();
                for(;a!=e;++a)if(0<=(b=event.getPointerId(a)))Controls.TouchMov(b,event.getX(a),event.getY(a));
/*
                float x=event.getX();
                float y=event.getY();
                float dx=x-mPreviousX;
                float dy=y-mPreviousY;
                if(y>0.5f*getHeight())dx=-dx;
                if(x<0.5f*getWidth() )dy=-dy;
                render.setAngle(render.getAngle()+((dx+dy)*(180.0f/320.0f)));
*/

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                Controls.TouchEnd(id);
                break;
            }
        }
        return true;
    }
}
